package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@ApplicationScoped
public class ProductoService {


    @PostConstruct
    public void init() {

    }

    public List<Producto> findAll(JwtDto token) {
        ResponseRestApi response = RestUtil
                .builder()
                .clazz(Producto.class)
                .jwtDto(token)
                .endpoint("/api/item")
                .build()
                .callGetAllAuth();

        if (response.getCodeHttp() == 200) {
            return (List<Producto>) response.getBody();
        } else {
            return new ArrayList<>();
        }
    }
}
