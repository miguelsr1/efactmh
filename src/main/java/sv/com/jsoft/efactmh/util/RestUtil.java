package sv.com.jsoft.efactmh.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.ws.rs.core.MediaType;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.EntityPk;
import sv.com.jsoft.efactmh.model.dto.ErrorResponseDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;

/**
 *
 * @author migue
 */
@SuperBuilder
@Slf4j
public class RestUtil {

    private String endpoint;
    private Class clazz;
    private JwtDto jwtDto;
    private Object body;
    private final Gson gson = new Gson();
    private final static String HOST = "http://localhost:8081";
    //private final static String HOST = "http://34.225.63.188:8080";

    public List callGet() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .GET()
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Type lst = TypeToken.getParameterized(List.class, clazz).getType();

            return gson.fromJson(response.body(), lst);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List callGet(JwtDto jwtDto) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .GET()
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Type lst = TypeToken.getParameterized(List.class, clazz).getType();

            return gson.fromJson(response.body(), lst);

        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Object callGetOne() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .GET()
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), clazz);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Object callPostAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)))
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), clazz);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void callPutAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)))
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            log.info("response: " + response.body());

            switch (response.statusCode()) {
                case 200:
                    MessageUtil.builder()
                            .severity(FacesMessage.SEVERITY_INFO)
                            .title("Información")
                            .message("Emisor actualizado")
                            .build()
                            .showMessage();
                    break;
                case 400:
                    ErrorResponseDto errorResponse = new Gson().fromJson(response.body(), ErrorResponseDto.class);

                    List<String> messages = errorResponse.getViolations().stream()
                            .map(ErrorResponseDto.Violation::getMessage) // Mapeamos cada Violation a su mensaje
                            .collect(Collectors.toList());

                    String result = String.join("\n", messages);

                    MessageUtil.builder()
                            .severity(FacesMessage.SEVERITY_WARN)
                            .title("Alerta")
                            .message(result)
                            .build()
                            .showMessage();
                    break;
            }
            //return gson.fromJson(response.body(), clazz);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            //return null;
        }
    }

    public Object callGetById() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            //Type lst = TypeToken.getParameterized(List.class, clazz).getType();
            Gson gson = new Gson();

            return gson.fromJson(response.body(), clazz);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int callPersistir(EntityPk data) {
        try {
            HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                    .uri(new URI(HOST + endpoint + (data.esNuevo() ? "" : data.getId())))
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8");
            httpBuilder = data.esNuevo()
                    ? httpBuilder.POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())))
                    : httpBuilder.PUT(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())));

            HttpRequest httpRequest = httpBuilder.build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return response.statusCode();
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int callPost(Object data) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(HOST + endpoint))
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers
                            .ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return response.statusCode();
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public <T> T callPost(Object data, Class<T> clazz, FieldNamingPolicy namingConvention) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(HOST + endpoint))
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers
                            .ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String responseStr = response.body();

            return new GsonBuilder().setFieldNamingPolicy(namingConvention).create().fromJson(responseStr, clazz);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ResponseDto callPostV2(Object data) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(HOST + endpoint))
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers
                            .ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return new ResponseDto(response.statusCode(), response.body());
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public <T> T getDataByTypeClass(ResponseDto responseDto, Class<T> clazz, FieldNamingPolicy namingConvention) {
        return new GsonBuilder()
                .setFieldNamingPolicy(namingConvention).create().fromJson(responseDto.getBody().toString(), clazz);
    }

    public int callPut(int id, Object data) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8090/" + endpoint + "/" + id))
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers
                            .ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return response.statusCode();
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
