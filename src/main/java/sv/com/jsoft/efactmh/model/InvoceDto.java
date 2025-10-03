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

    private Long idFactura;
    private Long idCliente;
    private Long idEstablecimiento;
    private Long idPuntoVenta;
    private String condicionOperacion;
    private String codigoDte;
    private String dateInvoce;
    //private int retencionIsr;
    private boolean aplicaIvaRetenido;
    private boolean aplicaRentaRetenido;
    private List<DetalleFacturaDto> detailInvoce;
    private List<DetallePago> detailPayments;
    private ClientTempDto clientTemp;

    public InvoceDto() {
        detailInvoce = new ArrayList<>();
        //retencionIsr = 0;
        aplicaIvaRetenido = false;
        aplicaRentaRetenido = false;
    }

    @Override
    public Long getId() {
        return idFactura;
    }

    @Override
    public boolean esNuevo() {
        return idFactura == null;
    }
}
