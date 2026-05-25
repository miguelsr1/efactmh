package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.ClientTempDto;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.DetallePago;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.model.dto.SendDteRequest;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.services.DteService;
import sv.com.jsoft.efactmh.services.InvoceService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.Constantes;

import static sv.com.jsoft.efactmh.util.Constantes.MSG_ALERT;

import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.MessageUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;

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
    @Getter
    @Setter
    private boolean existClient = true;

    private SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Getter
    @Setter
    private Date dateInvoce = new Date();
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
    private String queryParam;
    @Getter
    @Setter
    private String nombreCliente;
    @Getter
    @Setter
    private String nombreClienteReq;
    @Getter
    @Setter
    private String codigoTipoDocClienteReq;
    @Getter
    @Setter
    private String numDocClienteReq;
    @Getter
    @Setter
    private String correoClienteReq;
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
    private Long idCliente = 0l;

    @Getter
    @Setter
    private boolean sinDatos = false;
    @Getter
    @Setter
    private boolean requiereFactura = false;
    @Getter
    @Setter
    private Producto producto;
    @Getter
    private DetallePago detPago;
    @Getter
    @Setter
    private List<DetallePago> lstDetPago;
    @Getter
    @Setter
    private List<CatalogoDto> lstMetodoPago;
    @Getter
    @Setter
    private List<String> lstObservacionesMH;
    @Getter
    @Setter
    private ClienteResponse cliente;
    @Getter
    private InvoceDto invoce;

    @Inject
    SessionView sessionView;
    @Inject
    SessionService securityService;
    @Inject
    DteService dteServices;
    @Inject
    InvoceService invoceService;
    @Inject
    ClientRepository clientRepository;

    private Long idFac;

    @PostConstruct
    public void init() {
        cliente = new ClienteResponse();
        invoce = new InvoceDto();
        invoce.setCodigoDte("01");

        invoce.setCondicionOperacion("1"); //CONTADO POR DEFECTO

        lstDetPago = new ArrayList<>();
        detPago = new DetallePago();
        detPago.setTipoPago("01"); //EFECTIVO

        codigoTipoDocClienteReq = null;

        lstObservacionesMH = new ArrayList<>();

        taskSave = "taskPending";
        taskSendDte = "taskPending";
        taskComplete = "taskPending";

        fontWeightSave = "";
        fontWeightSendDte = "";
        fontWeightComplete = "";

        loadMetodoPago();
    }


    public void setDetPago(DetallePago detPago) {
        if (detPago != null) {
            this.detPago = detPago;
        }
    }

    public void loadMetodoPago() {
        lstMetodoPago = new ArrayList<>();
        switch (invoce.getCondicionOperacion()) {
            case "1":
                lstMetodoPago.add(new CatalogoDto("01", "EFECTIVO"));
                lstMetodoPago.add(new CatalogoDto("02", "TARJETA DE DEBITO"));
                lstMetodoPago.add(new CatalogoDto("03", "TARJETA DE CREDITO"));
                lstMetodoPago.add(new CatalogoDto("05", "TRANSFERENCIA-DEPOSITO BANCARIO"));
                detPago.setTipoPago("01");
                break;
            case "2", "3":
                lstMetodoPago.add(new CatalogoDto("02", "TARJETA DE DEBITO"));
                lstMetodoPago.add(new CatalogoDto("03", "TARJETA DE CREDITO"));
                lstMetodoPago.add(new CatalogoDto("04", "CHEQUE"));
                lstMetodoPago.add(new CatalogoDto("05", "TRANSFERENCIA-DEPOSITO BANCARIO"));
                detPago.setTipoPago("02");
                break;
            default:
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
        ResponseRestApi<ClienteResponse> obj = invoceService.findClient(queryParam);

        if (obj.getCodeHttp() == 200) {
            cliente = obj.getBody();

            if (invoce.getCodigoDte().equals("03") && !cliente.getInscritoIva()) {

                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_WARN)
                        .title(MSG_ALERT)
                        .message("EL RECEPTOR DEBE DE ESTAR INSCRITO AL IVA PARA EMITIR UN CCF")
                        .build()
                        .showMessage();

                cliente = new ClienteResponse();
                queryParam = "";
                return;
            }

            nombreCliente = (cliente.getTipoPersoneria().equals("N"))
                    ? cliente.getNombreCompleto()
                    : cliente.getRazonSocial();
            invoce.setIdCliente(cliente.getIdCliente());
        } else {
            PrimeFaces.current().executeScript("PF('dlgAddCustomer').show()");
        }
        log.info(cliente.toString());
    }

    public BigDecimal getSumas() {
        return invoce.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSumasCcf() {
        return invoce.getDetailInvoce().stream()
                .filter(det -> det.getTipoVenta() == 1)
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubTotal() {
        return getSumas().add(getIva()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getIva() {
        return switch (invoce.getCodigoDte()) {
            case "01", "14" -> BigDecimal.ZERO;
            case "03" -> getSumasCcf().multiply(BigDecimal.valueOf(0.13))
                    .setScale(2, RoundingMode.HALF_UP);
            default -> BigDecimal.ZERO;
        };
    }

    public BigDecimal getIvaRetenido() {
        if (invoce.getCodigoDte().equals("01")) {
            if (invoce.isAplicaIvaRetenido()) {
                BigDecimal porcentajeIva = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(100));
                return getSumas().divide(BigDecimal.valueOf(1.13), RoundingMode.HALF_UP).multiply(porcentajeIva);
            } else {
                return BigDecimal.ZERO;
            }
        } else if (invoce.getCodigoDte().equals("03")) {
            if (invoce.isAplicaIvaRetenido()) {
                BigDecimal porcentajeIva = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(100));
                return getSumas().multiply(porcentajeIva);
            } else {
                return BigDecimal.ZERO;
            }
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getRentaRetenido() {
        switch (invoce.getCodigoDte()) {
            case "01":
                return BigDecimal.ZERO;
            case "03":
                if (invoce.isAplicaRentaRetenido()) {
                    BigDecimal porcentajeIsr = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100));
                    return getSumas().multiply(porcentajeIsr);
                } else {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotal() {
        return switch (invoce.getCodigoDte()) {
            case "01" -> getTotalFe();
            case "03" -> getTotalCcf();
            case "14" -> getTotalSEx();
            default -> BigDecimal.ZERO;
        };
    }

    private BigDecimal getTotalFe() {
        if (invoce.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return invoce.getDetailInvoce().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(getIvaRetenido().negate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getTotalCcf() {
        if (invoce.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return getSumas()
                .add(getIva())
                .add(getIvaRetenido().negate())
                .add(getRentaRetenido().negate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * total para facturas de sujeto excluido
     *
     * @return
     */
    private BigDecimal getTotalSEx() {
        if (invoce.getDetailInvoce().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return getSumas();
    }

    public void showDlgDetFactura() {

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .responsive(true)
                .modal(true)
                .width("430px")
                .build();

        PrimeFaces.current().dialog().openDynamic("dialog/dlg-det-factura", options, null);
    }

    public void onDetFactura(SelectEvent<DetalleFacturaDto> event) {
        if (event.getObject() != null) {
            DetalleFacturaDto detFactura = event.getObject();
            invoce.getDetailInvoce().add(detFactura);
        }
    }

    public void addPaymentMethod() {
        lstDetPago.add(detPago);
        detPago = new DetallePago();
        detPago.setTipoPago("01"); //EFECTIVO

        invoce.setCondicionOperacion("1");
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

        invoce.setCondicionOperacion("1");

        if (lstDetPago.isEmpty()) {
            detPago.setMonto(getTotal());
        }
    }

    public void removeDetInvoce(int index) {
        invoce.getDetailInvoce().remove(index);
    }

    public void backStep() {
        switch (activeStep) {
            case 0:
                break;
            case 1, 2:
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
                case 0, 2:
                    activeStep++;
                    break;
                case 1:
                    activeStep++;
                    detPago.setMonto(getTotal());
                    break;
                default:
                    break;
            }
        }
    }

    private boolean stepValidate() {
        switch (activeStep) {
            case 0:
                switch (invoce.getCodigoDte()) {
                    case "01": //FE
                        if (!requiereFactura) {
                            if (cliente.getIdCliente() == null) {
                                JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                                        MSG_ALERT,
                                        "POR FAVOR AGREGE UN CLIENTE");
                                return false;
                            }
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
                if (invoce.getDetailInvoce().isEmpty()) {
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

            invoce.setDateInvoce(sfd.format(dateInvoce));
            invoce.setDetailPayments(lstDetPago);
            invoce.setIdEstablecimiento(Long.valueOf(sessionView.getIdEstablecimiento()));
            invoce.setIdPuntoVenta(sessionView.getIdPuntoVenta() != null ? Long.valueOf(sessionView.getIdPuntoVenta()) : null);

            //Persistiendo factura
            log.info("PERSISTIENDO FACTURA: " + invoce.toString());
            ResponseRestApi<IdDto> response = invoceService.saveInvoce(invoce);

            if (response.getCodeHttp() == 201) {
                IdDto newInvoce = response.getBody();
                idFac = newInvoce.getId();

                invoce.setIdFactura(idFac);

                //enviando a MH
                log.info("ENVIANDO DTE: " + idFac + " A MH");
                ResponseRestApi<ApiMhDteResponse> responseSendMh = dteServices.getSendMh(new SendDteRequest(idFac));

                if (responseSendMh == null) {
                    log.error("ERROR ENVIANDO DTE: " + idFac);
                    log.error("API: /api/secured/dte/send");
                    log.error("CODIGO ERROR: " + Constantes.COD_ERROR_NULL_RESPONSE);
                    log.info("LA FACTURA ID: " + idFac + " - SE ENVIARA POR CRON");

                    log.error("OCURRIO UN ERROR EN EL ENVIO DEL DTE - SE INTENTARÁ ENVIAR EN BREVE. " + Constantes.COD_ERROR_NULL_RESPONSE);
                    PrimeFaces.current().executeScript("PF('dlgDteError').show();");
                    return;
                }

                log.info("Finalizando");

                clearStatus();

                switch (responseSendMh.getCodeHttp()) {
                    case 200:
                        codigoGeneracion = responseSendMh.getBody().getCodigoGeneracion();

                        PrimeFaces.current().ajax().update("pnMsg");
                        PrimeFaces.current().executeScript("PF('dlgDteSave').show();");

                        dteServices.sendMail(idFac);
                        break;
                    case 400:
                        lstObservacionesMH.clear();
                        lstObservacionesMH = responseSendMh.getBody().getObservaciones();

                        PrimeFaces.current().ajax().update("pnlOutErrorMh");
                        PrimeFaces.current().executeScript("PF('dlgDteError').show();");
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
                log.error("ERROR CREANDO FACTURA: " + invoce.toString());
                log.error("CODIGO HTTP: " + response.getCodeHttp());
                log.error("MENSAJE ERROR: " + response.getBody());

                log.error("OCURRIO UN ERROR EN LA CREACION DE LA FACTURA");
                PrimeFaces.current().executeScript("PF('dlgError').show();");
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
        if (dateInvoce == null) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "REVISE LA FECHA DE LA FACTURA");
            return false;
        }
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
        if (requiereFactura && invoce.getCodigoDte().equals("01")) {
            invoce.setClientTemp(new ClientTempDto(nombreClienteReq, correoClienteReq, codigoTipoDocClienteReq, numDocClienteReq));
            invoce.setIdCliente(null);
        } else if (!requiereFactura) {
            invoce.setClientTemp(null);
        }

        BigDecimal total = lstDetPago.stream()
                .map(DetallePago::getMonto)
                .filter(Objects::nonNull) // Opcional, si puede haber montos nulos
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (getTotal().compareTo(total) != 0) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "DEBE DE REVISAR LA SUMATORIA DE LOS DETALLES DE PAGO");
            return false;
        }

        return true;
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
    }

    public void cleanFull() {
        loadMetodoPago();

        cliente = new ClienteResponse();
        invoce = new InvoceDto();
        invoce.setCodigoDte("01");

        invoce.setCondicionOperacion("1"); //CONTADO POR DEFECTO

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
        requiereFactura = false;
        queryParam = sinDatos ? "00000000-0" : null;
        if (queryParam != null) {
            findClient();
        }
    }

    public void requiereFacturaAction() {
        sinDatos = false;
    }

    public List<ClienteResponse> completeClient(String query) {
        return clientRepository.findClientBySearch(query, securityService.getEmisor().getCorreo());
    }

    public void onItemSelect(SelectEvent<ClienteResponse> event) {
        if (event.getObject() != null) {
            cliente = event.getObject();

            if (invoce.getCodigoDte().equals("03")) {
                if (cliente.getInscritoIva()) {
                    invoce.setIdCliente(cliente.getIdCliente());
                } else {
                    JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                            MSG_ALERT,
                            "EL CLIENTE DEBE DE ESTAR INSCRITO AL IVA PARA EMITIRLE UNA FACTURA DE CREDITO FISCAL");
                    cliente = new ClienteResponse();
                }
            } else {
                invoce.setIdCliente(cliente.getIdCliente());
            }
        } else {
            cliente = new ClienteResponse();
        }
    }

    public int getMaxNumDoc() {
        if (codigoTipoDocClienteReq == null) {
            return 0;
        }

        return switch (codigoTipoDocClienteReq) {
            case "13" -> 9;
            case "36" -> 14;
            default -> 30;
        };
    }
}
