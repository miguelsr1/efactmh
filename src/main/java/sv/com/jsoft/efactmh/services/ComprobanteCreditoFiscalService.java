package sv.com.jsoft.efactmh.services;

import java.math.RoundingMode;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sv.com.jsoft.efactmh.model.DetFacturaDto;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class ComprobanteCreditoFiscalService {

    private int count;

    public JSONArray getCuerpoDocumento(List<DetFacturaDto> lstDetFactura) {
        count = 1;

        JSONArray jsonCuerpoDoc = new JSONArray();
        JSONArray codsTributor = new JSONArray();
        codsTributor.add(0, "20");

        lstDetFactura.forEach(detFac -> {
            JSONObject jsonDoc = new JSONObject();
            jsonDoc.put("psv", 0);
            jsonDoc.put("codigo", null);
            jsonDoc.put("numItem", count);
            jsonDoc.put("cantidad", detFac.getCantidad());
            jsonDoc.put("tipoItem", 1);
            jsonDoc.put("tributos", codsTributor);
            jsonDoc.put("noGravado", 0);
            jsonDoc.put("precioUni", detFac.getPrecioUnitario());
            jsonDoc.put("uniMedida", 59);
            jsonDoc.put("codTributo", null);
            jsonDoc.put("montoDescu", 0);
            jsonDoc.put("ventaNoSuj", 0);
            jsonDoc.put("descripcion", detFac.getProducto());
            jsonDoc.put("ventaExenta", 0);
            jsonDoc.put("ventaGravada", detFac.getCantidad().multiply(detFac.getPrecioUnitario()).setScale(2, RoundingMode.UP));
            jsonDoc.put("numeroDocumento", null);
            jsonCuerpoDoc.add(jsonDoc);
            count++;
        });

        return jsonCuerpoDoc;
    }
}
