package sv.com.jsoft.efactmh.view;

import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
@Slf4j
public class TestOmnifacesPushView implements Serializable {

    @Inject
    @Push(channel = "notificaciones") // Mismo nombre que en o:socket
    private PushContext push;

    @Getter
    private String texto = "prueba";

    public void enviarMensajePush() {
        // Envía un mensaje a todos los clientes en el canal "notificaciones"
        try {
            push.send("Paso 1");
            Thread.sleep(2000);
            push.send("Paso 2");
            Thread.sleep(2000);
            push.send("Paso 3");
        } catch (Exception ex) {
            log.error("error enviarMensaje", ex);
        }
    }

    public void mostrarMensajeOS() {

        // Obtener el mensaje recibido del socket
        String message = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("data");

        texto = message;
        PrimeFaces.current().ajax().update("growl");
    }
}
