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
public class InvalidateRequest implements Serializable {

    /*private int tipoAnulacion;
    private String nombreSolicita;
    private String numDocSolicita;
    private String tipoDocSolicita;
    private String motivoAnulacion;
    private String nombreResponsable;
    private String numDocResponsable;
    private String tipoDocResponsable;*/
    private Long idFactura;
    //private String codigoGeneracionR;
    private Long idEstablecimiento;
    private Long idPuntoVenta;
}
