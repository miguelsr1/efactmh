package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
public class DteToInvalidate implements Serializable {

    private String codigoGeneracion;
    private String selloRecibido;
    private String nombreDte;
    private BigDecimal total;
    private String fechaProcesamiento;
}
