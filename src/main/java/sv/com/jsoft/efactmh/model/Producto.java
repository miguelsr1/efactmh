package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idProducto;
    private String codigo;
    private String nombre;
    private Double precioUnitario;
    private Boolean activo;
    private Integer idUnidadMedida;

    private Boolean eliminado;

    public Producto() {
    }

    public String toString() {
        return codigo + " - " + nombre;
    }
}
