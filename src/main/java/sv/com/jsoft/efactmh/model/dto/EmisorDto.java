package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class EmisorDto {

    private String codigoActividad;
    private String nit;
    private String nrc;
    private String nombreComercial;
    private String razonSocial;
    private String telefono;
    private String correo;
    private int idMunicipio;
    private String direccion;
    private String codigoEstablecimiento;
    private boolean activo;
    private String usuario;
}
