package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.services.CatalogoService;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class ProductoView implements Serializable {

    @Getter
    @Setter
    private Producto producto;
    @Getter
    private List<Producto> lstProducto;

    {
        producto = new Producto();
        lstProducto = new ArrayList<>();
    }
    
    @Inject
    CatalogoService catalogoService;
    
    @PostConstruct
    public void init(){
        lstProducto = catalogoService.getLstProducto();
    }

    public void guardar() {
        
    }
}
