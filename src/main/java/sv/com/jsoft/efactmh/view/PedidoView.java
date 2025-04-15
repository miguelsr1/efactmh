package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class PedidoView implements Serializable {

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
    private String nombreCliente;

    private Integer numeroPedido;
    private Date fechaPedido;
    private ClienteResponse cliente;
    private Pedido pedido;

    @Inject
    SessionService securityService;

    @Inject
    SessionView sessionView;

    {
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
    public void buscarCliente() {
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
        return pedido.getDetalleFacturaList().stream()
                .map(DetalleFacturaDto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void showDlgDetFactura() {

        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .draggable(false)
                .resizable(false)
                .maximizable(false)
                .responsive(true)
                .modal(true)
                .width("650px")
                .height("400px")
                .build();

        PrimeFaces.current().dialog().openDynamic("dialog/dlg-det-factura", options, null);
    }

    public void onDetFactura(SelectEvent<DetalleFacturaDto> event) {
        DetalleFacturaDto detFactura = event.getObject();
        pedido.getDetalleFacturaList().add(detFactura);
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
    
    public String showAddCustomerDialog(){
        return "/app/mantto/cliente.xhtml";
    }
}
