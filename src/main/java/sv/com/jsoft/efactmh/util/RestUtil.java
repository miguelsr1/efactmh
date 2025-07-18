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
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.ws.rs.core.MediaType;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.EntityPk;
import sv.com.jsoft.efactmh.model.Personeria;
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
    private final static String HOST = "http://localhost:8082";
    //private final static String HOST = "http://34.225.63.188:8080";

    public ResponseRestApi callGetOneAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200:
                    if (response.body() != null) {
                        if (clazz.equals(String.class)) {
                            return new ResponseRestApi(response.statusCode(),
                                    response.body());
                        }
                        return new ResponseRestApi(response.statusCode(),
                                gson.fromJson(response.body(), clazz));
                    }
                    break;
                case 404:
                    return new ResponseRestApi(response.statusCode(),
                            "DATO NO ENCONTRADO");
                case 401:
                    return new ResponseRestApi(response.statusCode(),
                            "ACCESO NO AUTORIZADO");
                default:
                    break;
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR postAuth - " + endpoint, ex);
            return null;
        }

        return null;
    }

    public ResponseRestApi callGetAllAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body() != null) {
                    Type lst = TypeToken.getParameterized(List.class, clazz).getType();
                    return new ResponseRestApi(response.statusCode(),
                            gson.fromJson(response.body(), lst));
                }
            } else if (response.statusCode() == 404) {
                return new ResponseRestApi(response.statusCode(), null);
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR get - " + endpoint, ex);

            String mensajeError;
            if (ex instanceof java.net.ConnectException) {
                mensajeError = "ERROR DE CONEXION";
            } else if (ex instanceof java.net.http.HttpTimeoutException) {
                mensajeError = "TIEMPO DE ESPERA SUPERADO";
            } else {
                mensajeError = "ERROR INESPERADO";
            }

            return new ResponseRestApi(-1, mensajeError);
        }
        return null;
    }

    /**
     * LISTO
     *
     * @return
     */
    public ResponseRestApi callPostAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 201:
                case 200:
                    if (response.body() != null) {
                        log.info("RESPONSE " + endpoint + ": " + response.body());

                        return new ResponseRestApi(response.statusCode(),
                                gson.fromJson(response.body(), clazz));
                    }
                    break;
                //RENOVAR JWT KEYCLOAK
                case 401:
                    break;
                default:
                    return new ResponseRestApi(response.statusCode(), response.body());
            }
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR postAuth - " + endpoint, ex);
            return null;
        }

        return null;
    }

    public void callPutAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)))
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
                            .message("Registro actualizado correctamente")
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
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR putAuth - " + endpoint, ex);
        }
    }

    public Object callGetById() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return new Gson().fromJson(response.body(), clazz);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR getById - " + endpoint, ex);
            return null;
        }
    }

    public int callPersistir(EntityPk data) {
        try {
            HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(new URI(HOST + endpoint + (data.esNuevo() ? "" : data.getId())))
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
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
            log.error("ERROR persistir - " + endpoint, ex);
            return 0;
        }
    }

    public int callUpdClient(Long idCliente, Personeria data) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(new URI(HOST + endpoint + idCliente))
                    .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                    .header("Authorization", "Bearer " + jwtDto.getAccessToken())
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return response.statusCode();
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR persistir - " + endpoint, ex);
            return 0;
        }
    }

    public ResponseDto callPost(Object data) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(new URI(HOST + endpoint))
                    .timeout(Duration.ofSeconds(5))
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers
                            .ofInputStream(() -> new ByteArrayInputStream(new Gson().toJson(data).getBytes())))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return new ResponseDto(response.statusCode(), response.body());
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR postV2 - " + endpoint, ex);

            String mensajeError;
            if (ex instanceof java.net.ConnectException) {
                mensajeError = "ERROR DE CONEXION";
            } else if (ex instanceof java.net.http.HttpTimeoutException) {
                mensajeError = "TIEMPO DE ESPERA SUPERADO";
            } else {
                mensajeError = "ERROR INESPERADO";
            }

            return new ResponseDto(-1, mensajeError);
        }
    }

    public <T> T getDataByTypeClass(ResponseDto responseDto, Class<T> clazz, FieldNamingPolicy namingConvention) {
        return new GsonBuilder()
                .setFieldNamingPolicy(namingConvention).create().fromJson(responseDto.getBody().toString(), clazz);
    }
}
