package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ClienteResponse {
    private Long idCliente;
    private String razonSocial;
    private String nombreCompleto;
    private int tipoPersoneria;
}