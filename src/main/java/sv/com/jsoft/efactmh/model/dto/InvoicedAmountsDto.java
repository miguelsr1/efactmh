package sv.com.jsoft.efactmh.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
public class InvoicedAmountsDto {

    private String periodo;
    private BigDecimal fe;
    private BigDecimal ccf;
    private BigDecimal fse;
    private BigDecimal nr;
    private BigDecimal nc;
    private BigDecimal nd;
    private BigDecimal anu;
}
