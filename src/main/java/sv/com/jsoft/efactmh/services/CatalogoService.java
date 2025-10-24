package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class CatalogoService implements Serializable {

    @Getter
    private List<CatalogoDto> lstTipoUnidadMedida;
    @Getter
    private List<CatalogoDto> lstDepartamentos;
    @Getter
    private List<CatalogoDto> lstGiros;
    @Getter
    private List<CatalogoDto> lstTipoDocumentosId;
    @Getter
    private List<MunicipioDto> lstMunicipios;
    @Getter
    private List<Producto> lstProducto;
    @Getter
    private Map<String, String> dtes;

    @Inject
    SessionService securityService;
    @Inject
    ProductoService productoService;

    @PostConstruct
    public void init() {
        lstTipoUnidadMedida = new ArrayList<>();
        loadTipoUnidadMedida();
        loadDatosUbicacion();
        loadGiros();
        loadTipoDocumentosId();
        loadItems();
        loadDtes();
    }
    
    private void loadDtes(){
        dtes = new HashMap<>();
        dtes.put("01", "FE");
        dtes.put("03", "CCF");
        
        dtes.put("04", "NR");
        dtes.put("05", "NC");
        dtes.put("06", "ND");
        
        dtes.put("14", "FSX");
        
        dtes.put("99", "ANU");
    }
    
    public void loadItems(){
        lstProducto = productoService.findAll(securityService.getToken());
    }

    private void loadTipoDocumentosId() {
        lstTipoDocumentosId = new ArrayList<>();
        lstTipoDocumentosId.add(new CatalogoDto("13", "DUI"));
        lstTipoDocumentosId.add(new CatalogoDto("36", "NIT"));
        lstTipoDocumentosId.add(new CatalogoDto("37", "OTRO"));
        lstTipoDocumentosId.add(new CatalogoDto("03", "PASAPORTE"));
        lstTipoDocumentosId.add(new CatalogoDto("02", "CARNET DE RESIDENTE"));
    }

    private void loadTipoUnidadMedida() {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/unidad-medidad")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAllAuth();

        if (response.getCodeHttp() == 200) {
            lstTipoUnidadMedida = (List<CatalogoDto>) response.getBody();
        }
    }

    private void loadDatosUbicacion() {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/departamento")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAllAuth();

        if (response.getCodeHttp() == 200) {
            lstDepartamentos = (List<CatalogoDto>) response.getBody();
        }

        response = RestUtil.builder()
                .endpoint("/api/catalogo/municipio")
                .clazz(MunicipioDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAllAuth();

        if (response.getCodeHttp() == 200) {
            lstMunicipios = (List<MunicipioDto>) response.getBody();
        }
    }

    public List<MunicipioDto> getMunicipioDtoByCodDepa(String codDepa) {
        return lstMunicipios
                .stream()
                .filter(mun -> mun.getCodDepartamento().compareTo(codDepa) == 0)
                .sorted(Comparator.comparing(MunicipioDto::getNombre))
                .collect(Collectors.toList());
    }

    private void loadGiros() {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/giro")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAllAuth();
        if (response.getCodeHttp() == 200) {
            lstGiros = (List<CatalogoDto>) response.getBody();
        }
    }

    public List<CatalogoDto> getLstEstablecimiento(JwtDto token) {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/establecimiento")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAllAuth();
        
        return (response.getCodeHttp() == 200) ? (List<CatalogoDto>) response.getBody() : new ArrayList<>();
    }

    public List<CatalogoDto> getLstPuntoVentaByEstablecimiento(JwtDto token, Long idEstablecimiento) {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/punto-venta/" + idEstablecimiento)
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAllAuth();
        
        return (response.getCodeHttp() == 200) ? (List<CatalogoDto>) response.getBody() : new ArrayList<>();
    }
}
