package sv.com.jsoft.efactmh.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sv.com.jsoft.efactmh.model.DetFacturaDto;
import sv.com.jsoft.efactmh.view.ViewFactura;

/**
 *
 * @author migue
 */
@ApplicationScoped
public class DteService {

    private final static ResourceBundle VARIABLES = ResourceBundle.getBundle("variables");

    /**
     * Firmador del JSON, previo al envio a MH
     *
     * @param jsonDte
     * @return
     */
    public JSONObject getFirmarDocumento(JSONObject jsonDte) {
        try {
            JSONParser parser = new JSONParser();

            JSONObject jsonFirmador = new JSONObject();

            jsonFirmador.put("nit", "08011403460017");
            jsonFirmador.put("activo", true);
            jsonFirmador.put("passwordPri", VARIABLES.getString("pass.pri"));
            jsonFirmador.put("dteJson", jsonDte);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VARIABLES.getString("url.test.firmador")))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonFirmador.toJSONString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonFirmado = (JSONObject) parser.parse(response.body());
            return jsonFirmado;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Envia JSON firmado a MH
     *
     * @param dteFirmado
     * @param token
     * @param version
     * @param tipoDte
     * @param uuid
     * @return
     */
    public JSONObject getProcesarMh(String dteFirmado, String token, Integer version, String tipoDte, String uuid) {
        try {
            JSONParser parser = new JSONParser();

            JSONObject jsonRequest = new JSONObject();

            jsonRequest.put("version", version);
            jsonRequest.put("ambiente", "00");
            jsonRequest.put("idEnvio", 1);
            jsonRequest.put("tipoDte", tipoDte);
            jsonRequest.put("codigoGeneracion", uuid);
            jsonRequest.put("documento", dteFirmado);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VARIABLES.getString("url.mh.test.recepcion")))
                    .headers("Content-Type", "application/json")
                    .headers("Authorization", token)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toJSONString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

            return jsonResponse;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public JSONObject getTokenMh() {
        try {
            JSONParser parser = new JSONParser();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("user", VARIABLES.getString("nit"));
            parameters.put("pwd", VARIABLES.getString("pass.pub"));

            String form = parameters.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VARIABLES.getString("url.mh.test.autenticador")))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

            return jsonResponse;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
