package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.model.dto.PuntoVentaDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@RequestScoped
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
        nombreEstable = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("nombreEstable");
        idEstablecimiento = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idEstablecimiento");
        
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

        IdDto idDto = (IdDto) rest.callPostAuth();
        log.info("PUNTO DE VENTA CREADO:" + idDto);
    }

    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
