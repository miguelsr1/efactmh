package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.dto.DteToInvalidate;
import sv.com.jsoft.efactmh.services.DteService;
import sv.com.jsoft.efactmh.services.SessionService;

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
    DteService dteService;
    @Inject
    SessionService sessionService;

    @PostConstruct
    public void init() {
        //idFactura = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idFactura");
        idFactura = 151l;
        dte = new DteToInvalidate();
        loadDte();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idFactura");
    }

    public void loadDte() {
        dte = dteService.findDteToInvalidate(idFactura, sessionService.getToken());
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void sendDteInvalidate() {
        //enviar dte a invalidar
        
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
