package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idDetallePedido;
    private short activo;
    private Pedido idPedido;
    private Producto idProducto;

    public DetallePedido() {
    }
}
