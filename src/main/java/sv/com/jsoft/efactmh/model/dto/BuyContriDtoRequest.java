package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

@Data
public class BuyContriDtoRequest {
    private String json;
    private short anho;
    private short mes;
    private int idTipoDocumento;
    private String numDocumento;
}
