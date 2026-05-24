package sv.com.jsoft.efactmh.services;

import com.google.gson.FieldNamingPolicy;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;

import sv.com.jsoft.efactmh.model.dto.LoginDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@ApplicationScoped
public class LoginServices implements Serializable {

    public ResponseDto login(LoginDto login) {
        RestUtil rest = RestUtil.builder().endpoint("/api/security/login").build();
        return rest.callPost(login);
    }
}
