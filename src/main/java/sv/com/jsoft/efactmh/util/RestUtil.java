package sv.com.jsoft.efactmh.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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
import java.time.LocalDateTime;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.ws.rs.core.MediaType;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.adapter.LocalDateTimeAdapter;
import sv.com.jsoft.efactmh.model.Personeria;
import sv.com.jsoft.efactmh.model.dto.ErrorMessageDto;
import sv.com.jsoft.efactmh.model.dto.ErrorResponseDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;

@SuperBuilder
@Slf4j
public class RestUtil {

    private String endpoint;
    private Class clazz;
    private String accessToken;
    private Object body;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    
    private static final String HOST = "http://localhost:8099";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public <T> ResponseRestApi<T> callGetOneAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .header("Authorization", "Bearer " + accessToken)
                    .timeout(Duration.ofSeconds(60))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> {
                    if (response.body() != null) {
                        if (clazz.equals(String.class)) {
                            yield new ResponseRestApi<>(response.statusCode(), (T) response.body());
                        }
                        yield new ResponseRestApi<>(response.statusCode(), (T) gson.fromJson(response.body(), clazz));
                    }
                    yield new ResponseRestApi<>(response.statusCode(), null);
                }
                case 404 -> new ResponseRestApi<>(response.statusCode(), (T) "DATO NO ENCONTRADO");
                case 401 -> new ResponseRestApi<>(response.statusCode(), (T) "ACCESO NO AUTORIZADO");
                default -> new ResponseRestApi<>(response.statusCode(), (T) response.body());
            };
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR postAuth - " + endpoint, ex);
            return null;
        }
    }

    public <T> ResponseRestApi<List<T>> callGetAllAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .header("Authorization", "Bearer " + accessToken)
                    .timeout(Duration.ofSeconds(3))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body() != null) {
                    Type lst = TypeToken.getParameterized(List.class, clazz).getType();
                    return new ResponseRestApi<>(response.statusCode(), gson.fromJson(response.body(), lst));
                }
            } else if (response.statusCode() == 404) {
                return new ResponseRestApi<>(response.statusCode(), null);
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

            return new ResponseRestApi<>(-1, (List<T>) List.of(mensajeError));
        }
        return null;
    }

    public <T> ResponseRestApi<T> callPostAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)))
                    .timeout(Duration.ofSeconds(6))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 201, 200, 400 -> {
                    if (response.body() != null) {
                        if (response.statusCode() == 400) {
                            log.error("RESPONSE " + endpoint + ": " + response.body());
                        } else {
                            log.info("RESPONSE " + endpoint + ": " + response.body());
                        }
                        yield new ResponseRestApi<>(response.statusCode(), (T) gson.fromJson(response.body(), clazz));
                    }
                    yield new ResponseRestApi<>(response.statusCode(), null);
                }
                case 401, 409 -> {
                    try {
                        yield new ResponseRestApi<>(response.statusCode(), null, gson.fromJson(response.body(), ErrorMessageDto.class));
                    } catch (JsonSyntaxException e) {
                        log.error("RESPUESTA WS: " + response.body());
                        log.error("ERROR EN CASTEO POR RESPUESTA 401", e);
                        yield new ResponseRestApi<>(response.statusCode(), null);
                    }
                }
                default -> new ResponseRestApi<>(response.statusCode(), (T) response.body());
            };
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR postAuth - " + endpoint, ex);
            return null;
        }
    }

    public void callPutAuth() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(HOST + endpoint))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .header("Authorization", "Bearer " + accessToken)
                    .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)))
                    .timeout(Duration.ofSeconds(3))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

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
                            .map(ErrorResponseDto.Violation::getMessage)
                            .toList();

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

    public int callUpdClient(Long idCliente, Personeria data) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(new URI(HOST + endpoint + idCliente))
                    .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(data)))
                    .header("Authorization", "Bearer " + accessToken)
                    .headers("Content-Type", MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .timeout(Duration.ofSeconds(3))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

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
                    .timeout(Duration.ofSeconds(3))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return new ResponseDto(response.statusCode(), response.body());
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            log.error("ERROR postV2 - " + HOST + endpoint, ex);

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
