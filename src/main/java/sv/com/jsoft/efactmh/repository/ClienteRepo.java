package sv.com.jsoft.efactmh.repository;

import javax.ejb.Stateless;
import sv.com.jsoft.efactmh.model.Cliente;

/**
 *
 * @author migue
 */
@Stateless
public class ClienteRepo extends AbstractRepository<Cliente, Integer> {

    public ClienteRepo() {
        super(Cliente.class);
    }
}
