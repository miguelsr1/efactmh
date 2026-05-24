package sv.com.jsoft.efactmh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmisorDto implements Serializable {

    private String codigoActividad;
    private String nit;
    private String nrc;
    private String nombreComercial;
    private String razonSocial;
    private String telefono;
    private String correo;
    private String direccion;
    private Boolean activo;
    private String codigoEstablecimiento;
    private String usuario;
}
