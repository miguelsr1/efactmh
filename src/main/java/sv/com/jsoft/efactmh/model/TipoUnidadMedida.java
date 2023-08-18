package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class TipoUnidadMedida implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idUnidadMedida;
    private String codigo;
    private String descripcion;

    public TipoUnidadMedida() {
    }
}
