package sv.com.jsoft.efactmh.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class InvoicedAmountsDto implements Serializable {

    private String periodo;
    private BigDecimal fe;
    private BigDecimal ccf;
    private BigDecimal fse;
    private BigDecimal nr;
    private BigDecimal nc;
    private BigDecimal nd;
    private BigDecimal anu;
}
