package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.DetallePago;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.model.dto.SendDteRequest;
import sv.com.jsoft.efactmh.services.DteService;
import sv.com.jsoft.efactmh.services.InvoceService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.Constantes;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ALERT;
import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
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
    private boolean makeInvoce = false;
    /*@Getter
    @Setter
    private boolean aplicaRetencionIsr = false;*/

    private int var = 1;
    @Getter
    private String taskSave;
    @Getter
    private String taskSendDte;
    @Getter
    private String taskComplete;
    @Getter
    private String fontWeightSave;
    @Getter
    private String fontWeightSendDte;
    @Getter
    private String fontWeightComplete;
    @Getter
    private String codigoGeneracion;
    @Getter
    @Setter
    private String numDocumentoReceptor;
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
    private boolean sinDatos = true;
    @Getter
    @Setter
    private Producto producto;
    private DetallePago detPago;
    @Getter
    @Setter
    private List<DetallePago> lstDetPago;
    @Getter
    @Setter
    private List<CatalogoDto> lstMetodoPago;

    private Date fechaPedido;
    private ClienteResponse cliente;
    private InvoceDto invoceDto;

    @Inject
    SessionView sessionView;
    @Inject
    SessionService securityService;
    @Inject
    DteService dteServices;
    @Inject
    InvoceService invoceService;

    @Inject
    @Push(channel = "chatChannel")
    private PushContext push;

    private Long idFac;

    @PostConstruct
    public void init() {
        fechaPedido = new Date();
        cliente = new ClienteResponse();
        invoceDto = new InvoceDto();
        invoceDto.setCodigoDte("01");

        invoceDto.setCondicionOperacion("1"); //CONTADO POR DEFECTO

        lstDetPago = new ArrayList<>();
        detPago = new DetallePago();
        detPago.setTipoPago("01"); //EFECTIVO

        taskSave = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";

        loadMetodoPago();

        facturaSinDatos();
    }

    public DetallePago getDetPago() {
        return detPago;
    }

    public void setDetPago(DetallePago detPago) {
        if (detPago != null) {
            this.detPago = detPago;
        }
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public InvoceDto getInvoce() {
        return invoceDto;
    }

    public void loadMetodoPago() {
        lstMetodoPago = new ArrayList<>();
        switch (invoceDto.getCondicionOperacion()) {
            case "1":
                lstMetodoPago.add(new CatalogoDto("01", "EFECTIVO"));
                detPago.setTipoPago("01");
                break;
            case "2":
            case "3":
                lstMetodoPago.add(new CatalogoDto("02", "TARJETA DE DEBITO"));
                lstMetodoPago.add(new CatalogoDto("03", "TARJETA DE CREDITO"));
                lstMetodoPago.add(new CatalogoDto("04", "CHEQUE"));
                lstMetodoPago.add(new CatalogoDto("05", "TRANSFERENCIA-DEPOSITO BANCARIO"));
                detPago.setTipoPago("02");
                break;
        }
    }

    //==========================================================================
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
                .endpoint("/api/secured/client/" + numDocumentoReceptor.replace(" ", ""))
                .build();
        ResponseRestApi obj = rest.callGetOneAuth();

        if (obj.getCodeHttp() == 200) {
            cliente = (ClienteResponse) obj.getBody();

            if (invoceDto.getCodigoDte().equals("03") && !cliente.getInscritoIva()) {

                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title(MSG_ALERT)
                        .message("EL RECEPTOR DEBE DE ESTAR INSCRITO AL IVA PARA EMITIR UN CCF")
                        .build()
                        .showMessage();

                cliente = new ClienteResponse();
                numDocumentoReceptor = "";
                return;
            }

            nombreCliente = (cliente.getTipoPersoneria() == 1)
                    ? cliente.getNombreCompleto()
                    : cliente.getRazonSocial();
            invoceDto.setIdCliente(cliente.getIdCliente());
        } else {
            PrimeFaces.current().executeScript("PF('dlgAddCustomer').show()");
        }
        log.info(cliente.toString());
    }

    public BigDecimal getSumas() {
        return invoceDto.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubTotal() {
        return getSumas().add(getIva()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getIva() {
        switch (invoceDto.getCodigoDte()) {
            case "01":
                return BigDecimal.ZERO;
            case "03":
                return getSumas().multiply(new BigDecimal(0.13)).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getIvaRetenido() {
        switch (invoceDto.getCodigoDte()) {
            case "01":
                return BigDecimal.ZERO;
            case "03":
                if (invoceDto.isAplicaIvaRetenido()) {
                    BigDecimal porcentajeIva = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(100));
                    return invoceDto.isAplicaIvaRetenido() ? getSumas().multiply(porcentajeIva) : getSumas().setScale(2, RoundingMode.HALF_UP);
                } else {
                    return BigDecimal.ZERO;
                }
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getRentaRetenido() {
        switch (invoceDto.getCodigoDte()) {
            case "01":
                return BigDecimal.ZERO;
            case "03":
                if (invoceDto.isAplicaRentaRetenido()) {
                    BigDecimal porcentajeIsr = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100));
                    return invoceDto.isAplicaRentaRetenido() ? getSumas().multiply(porcentajeIsr) : getSumas().setScale(2, RoundingMode.HALF_UP);
                } else {
                    return BigDecimal.ZERO;
                }
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getTotal() {
        switch (invoceDto.getCodigoDte()) {
            case "01":
                return getTotalFe();
            case "03":
                return getTotalCcf();
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal getTotalFe() {
        if (invoceDto.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return invoceDto.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getTotalCcf() {
        if (invoceDto.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return getSumas()
                .add(getIva())
                .add(getIvaRetenido().negate())
                .add(getRentaRetenido().negate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void showDlgDetFactura() {

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .modal(true)
                .width("350px")
                .height("550px")
                .build();

        PrimeFaces.current().dialog().openDynamic("dialog/dlg-det-factura", options, null);
    }

    public void onDetFactura(SelectEvent<DetalleFacturaDto> event) {
        if (event.getObject() != null) {
            DetalleFacturaDto detFactura = event.getObject();
            invoceDto.getDetailInvoce().add(detFactura);
        }
    }

    public void addPaymentMethod() {
        lstDetPago.add(detPago);
        detPago = new DetallePago();
        detPago.setTipoPago("01"); //EFECTIVO

        invoceDto.setCondicionOperacion("1");
    }

    public BigDecimal getTotalPagos() {
        totalPagos = BigDecimal.ZERO;
        lstDetPago.forEach(pago -> {
            totalPagos = totalPagos.add(pago.getMonto()).setScale(2, RoundingMode.HALF_UP);
        }
        );
        return totalPagos;
    }

    public String showAddCustomerDialog() {
        return "/app/mantto/cliente.xhtml";
    }

    public void removePaymentMethod(int index) {
        lstDetPago.remove(index);
        detPago = new DetallePago();
        detPago.setTipoPago("01"); //EFECTIVO

        invoceDto.setCondicionOperacion("1");
    }

    public void removeDetInvoce(int index) {
        invoceDto.getDetailInvoce().remove(index);
    }

    public void backStep() {
        switch (activeStep) {
            case 0:

                break;
            case 1:
            case 2:
                lstDetPago.clear();
                activeStep--;
                break;
            default:
                break;
        }
    }

    public void nextStep() {
        if (stepValidate()) {
            switch (activeStep) {
                case 0:
                    activeStep++;
                    break;
                case 1:
                    activeStep++;
                    detPago.setMonto(getTotal());
                    break;
                case 2:
                    activeStep++;
                    break;
                default:
                    break;
            }
        }
    }

    private boolean stepValidate() {
        switch (activeStep) {
            case 0:
                switch (invoceDto.getCodigoDte()) {
                    case "01": //FE
                        if (cliente.getIdCliente() == null) {
                            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                                    MSG_ALERT,
                                    "POR FAVOR AGREGE UN CLIENTE");
                            return false;
                        }
                        return true;
                    case "03": //CCF
                        if (cliente.getInscritoIva()) {
                            return true;
                        } else {
                            MessageUtil.builder()
                                    .severity(FacesMessage.SEVERITY_WARN)
                                    .title(MSG_ALERT)
                                    .message("EL CLIENTE SELECCIONADO DEBE DE ESTAR INSCRITO AL IVA")
                                    .build().showMessage();
                            return false;
                        }
                    default:
                        return true;
                }
            case 1:
                //VALIDAR QUE EXISTA UN ITEM AGREGADO EN LA FACTURA
                if (invoceDto.getDetailInvoce().isEmpty()) {
                    JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                            MSG_ALERT,
                            "POR FAVOR AGREGE UN ITEM A LA FACTURA");
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void preSave() {
        makeInvoce = validatePreSend();

        if (makeInvoce) {
            lstDetPago.forEach(det -> det.getMonto());

            try {
                invoceDto.setDetailPayments(lstDetPago);
                invoceDto.setIdEstablecimiento(Long.valueOf(sessionView.getIdEstablecimiento()));
                invoceDto.setIdPuntoVenta(sessionView.getIdPuntoVenta() != null ? Long.valueOf(sessionView.getIdPuntoVenta()) : null);

                //Persistiendo factura
                log.info("PERSISTIENDO FACTURA: " + invoceDto.toString());
                ResponseRestApi response = invoceService.saveInvoce(securityService.getToken(), invoceDto);

                if (response.getCodeHttp() == 201) {
                    IdDto newInvoce = (IdDto) response.getBody();
                    idFac = newInvoce.getId();

                    /*//advance 30%
                    addProgressAvance();*/
                    invoceDto.setIdFactura(idFac);

                    /*//advance 60%
                    addProgressAvance();*/
                    //enviando a MH
                    log.info("ENVIANDO DTE: " + idFac + " A MH");
                    ResponseRestApi<ApiMhDteResponse> responseSendMh = dteServices.getSendMh(new SendDteRequest(idFac), securityService.getToken());

                    if (responseSendMh == null) {
                        log.error("ERROR ENVIANDO DTE: " + idFac);
                        log.error("API: /api/secured/dte/send");
                        log.error("CODIGO ERROR: " + Constantes.COD_ERROR_NULL_RESPONSE);
                        log.info("LA FACTURA ID: " + idFac + " - SE ENVIARA POR CRON");
                        addProgressAvance();

                        log.error("OCURRIO UN ERROR EN EL ENVIO DEL DTE - SE INTENTARÁ ENVIAR EN BREVE. " + Constantes.COD_ERROR_NULL_RESPONSE);
                        PrimeFaces.current().executeScript("PF('dlgDteError').show();");
                        return;
                    }

                    //finalizar el proceso, aunque no se genere el DTE a MH
                    //advance 100%
                    //addProgressAvance();
                    log.info("Finalizando");

                    clearStatus();

                    switch (responseSendMh.getCodeHttp()) {
                        case 200:
                            codigoGeneracion = responseSendMh.getBody().getCodigoGeneracion();

                            /*JsfUtil.showMessageDialog(FacesMessage.SEVERITY_INFO,
                                    MSG_INFO,
                                    "FACTURA CREADA Y RECIBIDA EN MH. FACTURA: " + codigoGeneracion);*/
                            PrimeFaces.current().executeScript("PF('dlgDteSave').show();");

                            dteServices.sendMail(idFac, securityService.getToken());
                            break;
                        case 504:
                            /*
                            reintento despues de 8 segundos:
                            1. hacer consulta del estado del dte.
                            2. si no ha sido recibido, enviarlo nuevamente
                            esto hacerlo dos veces máximo
                             */

                            break;
                        default:
                            /*
                            si falla el envio, reintentar:
                            1. hacer consulta del estado del dte.
                            2. si no ha sido recibido, enviarlo nuevamente
                            esto hacerlo dos veces máximo
                             */

                            log.error("ERROR ENVIANDO DTE: " + idFac);
                            log.error("CODIGO HTTP: " + responseSendMh.getCodeHttp());
                            log.error("MENSAJE ERROR: " + responseSendMh.getBody());

                            log.error("OCURRIO UN ERROR EN EL ENVIO DEL DTE. " + Constantes.COD_ERROR_501_RESPONSE);

                            PrimeFaces.current().executeScript("PF('dlgDteError').show();");
                            break;
                    }

                } else {
                    log.error("ERROR CREANDO FACTURA: " + invoceDto.toString());
                    log.error("CODIGO HTTP: " + response.getCodeHttp());
                    log.error("MENSAJE ERROR: " + response.getBody());

                    log.error("OCURRIO UN ERROR EN LA CREACION DE LA FACTURA");
                    PrimeFaces.current().executeScript("PF('dlgError').show();");
                }
            } catch (InterruptedException ex) {
                log.error("ERROR ENVIANDO DTE", ex);
            }
        }
    }

    private void showMessageSaveInvoce(String msg) {
        JsfUtil.showMessageDialog(FacesMessage.SEVERITY_ERROR,
                Constantes.MSG_ERROR,
                msg);

        cleanFull();

        //PrimeFaces.current().executeScript("PF('chatDialog').hide()");
        PrimeFaces.current().ajax().update("mensajesPanel", "step", "dvClient", "dvDetInvoce", "dvDetPayment");
    }

    /**
     * valida el detalle de pagos previo a la creacion de la FACTURA
     *
     * @return
     */
    private boolean validatePreSend() {
        if (sessionView.getIdEstablecimiento() == null || sessionView.getIdPuntoVenta() == null) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "DEBE DE SELECCIONAR EL ESTABLECIMIENTO Y PUNTO DE VENTA");
            return false;
        }

        if (lstDetPago.isEmpty()) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "DEBE DE AGREGAR EL DETALLE DE PAGOS");
            return false;
        }

        BigDecimal total = lstDetPago.stream()
                .map(DetallePago::getMonto)
                .filter(monto -> monto != null) // Opcional, si puede haber montos nulos
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (getTotal().compareTo(total) != 0) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "DEBE DE REVISAR LA SUMATORIA DE LOS DETALLES DE PAGO");
            return false;
        }

        return true;
    }

    private void addProgressAvance() throws InterruptedException {
        push.send("" + var);
        var++;
        if (advance < 60) {
            advance += 30;
        } else {
            advance = 100;
        }
        Thread.sleep(1000);
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
                taskSendDte = "taskComplete";
                fontWeightSendDte = "font-weight: bold;";
                break;
            case "3":
                taskComplete = "taskComplete";
                fontWeightComplete = "font-weight: bold;";
                break;
            default:
                break;
        }
    }

    public void clearStatus() {
        taskSave = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";

        var = 1;
    }

    public void cleanFull() {
        loadMetodoPago();

        fechaPedido = new Date();
        cliente = new ClienteResponse();
        invoceDto = new InvoceDto();
        invoceDto.setCodigoDte("01");

        invoceDto.setCondicionOperacion("1"); //CONTADO POR DEFECTO

        lstDetPago = new ArrayList<>();
        detPago = new DetallePago();
        detPago.setTipoPago("01"); //EFECTIVO

        taskSave = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";
    }

    public void facturaSinDatos() {
        numDocumentoReceptor = sinDatos ? "00000000-0" : null;
        findClient();
    }
}
