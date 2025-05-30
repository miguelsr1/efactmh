package sv.com.jsoft.efactmh.view.estructura.service;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.PuntoVentaDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class EstablecimientoService {

    public List<EstablecimientoDto> getLstEstablecimiento(JwtDto token) {
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(EstablecimientoDto.class)
                .jwtDto(token)
                .endpoint("/api/establecimiento")
                .build()
                .callGetAuth();

        return rest.getCodeHttp() == 200 ? (List<EstablecimientoDto>) rest.getBody() : new ArrayList<>();
    }

    public List<PuntoVentaDto> getLstPuntosVentas(JwtDto token, Long idEstablecimiento) {
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(PuntoVentaDto.class)
                .jwtDto(token)
                .endpoint("/api/punto-venta/establecimiento/" + idEstablecimiento)
                .build()
                .callGet();

        return rest.getCodeHttp() == 200 ? (List<PuntoVentaDto>) rest.getBody() : new ArrayList<>();
    }
}
