package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Producto implements Serializable, EntityPk {

    private static final long serialVersionUID = 1L;
    private Long idProducto;
    private String nombre;
    private String codigoProducto;
    private String codigoItem;
    private String codigoUnidad;
    private Boolean activo;
    private Boolean exento;
    private BigDecimal precioUnitario;

    public Producto() {
        precioUnitario = BigDecimal.ZERO;
        codigoUnidad = "59";
        codigoItem = "01";
        activo = true;
        exento = false;
    }

    @Override
    public String toString() {
        return codigoItem + " - " + nombre;
    }

    @Override
    public Long getId() {
        return idProducto;
    }

    @Override
    public boolean esNuevo() {
        return idProducto == null;
    }
}
