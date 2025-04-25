package sv.com.jsoft.efactmh.view.estructura.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.PuntoVentaDto;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class EstablecimientoService {

    public List<EstablecimientoDto> getLstEstablecimiento(JwtDto token) {
        RestUtil rest = RestUtil
                .builder()
                .clazz(EstablecimientoDto.class)
                .jwtDto(token)
                .endpoint("/api/establecimiento")
                .build();

        return rest.callGet();
    }

    public List<PuntoVentaDto> getLstPuntosVentas(JwtDto token, Long idEstablecimiento) {
        RestUtil rest = RestUtil
                .builder()
                .clazz(PuntoVentaDto.class)
                .jwtDto(token)
                .endpoint("/api/punto-venta/establecimiento/" + idEstablecimiento)
                .build();

        return rest.callGet();
    }
}
