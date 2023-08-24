package sv.com.jsoft.efactmh.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Producto;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class CatalogoService {

    @Getter
    private List<Producto> lstProducto;

    {
        lstProducto = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        loadProduct();
    }

    private void loadProduct() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8090/hello/all")).GET().build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Type lst = new TypeToken<List<Producto>>() {
            }.getType();

            Gson gson = new Gson();

            lstProducto = gson.fromJson(response.body(), lst);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Producto getProductoByCodigo(String codigo){
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8090/hello/producto/"+codigo+"/")).GET().build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();

            return gson.fromJson(response.body(), Producto.class);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
