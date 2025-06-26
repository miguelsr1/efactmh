package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
public class PerJuridicaRequest implements Personeria, Serializable {

    private String personeria;
    private String nit;
    private String nrc;
    private String emailEmp;
    private String telefono;
    private String razonSocial;
    private String nombreComercial;
    private Boolean activo;
    private String codigoActividad;
    private String departamentoEmp;
    private String municipioEmp;
    private String direccionEmp;

    public PerJuridicaRequest() {
        this.personeria = "J";
        this.activo = true;
    }
}
