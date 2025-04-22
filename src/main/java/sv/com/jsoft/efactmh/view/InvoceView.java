package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.DetallePago;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.services.ComprobanteCreditoFiscalService;
import sv.com.jsoft.efactmh.services.ContribuyenteService;
import sv.com.jsoft.efactmh.services.DteService;
import sv.com.jsoft.efactmh.services.IdentificacionService;
import sv.com.jsoft.efactmh.services.ResumenService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@ViewScoped
@Named
@Slf4j
public class InvoceView implements Serializable {

    private final static ResourceBundle VARIABLES = ResourceBundle.getBundle("variables");

    private BigDecimal totalPagos;

    @Getter
    private boolean existeCliente = false;
    private int var = 1;
    @Getter
    private String taskSave;
    @Getter
    private String taskDte;
    @Getter
    private String taskSendDte;
    @Getter
    private String taskComplete;
    @Getter
    private String fontWeightSave;
    @Getter
    private String fontWeightDte;
    @Getter
    private String fontWeightSendDte;
    @Getter
    private String fontWeightComplete;
    @Getter
    @Setter
    private String numeroDocumento;
    @Getter
    @Setter
    private String tipoPago;
    @Getter
    @Setter
    private String nombreCliente;
    @Getter
    @Setter
    private String mensaje = "";
    @Getter
    @Setter
    private Integer activeStep = 0;
    @Getter
    private Integer advance = 0;

    @Getter
    @Setter
    private Producto producto;
    @Getter
    @Setter
    private DetallePago detPago;
    @Getter
    @Setter
    private List<DetallePago> lstDetPago;

    private Integer numeroPedido;
    private Date fechaPedido;
    private ClienteResponse cliente;
    private InvoceDto pedido;

    @Inject
    SessionView sessionView;

    @Inject
    SessionService securityService;

    @Inject
    DteService dteServices;

    @Inject
    private ContribuyenteService contribuyenteServices;
    @Inject
    private IdentificacionService identificacionServices;
    @Inject
    private ComprobanteCreditoFiscalService comprobanteCreditoFiscalServices;
    @Inject
    private ResumenService resumenServices;
    @Inject
    private SessionService sessionService;

    @Inject
    @Push(channel = "chatChannel")
    private PushContext push;

    private BigDecimal IVA;
    private String AMBIENTE_MH;
    private String NIT;
    private String PASS_PRI;

    @PostConstruct
    public void init() {
        fechaPedido = new Date();
        cliente = new ClienteResponse();
        pedido = new InvoceDto();
        numeroPedido = 0;
        lstDetPago = new ArrayList<>();
        detPago = new DetallePago();

        taskSave = "taskPending";
        taskDte = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightDte = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";

        IVA = new BigDecimal(VARIABLES.getString("mh.iva")).divide(new BigDecimal(100));
        AMBIENTE_MH = sessionService.getParametroDto().getAmbiente();
        NIT = sessionService.getParametroDto().getUserJwt();
        PASS_PRI = sessionService.getParametroDto().getPasswordPrivado();
    }

