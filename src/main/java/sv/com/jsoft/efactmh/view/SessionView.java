package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.Constantes;
import static sv.com.jsoft.efactmh.util.Constantes.MSG_ALERT;
import sv.com.jsoft.efactmh.util.JsfUtil;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionView implements Serializable {

    @Inject
    SessionService sessionService;

    private String idEstablecimiento;
    private String idPuntoVenta;

    @Getter
    @Setter
    private Boolean aceptaPagoPlazo = true;
    //private boolean sinParametrosIniciales = false;

    @PostConstruct
    public void init() {
        loadCookies();
    }

    private void loadCookies() {
        Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        if (requestCookieMap.containsKey("idEstable")) {
            idEstablecimiento = ((Cookie) requestCookieMap.get("idEstable")).getValue();
        }
        if (requestCookieMap.containsKey("idPuntoV")) {
            idPuntoVenta = ((Cookie) requestCookieMap.get("idPuntoV")).getValue();
        }
    }

    public void validateParamInitial() {
        if (idEstablecimiento == null || idPuntoVenta == null) {
            JsfUtil.showMessageDialog(FacesMessage.SEVERITY_WARN,
                    MSG_ALERT,
                    "ES REQUERIDO REALIZAR LA CONFIGURACIÓN INICIAL");
        }
    }

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(String idEstablecimiento) {
        JsfUtil.eliminarCookie("idPuntoV");
        idPuntoVenta = null;
        if (idEstablecimiento == null) {
            JsfUtil.eliminarCookie("idEstable");
        } else {
            JsfUtil.crearCookie("idEstable", idEstablecimiento);
            JsfUtil.eliminarCookie("idPuntoV");
            //sinParametrosIniciales = false;
        }
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getIdPuntoVenta() {
        return idPuntoVenta;
    }

    public void setIdPuntoVenta(String idPuntoVenta) {
        if (idPuntoVenta == null) {
            JsfUtil.eliminarCookie("idPuntoV");
        } else {
            JsfUtil.crearCookie("idPuntoV", idPuntoVenta);
            //sinParametrosIniciales = false;
        }
        this.idPuntoVenta = idPuntoVenta;
    }

    public String userName() {
        return sessionService.getUserName();
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().clear();
        context.getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public List<CatalogoDto> getLstPuntoVenta() {
        if (idEstablecimiento == null) {
            return new ArrayList<>();
        }
        return sessionService.getLstPuntoVenta(Long.valueOf(idEstablecimiento));
    }
}
