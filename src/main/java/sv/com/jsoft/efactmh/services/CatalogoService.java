package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class CatalogoService {

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

    @Inject
    SessionService securityService;

    @PostConstruct
    public void init() {
        //loadProduct();
        lstTipoUnidadMedida = new ArrayList<>();
        loadTipoUnidadMedida();
        loadDatosUbicacion();
        loadGiros();
        loadTipoDocumentosId();
    }

    private void loadTipoDocumentosId(){
        lstTipoDocumentosId = new ArrayList<>();
        lstTipoDocumentosId.add(new CatalogoDto("13","DUI"));
        lstTipoDocumentosId.add(new CatalogoDto("36","NIT"));
        lstTipoDocumentosId.add(new CatalogoDto("37","OTRO"));
        lstTipoDocumentosId.add(new CatalogoDto("03","PASAPORTE"));
        lstTipoDocumentosId.add(new CatalogoDto("02","CARNET DE RESIDENTE"));
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
                .callGet();

        if (response.getCodeHttp() == 200) {
            lstDepartamentos = (List<CatalogoDto>) response.getBody();
        }

        response = RestUtil.builder()
                .endpoint("/api/catalogo/municipio")
                .clazz(MunicipioDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();

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
                .callGet();
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
                .callGetAuth();
        return (response.getCodeHttp() == 200) ? (List<CatalogoDto>) response.getBody() : new ArrayList<>();
    }

    public List<CatalogoDto> getLstPuntoVentaByEstablecimiento(JwtDto token, Long idEstablecimiento) {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/punto-venta/" + idEstablecimiento)
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGetAuth();
        return (response.getCodeHttp() == 200) ? (List<CatalogoDto>) response.getBody() : new ArrayList<>();
    }
}
