package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.TipoVentaDto;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class TipoItemService implements Serializable {

    @Getter
    private List<CatalogoDto> lstTipoItems;
    @Getter
    private List<TipoVentaDto> lstTipoVentas;

    @PostConstruct
    public void init() {
        lstTipoItems = Arrays.asList(new CatalogoDto("1", "BIEN"),
                new CatalogoDto("2", "SERVICIO"),
                new CatalogoDto("3", "AMBOS"),
                new CatalogoDto("4", "OTROS"));
        
        lstTipoVentas = Arrays.asList(new TipoVentaDto(1, "GRAVADA"),
                new TipoVentaDto(2, "NO SUJETA"),
                new TipoVentaDto(3, "EXENTA"));
    }

}
