package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class TipoItemService implements Serializable {

    @Getter
    private List<CatalogoDto> lstTipoItems;

    @PostConstruct
    public void init() {
        lstTipoItems = Arrays.asList(new CatalogoDto("1", "BIEN"),
                new CatalogoDto("2", "SERVICIO"),
                new CatalogoDto("3", "AMBOS"),
                new CatalogoDto("4", "OTROS"));
    }

}
