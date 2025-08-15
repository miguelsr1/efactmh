package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
@ToString
public class DashboardDto implements Serializable {

    private Long idDashboard;
    private Long idContribuyente;
    private Long idFactura;
    private String abreviatura;
    private String numDocumento;
    private String nombreCompleto;
    private String correo;
    private String descripcion;
    private Integer estado;
    private String codigoDte;
    private Long idEstablecimiento;
    private Long idPuntoVenta;
    private LocalDateTime fechaCreacion;
    private BigDecimal monto;
    private String idFormaPago;
    private String descripcionFormaPago;
}
