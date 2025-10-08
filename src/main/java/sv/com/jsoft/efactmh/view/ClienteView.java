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
import sv.com.jsoft.efactmh.model.PerJuridicaRequest;
import sv.com.jsoft.efactmh.model.PerNaturalRequest;
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
    private boolean inscritoIva = false;
    @Getter
    @Setter
    private boolean edit = false;
    @Getter
    @Setter
    private int tipoPersoneria;
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

    private PerNaturalRequest pn;
    private PerJuridicaRequest pj;
    private ClienteDto clienteDto;
    @Getter
    private List<ClienteDto> lstCliente;

    @Inject
    UbicacionService ubicacionService;
    @Inject
    SessionService sessionService;

    @PostConstruct
    public void init() {
        inicializar();
    }

    private void inicializar() {
        disabled = true;
        edit = false;
        inscritoIva = false;
        tipoDoc = 1;
        tipoPersoneria = 0;
        clienteDto = new ClienteDto();
        pn = new PerNaturalRequest();
        lstMunicipio = new ArrayList<>();
        codigoDepa = "06";

        ResponseRestApi response = RestUtil
                .builder()
                .clazz(ClienteDto.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/secured/client/")
                .build()
                .callGetAllAuth();

        if (response.getCodeHttp() == 200) {
            lstCliente = (List<ClienteDto>) response.getBody();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public void setClienteDto(ClienteDto ClienteDto) {
        this.clienteDto = ClienteDto;
    }

    public PerNaturalRequest getPn() {
        return (PerNaturalRequest) pn;
    }

    public void setPn(PerNaturalRequest cliente) {
        if (cliente != null) {
            this.pn = cliente;
        }
    }

    public PerJuridicaRequest getPj() {
        return pj;
    }

    public void setPj(PerJuridicaRequest cliente) {
        if (cliente != null) {
            this.pj = cliente;
        }
    }

    public int getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public List<MunicipioDto> getLstMunicipio() {
        return ubicacionService.findMunicipioByDepa(codigoDepa);
    }
    // </editor-fold>

    public void nuevo() {
        pn = new PerNaturalRequest();
        pn.setPersoneria("N");
        pn.setDepartamento("06");
        pn.setMunicipio("14");
        pn.setCodigoTipoDoc("13");
        disabled = false;
        tipoPersoneria = 1;
    }

    public void cancelar() {
        inicializar();
    }

    public void iniciarClient() {
        pn = new PerNaturalRequest();
        pj = new PerJuridicaRequest();
    }

    public void guardar() {
        int codeResponse;
        if (tipoPersoneria == 1) {
            pn.setDepartamento(codigoDepa);
            //pn.setTipoDocumento(1);
            pn.setActivo(true);

            if (edit) {
                codeResponse = RestUtil
                        .builder()
                        .clazz(ClienteDto.class)
                        .jwtDto(sessionService.getToken())
                        .endpoint("/api/secured/client/pn/")
                        .build()
                        .callUpdClient(clienteDto.getIdCliente(), pn);
            } else {
                codeResponse = RestUtil
                        .builder()
                        .clazz(String.class)
                        .jwtDto(sessionService.getToken())
                        .body(pn)
                        .endpoint("/api/secured/client/pn/")
                        .build()
                        .callPostAuth().getCodeHttp();
            }
        } else {
            pj.setDepartamentoEmp(codigoDepa);
            if (edit) {
                pj.setActivo(true);
                codeResponse = RestUtil
                        .builder()
                        .clazz(ClienteDto.class)
                        .jwtDto(sessionService.getToken())
                        .endpoint("/api/secured/client/pj/")
                        .build()
                        .callUpdClient(clienteDto.getIdCliente(), pj);
            } else {

                codeResponse = RestUtil
                        .builder()
                        .clazz(String.class)
                        .jwtDto(sessionService.getToken())
                        .body(pj)
                        .endpoint("/api/secured/client/pj/")
                        .build()
                        .callPostAuth().getCodeHttp();
            }
        }

        JsfUtil.mensajeFromEnum(codeResponse != 200 ? TipoMensaje.ERROR : (!edit ? TipoMensaje.INSERT : TipoMensaje.UPDATE));

        inicializar();

        PrimeFaces.current().ajax().update("tblCli");
    }

    public void onRowSelect(SelectEvent<ClienteDto> event) {
        edit = true;
        disabled = false;
        clienteDto = event.getObject();
        idMuni = clienteDto.getIdMunicipio();
        MunicipioDto m = (MunicipioDto) RestUtil.builder()
                .clazz(MunicipioDto.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/catalogo/municipio/" + idMuni)
                .build().callGetOneAuth().getBody();
        codigoDepa = m.getCodDepartamento();

        ResponseRestApi response = RestUtil.builder()
                .clazz(Cliente.class)
                .jwtDto(sessionService.getToken())
                .endpoint("/api/secured/client/update/" + clienteDto.getIdCliente())
                .build()
                .callGetOneAuth();

        if (response.getCodeHttp() == 200) {
            Cliente client = (Cliente) response.getBody();
            tipoPersoneria = client.getTipoPersoneria();

            if (client.getTipoPersoneria() == 1) {
                pn = new PerNaturalRequest();
                inscritoIva = (client.getNrcPersona() != null);

                pn.setCodigoActividad(client.getCodigoActividad());
                pn.setDepartamento(client.getDepartamento());
                pn.setDireccion(client.getDireccion());
                pn.setEmail(client.getEmail());
                pn.setMunicipio(client.getMunicipio());
                pn.setNit(client.getNit());
                pn.setNombreCompleto(client.getRazonSocial());
                pn.setNrc(client.getNrcPersona());
                pn.setNumDocumento(client.getNumDocumento());
                pn.setPersoneria("N");
                pn.setTelefono(client.getTelefono());
                //pn.setTipoDocumento(client.getTipoDocumento());
                pn.setActivo(client.getActivo());
                pn.setCodigoTipoDoc(client.getCodigoTipoDoc());
            } else {
                pj = new PerJuridicaRequest();
                inscritoIva = true;

                pj.setCodigoActividad(client.getCodigoActividad());
                pj.setDepartamentoEmp(client.getDepartamento());
                pj.setMunicipioEmp(client.getMunicipioEmp());
                pj.setDireccionEmp(client.getDireccionEmp());
                pj.setEmailEmp(client.getEmail());
                pj.setNit(client.getNit());
                pj.setNrc(client.getNrc());
                pj.setRazonSocial(client.getRazonSocial());
                pj.setNombreComercial(client.getNombreComercial());
                pj.setPersoneria("J");
                pj.setTelefono(client.getTelefono());
                pj.setActivo(client.getActivo());
            }
        }
    }
}
