package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private InvoceDto idPedido;
    private Producto idProducto;
    private BigDecimal cantidad;

    public DetallePedido() {
        cantidad = BigDecimal.ZERO;
    }

    public Producto getIdProducto() {
        if (idProducto == null) {
            idProducto = new Producto();
        }
        return idProducto;
    }

    public BigDecimal getSubTotal() {
        return cantidad.multiply(idProducto.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);
    }
}
