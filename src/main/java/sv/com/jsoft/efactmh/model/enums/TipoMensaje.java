package sv.com.jsoft.efactmh.model.enums;

import java.util.Arrays;
import javax.faces.application.FacesMessage;

/**
 *
 * @author migue
 */
public enum TipoMensaje {
    INSERT("Registro almacenado satisfactoriamente", FacesMessage.SEVERITY_INFO, "Información"),
    UPDATE("Registro actualizado satisfactoriamente", FacesMessage.SEVERITY_INFO, "Información"),
    ERROR("Ah ocurrido un error", FacesMessage.SEVERITY_ERROR, "Error"),
    CONFLICT("ESTE REGISTRO YA EXISTE", FacesMessage.SEVERITY_WARN, "Alerta"),
    DELETE("Registro eliminado satisfactoriamente", FacesMessage.SEVERITY_INFO, "Información");

    private String mensaje;
    private String tituloMensaje;
    private FacesMessage.Severity tipoMensaje;

    TipoMensaje(String msj, FacesMessage.Severity tipo, String tituloMensaje) {
        mensaje = msj;
        tipoMensaje = tipo;
        this.tituloMensaje = tituloMensaje;
    }

    public static TipoMensaje fromString(String s)
            throws IllegalArgumentException {
        return Arrays.stream(TipoMensaje.values())
                .filter(r -> r.mensaje.equals(r))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown value: " + s));
    }

    public String getMensaje() {
        return mensaje;
    }
    
    public FacesMessage.Severity getTipoMensaje(){
        return tipoMensaje;
    }

    public String getTituloMensaje() {
        return tituloMensaje;
    }

}
