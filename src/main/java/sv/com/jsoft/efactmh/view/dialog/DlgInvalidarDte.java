package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.dto.DteToInvalidate;
import sv.com.jsoft.efactmh.model.dto.InvalidateRequest;
import sv.com.jsoft.efactmh.services.DteService;
import sv.com.jsoft.efactmh.services.InvalidateService;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.view.SessionView;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
@Slf4j
public class DlgInvalidarDte implements Serializable {

    private Long idFactura;
    @Getter
    private DteToInvalidate dte;
    @Getter
    @Setter
    private int tipoInvalidacion;
    @Getter
    @Setter
    private String motivo;
    @Getter
    @Setter
    private String nomResponsable;
    @Getter
    @Setter
    private String tipoDocResponsable;
    @Getter
    @Setter
    private String numDocResponsable;
    @Getter
    @Setter
    private String nomSolicitante;
    @Getter
    @Setter
    private String tipoDocSolicitante;
    @Getter
    @Setter
    private String numDocSolicitante;
    @Getter
    @Setter
    private String dteR;

    @Inject
    SessionService sessionService;
    @Inject
    InvalidateService invalidateService;
    @Inject
    SessionView sessionView;

    @PostConstruct
    public void init() {
        idFactura = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idFactura");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idFactura");
        loadDte();
    }

    public void loadDte() {
        dte = invalidateService.findDteToInvalidate(idFactura, sessionService.getToken());
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void sendDteInvalidate() {
        //enviar dte a invalidar
        InvalidateRequest request = new InvalidateRequest();
        
        request.setIdEstablecimiento(Long.valueOf(sessionView.getIdEstablecimiento()));
        request.setIdPuntoVenta(sessionView.getIdPuntoVenta() == null ? null : Long.valueOf(sessionView.getIdPuntoVenta()));
        request.setIdFactura(idFactura);
        request.setMotivoAnulacion(motivo);
        request.setNombreResponsable(nomResponsable);
        request.setNombreSolicita(nomSolicitante);
        request.setNumDocResponsable(numDocResponsable);
        request.setNumDocSolicita(numDocSolicitante);
        request.setTipoAnulacion(tipoInvalidacion);
        request.setTipoDocResponsable(tipoDocResponsable);
        request.setTipoDocSolicita(tipoDocSolicitante);
        
        ResponseRestApi response = invalidateService.createInvalidate(request, sessionService.getToken());
        
        PrimeFaces.current().dialog().closeDynamic(null);
        
        switch (response.getCodeHttp()) {
            case 200:
                JsfUtil.showMessageDialog(FacesMessage.SEVERITY_INFO, "INFORMACION", "DTE ANULADO");
                break;
            case 400:
                JsfUtil.showMessageDialog(FacesMessage.SEVERITY_ERROR, "ERROR", "OCURRIO UN ERROR EN LA ANULACION");
                break;
        }
    }
}
