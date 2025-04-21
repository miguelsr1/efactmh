package sv.com.jsoft.efactmh.services;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import org.json.simple.JSONObject;

/**
 *
 * @author migue
 */
@ApplicationScoped
public class IdentificacionService {

    private final SimpleDateFormat sdDate = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdTime = new SimpleDateFormat("HH:mm:ss");
    private JSONObject jsonIdentificacion;

    public JSONObject getIdentificacion(String uuid, String codigoDte, Long idFactura, Integer idVersion, String ambiente) {
        jsonIdentificacion = new JSONObject();
        String numeroControl;
        numeroControl = MessageFormat.format("DTE-{0}-{1}-{2}", codigoDte, String.format("%0" + 8 + "d", 0), String.format("%0" + 15 + "d", idFactura));

        jsonIdentificacion.put("fecEmi", sdDate.format(new Date()));
        jsonIdentificacion.put("horEmi", sdTime.format(new Date()));
        jsonIdentificacion.put("tipoDte", codigoDte);
        jsonIdentificacion.put("version", idVersion);
        jsonIdentificacion.put("ambiente", ambiente);
        jsonIdentificacion.put("tipoModelo", 1);
        jsonIdentificacion.put("tipoMoneda", "USD");
        jsonIdentificacion.put("motivoContin", null);
        jsonIdentificacion.put("numeroControl", numeroControl);
        jsonIdentificacion.put("tipoOperacion", 1);
        jsonIdentificacion.put("codigoGeneracion", uuid);
        jsonIdentificacion.put("tipoContingencia", null);

        return jsonIdentificacion;
    }
}
