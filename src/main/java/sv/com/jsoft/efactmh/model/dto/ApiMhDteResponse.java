package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ApiMhDteResponse implements Serializable {

    private String estado;
    private String descripcionMsg;
    private List<String> observaciones;
}
