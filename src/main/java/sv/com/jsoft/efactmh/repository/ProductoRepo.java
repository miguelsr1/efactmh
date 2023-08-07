package sv.com.jsoft.efactmh.repository;

import javax.ejb.Stateless;
import sv.com.jsoft.efactmh.model.Producto;

/**
 *
 * @author migue
 */
@Stateless
public class ProductoRepo extends AbstractRepository<Producto, Integer> {

    public ProductoRepo() {
        super(Producto.class);
    }
}
