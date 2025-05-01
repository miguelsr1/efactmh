package sv.com.jsoft.efactmh.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class ComprobanteCreditoFiscalService {

    private int count;

    public JSONArray getCuerpoDocumento(List<DetalleFacturaDto> lstDetFactura, String codigoDte, BigDecimal iva) {
        count = 1;

        JSONArray jsonCuerpoDoc = new JSONArray();
        JSONArray jsonCodsTributos = new JSONArray();
        jsonCodsTributos.add(0, "20");
        
        JSONObject codsTributo = null;

        lstDetFactura.forEach(detFac -> {
            JSONObject jsonDoc = new JSONObject();
            jsonDoc.put("codTributo", null);
            jsonDoc.put("noGravado", 0);
            jsonDoc.put("psv", 0);
            jsonDoc.put("numeroDocumento", null);
            jsonDoc.put("ventaNoSuj", 0);
            jsonDoc.put("ventaExenta", 0);
            jsonDoc.put("ventaGravada", detFac.getCantidad().multiply(detFac.getPrecioUnitario()).setScale(2, RoundingMode.UP));
            
            switch(codigoDte){
                case "01":
                    jsonDoc.put("ivaItem", getMontoIva(codigoDte, (BigDecimal) jsonDoc.get("ventaGravada"), iva));
                    break;
            }            
            
            jsonDoc.put("tributos", "01".equals(codigoDte) ? codsTributo : jsonCodsTributos);
            jsonDoc.put("numItem", count);
            jsonDoc.put("tipoItem", 1);
            jsonDoc.put("cantidad", detFac.getCantidad());
            jsonDoc.put("codigo", null);
            jsonDoc.put("descripcion", detFac.getNombre());
            jsonDoc.put("uniMedida", 59);
            jsonDoc.put("precioUni", detFac.getPrecioUnitario());
            jsonDoc.put("montoDescu", 0);
            jsonCuerpoDoc.add(jsonDoc);
            count++;
        });

        return jsonCuerpoDoc;
    }

    private BigDecimal getMontoIva(String codigoDte, BigDecimal ventaGravada, BigDecimal iva) {
        switch (codigoDte) {
            case "01":
                return ventaGravada.multiply(iva).setScale(2, RoundingMode.UP);
            default:
                return BigDecimal.ZERO;
        }
    }
}
