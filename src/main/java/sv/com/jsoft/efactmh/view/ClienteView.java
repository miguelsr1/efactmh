package sv.com.jsoft.efactmh.view;

import com.google.gson.Gson;
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
import sv.com.jsoft.efactmh.util.JsfUtil;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente != null) {
            this.cliente = cliente;
        }
    }

    public int getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public void nuevo() {
        cliente = new Cliente();
    }

    public void guardar() {
        if (cliente != null) {
            cliente.setTipoDocumento(tipoDoc);
            cliente.setDocumentoContacto(tipoDoc == 1 ? duiContacto : nitContacto);
            cliente.setActivo(true);
            RestUtil res = RestUtil
                    .builder()
                    .endpoint("cliente/")
                    .build();
            int codeResponse;
            boolean nuevo = cliente.getIdCliente() == null;
            if (nuevo) {
                codeResponse = res.callPost(cliente);
            } else {
                codeResponse = res.callPut(cliente.getIdCliente(), cliente);
            }

            if (codeResponse == 200) {
                if (nuevo) {
                    JsfUtil.mensajeInsert();
                } else {
                    JsfUtil.mensajeUpdate();
                }
            } else {
                JsfUtil.mensajeError("Ah ocurrido un error");
            }
        } else {

        }
    }

    public List<Municipio> getLstMunicipio() {
        return ubicacionService.findMunicipioByDepa(codigoDepa);
    }

    public void onRowSelect(SelectEvent<Cliente> event) {
        cliente = event.getObject();
        idMuni = cliente.getIdMunicipio();
        Municipio m = (Municipio) RestUtil.builder()
                .clazz(Municipio.class)
                .endpoint("catalogos/municipio/" + idMuni)
                .build().callGetById();
        codigoDepa = m.getCodigoDepartamento();
        tipoDoc = cliente.getTipoDocumento();
        switch (tipoDoc) {
            case 1:
                duiContacto = cliente.getDocumentoContacto();
                break;
            default:
                nitContacto = cliente.getDocumentoContacto();
                break;
        }

    }
}
