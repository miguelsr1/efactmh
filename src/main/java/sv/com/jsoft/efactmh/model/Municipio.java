package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idMunicipio;
    private String codigo;
    private String nombre;
    private Departamento codigoDepartamento;

    public Municipio() {
    }
}