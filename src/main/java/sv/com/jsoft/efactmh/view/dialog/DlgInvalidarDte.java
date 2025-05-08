package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
@Slf4j
public class DlgInvalidarDte implements Serializable {

    private Long idFactura;

    @PostConstruct
    public void init() {
        //idFactura = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idFactura");
        idFactura = 147l;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idFactura");
    }
    
    public void loadDte(){
        
    }
}
