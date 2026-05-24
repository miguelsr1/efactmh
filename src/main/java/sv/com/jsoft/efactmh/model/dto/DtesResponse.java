package sv.com.jsoft.efactmh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtesResponse implements Serializable {

    private Long idFactura;
    private String abrevDte;
    private String tipoDte;
    private String numDocumento;
    private String nombre;
    private String correo;
    private String fechaCreacion;
    private String codigoGeneracion;
    private String selloRecibido;
    private String estadoDescripcion;
    private String observaciones;
    private short estado;
    private String codigoDte;
    private Long idEstablecimiento;
    private Long idPuntoVenta;
}
