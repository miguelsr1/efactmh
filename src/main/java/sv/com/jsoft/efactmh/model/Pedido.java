package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<DetalleFacturaDto> detalleFacturaList;
    private Cliente idCliente;
    private EstadoPedido idEstadoPedido;

    public Pedido() {
        detalleFacturaList = new ArrayList<>();
    }
}
