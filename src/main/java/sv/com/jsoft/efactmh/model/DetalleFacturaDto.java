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

    private String codigo;
    private BigDecimal cantidad;
    private String descripcion;
    private BigDecimal precioUni;

    public BigDecimal getSubTotal() {
        return cantidad.multiply(precioUni).setScale(2, RoundingMode.HALF_UP);
    }
}
