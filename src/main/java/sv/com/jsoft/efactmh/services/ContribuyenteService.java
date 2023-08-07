package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONObject;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class ContribuyenteService {

    private JSONObject jsonEmisor = new JSONObject();
    private JSONObject jsonReceptor = new JSONObject();

    private JSONObject getJsonEmisor(String nit) {
        JSONObject jsonDireccion = new JSONObject();

        jsonDireccion.put("departamento", "08");
        jsonDireccion.put("municipio", "05");
        jsonDireccion.put("complemento", "Barrio el centro, calle principal #5, Olocuilta, La Paz");

        jsonEmisor.put("nit", "08011403460017");
        jsonEmisor.put("nrc", "91669");
        jsonEmisor.put("correo", "stj.olocuilta@gmail.com");
        jsonEmisor.put("nombre", "AGUILAR DE ARTIGA, MARIA JULIA");
        jsonEmisor.put("telefono", "23306008");
        jsonEmisor.put("direccion", jsonDireccion);
        jsonEmisor.put("codEstable", "01");
        jsonEmisor.put("codActividad", "46900");
        jsonEmisor.put("codEstableMH", null);
        jsonEmisor.put("codPuntoVenta", "6");
        jsonEmisor.put("descActividad", "Venta al por mayor de otros productos");
        jsonEmisor.put("codPuntoVentaMH", null);
        jsonEmisor.put("nombreComercial", "SUPER TIENDA JULITA");
        jsonEmisor.put("tipoEstablecimiento", "02");

        return jsonEmisor;
    }

    private JSONObject getJsonReceptor(String nit) {
        JSONObject jsonDireccion = new JSONObject();
        jsonDireccion.put("departamento", "06");
        jsonDireccion.put("municipio", "14");
        jsonDireccion.put("complemento", "ENTRE LA 83 Y LA 85 AV.");

        jsonReceptor.put("nit", "06140107161013");
        jsonReceptor.put("nrc", "2514445");
        jsonReceptor.put("nombre", "TEC LOGISTICA, S.A DE C.V");
        jsonReceptor.put("codActividad", "52240");
        jsonReceptor.put("descActividad", "MANIPULACION DE CARGA");
        jsonReceptor.put("nombreComercial", null);
        jsonReceptor.put("direccion", jsonDireccion);
        jsonReceptor.put("telefono", null);
        jsonReceptor.put("correo", "stj.olocuilta@gmail.com");

        return jsonReceptor;
    }

    public JSONObject getContribuyente(String nit, boolean isEmisor) {
        if (isEmisor) {
            return getJsonEmisor(nit);
        } else {
            return getJsonReceptor(nit);
        }
    }
}
