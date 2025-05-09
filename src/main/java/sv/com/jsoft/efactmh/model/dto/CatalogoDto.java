package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
public class CatalogoDto implements Serializable {

    private String id;
    private String nombre;
}
