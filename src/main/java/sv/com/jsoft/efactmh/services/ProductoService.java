package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;

import jakarta.inject.Inject;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@ApplicationScoped
public class ProductoService implements Serializable {

    @Inject
    SessionService sessionService;

    @PostConstruct
    public void init() {

    }

    public List<Producto> findAll() {
        ResponseRestApi response = RestUtil
                .builder()
                .clazz(Producto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/item")
                .build()
                .callGetAllAuth();

        if (response.getCodeHttp() == 200) {
            return (List<Producto>) response.getBody();
        } else {
            return new ArrayList<>();
        }
    }
}
