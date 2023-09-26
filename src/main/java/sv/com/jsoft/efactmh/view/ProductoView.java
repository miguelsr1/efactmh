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
import org.primefaces.event.SelectEvent;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.enums.TipoMensaje;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.util.JsfUtil;
import sv.com.jsoft.efactmh.util.RestUtil;

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

    private RestUtil res;

    @PostConstruct
    public void init() {
        lstProducto = catalogoService.getLstProducto();

        res = RestUtil
                .builder()
                .clazz(Producto.class)
                .endpoint("item/")
                .build();
    }

    public void guardar() {
        int codeResponse = res.callPersistir(producto);
        JsfUtil.mensajeFromEnum(codeResponse != 200 ? TipoMensaje.ERROR : (producto.esNuevo() ? TipoMensaje.INSERT : TipoMensaje.UPDATE));

        /*boolean nuevo = producto.getIdProducto() == null;
        if (nuevo) {
            codeResponse = res.callPost(producto);
        } else {
            codeResponse = res.callPut(producto.getIdProducto(), producto);
        }

        if (codeResponse == 200) {
            if (nuevo) {
                JsfUtil.mensajeInsert();
            } else {
                JsfUtil.mensajeUpdate();
            }
        } else {
            JsfUtil.mensajeError("Ah ocurrido un error");
        }*/
    }

    public void nuevo() {
        producto = new Producto();
    }

    public void onRowSelect(SelectEvent<Producto> event) {
        producto = event.getObject();
    }
}
