package sv.com.jsoft.efactmh.model;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
public class DetFacturaDto {

    private boolean eliminar;
    private String codigo;
    private String producto;
    private String descripcion;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
}
