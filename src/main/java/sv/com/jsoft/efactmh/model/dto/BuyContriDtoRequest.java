package sv.com.jsoft.efactmh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyContriDtoRequest implements Serializable {
    private String json;
    private short anho;
    private short mes;
    private int idTipoDocumento;
    private String numDocumento;
}
