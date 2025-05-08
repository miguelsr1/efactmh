package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.ParametroDto;
import sv.com.jsoft.efactmh.services.SessionService;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class ParametrosView implements Serializable {

    @Inject
    SessionService securityService;

    private List<ParametroDto> lstParametros;
    @Getter
    @Setter
    private ParametroDto parametroDto;

    @PostConstruct
    public void init() {
        loadParametros();
    }

    public List<ParametroDto> getLstParametros() {
        return lstParametros;
    }

    public void loadParametros() {
        RestUtil rest = RestUtil
                .builder()
                .clazz(ParametroDto.class)
                .jwtDto(securityService.getToken())
                .endpoint("/api/secured/emisor/parametro/all")
                .build();

        ResponseRestApi response = rest
                .callGetAuth();
        if (response.getCodeHttp() == 200) {
            lstParametros = (List<ParametroDto>) response.getBody();
        }
    }
}
