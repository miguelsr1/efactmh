package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.dto.DteToInvalidate;
import sv.com.jsoft.efactmh.model.dto.InvalidateRequest;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
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

    public ResponseDto findDteToInvalidate(Long idFactura, String codigoDte, String codigoGeneracion, JwtDto token) {
        //validar tiempo de validez de anulacion
        RestUtil rest = RestUtil.builder()
                .endpoint("/api/secured/invalidate/all/" + codigoDte)
                .clazz(String.class)
                .jwtDto(token)
                .build();
        ResponseRestApi response = rest.callGetAllAuth();

        if (response.getCodeHttp() != 200) {
            return new ResponseDto(1, "NO SE ENCONTRARON DTES");
        }

        List<String> lstDtes = (List<String>) response.getBody();

        if (lstDtes.stream().filter(dte -> dte.equals(codigoGeneracion)).findFirst().isPresent()) {
            rest = RestUtil.builder()
                    .endpoint("/api/secured/invalidate/" + idFactura)
                    .clazz(DteToInvalidate.class)
                    .jwtDto(token)
                    .build();
            response = rest.callGetOneAuth();
            if (response.getCodeHttp() == 200) {
                return new ResponseDto(0, (DteToInvalidate) response.getBody());
            } else {
                return new ResponseDto(1, "NO SE ENCONTRO EL DTE: " + codigoGeneracion);
            }
        } else {
            return new ResponseDto(1, "EL DTE NO CUMPLE CON LOS PLAZOS PARA SU ANULACIÓN");
        }
    }
}
