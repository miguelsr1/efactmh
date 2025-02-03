package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Departamento;
import sv.com.jsoft.efactmh.model.MunicipioDto;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class UbicacionService {

    @Getter
    List<Departamento> lstDepartamento;

    @Getter
    List<MunicipioDto> lstMunicipio;

    @PostConstruct
    public void init() {
        /*RestUtil res = RestUtil.builder().endpoint("catalogos/departamento/").clazz(Departamento.class).build();
        lstDepartamento = res.callGet();*/
    }

    public List<MunicipioDto> findMunicipioByDepa(String depa) {
        //RestUtil res = RestUtil.builder().endpoint("catalogos/municipios/" + depa).clazz(Municipio.class).build();
        return null;
    }

}
