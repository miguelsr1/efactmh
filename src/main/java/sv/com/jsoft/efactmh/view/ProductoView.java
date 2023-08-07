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
import sv.com.jsoft.efactmh.model.TipoUnidadMedida;
import sv.com.jsoft.efactmh.repository.ProductoRepo;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.util.JsfUtil;

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

    @Inject
    private ProductoRepo productoRepo;
    @Inject
    private CatalogoService catalogoService;

    {
        producto = new Producto();
        lstProducto = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        lstProducto = productoRepo.findAll();
    }

    public void guardar() {
        productoRepo.save(producto);
        JsfUtil.mensajeInsert();
        lstProducto = productoRepo.findAll();
    }

    public List<TipoUnidadMedida> getLstUnidadMedida() {
        return catalogoService.getLstUnidadMedida();
    }
}
