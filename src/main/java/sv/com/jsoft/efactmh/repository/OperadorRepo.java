package sv.com.jsoft.efactmh.repository;

import javax.ejb.Stateless;
import sv.com.jsoft.efactmh.model.Operador;

/**
 *
 * @author migue
 */
@Stateless
public class OperadorRepo extends AbstractRepository<Operador, String> {

    public OperadorRepo() {
        super(Operador.class);
    }

}
