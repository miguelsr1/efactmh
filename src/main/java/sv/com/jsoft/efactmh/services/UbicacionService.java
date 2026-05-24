package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class UbicacionService {

    @Getter
    List<CatalogoDto> lstDepartamento;

    @Getter
    List<MunicipioDto> lstMunicipio;
    @Inject
    SessionService sessionService;

    @PostConstruct
    public void init() {
        ResponseRestApi<List<CatalogoDto>> rest = RestUtil
                .builder()
                .clazz(CatalogoDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/catalogo/departamento/")
                .build()
                .callGetAllAuth();
        
        if (rest.getCodeHttp() == 200) {
            lstDepartamento = rest.getBody();
        } else {
            lstDepartamento = new ArrayList<>();
        }
    }

    public List<MunicipioDto> findMunicipioByDepa(String depa) {
        if(depa == null){
            return new ArrayList<>();
        }
        
        ResponseRestApi<List<MunicipioDto>> rest = RestUtil
                .builder()
                .clazz(MunicipioDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/catalogo/municipio/departamento/" + depa)
                .build()
                .callGetAllAuth();
        
        if (rest.getCodeHttp() == 200) {
            return rest.getBody();
        } else {
            return new ArrayList<>();
        }
    }
    
    public MunicipioDto findMunicipioById(Long idMuni){
        return (MunicipioDto) RestUtil.builder()
                .clazz(MunicipioDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/catalogo/municipio/" + idMuni)
                .build().callGetOneAuth().getBody();
    }
}
