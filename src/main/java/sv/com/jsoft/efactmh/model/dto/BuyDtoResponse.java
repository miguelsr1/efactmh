package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;

/**
 *
 * @author admin
 */
@Data
public class BuyDtoResponse implements Serializable {
    private String codigoGeneracion;
    private String fecha;
    private String nit;
    private String nombre;
    private BigDecimal monto;
    private Timestamp fechacrea;
    private int tipoCompra;
}
