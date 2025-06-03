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
    private Long idCliente;
    private String nit;
    private String nrc;
    private String nrcPersona;
    private String email;
    private String direccion;
    private String direccionEmp;
    private String telefono;
    private String nombreContacto;
    private String documentoContacto;
    private String razonSocial;
    private String nombreComercial;
    private Integer idMunicipio;
    private List<InvoceDto> pedidoList;
    private Boolean activo;
    private Integer tipoDocumento;
    private Boolean inscritoIva;
    private Boolean eliminado;
    private int tipoPersoneria;
    private String numDocumento;
    private String codigoActividad;
    
    private String municipioEmp;
    private String departamentoEmp;
    
    private String municipio;
    private String departamento;

    public Cliente() {
    }

    @Override
    public Long getId() {
        return idCliente;
    }

    @Override
    public boolean esNuevo() {
        return idCliente == null;
    }

}
