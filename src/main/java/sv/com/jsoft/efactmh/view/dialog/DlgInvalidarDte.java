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
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
import sv.com.jsoft.efactmh.services.InvalidateService;
import sv.com.jsoft.efactmh.services.SessionService;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ALERT;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ERROR;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_INFO;
import sv.com.jsoft.efactmh.util.MessageUtil;
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
    private String codigoDte;
    @Inject
    SessionService sessionService;
    @Inject
    InvalidateService invalidateService;
    @Inject
    SessionView sessionView;
    
    @PostConstruct
    public void init() {
        dte = (DteToInvalidate) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dteInv");
        idFactura = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idFactura");
        codigoDte = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigoDte");
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("dteInv");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idFactura");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("codigoDte");
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void sendDteInvalidate() {
        if (tipoInvalidacion != 2 && !validarDteR()) {
            return;
        }
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
        request.setCodigoGeneracionR(tipoInvalidacion != 2 ? dteR : null);
        
        ResponseRestApi response = invalidateService.createInvalidate(request, sessionService.getToken());
        
        PrimeFaces.current().dialog().closeDynamic(null);
        
        switch (response.getCodeHttp()) {
            case 200:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_INFO)
                        .title(MSG_INFO)
                        .message("DTE ANULADO")
                        .build()
                        .showMessage();
                break;
            case 400:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_ERROR)
                        .title(MSG_ERROR)
                        .message("OCURRIO UN ERROR EN LA ANULACION")
                        .build()
                        .showMessage();
                break;
        }
    }
    
    public void findDteR() {
        validarDteR();
    }
    
    private Boolean validarDteR() {
        ResponseDto resposeDto = invalidateService.findDteToInvalidateByReplace(codigoDte, dteR, sessionService.getToken());
        if (resposeDto.getStatusCode() == 1) {
            MessageUtil.builder()
                    .severity(FacesMessage.SEVERITY_WARN)
                    .title(MSG_ALERT)
                    .message("EL DTE A REEMPLAZAR NO CUMPLE CON LOS TIEMPOS DE INVALIDACION O NO ES DEL MISMO TIPO DE DTE")
                    .build()
                    .showMessage();
            return false;
        }
        return true;
    }
}
