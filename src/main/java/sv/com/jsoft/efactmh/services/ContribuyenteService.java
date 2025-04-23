package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONObject;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.ReceptorDto;
import sv.com.jsoft.efactmh.util.RestUtil;

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

    private JSONObject getJsonReceptor(String numeroDocumento, JwtDto token) {

        RestUtil rest = RestUtil
                .builder()
                .clazz(ReceptorDto.class)
                .jwtDto(token)
                .endpoint("/api/dte/receptor/" + numeroDocumento)
                .build();

        ReceptorDto receptor = (ReceptorDto) rest
                .callGetOne();

        JSONObject jsonDireccion = new JSONObject();
        jsonDireccion.put("departamento", receptor.getDireccion().getDepartamento());
        jsonDireccion.put("municipio", receptor.getDireccion().getMunicipio());
        jsonDireccion.put("complemento", receptor.getDireccion().getComplemento());

        jsonReceptor.put("nit", receptor.getNit());
        jsonReceptor.put("nrc", receptor.getNrc());
        jsonReceptor.put("nombre", receptor.getNombre());
        jsonReceptor.put("codActividad", receptor.getCodActividad());
        jsonReceptor.put("descActividad", receptor.getDescActividad());
        jsonReceptor.put("nombreComercial", receptor.getNombreComercial());
        jsonReceptor.put("direccion", jsonDireccion);
        jsonReceptor.put("telefono", receptor.getTelefono());
        jsonReceptor.put("correo", receptor.getCorreo());

        return jsonReceptor;
    }

    public JSONObject getContribuyente(String numDocumentoEmisor, String numDocumentoReceptor, JwtDto token, boolean isEmisor) {
        if (isEmisor) {
            return getJsonEmisor(numDocumentoEmisor);
        } else {
            return getJsonReceptor(numDocumentoReceptor, token);
        }
    }

    public void actualizar() {

    }
}
