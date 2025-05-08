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
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import sv.com.jsoft.efactmh.model.Cliente;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.dto.ClienteDto;
import sv.com.jsoft.efactmh.model.enums.TipoMensaje;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.services.UbicacionService;
import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
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
    private boolean disabled = true;
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
    private Long idMuni;

    private List<MunicipioDto> lstMunicipio;

    private Cliente cliente;
    private ClienteDto clienteDto;
    @Getter
    private List<ClienteDto> lstCliente;

    private RestUtil res;

    @Inject
    UbicacionService ubicacionService;
    @Inject
    SessionService sessionService;

    {
        tipoDoc = 1;
        cliente = new Cliente();
        lstMunicipio = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        res = RestUtil
                .builder()
                .clazz(ClienteDto.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/secured/client/")
                .build();

        ResponseRestApi response = res.callGetAuth();
        if (response.getCodeHttp() == 200) {
            lstCliente = (List<ClienteDto>) response.getBody();
        }
    }

    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public void setClienteDto(ClienteDto ClienteDto) {
        this.clienteDto = ClienteDto;
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
        disabled = false;
    }
    
    public void cancelar() {
        cliente = new Cliente();
        disabled = true;
    }

    public void guardar() {
        if (cliente != null) {
            tipoDoc = (duiContacto.length() == 10) ? 1 : 2;
            cliente.setTipoDocumento(tipoDoc);
            cliente.setDocumentoContacto(duiContacto);
            cliente.setActivo(true);

            int codeResponse = res.callPersistir(cliente);

            JsfUtil.mensajeFromEnum(codeResponse != 200 ? TipoMensaje.ERROR : (cliente.esNuevo() ? TipoMensaje.INSERT : TipoMensaje.UPDATE));

            cliente = new Cliente();
            duiContacto = "";
            disabled = true;

            PrimeFaces.current().ajax().update("tblCli");
        }
    }

    public List<MunicipioDto> getLstMunicipio() {
        return ubicacionService.findMunicipioByDepa(codigoDepa);
    }

    public void onRowSelect(SelectEvent<ClienteDto> event) {
        disabled = false;
        clienteDto = event.getObject();
        idMuni = clienteDto.getIdMunicipio();
        MunicipioDto m = (MunicipioDto) RestUtil.builder()
                .clazz(MunicipioDto.class)
                .endpoint("catalogos/municipio/" + idMuni)
                .build().callGetById();
        tipoDoc = cliente.getTipoDocumento();
        duiContacto = cliente.getDocumentoContacto();
    }
}
