package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ClienteDto implements Serializable {

    private Long idCliente;
    private String tipoPersoneria;
    private String nombre;
    private String nombreComercial;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nrc;
    private String correo;
    private Long idMunicipio;
}
