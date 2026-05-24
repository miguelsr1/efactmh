package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoVentaDto implements Serializable {

    private Integer id;
    private String nombre;
}
