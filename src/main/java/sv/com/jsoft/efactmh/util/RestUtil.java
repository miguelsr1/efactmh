package sv.com.jsoft.efactmh.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import lombok.experimental.SuperBuilder;

/**
 *
 * @author migue
 */
@SuperBuilder
public class RestUtil {

    private String endpoint;
    private Class clazz;

    public List callGet() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8090/" + endpoint))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Type lst = TypeToken.getParameterized(List.class, clazz).getType();

            Gson gson = new Gson();

            return gson.fromJson(response.body(), lst);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
