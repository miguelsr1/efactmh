package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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
