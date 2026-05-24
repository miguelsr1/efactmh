package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DteToInvalidate implements Serializable {

    private String codigoGeneracion;
    private String selloRecibido;
    private String nombreDte;
    private BigDecimal total;
    private String fechaProcesamiento;
}
