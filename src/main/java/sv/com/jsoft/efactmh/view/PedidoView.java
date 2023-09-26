package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.Cliente;
import sv.com.jsoft.efactmh.model.DetallePedido;
import sv.com.jsoft.efactmh.model.Pedido;
import sv.com.jsoft.efactmh.model.Producto;

/**
 *
 * @author migue
 */
@ViewScoped
@Named
public class PedidoView implements Serializable {

    @Getter
    @Setter
    private String nit;
    private Integer numeroPedido;

    private Date fechaPedido;
    private Cliente cliente;
    private Pedido pedido;
    @Getter
    @Setter
    private DetallePedido detPedido;
    @Getter
    @Setter
    private Producto producto;

    {
        fechaPedido = new Date();
        cliente = new Cliente();
        pedido = new Pedido();
        numeroPedido = 0;
        detPedido = new DetallePedido();
    }

    //==========================================================================
    public Date getFechaPedido() {
        return fechaPedido;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pedido getPedido() {
        return pedido;
    }

    //==========================================================================
    public void agregarProductoADetallePedido() {
        detPedido.setIdProducto(producto);
    }

    public void agregarItem() {
        pedido.getDetallePedidoList().add(detPedido);

        detPedido = new DetallePedido();
    }

    public Double getSubTotal() {
        return pedido.getDetallePedidoList().stream()
                .mapToDouble(d -> d.getSubTotal())
                .sum();
    }

    public Double getIva() {
        return 0d;
    }

    public Double getTotal() {
        return pedido.getDetallePedidoList().stream()
                .mapToDouble(d -> d.getSubTotal())
                .sum();
    }
}
