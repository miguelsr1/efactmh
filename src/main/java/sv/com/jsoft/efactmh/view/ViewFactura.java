package sv.com.jsoft.efactmh.view;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.DetFacturaDto;
import sv.com.jsoft.efactmh.services.ComprobanteCreditoFiscalServices;
import sv.com.jsoft.efactmh.services.ContribuyenteServices;
import sv.com.jsoft.efactmh.util.CantidadALetras;

/**
 *
 * @author msanchez
 */
@Named
@ViewScoped
public class ViewFactura implements Serializable {

    @Getter
    @Setter
    private Integer idFac;

    private final DecimalFormat df = new DecimalFormat("0.00");
    private final SimpleDateFormat sdDate = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdTime = new SimpleDateFormat("HH:mm:ss");

    @Getter
    @Setter
    private DetFacturaDto detFacturaDto = new DetFacturaDto();
    @Getter
    private List<DetFacturaDto> lstDetFactura = new ArrayList();

    @Getter
    private String responseMh = "";
    private String uuid = "";

    @Inject
    private ContribuyenteServices contribuyenteServices;
    
    @Inject
    private ComprobanteCreditoFiscalServices comprobanteCreditoFiscalServices;

    public void addDetFact() {
        if (detFacturaDto.getCodigo() == null || detFacturaDto.getProducto() == null
                || detFacturaDto.getCantidad() == null || detFacturaDto.getPrecioUnitario() == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ALERTA", "TODOS LOS CAMPOS SON REQUERIDOS");
            PrimeFaces.current().dialog().showMessageDynamic(message);
        } else {
            lstDetFactura.add(detFacturaDto);
            detFacturaDto = new DetFacturaDto();
        }
    }

    public BigDecimal getTotal() {
        return lstDetFactura.stream()
                .map(x -> x.getCantidad().multiply(x.getPrecioUnitario()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void procesar() {
        uuid = UUID.randomUUID().toString().toUpperCase();
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwNjE0MDIwMzk4MTAxMyIsImF1dGhvcml0aWVzIjpbIlVTRVIiLCJVU0VSX0FQSSIsIlVzdWFyaW8iXSwiaWF0IjoxNjg4MTM3MDk0LCJleHAiOjE2ODgyMjM0OTR9.KZAnDsLhEIVxR8-2q5VHSiyCEMriJR3mdz-uMUQqGLScgzoIjxINKgs5_8PaYIIyGrv_aDDEi82hLfiNLx5tNg";

        JSONObject jsonDte = getDteJsonCCFE(1);
        System.out.println(jsonDte.toJSONString());
        JSONObject jsonFirmado = getFirmarDocumento(jsonDte);
        getProcesarMh(jsonFirmado.get("body").toString(), token, 3, "03");
    }

    private JSONObject getDteJsonCCFE(int id) {
        BigDecimal montoTotal = getTotal();
        BigDecimal ivaMonto = montoTotal.multiply(new BigDecimal("0.13"));
        BigDecimal montoTotalAPagar = montoTotal.add(ivaMonto);

        JSONObject jsonRoot = new JSONObject();
        JSONArray jsonCuerpoDoc = comprobanteCreditoFiscalServices.getCuerpoDocumento(lstDetFactura);
        JSONArray jsonTributos = new JSONArray();
        JSONObject jsonReceptor = contribuyenteServices.getContribuyente("", true);
        JSONObject jsonEmisor = contribuyenteServices.getContribuyente("", false);

        JSONObject jsonIdentificacion = new JSONObject();
        JSONObject jsonResumen = new JSONObject();
        JSONObject jsonTributo = new JSONObject();

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

        jsonIdentificacion.put("fecEmi", sdDate.format(new Date()));
        jsonIdentificacion.put("horEmi", sdTime.format(new Date()));
        jsonIdentificacion.put("tipoDte", "03");
        jsonIdentificacion.put("version", 3);
        jsonIdentificacion.put("ambiente", "00");
        jsonIdentificacion.put("tipoModelo", 1);
        jsonIdentificacion.put("tipoMoneda", "USD");
        jsonIdentificacion.put("motivoContin", null);
        jsonIdentificacion.put("numeroControl", "DTE-03-00000000-" + String.format("%0" + 15 + "d", idFac));
        jsonIdentificacion.put("tipoOperacion", 1);
        jsonIdentificacion.put("codigoGeneracion", uuid);
        jsonIdentificacion.put("tipoContingencia", null);

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

    public JSONObject getFirmarDocumento(JSONObject jsonDte) {
        try {
            JSONParser parser = new JSONParser();

            JSONObject jsonFirmador = new JSONObject();

            jsonFirmador.put("nit", "06140203981013");
            jsonFirmador.put("activo", true);
            jsonFirmador.put("passwordPri", "xiXDPXyG75kppZ");
            jsonFirmador.put("dteJson", jsonDte);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://192.168.10.41:8113/firmardocumento/"))
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

    public JSONObject getProcesarMh(String dteFirmado, String token, int version, String tipoDte) {
        try {
            JSONParser parser = new JSONParser();

            JSONObject jsonRequest = new JSONObject();

            jsonRequest.put("version", version);
            jsonRequest.put("ambiente", "00");
            jsonRequest.put("idEnvio", 1);
            jsonRequest.put("tipoDte", tipoDte);
            jsonRequest.put("codigoGeneracion", uuid);
            jsonRequest.put("documento", dteFirmado);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://apitest.dtes.mh.gob.sv/fesv/recepciondte"))
                    .headers("Content-Type", "application/json")
                    .headers("Authorization", token)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toJSONString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = (JSONObject) parser.parse(response.body());
            responseMh = jsonResponse.toJSONString();

            if (jsonResponse.get("estado").equals("PROCESADO")) {
                idFac = null;
                detFacturaDto = new DetFacturaDto();
                lstDetFactura.clear();
                System.out.println("respuesta mh: " + responseMh);
            }

            return jsonResponse;
        } catch (URISyntaxException | IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(ViewFactura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
