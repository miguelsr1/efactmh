package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.Departamento;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class TipoItemService implements Serializable {

    @Getter
    private List<Departamento> lstTipoItems;

    @PostConstruct
    public void init() {
        lstTipoItems = Arrays.asList(new Departamento("1", "BIEN"),
                new Departamento("2", "SERVICIO"),
                new Departamento("3", "AMBOS"),
                new Departamento("4", "OTROS"));
    }

}
