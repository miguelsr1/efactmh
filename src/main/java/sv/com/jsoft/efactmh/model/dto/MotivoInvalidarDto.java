package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
public class MotivoInvalidarDto {

    private int tipoAnulacion;
    private String nombreSolicita;
    private String numDocSolicita;
    private String tipoDocSolicita;
    private String motivoAnulacion;
    private String nombreResponsable;
    private String numDocResponsable;
    private String tipoDocResponsable;
}
