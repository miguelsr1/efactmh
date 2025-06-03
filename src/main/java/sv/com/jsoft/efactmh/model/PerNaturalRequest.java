package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
public class PerNaturalRequest implements Personeria, Serializable {

    private String personeria;
    private String nit;
    private String nrc;
    private String email;
    private String telefono;
    private String numDocumento;
    private String nombreCompleto;
    private Boolean activo;
    private Integer tipoDocumento;
    private String codigoActividad;
    private String departamento;
    private String municipio;
    private String direccion;
}
