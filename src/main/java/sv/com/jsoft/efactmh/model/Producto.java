package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Producto implements Serializable, EntityPk {

    private static final long serialVersionUID = 1L;
    private Long idProducto;
    private String codigo;
    private String nombre;
    private Double precioUnitario;
    private Boolean activo;
    private Integer idUnidadMedida;

    private Boolean eliminado;

    {
        precioUnitario = 0d;
    }

    public Producto() {
    }

    public String toString() {
        return codigo + " - " + nombre;
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
