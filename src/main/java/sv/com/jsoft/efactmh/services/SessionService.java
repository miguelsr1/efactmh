package sv.com.jsoft.efactmh.services;

import java.io.Serializable;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.wildfly.security.http.oidc.OidcPrincipal;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.PlanMensual;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.ParametroDto;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionService implements Serializable {

    @Getter
    private Emisor emisor;
    @Getter
    private ParametroDto parametroDto;
    @Getter
    private Long idContribuyente;
    @Getter
    private List<CatalogoDto> lstEstablecimiento;

    @Inject
    CatalogoService catalogoService;
    @Inject
    EmisorService emisorService;
    @Inject
    ClientRepository clientRepository;
    @Inject
    HttpServletRequest request;
    @Getter
    private OidcPrincipal oidcPrincipal;

    @PostConstruct
    public void init() {
        loadJwtFromOidc();
    }

    private void loadJwtFromOidc() {
        oidcPrincipal = (OidcPrincipal) request.getUserPrincipal();
    }

    public List<String> getRoles() {
        return oidcPrincipal == null ?
                List.of() :
                oidcPrincipal.getOidcSecurityContext()
                        .getToken()
                        .getRealmAccessClaim()
                        .getRoles();
    }

    public String getUserName() {
        return oidcPrincipal == null ?
                "Usuario Desconocido" :
                oidcPrincipal.getOidcSecurityContext()
                        .getToken()
                        .getName();
    }

    public void loadEmisor() {
        ResponseRestApi<Emisor> response = emisorService.getEmisor();
        if (response.getCodeHttp() == 200) {
            emisor = response.getBody();

            idContribuyente = clientRepository.findContribuyenteByUser(emisor.getCorreo());
        }
    }

    public void loadEstablecimiento() {
        lstEstablecimiento = catalogoService.getLstEstablecimiento();
    }


    public List<CatalogoDto> getLstPuntoVenta(Long idEstablecimiento) {
        return catalogoService.getLstPuntoVentaByEstablecimiento(idEstablecimiento);
    }

    public void cargarParametrosMh() {
        ResponseRestApi<List<ParametroDto>> rest = RestUtil
                .builder()
                .clazz(ParametroDto.class)
                .accessToken(getAccessTokenString())
                .endpoint("/api/secured/emisor/parametro/all")
                .build()
                .callGetAllAuth();

        if (rest.getCodeHttp() == 200) {
            List<ParametroDto> lst = rest.getBody();
            parametroDto = lst.stream().filter(param -> param.getActivo()).findFirst().orElse(null);
        }
    }

    public ResponseRestApi<PlanMensual> getPlanMensual() {
        return RestUtil
                .builder()
                .clazz(PlanMensual.class)
                .accessToken(getAccessTokenString())
                .endpoint("/api/secured/plan")
                .build()
                .callGetOneAuth();
    }

    public String getAccessTokenString() {
        return oidcPrincipal.getOidcSecurityContext().getTokenString();
    }
}
