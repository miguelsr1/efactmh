package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONObject;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class ContribuyenteServices {

    private JSONObject jsonEmisor = new JSONObject();
    private JSONObject jsonReceptor = new JSONObject();

    private JSONObject getJsonEmisor(String nit) {
        JSONObject jsonDireccion = new JSONObject();

        jsonDireccion.put("departamento", "06");
        jsonDireccion.put("municipio", "14");
        jsonDireccion.put("complemento", "BLVD TUTUNICHAPA Y AV LEGAZPI #11 URB SIGLO XXI");

        jsonEmisor.put("nit", "06140203981013");
        jsonEmisor.put("nrc", "1047582");
        jsonEmisor.put("correo", "prueba@prueba.com");
        jsonEmisor.put("nombre", "SERTRACEN, S.A. DE C.V.");
        jsonEmisor.put("telefono", "2261-7300");
        jsonEmisor.put("direccion", jsonDireccion);
        jsonEmisor.put("codEstable", "01");
        jsonEmisor.put("codActividad", "94990");
        jsonEmisor.put("codEstableMH", "m123");
        jsonEmisor.put("codPuntoVenta", null);
        jsonEmisor.put("descActividad", "Actividades de asociaciones n.c.p.");
        jsonEmisor.put("codPuntoVentaMH", null);
        jsonEmisor.put("nombreComercial", "SERTRACEN, S.A. DE C.V.");
        jsonEmisor.put("tipoEstablecimiento", "01");

        return jsonEmisor;
    }

    private JSONObject getJsonReceptor(String nit) {
        JSONObject jsonDireccion = new JSONObject();
        jsonDireccion.put("departamento", "06");
        jsonDireccion.put("municipio", "14");
        jsonDireccion.put("complemento", "1 AVENIDA SUR 630");

        jsonReceptor.put("codActividad", "46298");
        jsonReceptor.put("descActividad", "ESTA ES UNA PRUEBA");
        jsonReceptor.put("correo", "sin_correo@dominio.com");
        jsonReceptor.put("direccion", jsonDireccion);
        jsonReceptor.put("nit", "06171402851016");
        jsonReceptor.put("nombreComercial", null);
        jsonReceptor.put("telefono", "2133-3600");
        jsonReceptor.put("nrc", "2952842");
        jsonReceptor.put("nombre", "VICE MINISTERIO DE TRANSPORTE");

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
