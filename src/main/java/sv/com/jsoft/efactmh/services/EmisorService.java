package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
@Slf4j
public class EmisorService {

    public ResponseRestApi getEmisor(JwtDto jwt) {
        return RestUtil
                .builder()
                .clazz(Emisor.class)
                .jwtDto(jwt)
                .endpoint("/api/secured/emisor")
                .build()
                .callGetOneAuth();
    }
}
