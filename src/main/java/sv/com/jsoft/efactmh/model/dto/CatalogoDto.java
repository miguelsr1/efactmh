package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author migue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogoDto implements Serializable {

    private String id;
    private String nombre;
}
