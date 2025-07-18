package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.NodeExpandEvent;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
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

        loadDataEmisor();
    }

    private void loadDataEmisor() {
        ResponseRestApi response = RestUtil
                .builder()
                .clazz(Emisor.class)
                .jwtDto(securityService.getToken())
                .endpoint("/api/secured/emisor")
                .build()
                .callGetOneAuth();

        if (response.getCodeHttp() == 200) {
            emisor = (Emisor) response.getBody();
            municipio = emisor.getCodigoDepartamento().concat(emisor.getCodigoMunicipio());
            lstMunicipios = catalogoService.getMunicipioDtoByCodDepa(emisor.getCodigoDepartamento());
            lstDepartamentos = catalogoService.getLstDepartamentos();
        } else {
            MessageUtil.builder().message("NO SE HAN CARGADO LOS DATOS DEL EMISOR").build().showMessage();
        }
    }

    public void updateListaMunicipios() {
        lstMunicipios = catalogoService.getMunicipioDtoByCodDepa(emisor.getCodigoDepartamento());
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

    public void onNodeExpand(NodeExpandEvent event) {
        log.info("EXPANDI");
    }
}
