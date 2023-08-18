package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.Cliente;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class ClienteView implements Serializable {

    private int tipoDoc;
    @Getter
    @Setter
    private String codigoDepa;
    @Getter
    @Setter
    private String duiContacto;
    @Getter
    @Setter
    private String nitContacto;
    @Getter
    @Setter
    private Integer idMuni;

    @Getter
    @Setter
    private Cliente cliente;
    @Getter
    private List<Cliente> lstCliente;

    {
        tipoDoc = 1;
        cliente = new Cliente();
    }

    @PostConstruct
    public void init() {
    }

    public int getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public void guardar() {
    }
}
