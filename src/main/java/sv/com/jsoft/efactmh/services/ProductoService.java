package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class ProductoService {

    /*@Getter
    private List<CatalogoDto> lstTipoItems;*/

    @PostConstruct
    public void init() {
        /*lstTipoItems = Arrays.asList(
                new CatalogoDto("01", "BIEN"),
                new CatalogoDto("02", "SERVICIO"),
                new CatalogoDto("03", "AMBOS"),
                new CatalogoDto("04", "OTROS"));*/
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
