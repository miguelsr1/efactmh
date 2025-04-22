package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ClienteResponse {
    private Long idCliente;
    /*private String nit;
    private String nrc;
    private String codigoActividad;*/
    private String razonSocial;
    /*private String nombreComercial;
    private String telefono;
    private String correoEmp;
    private String municipioEmp;
    private String departamentoEmp;
    private String direccionEmp;*/
    private String nombreCompleto;
    /*private String correo;
    private String tipoDocumento;
    private String numDocumento;
    private String municipio;
    private String departamento;
    private String direccion;*/
    private int tipoPersoneria;
}
