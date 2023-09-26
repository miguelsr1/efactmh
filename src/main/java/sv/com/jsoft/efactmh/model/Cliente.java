package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class Cliente implements Serializable, EntityPk {

    private static final long serialVersionUID = 1L;
    private Integer idCliente;
    private String codigo;
    private String nit;
    private String nrc;
    private String email;
    private String direccion;
    private String telefono;
    private String nombreContacto;
    private String documentoContacto;
    private String razonSocial;
    private String nombreComercial;
    private Integer idMunicipio;
    private List<Pedido> pedidoList;
    private Boolean activo;
    private Integer tipoDocumento;

    private Boolean eliminado;

    public Cliente() {
    }

    @Override
    public Integer getId() {
        return idCliente;
    }

    @Override
    public boolean esNuevo() {
        return idCliente == null;
    }

}
