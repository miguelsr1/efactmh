package sv.com.jsoft.efactmh.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Builder;

/**
 *
 * @author migue
 */
@Builder
public class MessageUtil {

    private String title;
    private String message;
    private FacesMessage.Severity severity;
    
    public void showMessage(){
        FacesContext
                .getCurrentInstance()
                .addMessage(null, 
                        new FacesMessage(severity, title, message));
    }
}
