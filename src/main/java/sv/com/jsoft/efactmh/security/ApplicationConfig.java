package sv.com.jsoft.efactmh.security;

import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

/**
 *
 * @author hantsy
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:/FacturaDS",
        callerQuery = "select password from usuario where correo_electronico = ?",
        //groupsQuery = "select rol from operador where user = ?",
        //hashAlgorithm = Pbkdf2PasswordHash.class,
        priorityExpression = "#{100}"/*,
        /*hashAlgorithmParameters = {
            "Pbkdf2PasswordHash.Iterations=3072",
            "${applicationConfig.dyna}"
        }*/ // just for test / example
)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/index.xhtml",
                errorPage = "/access.xhtml", // DRAFT API - must be set to empty for now
                useForwardToLogin = false,
                useForwardToLoginExpression = ""
        )
)
@Deprecated
public class ApplicationConfig {

    /*public String[] getDyna() {
        return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
    }*/

}
