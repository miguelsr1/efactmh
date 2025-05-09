package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.dto.InvalidateRequest;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.JsfUtil;
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
                .endpoint("/api/secured/dte/invalidate")
                .jwtDto(token)
                .body(request)
                .build();
        return rest.callPostAuth();
    }
}
