package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.com.jsoft.efactmh.services.SessionService;

/**
 *
 * @author msanchez
 */
@Named
@ViewScoped
public class ConfigView implements Serializable {

    @Inject
    SessionService sessionService;

    

    

}
