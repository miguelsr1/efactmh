package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Operador;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionView implements Serializable {
    
    @Getter
    private Operador usuario;

    @Inject
    SecurityContext securityContext;
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().clear();
        context.getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

}
