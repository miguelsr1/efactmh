package sv.com.jsoft.efactmh.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class Encriptador implements Serializable {

    private Map<String, String> parameters = new HashMap<>();

    @Inject
    Pbkdf2PasswordHash passwordHash;

    @PostConstruct
    public void init() {
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
    }

    public String encriptar(String cadenaCaracteres) {
        passwordHash.initialize(parameters);
        return passwordHash.generate(cadenaCaracteres.toCharArray());
    }
}
