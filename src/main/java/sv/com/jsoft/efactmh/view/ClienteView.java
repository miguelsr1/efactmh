package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import sv.com.jsoft.efactmh.model.Cliente;
import sv.com.jsoft.efactmh.model.Municipio;
import sv.com.jsoft.efactmh.services.UbicacionService;
import sv.com.jsoft.efactmh.util.RestUtil;

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

    private List<Municipio> lstMunicipio;

    @Getter
    @Setter
    private Cliente cliente;
    @Getter
    private List<Cliente> lstCliente;

    @Inject
    UbicacionService ubicacionService;

    {
        tipoDoc = 1;
        cliente = new Cliente();
        lstMunicipio = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        RestUtil res = RestUtil
                .builder()
                .endpoint("cliente/")
                .clazz(Cliente.class)
                .build();
        lstCliente = res.callGet();
    }

    public int getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public void guardar() {
    }

    public List<Municipio> getLstMunicipio() {
        return ubicacionService.findMunicipioByDepa(codigoDepa);
    }

    public void onRowSelect(SelectEvent<Cliente> event) {
        cliente = event.getObject();
    }
}
