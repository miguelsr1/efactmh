package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class EmisorDto {

    private String codigoActividad;
    private Integer idMunicipio;
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
