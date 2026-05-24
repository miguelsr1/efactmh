package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.LoginDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
import sv.com.jsoft.efactmh.services.LoginServices;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.Constantes;
import sv.com.jsoft.efactmh.util.MessageUtil;

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
    LoginServices loginServices;
    @Inject
    SessionService securityService;

    public String validarProveedor() {
        return validarLogin("app/index");
    }

    private String validarLogin(String urlWelcome) {
        ResponseDto response = loginServices.login(new LoginDto(usuario, claveAcceso));

        switch (response.getStatusCode()) {
            case -1:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_ERROR)
                        .title(Constantes.MSG_ERROR)
                        .message(response.getBody().toString())
                        .build()
                        .showMessage();
                break;
            case 401:
                MessageUtil.builder()
                        .severity(FacesMessage.SEVERITY_ERROR)
                        .title(Constantes.MSG_ERROR)
                        .message("Usuario/Clave de acceso incorrectos!")
                        .build()
                        .showMessage();
                break;
            case 200:
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", usuario);

                setIpAddessAndUserAgent();
                return urlWelcome + "?faces-redirect=true";
            default:
                break;
        }
        return null;
    }

    /**
     * metodo para gaurdar datos importantes de sesión
     *
     * @author
     */
    private void setIpAddessAndUserAgent() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(true);

        httpSession.setAttribute("clientIpAddress", request.getRemoteAddr());
        httpSession.setAttribute("clientUserAgent", request.getHeader("User-Agent"));
        httpSession.setAttribute("loggedIn", true);
    }
}
