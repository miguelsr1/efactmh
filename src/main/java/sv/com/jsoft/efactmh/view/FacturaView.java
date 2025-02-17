package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class FacturaView implements Serializable{
    
    @Setter
    @Getter
    private String tipoDte;
}
