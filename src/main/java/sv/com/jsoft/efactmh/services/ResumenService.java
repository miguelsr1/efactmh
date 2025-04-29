package sv.com.jsoft.efactmh.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sv.com.jsoft.efactmh.model.DetallePago;
import sv.com.jsoft.efactmh.util.CantidadALetras;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class ResumenService {

    private JSONObject jsonResumen;
    private JSONArray jsonPagos;

    public JSONObject getResumen(BigDecimal montoTotalAPagar, BigDecimal montoTotal, BigDecimal ivaMonto, List<DetallePago> lstPagos) {
        jsonResumen = new JSONObject();
        JSONObject jsonTributo = new JSONObject();
        JSONArray jsonTributos = new JSONArray();

        jsonTributo.put("descripcion", "Impuesto al Valor Agregado 13%");
        jsonTributo.put("codigo", "20");
        jsonTributo.put("valor", ivaMonto.setScale(2, RoundingMode.UP));
        jsonTributos.add(jsonTributo);

        jsonResumen.put("pagos", getPagos(lstPagos));
        jsonResumen.put("ivaRete1", 0);
        jsonResumen.put("subTotal", montoTotal);
        jsonResumen.put("tributos", jsonTributos);
        //jsonResumen.put("ivaPerci1", 0); //ccfe, nce Y nde
        jsonResumen.put("totalIva", ivaMonto.setScale(2, RoundingMode.UP)); //ccfe, nce Y nde
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
    
    private JSONArray getPagos(List<DetallePago> lstPagos){
        JSONArray jsonPagos = new JSONArray();
        
        lstPagos.forEach(det->{
            JSONObject jsonPago = new JSONObject();
            jsonPago.put("codigo", det.getTipoPago());
            jsonPago.put("montoPago", det.getMonto());
            jsonPago.put("referencia", det.getNumeroReferencia());
            jsonPago.put("periodo", det.getPeriodoPlazo());
            jsonPago.put("plazo", det.getPlazo());
            
            jsonPagos.add(jsonPago);
        });
        
        return jsonPagos;
    }
}
