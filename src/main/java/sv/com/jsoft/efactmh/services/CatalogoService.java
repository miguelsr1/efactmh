package sv.com.jsoft.efactmh.services;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.TipoUnidadMedida;
import sv.com.jsoft.efactmh.repository.TipoUnidadMedidaRepo;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class CatalogoService {

    @Getter
    private List<TipoUnidadMedida> lstUnidadMedida;

    @Inject
    TipoUnidadMedidaRepo tipoUnidadMedidaRepo;

    @PostConstruct
    public void init() {
        lstUnidadMedida = tipoUnidadMedidaRepo.findAll();
    }

}
