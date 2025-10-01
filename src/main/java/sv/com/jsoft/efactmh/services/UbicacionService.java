package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
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
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(CatalogoDto.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/catalogo/departamento/")
                .build()
                .callGetAllAuth();
        
        if (rest.getCodeHttp() == 200) {
            lstDepartamento = (List<CatalogoDto>) rest.getBody();
        } else {
            lstDepartamento = new ArrayList<>();
        }
    }

    public List<MunicipioDto> findMunicipioByDepa(String depa) {
        if(depa == null){
            return new ArrayList<>();
        }
        
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(MunicipioDto.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/catalogo/municipio/departamento/" + depa)
                .build()
                .callGetAllAuth();
        
        if (rest.getCodeHttp() == 200) {
            return (List<MunicipioDto>) rest.getBody();
        } else {
            return new ArrayList<>();
        }
    }
}
