package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Departamento;
import sv.com.jsoft.efactmh.model.Municipio;

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

}
