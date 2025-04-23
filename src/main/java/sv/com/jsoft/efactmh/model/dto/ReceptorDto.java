package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ReceptorDto {

    private String nit;
    private String nrc;
    private String nombre;
    private String codActividad;
    private String descActividad;
    private String nombreComercial;
    private Direccion direccion;
    private String telefono;
    private String correo;

    @Data
    public class Direccion {
        private String departamento;
        private String municipio;
        private String complemento;
    }
}
