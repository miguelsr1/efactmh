package sv.com.jsoft.efactmh.model.dto;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 *
 * @author migue
 */
public class ClienteResponse implements Serializable {

    private Long idCliente;
    private String razonSocial;
    private String nombreCompleto;
    private String tipoPersoneria;
    private boolean inscritoIva;

    public ClienteResponse() {
    }

    @ConstructorProperties({"idCliente", "razonSocial", "nombreCompleto", "tipoPersoneria", "inscritoIva"})
    public ClienteResponse(Long idCliente, String razonSocial, String nombreCompleto, String tipoPersoneria, boolean inscritoIva) {
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
        this.nombreCompleto = nombreCompleto;
        this.tipoPersoneria = tipoPersoneria;
        this.inscritoIva = inscritoIva;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoPersoneria() {
        return tipoPersoneria;
    }

    public void setTipoPersoneria(String tipoPersoneria) {
        this.tipoPersoneria = tipoPersoneria;
    }

    public Boolean getInscritoIva() {
        return inscritoIva;
    }

    public void setInscritoIva(Boolean inscritoIva) {
        this.inscritoIva = inscritoIva;
    }

}
