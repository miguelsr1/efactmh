package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.LoginDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
import sv.com.jsoft.efactmh.services.LoginServices;
import sv.com.jsoft.efactmh.services.SessionService;
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
    LoginServices loginServices;
    @Inject
    SessionService securityService;
    
    public String validarProveedor() {
        return validarLogin("app/home");
    }
    
    private String validarLogin(String urlWelcome) {
        LoginDto loginDto = new LoginDto(usuario, claveAcceso);
        
        ResponseDto response = loginServices.login(loginDto);
        switch (response.getStatusCode()) {
            case 401:
                JsfUtil.mensajeError("Usuario/Clave de acceso incorrectos!");
                break;
            case 200:
                securityService.setToken(loginServices.getToken(response));
                return urlWelcome + "?faces-redirect=true";
            default:
                break;
        }
        return null;
    }
    
}
