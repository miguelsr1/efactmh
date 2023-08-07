package sv.com.jsoft.efactmh.repository;

import javax.ejb.Stateless;
import sv.com.jsoft.efactmh.model.Departamento;

/**
 *
 * @author migue
 */
@Stateless
public class DepartamentoRepo extends AbstractRepository<Departamento, String> {

    public DepartamentoRepo() {
        super(Departamento.class);
    }

}
