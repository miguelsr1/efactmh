package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.DetallePago;
import sv.com.jsoft.efactmh.model.Pedido;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
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

    @Getter
    @Setter
    private String tipoDte;
    @Getter
    @Setter
    private String numeroDocumento;
    @Getter
    @Setter
    private String tipoPago;
    @Getter
    @Setter
    private List<DetallePago> lstDetPago;
    @Getter
    @Setter
    private DetallePago detPago;

    private BigDecimal totalPagos;

    @Getter
    @Setter
    private Producto producto;
    @Getter
    private boolean existeCliente = false;
    @Getter
    @Setter
    private String nombreCliente;

    @Getter
    @Setter
    private Integer activeStep = 0;

    private Integer numeroPedido;
    private Date fechaPedido;
    private ClienteResponse cliente;
    private Pedido pedido;

    @Inject
    SessionService securityService;

    @Inject
    SessionView sessionView;

    @PostConstruct
    public void init() {
        fechaPedido = new Date();
        cliente = new ClienteResponse();
        pedido = new Pedido();
        numeroPedido = 0;
        lstDetPago = new ArrayList<>();
        detPago = new DetallePago();
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

    public Pedido getPedido() {
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
        } else {
            PrimeFaces.current().executeScript("PF('dlgAddCustomer').show()");
        }
        log.info(cliente.toString());
    }

    public BigDecimal getSubTotal() {
        return pedido.getDetalleFacturaList().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getIva() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotal() {
        if (pedido.getDetalleFacturaList().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return pedido.getDetalleFacturaList().stream()
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
            pedido.getDetalleFacturaList().add(detFactura);
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
        pedido.getDetalleFacturaList().remove(index);
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

    public void save() {
        RestUtil rest = RestUtil
                .builder()
                .clazz(Pedido.class)
                .jwtDto(securityService.getToken())
                .endpoint("/api/invoce")
                .build();
        
        pedido.setDetallePagoList(lstDetPago);

        int idPedido = rest.callPersistir(pedido);
        System.out.println("");
    }
}
