package sv.com.jsoft.efactmh.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author migue
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AmountDto {
    private Long id;
    private String fecha;
    private BigDecimal monto;
}
