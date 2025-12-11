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
public class DetalleFacturaDto implements Serializable {

    private String codigoProducto;
    private String nombre;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private String codigoUnidad;
    private String codigoItem;

    public BigDecimal getSubTotal() {
        return cantidad.multiply(precioUnitario).setScale(6, RoundingMode.HALF_UP);
    }
}
