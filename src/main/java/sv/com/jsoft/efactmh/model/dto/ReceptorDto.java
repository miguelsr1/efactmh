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
public class ReceptorDto implements Serializable {

    private String nrc;
    private String tipoDocumento;
    private String numDocumento;
    private String nombre;
    private String codActividad;
    private String descActividad;
    private String nombreComercial;
    private Direccion direccion;
    private String telefono;
    private String correo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Direccion {
        private String departamento;
        private String municipio;
        private String complemento;
    }
}
