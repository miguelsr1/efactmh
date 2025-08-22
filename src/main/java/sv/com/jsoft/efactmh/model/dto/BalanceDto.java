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
public class BalanceDto {

    private Long limiteDte;
    private Long subTotal;
    private Long pendiente;
}
