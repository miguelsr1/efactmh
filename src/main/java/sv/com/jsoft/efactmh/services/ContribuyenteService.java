package sv.com.jsoft.efactmh.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
@Slf4j
public class ContribuyenteService {

    @Inject
    SessionService sessionService;

    public JSONObject getJsonEmisor(String nit, Long idEstablecimiento, Long idPuntoVenta) {
        try {
            JSONParser parser = new JSONParser();

            ResponseRestApi<String> strEmisor = RestUtil
                    .builder()
                    .clazz(String.class)
                    .accessToken(sessionService.getAccessTokenString())
                    .endpoint("/api/secured/dte/emisor/" + nit + "/" + idEstablecimiento + "/" + idPuntoVenta)
                    .build()
                    .callGetOneAuth();

            return (JSONObject) parser.parse(strEmisor.getBody());
        } catch (ParseException ex) {
            log.error("ERROR OBTENIENDO EMISOR: " + nit);
            return null;
        }
    }

    public JSONObject getJsonReceptor(String codigoDte, String numDocumento) {
        try {
            JSONParser parser = new JSONParser();

            ResponseRestApi<String> strReceptor = RestUtil
                    .builder()
                    .clazz(String.class)
                    .accessToken(sessionService.getAccessTokenString())
                    .endpoint("/api/secured/dte/receptor/" + codigoDte + "/" + numDocumento)
                    .build()
                    .callGetOneAuth();

            return (JSONObject) parser.parse(strReceptor.getBody());
        } catch (ParseException ex) {
            log.error("ERROR OBTENIENDO RECEPTOR: " + numDocumento);
            return null;
        }
    }
}
