package sv.com.jsoft.efactmh.model;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class DetallePago {
    private Long idDetallePago;
    private String tipoPago;
    private BigDecimal monto;
    private String numeroReferencia;
    private String plazo;
    private int periodoPlazo;
    
    public String getTipoPagoStr(){
        if(tipoPago == null){
            return "NA";
        }
        switch (tipoPago) {
            case "01":
                return "EFECTIVO";
            case "02":
                return "TARJETA DEBITO";
            case "03":
                return "TARJETA CREDITO";
            case "04":
                return "CHEQUE";
            case "05":
                return "TRANSFERENCIA-DEPOSITO BANCARIO";
            default:
                return "N/A";
        }
    }
    
    public String getTipoPlazoStr(){
        if(plazo == null){
            return "NA";
        }
        switch (plazo) {
            case "01":
                return "DÍAS";
            case "02":
                return "MESES";
            case "03":
                return "AÑOS";
            default:
                return "N/A";
        }
    }
}
