package sv.com.jsoft.efactmh.view.estructura.service;

import java.util.ArrayList;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;

import jakarta.inject.Inject;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.model.dto.PuntoVentaDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class EstablecimientoService implements Serializable{

    @Inject
    SessionService sessionService;

    public List<EstablecimientoDto> getLstEstablecimiento() {
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(EstablecimientoDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/establecimiento")
                .build()
                .callGetAllAuth();

        return rest.getCodeHttp() == 200 ? (List<EstablecimientoDto>) rest.getBody() : new ArrayList<>();
    }

    public List<PuntoVentaDto> getLstPuntosVentas(Long idEstablecimiento) {
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(PuntoVentaDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/punto-venta/establecimiento/" + idEstablecimiento)
                .build()
                .callGetAllAuth();

        return rest.getCodeHttp() == 200 ? (List<PuntoVentaDto>) rest.getBody() : new ArrayList<>();
    }
}
