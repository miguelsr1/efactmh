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
public class InvoceDto implements Serializable, EntityPk {

    private static final long serialVersionUID = 1L;
    
    private Integer idFactura;
    private Long idCliente;
    private Long numeroFactura;
    private String codigoDte;
    private List<DetalleFacturaDto> detailInvoce;
    private List<DetallePago> detailPayments;

    public InvoceDto() {
        detailInvoce = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return idFactura;
    }

    @Override
    public boolean esNuevo() {
        return idFactura == null;
    }
}
