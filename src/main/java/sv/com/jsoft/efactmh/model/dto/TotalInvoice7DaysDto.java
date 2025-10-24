package sv.com.jsoft.efactmh.model.dto;

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
public class TotalInvoice7DaysDto {

    private Long id;
    private String fecha;
    private Long monto;
}
