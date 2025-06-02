package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
public class Departamento implements Serializable {

    private String id;
    private String nombre;

    public Departamento() {
    }
}
