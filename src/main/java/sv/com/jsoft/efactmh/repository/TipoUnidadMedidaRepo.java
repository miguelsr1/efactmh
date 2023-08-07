package sv.com.jsoft.efactmh.repository;

import javax.ejb.Stateless;
import sv.com.jsoft.efactmh.model.TipoUnidadMedida;

/**
 *
 * @author migue
 */
@Stateless
public class TipoUnidadMedidaRepo extends AbstractRepository<TipoUnidadMedida, Integer> {

    public TipoUnidadMedidaRepo() {
        super(TipoUnidadMedida.class);
    }

}