    //==========================================================================
    public Date getFechaPedido() {
        return fechaPedido;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public InvoceDto getPedido() {
        return pedido;
    }

    //metodo que valida si el establecimiento permite pago a plazo en modalida credito
    public boolean getAceptaPagoPlazo() {
        return sessionView.getAceptaPagoPlazo();
    }

    //==========================================================================
    public void findClient() {
        RestUtil rest = RestUtil
                .builder()
                .clazz(ClienteResponse.class)
                .jwtDto(securityService.getToken())
                .endpoint("/api/secured/client/" + numeroDocumento)
                .build();
        Object obj = rest.callGetOne();
        existeCliente = (obj != null);

        if (existeCliente) {
            cliente = (ClienteResponse) rest.callGetOne();
            nombreCliente = (cliente.getTipoPersoneria() == 1)
                    ? cliente.getNombreCompleto()
                    : cliente.getRazonSocial();
            pedido.setIdCliente(cliente.getIdCliente());
        } else {
            PrimeFaces.current().executeScript("PF('dlgAddCustomer').show()");
        }
        log.info(cliente.toString());
    }

    public BigDecimal getSubTotal() {
        return pedido.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getIva() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotal() {
        if (pedido.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return pedido.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void showDlgDetFactura() {

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .modal(true)
                .width("350px")
                .height("460px")
                .build();

        PrimeFaces.current().dialog().openDynamic("dialog/dlg-det-factura", options, null);
    }

    public void onDetFactura(SelectEvent<DetalleFacturaDto> event) {
        if (event.getObject() != null) {
            DetalleFacturaDto detFactura = event.getObject();
            pedido.getDetailInvoce().add(detFactura);
        }
    }

    public void addPaymentMethod() {
        lstDetPago.add(detPago);
        detPago = new DetallePago();
    }

    public BigDecimal getTotalPagos() {
        totalPagos = BigDecimal.ZERO;
        lstDetPago.forEach(pago -> {
            totalPagos = totalPagos.add(pago.getMonto());
        }
        );
        return totalPagos;
    }

    public String showAddCustomerDialog() {
        return "/app/mantto/cliente.xhtml";
    }

    public void removePaymentMethod(int index) {
        lstDetPago.remove(index);
    }

    public void removeDetInvoce(int index) {
        pedido.getDetailInvoce().remove(index);
    }

    public void backStep() {
        switch (activeStep) {
            case 0:

                break;
            case 1:
            case 2:
                activeStep--;
                break;
            default:
                break;
        }
    }

    public void nextStep() {
        switch (activeStep) {
            case 0:

                activeStep++;
                break;
            case 1:

                activeStep++;
                break;
            case 2:

                activeStep++;
                break;
            default:
                break;
        }
    }

    private String uuid;
    private Long idFac;

    public void save() {
        clearStatus();
        
        try {
            pedido.setDetailPayments(lstDetPago);
            
            RestUtil rest = RestUtil
                    .builder()
                    .clazz(IdDto.class)
                    .jwtDto(securityService.getToken())
                    .body(pedido)
                    .endpoint("/api/invoce").build();
            
            IdDto newInvoce = (IdDto) rest.callPostAuth();
            idFac = newInvoce.getId();
            
            push.send("" + var);
            var++;
            advance += 25;
            Thread.sleep(1000);
                        
            
            JSONObject jsonDte = getDteJson();
            
            push.send("" + var);
            var++;
            advance += 25;
            Thread.sleep(1000);
            
            log.info(jsonDte.toJSONString());
            uuid = UUID.randomUUID().toString().toUpperCase();
            
            JSONObject jsonFirmado = dteServices.getFirmarDocumento(jsonDte, sessionService.getParametroDto());
            
            push.send("" + var);
            var++;
            advance += 25;
            Thread.sleep(1000);
            
            JSONObject jsonResponse = dteServices.getProcesarMh(jsonFirmado.get("body").toString(),
                    securityService.getToken().getAccessToken(),
                    3,
                    pedido.getCodigoDte(),
                    uuid);
            
            push.send("" + var);
            var++;
            advance += 25;
            Thread.sleep(1000);
            
            log.info(jsonResponse.toJSONString());
        } catch (InterruptedException ex) {
            Logger.getLogger(InvoceView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviar(String mensaje) {
        push.send(mensaje); // Notifica al cliente vía WebSocket
    }

    private JSONObject getDteJson() {
        BigDecimal montoTotal = getTotal();
        BigDecimal ivaMonto = montoTotal.multiply(IVA);
        BigDecimal montoTotalAPagar = montoTotal.add(ivaMonto);

        JSONObject jsonRoot = new JSONObject();

        JSONObject jsonEmisor = contribuyenteServices.getContribuyente("", true);
        JSONObject jsonReceptor = contribuyenteServices.getContribuyente("", false);

        JSONObject jsonIdentificacion = identificacionServices.getIdentificacion(uuid, pedido.getCodigoDte(), idFac, 3, "00");
        JSONObject jsonResumen = resumenServices.getResumen(montoTotalAPagar, montoTotal, ivaMonto, pedido.getDetailPayments());
        JSONArray jsonCuerpoDoc = comprobanteCreditoFiscalServices.getCuerpoDocumento(pedido.getDetailInvoce());

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

    public void enviar() {
        try {
            push.send("" + var);
            var++;
            advance = 25;
            Thread.sleep(2000);
            push.send("" + var);
            var++;
            advance = 50;
            Thread.sleep(2000);
            push.send("" + var);
            var++;
            advance = 75;
            Thread.sleep(2000);
            push.send("" + var);
            var++;
            advance = 100;
        } catch (InterruptedException ex) {
            log.error("error enviarMensaje", ex);
        }

    }

    public void showActiveStep() {
        // Obtener el mensaje recibido del socket
        String message = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("data");

        switch (message) {
            case "1":
                taskSave = "taskComplete";
                fontWeightSave = "font-weight: bold";
                break;
            case "2":
                taskDte = "taskComplete";
                fontWeightDte = "font-weight: bold;";
                break;
            case "3":
                taskSendDte = "taskComplete";
                fontWeightSendDte = "font-weight: bold;";
                break;
            case "4":
                taskComplete = "taskComplete";
                fontWeightComplete = "font-weight: bold;";
                break;
            default:
                break;
        }
    }

    public void clearStatus() {
        taskSave = "taskPending";
        taskDte = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightDte = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";

        var = 1;
    }
}
