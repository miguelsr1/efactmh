package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
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
public class PuntoVentaDto implements Serializable {
    private Long idPuntoVenta;
    private Long idEstablecimiento;
    private String nombrePuntoVenta;
    private String codigoPuntoVenta;
    private String codigoPuntoVentaMh;
    private Boolean activo;
}
