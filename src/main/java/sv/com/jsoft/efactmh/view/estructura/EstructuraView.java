package sv.com.jsoft.efactmh.view.estructura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.model.dto.PuntoVentaDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.view.estructura.service.EstablecimientoService;

/**
 *
 * @author migue
 */
@ViewScoped
@Named
@Slf4j
public class EstructuraView implements Serializable {

    private Long idEstablecimiento;

    @Getter
    @Setter
    private EstablecimientoDto estable;
    @Getter
    @Setter
    private List<EstablecimientoDto> lstEstable;
    private List<PuntoVentaDto> lstPuntosVentas;
    @Inject
    SessionService securityService;
    @Inject
    EstablecimientoService estableService;

    @PostConstruct
    public void init() {
        estable = new EstablecimientoDto();
        lstPuntosVentas = new ArrayList();
        loadEstablecimientos();
    }

    public void onRowSelect(SelectEvent<EstablecimientoDto> event) {
        estable = event.getObject();
        idEstablecimiento = estable.getIdEstablecimiento();
        loadPuntosVenta();
    }

    public void loadPuntosVenta() {
        lstPuntosVentas = estableService.getLstPuntosVentas(securityService.getToken(), idEstablecimiento);
    }

    private void loadEstablecimientos() {
        lstEstable = estableService.getLstEstablecimiento(securityService.getToken());
    }

    public void showDlgEstablecimiento() {

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .modal(true)
                .width("400px")
                .height("460px")
                .build();

        PrimeFaces.current().dialog().openDynamic("mantto/dialog/dlg-establecimiento", options, null);
    }

    public void showDlgPuntoVenta() {
        if (estable == null) {
            MessageUtil.builder()
                    .message("DEBE SELECCIONAR UN ESTABLECIMIENTO")
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title("ERROR")
                    .build()
                    .showMessage();
            return;
        }
        
        this.idEstablecimiento = estable.getIdEstablecimiento();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idEstablecimiento", idEstablecimiento);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nombreEstable", estable.getNombreSucursal());

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .modal(true)
                .width("400px")
                .height("460px")
                .build();

        PrimeFaces.current().dialog().openDynamic("mantto/dialog/dlg-punto-venta", options, null);
    }

    public void onEstablecimiento(SelectEvent<EstablecimientoDto> event) {
        if (event.getObject() != null) {
            EstablecimientoDto establecimientoDto = event.getObject();
            lstEstable.add(establecimientoDto);
        }
    }

    public void agregarEstablecimiento() {
        lstEstable.add(estable);
        estable = new EstablecimientoDto();
    }

    public List<PuntoVentaDto> getLstPuntosVentas() {
        return lstPuntosVentas;
    }
}
