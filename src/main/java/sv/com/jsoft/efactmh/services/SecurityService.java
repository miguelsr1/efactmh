package sv.com.jsoft.efactmh.services;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.wildfly.security.http.oidc.OidcPrincipal;

import java.io.Serializable;
import java.util.Base64;

@Named
@SessionScoped
public class SecurityService implements Serializable {

    HttpServletRequest request;

    @Getter
    private String jwt;

    public SecurityService(){}

    @Inject
    public SecurityService(HttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    public void init() {
        loadJwtFromOidc();
    }

    private void loadJwtFromOidc() {
        var oidcPrincipal = (OidcPrincipal) request.getUserPrincipal();
        request.isUserInRole("ROL_EMISOR");
        jwt =  new String(Base64.getDecoder().decode(oidcPrincipal.getOidcSecurityContext().getIDTokenString().split("\\.")[1]));
    }
}
