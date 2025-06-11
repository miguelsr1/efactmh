package sv.com.jsoft.efactmh.services;

import com.google.gson.FieldNamingPolicy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.LoginDto;
import sv.com.jsoft.efactmh.model.dto.ResponseDto;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class LoginServices {

    public ResponseDto login(LoginDto login) {
        RestUtil rest = RestUtil.builder().endpoint("/api/security/login").build();
        return rest.callPost(login);
    }

    public JwtDto getToken(ResponseDto responseDto) {
        return RestUtil.builder().build().getDataByTypeClass(responseDto, JwtDto.class, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }
}
