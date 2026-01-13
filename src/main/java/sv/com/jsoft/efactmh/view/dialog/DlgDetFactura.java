package sv.com.jsoft.efactmh.view.dialog;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.services.CatalogoService;
import sv.com.jsoft.efactmh.services.TipoItemService;
import sv.com.jsoft.efactmh.services.UnidadMedidaService;

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
    
    private Producto producto;
    
    @Inject
    TipoItemService tipoItemService;
    @Inject
    UnidadMedidaService unidadMedidaService;
    @Inject
    CatalogoService catalogoService;
    
    @PostConstruct
    public void init() {
        detFactura = new DetalleFacturaDto();
        detFactura.setCodigoItem("1");
        detFactura.setCodigoUnidad("59");
        detFactura.setTipoVenta(1);
        
        catalogoService.loadItems();
    }
    
    public void addItem() {
        detFactura.setNombre(detFactura.getNombre().toUpperCase());
        
        PrimeFaces.current().dialog().closeDynamic(detFactura);
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public List<CatalogoDto> getItems() {
        return tipoItemService.getLstTipoItems();
    }
    
    public List<CatalogoDto> getUnidadesMedidas() {
        return unidadMedidaService.getLstUnidadesMedidas();
    }
    
    public void selProductoListener() {
        producto = catalogoService.getLstProducto().stream().filter(item -> item.getCodigoProducto().equals(detFactura.getCodigoProducto())).findFirst().orElse(null);
        if (producto != null) {
            detFactura.setPrecioUnitario(producto.getPrecioUnitario());
            detFactura.setNombre(producto.getNombre());
            detFactura.setCodigoItem(producto.getCodigoItem());
            detFactura.setCodigoUnidad(producto.getCodigoUnidad());
        }
    }
}
