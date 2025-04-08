package sv.com.jsoft.efactmh.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.util.Base64;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.dto.JwtDto;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionService implements Serializable {

    @Getter
    private String userName;

    @Getter
    private JwtDto token;

    public void setToken(JwtDto token) {
        this.token = token;
        String jwtData = new String(Base64.getDecoder().decode(token.getAccessToken().split("\\.")[1]));
        userName = new Gson().fromJson(jwtData, JsonObject.class).get("name").getAsString();
    }
}