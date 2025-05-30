package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @Setter
    private String numDocumentoReceptor;
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

        invoceDto.setCondicionOperacion("1"); //CONTADO POR DEFECTO

        lstDetPago = new ArrayList<>();
        detPago = new DetallePago();

        taskSave = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";
        
        loadMetodoPago();
    }

    public void loadMetodoPago() {
        lstMetodoPago = new ArrayList<>();
        switch (invoceDto.getCondicionOperacion()) {
            case "1":
                lstMetodoPago.add(new CatalogoDto("01", "EFECTIVO"));
                break;
            case "2":
                lstMetodoPago.add(new CatalogoDto("02", "TARJETA DE DEBITO"));
                lstMetodoPago.add(new CatalogoDto("03", "TARJETA DE CREDITO"));
                lstMetodoPago.add(new CatalogoDto("04", "CHEQUE"));
                lstMetodoPago.add(new CatalogoDto("05", "TRANSFERENCIA-DEPOSITO BANCARIO"));
                break;
            case "3":
                lstMetodoPago.add(new CatalogoDto("02", "TARJETA DE DEBITO"));
                lstMetodoPago.add(new CatalogoDto("03", "TARJETA DE CREDITO"));
                lstMetodoPago.add(new CatalogoDto("04", "CHEQUE"));
                lstMetodoPago.add(new CatalogoDto("05", "TRANSFERENCIA-DEPOSITO BANCARIO"));
                break;
        }
    }

    //==========================================================================
    public Date getFechaPedido() {
        return fechaPedido;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public InvoceDto getInvoce() {
        return invoceDto;
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
                .endpoint("/api/secured/client/" + numDocumentoReceptor)
                .build();
        ResponseRestApi obj = rest.callGetOneAuth();

        if (obj.getCodeHttp() == 200) {
            cliente = (ClienteResponse) obj.getBody();
            nombreCliente = (cliente.getTipoPersoneria() == 1)
                    ? cliente.getNombreCompleto()
                    : cliente.getRazonSocial();
            invoceDto.setIdCliente(cliente.getIdCliente());
        } else {
            PrimeFaces.current().executeScript("PF('dlgAddCustomer').show()");
        }
        log.info(cliente.toString());
    }

    public BigDecimal getSubTotal() {
        return invoceDto.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getIva() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotal() {
        if (invoceDto.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return invoceDto.getDetailInvoce().stream()
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
            invoceDto.getDetailInvoce().add(detFactura);
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
        invoceDto.getDetailInvoce().remove(index);
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
        if (stepValidate()) {
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
    }

    private boolean stepValidate() {
        switch (activeStep) {
            case 0:
                switch (invoceDto.getCodigoDte()) {
                    case "01": //FE
                        if (cliente.getIdCliente() == null) {
                            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN, MSG_ALERT, "POR FAVOR AGREGE UN CLIENTE");
                            return false;
                        }
                        return true;
                    case "03": //CCF
                        return true;
                    default:
                        return true;
                }
            case 1:
                //VALIDAR QUE EXISTA UN ITEM AGREGADO EN LA FACTURA
                if (invoceDto.getDetailInvoce().isEmpty()) {
                    JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN, MSG_ALERT, "POR FAVOR AGREGE UN ITEM A LA FACTURA");
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void preSave() {
        makeInvoce = validatePreSend();
    }

    public void save() {
        if (makeInvoce) {
            lstDetPago.forEach(det -> det.getMonto());

            try {
                invoceDto.setDetailPayments(lstDetPago);
                invoceDto.setIdEstablecimiento(Long.valueOf(sessionView.getIdEstablecimiento()));
                invoceDto.setIdPuntoVenta(sessionView.getIdPuntoVenta() != null ? Long.valueOf(sessionView.getIdPuntoVenta()) : null);

                //Persistiendo factura
                ResponseRestApi response = invoceService.saveInvoce(securityService.getToken(), invoceDto);

                if (response.getCodeHttp() == 201) {
                    IdDto newInvoce = (IdDto) response.getBody();
                    idFac = newInvoce.getId();

                    //advance 30%
                    addProgressAvance();

                    invoceDto.setIdFactura(idFac);

                    //advance 60%
                    addProgressAvance();

                    //enviando a MH
                    response = dteServices.getSendMh(new SendDteRequest(idFac), securityService.getToken());

                    if (response == null) {
                        log.error("ERROR ENVIANDO DTE: " + idFac);
                        log.error("API: /api/secured/dte/send");
                        log.error("CODIGO ERROR: " + Constantes.COD_ERROR_NULL_RESPONSE);

                        showMessageSaveInvoce("OCURRIO UN ERROR EN EL ENVIO DEL DTE. " + Constantes.COD_ERROR_NULL_RESPONSE);
                        return;
                    }

                    switch (response.getCodeHttp()) {
                        case 200:
                            //advance 100%
                            addProgressAvance();

                            log.info("Finalizando");

                            clearStatus();
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
                            log.error("CODIGO HTTP: " + response.getCodeHttp());
                            log.error("MENSAJE ERROR: " + response.getBody());

                            showMessageSaveInvoce("OCURRIO UN ERROR EN EL ENVIO DEL DTE. " + Constantes.COD_ERROR_501_RESPONSE);
                            break;
                    }
                } else {
                    log.error("ERROR CREANDO FACTURA: " + invoceDto.toString());
                    log.error("CODIGO HTTP: " + response.getCodeHttp());
                    log.error("MENSAJE ERROR: " + response.getBody());

                    showMessageSaveInvoce("OCURRIO UN ERROR EN LA CREACION DE LA FACTURA");
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

        PrimeFaces.current().executeScript("PF('chatDialog').hide()");
        PrimeFaces.current().ajax().update("mensajesPanel", "step", "dvClient", "dvDetInvoce", "dvDetPayment");
    }

    /**
     * valida el detalle de pagos previo a la creacion de la FACTURA
     *
     * @return
     */
    private boolean validatePreSend() {
        if (lstDetPago.isEmpty()) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN, MSG_ALERT, "DEBE DE AGREGAR EL DETALLE DE PAGOS");
            return false;
        }

        BigDecimal total = lstDetPago.stream()
                .map(DetallePago::getMonto)
                .filter(monto -> monto != null) // Opcional, si puede haber montos nulos
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (getTotal().compareTo(total) != 0) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN, MSG_ALERT, "DEBE DE REVISAR LA SUMATORIA DE LOS DETALLES DE PAGO");
            return false;
        }

        PrimeFaces.current().executeScript("PF('chatDialog').show()");
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
        existeCliente = false;

        activeStep = 0;
        advance = 0;

        var = 1;

        numDocumentoReceptor = "";
        nombreCliente = "";

        lstDetPago.clear();
        totalPagos = BigDecimal.ZERO;

        invoceDto = new InvoceDto();
        detPago = new DetallePago();
        cliente = new ClienteResponse();

        clearStatus();
    }
}
