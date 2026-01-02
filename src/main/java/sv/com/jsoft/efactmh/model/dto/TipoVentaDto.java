package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
public class TipoVentaDto implements Serializable {

    private Integer id;
    private String nombre;
}
