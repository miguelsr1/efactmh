package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.services.SessionService;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionView implements Serializable{
    
    @Inject
    SessionService sessionService;
    
    @Getter
    private Boolean aceptaPagoPlazo = true;
    
    public String userName(){
        return sessionService.getUserName();
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().clear();
        context.getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
}