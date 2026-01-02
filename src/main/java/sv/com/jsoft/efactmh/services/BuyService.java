package sv.com.jsoft.efactmh.services;

import com.google.gson.JsonObject;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class BuyService {

    public ResponseRestApi<ApiMhDteResponse> save(String json, JwtDto token) {
        JsonObject jsonRequest = new JsonObject();
        
        jsonRequest.addProperty("json", json);
        
        RestUtil restUtil = RestUtil.builder()
                .endpoint("/api/secured/buy")
                .clazz(ApiMhDteResponse.class)
                .jwtDto(token)
                .body(jsonRequest.toString())
                .build();

        return restUtil.callPostAuth();
    }
}
