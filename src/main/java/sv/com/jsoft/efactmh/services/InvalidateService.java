package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.dto.DteToInvalidate;
import sv.com.jsoft.efactmh.model.dto.InvalidateRequest;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
@Slf4j
public class InvalidateService {

    public ResponseRestApi createInvalidate(InvalidateRequest request, JwtDto token) {
        RestUtil rest = RestUtil.builder()
                .endpoint("/api/secured/invalidate")
                .jwtDto(token)
                .body(request)
                .build();
        return rest.callPostAuth();
    }
    
    public DteToInvalidate findDteToInvalidate(Long idFactura, JwtDto token) {
        RestUtil rest = RestUtil.builder()
                .endpoint("/api/secured/invalidate/" + idFactura)
                .clazz(DteToInvalidate.class)
                .jwtDto(token)
                .build();
        ResponseRestApi response = rest.callGetOneAuth();
        if (response.getCodeHttp() == 200) {
            return (DteToInvalidate) response.getBody();
        } else {
            return null;
        }
    }
}
