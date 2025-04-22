package sv.com.jsoft.efactmh.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.model.dto.ParametroDto;
import sv.com.jsoft.efactmh.view.ViewFactura;

/**
 *
 * @author migue
 */
@ApplicationScoped
public class DteService {

    private final static ResourceBundle VARIABLES = ResourceBundle.getBundle("variables");

    private BigDecimal IVA;

    @Inject
    private ContribuyenteService contribuyenteServices;
    @Inject
    private IdentificacionService identificacionServices;
    @Inject
    private ComprobanteCreditoFiscalService comprobanteCreditoFiscalServices;
    @Inject
    private ResumenService resumenServices;

    @PostConstruct
    public void init() {
        IVA = new BigDecimal(VARIABLES.getString("mh.iva")).divide(new BigDecimal(100));
    }

    public JSONObject getDteJson(InvoceDto invoce, ClienteResponse client) {
        String uuid = UUID.randomUUID().toString().toUpperCase();

        BigDecimal montoTotal = getTotal(invoce);
        BigDecimal ivaMonto = montoTotal.multiply(IVA);
        BigDecimal montoTotalAPagar = montoTotal.add(ivaMonto);

        JSONObject jsonRoot = new JSONObject();

        JSONObject jsonEmisor = contribuyenteServices.getContribuyente("", null, true);
        JSONObject jsonReceptor = contribuyenteServices.getContribuyente("", client, false);

        JSONObject jsonIdentificacion = identificacionServices.getIdentificacion(uuid, 
                invoce.getCodigoDte(), 
                invoce.getIdFactura(), 
                getVesionDte(invoce.getCodigoDte()), 
                "00");
        
        JSONObject jsonResumen = resumenServices.getResumen(montoTotalAPagar, montoTotal, ivaMonto, invoce.getDetailPayments());
        JSONArray jsonCuerpoDoc = comprobanteCreditoFiscalServices.getCuerpoDocumento(invoce.getDetailInvoce(),
                invoce.getCodigoDte(),
                IVA);

        jsonRoot.put("emisor", jsonEmisor);
        jsonRoot.put("resumen", jsonResumen);
        jsonRoot.put("apendice", null);
        jsonRoot.put("receptor", jsonReceptor);
        jsonRoot.put("extension", null);
        jsonRoot.put("ventaTercero", null);
        jsonRoot.put("identificacion", jsonIdentificacion);
        jsonRoot.put("cuerpoDocumento", jsonCuerpoDoc); //ARRAY
        jsonRoot.put("otrosDocumentos", null);
        jsonRoot.put("documentoRelacionado", null);

        return jsonRoot;
    }

    private BigDecimal getTotal(InvoceDto invoce) {
        if (invoce.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return invoce.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Envia JSON firmado a MH
     *
     * @param dteFirmado
     * @param token
     * @param tipoDte
     * @param uuid
     * @return
     */
    public JSONObject getProcesarMh(String dteFirmado, String token, String tipoDte, String uuid) {
        try {
            Integer version = getVesionDte(tipoDte);

            JSONParser parser = new JSONParser();

            JSONObject jsonRequest = new JSONObject();

            jsonRequest.put("version", version);
            jsonRequest.put("ambiente", "00");
            jsonRequest.put("idEnvio", 1);
            jsonRequest.put("tipoDte", tipoDte);
            jsonRequest.put("codigoGeneracion", uuid);
            jsonRequest.put("documento", dteFirmado);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VARIABLES.getString("url.mh.test.recepcion")))
                    .headers("Content-Type", "application/json")
                    .headers("Authorization", ((JSONObject)getTokenMh().get("body")).get("token").toString())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toJSONString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

            return jsonResponse;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private int getVesionDte(String codigoDte) {
        switch (codigoDte) {
            case "01":
                return 1;
            case "03":
                return 3;
            case "04":
                return 3;
            case "05":
                return 3;
            case "06":
                return 3;
            case "07":
                return 1;
            case "08":
                return 1;
            case "09":
                return 1;
            case "11":
                return 1;
            case "14":
                return 1;
            case "15":
                return 1;
            default:
                return 0;
        }
    }

    public JSONObject getTokenMh() {
        try {
            JSONParser parser = new JSONParser();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("user", VARIABLES.getString("nit"));
            parameters.put("pwd", VARIABLES.getString("pass.pub"));

            String form = parameters.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VARIABLES.getString("url.mh.test.autenticador")))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());

            return jsonResponse;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Firmador del JSON, previo al envio a MH
     *
     * @param jsonDte
     * @param parametroDto
     * @return
     */
    public JSONObject getFirmarDocumento(JSONObject jsonDte, ParametroDto parametroDto) {
        try {
            JSONParser parser = new JSONParser();

            JSONObject jsonFirmador = new JSONObject();

            jsonFirmador.put("nit", parametroDto.getUserJwt());
            jsonFirmador.put("activo", true);
            jsonFirmador.put("passwordPri", parametroDto.getPasswordPrivado());
            jsonFirmador.put("dteJson", jsonDte);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VARIABLES.getString("url.test.firmador")))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonFirmador.toJSONString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonFirmado = (JSONObject) parser.parse(response.body());
            return jsonFirmado;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
