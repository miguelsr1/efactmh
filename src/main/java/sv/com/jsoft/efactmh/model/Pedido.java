package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Pedido implements Serializable, EntityPk {

    private static final long serialVersionUID = 1L;
    private Integer idPedido;
    private Long idCliente;
    private Long numeroFactura;
    private String codigoDte;
    private List<DetalleFacturaDto> detalleFacturaList;
    private List<DetallePago> detallePagoList;

    public Pedido() {
        detalleFacturaList = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return idPedido;
    }

    @Override
    public boolean esNuevo() {
        return idPedido == null;
    }
}
