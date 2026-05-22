package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import sv.com.jsoft.efactmh.util.JsfUtil;

/**
 *
 * @author migue
 */
@Named
@RequestScoped
public class MonitorDeSessionView implements Serializable {

    private Integer sessionTimeOut = 1500000;

    public void cerrarSession() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        JsfUtil.redirectToIndex();
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
