package sv.com.jsoft.efactmh.util;

import java.io.File;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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
}