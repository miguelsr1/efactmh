package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.NodeExpandEvent;
import sv.com.jsoft.efactmh.model.ActividadEconomica;
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
    private String actPrimaria;
    @Getter
    @Setter
    private String codigoActividad;
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
    @Getter
    private List<ActividadEconomica> lstActivadesEco;

    @Inject
    CatalogoService catalogoService;
    @Inject
    SessionService securityService;

    @PostConstruct
    public void init() {
        lstGiros = catalogoService.getLstGiros();
        lstActivadesEco = new ArrayList<>();
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

            actPrimaria = emisor.getCodigoActividad()
                    .concat(" - ")
                    .concat(getGiroByCodigo(emisor.getCodigoActividad()).getNombre());
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

    public void agregarGiro() {
        CatalogoDto giroSelected = getGiroByCodigo(codigoActividad);

        if (lstActivadesEco.stream().filter(act -> act.getCodigoActividad().equals(codigoActividad)).findFirst().isEmpty()) {
            lstActivadesEco.add(new ActividadEconomica(codigoActividad, giroSelected.getNombre(), lstActivadesEco.size() + 2));
        } else {
            MessageUtil.builder().message("NO SE HAN CARGADO LOS DATOS DEL EMISOR").build().showMessage();
        }
    }
    
    public void removerActividad(String codigo){
        lstActivadesEco.removeIf(act -> act.getCodigoActividad().equals(codigo));
    }

    private CatalogoDto getGiroByCodigo(String codigo) {
        return lstGiros.stream()
                .filter(giro -> giro.getId().equals(codigo))
                .findFirst()
                .get();
    }
}
