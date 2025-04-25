package sv.com.jsoft.efactmh.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.ParametroDto;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionService implements Serializable {

    @Getter
    private ParametroDto parametroDto;
    @Getter
    private String userName;
    
    @Getter
    private JwtDto token;
    private List<CatalogoDto> lstEstablecimiento;
    
    @Inject
    CatalogoService catalogoService;

    public void setToken(JwtDto token) {
        this.token = token;
        String jwtData = new String(Base64.getDecoder().decode(token.getAccessToken().split("\\.")[1]));
        userName = new Gson().fromJson(jwtData, JsonObject.class).get("name").getAsString();

        cargarParametrosMh();
        loadEstablecimiento();
    }

    private void loadEstablecimiento(){
        lstEstablecimiento = catalogoService.getLstEstablecimiento(token);
    }
    
    public List<CatalogoDto> getLstEstablecimiento(){
        return lstEstablecimiento;
    }
    
    public List<CatalogoDto> getLstPuntoVenta(Long idEstablecimiento){
        return catalogoService.getLstPuntoVentaByEstablecimiento(token, idEstablecimiento);
    }
    
    private void cargarParametrosMh() {
        RestUtil rest = RestUtil
                .builder()
                .clazz(ParametroDto.class)
                .jwtDto(token)
                .endpoint("/api/secured/emisor/parametro/all")
                .build();

        List<ParametroDto> lst = rest.callGet();

        parametroDto = lst.stream().filter(param -> param.getActivo()).findFirst().orElse(null);
    }
}
