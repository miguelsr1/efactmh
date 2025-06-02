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
import sv.com.jsoft.efactmh.model.Departamento;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
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
    
    @Inject
    TipoItemService tipoItemService;
    @Inject
    UnidadMedidaService unidadMedidaService;

    @PostConstruct
    public void init() {
        detFactura = new DetalleFacturaDto();
        detFactura.setCodigoItem("1");
        detFactura.setCodigoUnidad("59");
    }

    public void addItem() {
        PrimeFaces.current().dialog().closeDynamic(detFactura);
    }
    
    public void closeDgl() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public List<Departamento> getItems(){
        return tipoItemService.getLstTipoItems();
    }
    
    public List<Departamento> getUnidadesMedidas(){
        return unidadMedidaService.getLstUnidadesMedidas();
    }
}
