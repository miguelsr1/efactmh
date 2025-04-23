package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.EstablecimientoDto;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
@Slf4j
public class EmisorView implements Serializable {

    @Getter
    @Setter
    private EstablecimientoDto estable;
    @Getter
    @Setter
    private List<EstablecimientoDto> lstEstable;

    @Getter
    @Setter
    private Emisor emisor;
    @Getter
    @Setter
    private String municipio;
    @Getter
    private List<CatalogoDto> lstDepartamentos;
    @Getter
    private List<MunicipioDto> lstMunicipios;
    @Getter
    private List<CatalogoDto> lstGiros;

    @Inject
    CatalogoService catalogoService;
    @Inject
    SessionService securityService;

    @PostConstruct
    public void init() {
        lstGiros = catalogoService.getLstGiros();
        estable = new EstablecimientoDto();
        loadEstablecimientos();
        loadDataEmisor();
    }

    private void loadDataEmisor() {
        RestUtil rest = RestUtil
                .builder()
                .clazz(Emisor.class)
                .jwtDto(securityService.getToken())
                .endpoint("/api/secured/emisor")
                .build();

        emisor = (Emisor) rest
                .callGetOne();

        municipio = emisor.getCodigoDepartamento().concat(emisor.getCodigoMunicipio());
        lstMunicipios = catalogoService.getMunicipioDtoByCodDepa(emisor.getCodigoDepartamento());
        lstDepartamentos = catalogoService.getLstDepartamentos();
    }

    private void loadEstablecimientos() {
        lstEstable = new ArrayList<>();

        RestUtil rest = RestUtil
                .builder()
                .clazz(EstablecimientoDto.class)
                .jwtDto(securityService.getToken())
                .endpoint("/api/establecimiento")
                .build();

        lstEstable = rest.callGet();
    }

    public void updateListaMunicipios() {
        lstMunicipios = catalogoService.getMunicipioDtoByCodDepa(emisor.getCodigoDepartamento());
    }

    public void agregarEstablecimiento() {
        lstEstable.add(estable);
        estable = new EstablecimientoDto();
    }

    public void guardar() {
        RestUtil rest = RestUtil
                .builder()
                .jwtDto(securityService.getToken())
                .body(emisor)
                .endpoint("/api/secured/emisor/").build();

        rest.callPutAuth();
    }

    public String cancelar() {
        return "/app/home";
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

        PrimeFaces.current().dialog().openDynamic("dialog/dlg-establecimiento", options, null);
    }

    public void showDlgPuntoVenta(Long idEstablecimiento, String nombreEstable) {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idEstablecimiento", idEstablecimiento);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("nombreEstable", nombreEstable);

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .modal(true)
                .width("400px")
                .height("460px")
                .includeViewParams(true)
                .build();

        PrimeFaces.current().dialog().openDynamic("dialog/dlg-punto-venta", options, null);
    }

    public void onEstablecimiento(SelectEvent<EstablecimientoDto> event) {
        if (event.getObject() != null) {
            EstablecimientoDto establecimientoDto = event.getObject();
            lstEstable.add(establecimientoDto);
        }
    }

    public void onNodeExpand(NodeExpandEvent event) {
        log.info("EXPANDI");
    }
}
