package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.util.JsfUtil;

/**
 *
 * @author migue
 */
@Named
@RequestScoped
public class LoginView implements Serializable {

    @Getter
    @Setter
    @NotEmpty
    private String usuario;
    @Getter
    @Setter
    @NotEmpty
    private String claveAcceso;


    @Inject
    SecurityContext securityContext;
    

    public String validarProveedor() {
        return validarLogin(usuario, claveAcceso, "app/home");
    }

    private String validarLogin(String usuario, String clave, String urlWelcome) {
        switch (processAuthentication(usuario, clave)) {
            case SEND_CONTINUE:
                break;
            case SEND_FAILURE:
                JsfUtil.mensajeError("Usuario/Clave de acceso incorrectos!");
                break;
            case SUCCESS:
                return urlWelcome + "?faces-redirect=true";
            default:
                break;
        }
        return null;
    }

    private AuthenticationStatus processAuthentication(String user, String pass) {
        return securityContext.authenticate(
                (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(),
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
                AuthenticationParameters.withParams().credential(
                        new UsernamePasswordCredential(user, pass))
        );
    }

}
