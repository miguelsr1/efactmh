package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.Cliente;
import sv.com.jsoft.efactmh.model.Municipio;
import sv.com.jsoft.efactmh.repository.ClienteRepo;
import sv.com.jsoft.efactmh.services.UbicacionService;

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

    @Inject
    UbicacionService ubicacionService;
    @Inject
    ClienteRepo cliRepo;

    @PostConstruct
    public void init() {
        lstCliente = cliRepo.findAll();
    }

    public int getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public List<Municipio> getLstMunicipio() {
        return ubicacionService.getLstMunicipio().stream().filter(mun -> mun.getCodigoDepartamento().getCodigo().equals(codigoDepa)).collect(Collectors.toList());
    }

    public void guardar() {
        cliente.setTipoDocumento(tipoDoc);
        cliente.setActivo((short) 1);
        cliente.setNit(cliente.getNit().replace("-", ""));
        cliente.setDocumentoContacto(tipoDoc == 1 ? duiContacto : nitContacto);
        cliRepo.save(cliente);

        
        lstCliente = cliRepo.findAll();
    }
}
