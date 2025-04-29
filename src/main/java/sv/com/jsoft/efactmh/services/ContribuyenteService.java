package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.ReceptorDto;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
@Slf4j
public class ContribuyenteService {

    public JSONObject getJsonEmisor(String nit, Long idEstablecimiento, Long idPuntoVenta, JwtDto token) {
        try {
            JSONParser parser = new JSONParser();

            RestUtil rest = RestUtil
                    .builder()
                    .clazz(String.class)
                    .jwtDto(token)
                    .endpoint("/api/secured/dte/emisor/" + nit + "/" + idEstablecimiento + "/" + idPuntoVenta)
                    .build();

            String strEmisor = rest.callGetOne().toString();

            return (JSONObject) parser.parse(strEmisor);
        } catch (ParseException ex) {
            log.error("ERROR OBTENIENDO EMISOR: " + nit);
            return null;
        }
    }

    public JSONObject getJsonReceptor(String numDocumento, JwtDto token) {
        try {
            JSONParser parser = new JSONParser();

            RestUtil rest = RestUtil
                    .builder()
                    .clazz(ReceptorDto.class)
                    .jwtDto(token)
                    .endpoint("/api/secured/dte/receptor/" + numDocumento)
                    .build();

            String strEmisor = rest.callGetOne().toString();

            return (JSONObject) parser.parse(strEmisor);
        } catch (ParseException ex) {
            log.error("ERROR OBTENIENDO RECEPTOR: " + numDocumento);
            return null;
        }
    }


    public void actualizar() {

    }
}
