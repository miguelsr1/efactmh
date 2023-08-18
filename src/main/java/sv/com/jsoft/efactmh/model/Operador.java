package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Operador implements Serializable {

    private static final long serialVersionUID = 1L;
    private String user;
    private String password;
    private String nombreUsuario;
    private String email;
    private short activo;
    private short bloqueado;
    private List<Pedido> pedidoList;
    private String rol;

    public Operador() {
    }
}
