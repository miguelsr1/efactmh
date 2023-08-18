package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idPedido;
    private Date fechaRegistro;
    private List<DetallePedido> detallePedidoList;
    private Cliente idCliente;
    private EstadoPedido idEstadoPedido;
    private Operador user;

    public Pedido() {
    }
}
