package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Departamento;
import sv.com.jsoft.efactmh.model.Municipio;
import sv.com.jsoft.efactmh.repository.DepartamentoRepo;
import sv.com.jsoft.efactmh.repository.MunicipioRepo;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class UbicacionService {

    @Inject
    DepartamentoRepo depaRepo;
    @Inject
    MunicipioRepo muniRepo;

    @Getter
    List<Departamento> lstDepartamento;

    @Getter
    List<Municipio> lstMunicipio;

    @PostConstruct
    public void init() {
        lstDepartamento = depaRepo.findAll();
        lstMunicipio = muniRepo.findAll();
    }

}
