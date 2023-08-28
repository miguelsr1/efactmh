package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Departamento;
import sv.com.jsoft.efactmh.model.Municipio;
import sv.com.jsoft.efactmh.util.RestUtil;

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
    List<Municipio> lstMunicipio;

    @PostConstruct
    public void init() {
        RestUtil res = RestUtil.builder().endpoint("catalogos/departamento/").clazz(Departamento.class).build();
        lstDepartamento = res.callGet();
    }

    public List<Municipio> findMunicipioByDepa(String depa) {
        RestUtil res = RestUtil.builder().endpoint("catalogos/municipio/" + depa).clazz(Municipio.class).build();
        return res.callGet();
    }

}
