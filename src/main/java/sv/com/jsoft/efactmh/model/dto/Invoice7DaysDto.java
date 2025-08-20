package sv.com.jsoft.efactmh.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author msanchez
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Invoice7DaysDto {

    private String fecha;
    private Long cantidad;
    private BigDecimal monto;
    private String codigoDte;
}
