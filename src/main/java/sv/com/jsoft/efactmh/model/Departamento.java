package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nombre;
    private List<Municipio> municipioList;

    public Departamento() {
    }
}
