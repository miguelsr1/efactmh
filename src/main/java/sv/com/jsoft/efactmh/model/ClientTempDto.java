package sv.com.jsoft.efactmh.model;

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
public class ClientTempDto implements Serializable {

    private String nombre;
    private String correo;

}
