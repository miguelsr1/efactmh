package sv.com.jsoft.efactmh.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sv.com.jsoft.efactmh.util.CantidadALetras;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class ResumenServices {

    private JSONObject jsonResumen;

    public JSONObject getResumen(BigDecimal montoTotalAPagar, BigDecimal montoTotal, BigDecimal ivaMonto) {
        jsonResumen = new JSONObject();
        JSONObject jsonTributo = new JSONObject();
        JSONArray jsonTributos = new JSONArray();

        jsonTributo.put("descripcion", "Impuesto al Valor Agregado 13%");
        jsonTributo.put("codigo", "20");
        jsonTributo.put("valor", ivaMonto.setScale(2, RoundingMode.UP));
        jsonTributos.add(jsonTributo);

        jsonResumen.put("pagos", null);
        jsonResumen.put("ivaRete1", 0);
        jsonResumen.put("subTotal", montoTotal);
        jsonResumen.put("tributos", jsonTributos);
        jsonResumen.put("ivaPerci1", 0);
        jsonResumen.put("reteRenta", 0);
        jsonResumen.put("descuNoSuj", 0);
        jsonResumen.put("saldoFavor", 0);
        jsonResumen.put("totalDescu", 0);
        jsonResumen.put("totalNoSuj", 0);
        jsonResumen.put("totalPagar", montoTotalAPagar.setScale(2, RoundingMode.UP));
        jsonResumen.put("descuExenta", 0);
        jsonResumen.put("totalExenta", 0);
        jsonResumen.put("totalLetras", CantidadALetras.aLetras(montoTotal) + " Dolares Americanos");
        jsonResumen.put("descuGravada", 0);
        jsonResumen.put("totalGravada", montoTotal);
        jsonResumen.put("subTotalVentas", montoTotal);
        jsonResumen.put("totalNoGravado", 0);
        jsonResumen.put("condicionOperacion", 2);
        jsonResumen.put("numPagoElectronico", null);
        jsonResumen.put("montoTotalOperacion", montoTotalAPagar.setScale(2, RoundingMode.UP));
        jsonResumen.put("porcentajeDescuento", 0);

        return jsonResumen;
    }
}
