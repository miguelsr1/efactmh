package sv.com.jsoft.efactmh.view.estructura;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.model.dto.PuntoVentaDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ViewScoped
@Named
@Slf4j
public class DlgPuntoVenta implements Serializable {

    @Getter
    @Setter
    private PuntoVentaDto puntoVentaDto;
    @Inject
    SessionService sessionService;
    @Getter
    private String nombreEstable;
    private Long idEstablecimiento;

    @PostConstruct
    public void init() {
        nombreEstable = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nombreEstable");
        idEstablecimiento = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idEstablecimiento");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("nombreEstable");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idEstablecimiento");

        puntoVentaDto = new PuntoVentaDto();
        puntoVentaDto.setIdEstablecimiento(idEstablecimiento);
        puntoVentaDto.setActivo(Boolean.TRUE);
    }

    public void add() {

        save();

        PrimeFaces.current().dialog().closeDynamic(puntoVentaDto);
    }

    private void save() {
        RestUtil rest = RestUtil
                .builder()
                .clazz(IdDto.class)
                .jwtDto(sessionService.getToken())
                .body(puntoVentaDto)
                .endpoint("/api/punto-venta").build();

        ResponseRestApi response = rest.callPostAuth();
        if (response.getCodeHttp() == 201) {
            IdDto idDto = (IdDto) response.getBody();
            log.info("PUNTO DE VENTA CREADO:" + idDto);
        }
    }

    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
