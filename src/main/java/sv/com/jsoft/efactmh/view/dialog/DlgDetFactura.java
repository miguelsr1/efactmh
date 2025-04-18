package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
@Slf4j
public class DlgDetFactura implements Serializable {

    @Getter
    @Setter
    private DetalleFacturaDto detFactura;

    @PostConstruct
    public void init() {
        detFactura = new DetalleFacturaDto();
    }

    public void addItem() {
        PrimeFaces.current().dialog().closeDynamic(detFactura);
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
