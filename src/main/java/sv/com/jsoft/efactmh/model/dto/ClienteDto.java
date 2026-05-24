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
    private String codigoTipoDoc;
}
