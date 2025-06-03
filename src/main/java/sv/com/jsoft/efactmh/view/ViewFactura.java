package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.DetFacturaDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.services.ComprobanteCreditoFiscalService;
import sv.com.jsoft.efactmh.services.ContribuyenteService;
import sv.com.jsoft.efactmh.services.DteService;
import sv.com.jsoft.efactmh.services.IdentificacionService;
import sv.com.jsoft.efactmh.services.ResumenService;
import sv.com.jsoft.efactmh.services.SessionService;

/**
 *
 * @author msanchez
 */
@Named
@ViewScoped
public class ViewFactura implements Serializable {

    private final DecimalFormat df = new DecimalFormat("0.00");

    private final static ResourceBundle VARIABLES = ResourceBundle.getBundle("variables");
    private BigDecimal IVA;
    private String AMBIENTE_MH;
    private String NIT;
    private String PASS_PRI;
    
    @Getter
    @Setter
    private Producto producto;

    @Getter
    @Setter
    private Integer idFac;

    @Getter
    @Setter
    private DetFacturaDto detFacturaDto = new DetFacturaDto();
    @Getter
    private List<DetFacturaDto> lstDetFactura = new ArrayList();

    @Getter
    private String responseMh = "";
    private String uuid = "";

    @Inject
    private ContribuyenteService contribuyenteServices;

    @Inject
    private ComprobanteCreditoFiscalService comprobanteCreditoFiscalServices;

    @Inject
    private ResumenService resumenServices;

    @Inject
    private IdentificacionService identificacionServices;

    @Inject
    private DteService dteServices;

    @Inject
    private CatalogoService catServices;
    
    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {
        IVA = new BigDecimal(VARIABLES.getString("mh.iva")).divide(new BigDecimal(100));
        AMBIENTE_MH = VARIABLES.getString("mh.ambiente");
        NIT = sessionService.getParametroDto().getUserJwt();
        PASS_PRI = sessionService.getParametroDto().getPwdJwt();
    }

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
        //JSONObject jsonToken = dteServices.getTokenMh();

        //String token = ((JSONObject) jsonToken.get("body")).get("token").toString();
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwODAxMTQwMzQ2MDAxNyIsImF1dGhvcml0aWVzIjpbIlVTRVIiLCJVU0VSX0FQSSIsIlVzdWFyaW8iXSwiaWF0IjoxNjg4NjIwMTEyLCJleHAiOjE2ODg3MDY1MTJ9.wUFdarxxJvGpkG7YRMMMoKD8SP1swKVhynEWNXp0EATSwcn3vRi6wfYj_T6aDs-0nCw3Ld9Oqvf2WzyzCYYU-A";

        idFac = 101;
        JSONObject jsonDte = getDteJsonCCFE(idFac);
        System.out.println(jsonDte.toJSONString());
        JSONObject jsonFirmado = dteServices.getFirmarDocumento(jsonDte, null);
        JSONObject jsonResponse = dteServices.getProcesarMh(jsonFirmado.get("body").toString(), token, "03", uuid);
        responseMh = jsonResponse.toJSONString();
    }

    private JSONObject getDteJsonCCFE(int id) {
        BigDecimal montoTotal = getTotal();
        BigDecimal ivaMonto = montoTotal.multiply(IVA);
        BigDecimal montoTotalAPagar = montoTotal.add(ivaMonto);

        JSONObject jsonRoot = new JSONObject();

        JSONObject jsonEmisor = null; // contribuyenteServices.getContribuyente("", true);
        JSONObject jsonReceptor = null; //contribuyenteServices.getContribuyente("",  false);

        JSONObject jsonIdentificacion = identificacionServices.getIdentificacion(uuid, "03", 0l, 3, "00");
        JSONObject jsonResumen = resumenServices.getResumen(null, montoTotalAPagar, montoTotal, ivaMonto, null);
        //esto es funcional para el tipo de dato DetFacturaDto.class
        //JSONArray jsonCuerpoDoc = comprobanteCreditoFiscalServices.getCuerpoDocumento(lstDetFactura);

        jsonRoot.put("emisor", jsonEmisor);
        jsonRoot.put("resumen", jsonResumen);
        jsonRoot.put("apendice", null);
        jsonRoot.put("receptor", jsonReceptor);
        jsonRoot.put("extension", null);
        jsonRoot.put("ventaTercero", null);
        jsonRoot.put("identificacion", jsonIdentificacion);
        //jsonRoot.put("cuerpoDocumento", jsonCuerpoDoc); //ARRAY
        jsonRoot.put("otrosDocumentos", null);
        jsonRoot.put("documentoRelacionado", null);

        return jsonRoot;
    }

    
    /*public List<Producto> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<Producto> lstProducto = catServices.getLstProducto();
       
        return lstProducto.stream().filter(t -> t.getCodigoItem().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }*/
}
