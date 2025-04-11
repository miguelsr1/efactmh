package sv.com.jsoft.efactmh.model;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class DetallePago {
    private BigDecimal monto;
    private String numeroReferencia;
    private String plazo;
    private int periodoPlazo;
}
