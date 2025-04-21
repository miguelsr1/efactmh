package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class EstadoPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idEstadoPedido;
    private String descripcion;
    private List<InvoceDto> pedidoList;

    public EstadoPedido() {
    }
}