package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.services.CatalogoService;
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

    @Getter
    @Setter
    private String idEstablecimiento;
    @Getter
    @Setter
    private CatalogoDto puntoVenta;

    public List<CatalogoDto> getLstPuntoVenta() {
        if (idEstablecimiento == null) {
            return new ArrayList<>();
        }
        return sessionService.getLstPuntoVenta(Long.valueOf(idEstablecimiento));
    }

}
