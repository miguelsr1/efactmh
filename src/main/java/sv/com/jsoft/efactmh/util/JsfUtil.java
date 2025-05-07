package sv.com.jsoft.efactmh.util;

import java.io.File;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.enums.TipoMensaje;

@Slf4j
public class JsfUtil {

    public final String PATH_REPORTES = File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "reporte" + File.separator;

    private static FacesMessage msg;

    public static void mensajeUpdate() {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Actualización exitosa.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeInsert() {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Registro almacenado satisfactoriamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeDelete() {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Registro eliminado satisfactoriamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeFromEnum(TipoMensaje tipoMsj) {
        msg = new FacesMessage(tipoMsj.getTipoMensaje(), tipoMsj.getTituloMensaje(), tipoMsj.getMensaje());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeAlerta(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "" + mensaje + "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeError(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "" + mensaje + "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void mensajeInformacion(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "" + mensaje + "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void crearCookie(String name, String valor) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext.getExternalContext().getRequestCookieMap().containsKey(name)) {

        } else {
            Cookie cookie = new Cookie(name, valor);
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 días en segundos
            cookie.setPath("/"); // Disponible en toda la aplicación
            cookie.setHttpOnly(true); // Opcional: evita acceso por JavaScript

            ((HttpServletResponse) facesContext.getExternalContext().getResponse()).addCookie(cookie);
        }
    }

    public static void eliminarCookie(String nombre) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Cookie cookie = new Cookie(nombre, null);
        cookie.setPath("/");         // Debe coincidir con el path original
        cookie.setMaxAge(0);         // 0 = eliminar inmediatamente
        cookie.setHttpOnly(true);    // Opcional, igual que al crearla

        response.addCookie(cookie);
    }

    public static void redirectToIndex() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getApplicationContextPath());
        } catch (IOException ex) {
            log.error("Error redireccionando al Index", ex);
        }
    }
    
    public static void showMessageDialog(FacesMessage.Severity severity, String title, String message){
        msg = new FacesMessage(severity, title, message);
        PrimeFaces.current().dialog().showMessageDynamic(msg);
    }
}
