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
    private Boolean activo;
    private Pedido idPedido;
    private Producto idProducto;
    private Double cantidad;

    {
        cantidad = 0d;
    }

    public DetallePedido() {
    }

    public Producto getIdProducto() {
        if (idProducto == null) {
            idProducto = new Producto();
        }
        return idProducto;
    }

    public Double getSubTotal() {
        return cantidad * idProducto.getPrecioUnitario();
    }
}
