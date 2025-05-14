package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@Named
@ApplicationScoped
public class InvoceService {

    public ResponseRestApi saveInvoce(JwtDto token, InvoceDto invoceDto) {
        //Persistiendo factura
        return RestUtil
                .builder()
                .clazz(IdDto.class)
                .jwtDto(token)
                .body(invoceDto)
                .endpoint("/api/invoce")
                .build()
                .callPostAuth();
    }
}
