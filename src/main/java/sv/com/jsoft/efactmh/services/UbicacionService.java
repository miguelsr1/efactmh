package sv.com.jsoft.efactmh.services;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
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

    @PostConstruct
    public void init() {
        RestUtil res = RestUtil.builder().endpoint("/api/catalogo/departamento/").clazz(CatalogoDto.class).build();
        ResponseRestApi rest = res.callGet();
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
        
        RestUtil res = RestUtil.builder().endpoint("/api/catalogo/municipio/departamento/" + depa).clazz(MunicipioDto.class).build();
        ResponseRestApi rest = res.callGet();
        if (rest.getCodeHttp() == 200) {
            return (List<MunicipioDto>) rest.getBody();
        } else {
            return new ArrayList<>();
        }
    }
}
