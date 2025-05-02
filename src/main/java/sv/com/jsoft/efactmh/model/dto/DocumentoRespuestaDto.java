package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class DocumentoRespuestaDto implements Serializable {

    private String codigoGeneracion;
    private int versionApp;
    private String estado;
    private String fhProcesamiento;
    private String descripcionMsg;
    private String ambiente;
    private String clasificaMsg;
    private String selloRecibido;
    private List<String> observaciones;
    private String codigoMsg;
    private int version;
}
