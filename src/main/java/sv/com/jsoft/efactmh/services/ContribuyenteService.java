package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONObject;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;

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

        jsonEmisor.put("nit", "06141204841181W");
        jsonEmisor.put("nrc", "91669");
        jsonEmisor.put("correo", "miguelsr1@gmail.com");
        jsonEmisor.put("nombre", "MIGUEL ISAAC SANCHEZ RAMOS");
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

    private JSONObject getJsonReceptor(ClienteResponse client) {
        JSONObject jsonDireccion = new JSONObject();
        jsonDireccion.put("departamento", client.getDepartamento());
        jsonDireccion.put("municipio", client.getMunicipio());
        jsonDireccion.put("complemento", client.getDireccion());

        jsonReceptor.put("nit", client.getNit());
        jsonReceptor.put("nrc", client.getNrc());
        jsonReceptor.put("nombre", client.getNombreCompleto());
        jsonReceptor.put("codActividad", client.getCodigoActividad());
        jsonReceptor.put("descActividad", "MANIPULACION DE CARGA");
        jsonReceptor.put("nombreComercial", client.getNombreComercial());
        jsonReceptor.put("direccion", jsonDireccion);
        jsonReceptor.put("telefono", client.getTelefono());
        jsonReceptor.put("correo", client.getCorreo());

        return jsonReceptor;
    }

    public JSONObject getContribuyente(String nit, ClienteResponse client, boolean isEmisor) {
        if (isEmisor) {
            return getJsonEmisor(nit);
        } else {
            return getJsonReceptor(client);
        }
    }
    
    public void actualizar(){
        
    }
}
