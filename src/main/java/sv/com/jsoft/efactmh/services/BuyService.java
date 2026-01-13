package sv.com.jsoft.efactmh.services;

import com.google.gson.JsonObject;
import java.time.LocalDate;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.BuyDtoRequest;
import sv.com.jsoft.efactmh.model.dto.BuyDtoResponse;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class BuyService {

    public ResponseRestApi<ApiMhDteResponse> save(String json, LocalDate buyDate, JwtDto token) {

        BuyDtoRequest request = new BuyDtoRequest();
        request.setJson(json);
        request.setAnho((short) buyDate.getYear());
        request.setMes((short) buyDate.getMonthValue());

        RestUtil restUtil = RestUtil.builder()
                .endpoint("/api/secured/buy")
                .clazz(ApiMhDteResponse.class)
                .jwtDto(token)
                .body(request)
                .build();

        return restUtil.callPostAuth();
    }

    public List<BuyDtoResponse> getList(LocalDate buyDate, JwtDto token) {
        RestUtil restUtil = RestUtil.builder()
                .endpoint("/api/secured/buy/" + buyDate.getYear() + "/" + buyDate.getMonthValue() + "/")
                .clazz(BuyDtoResponse.class)
                .jwtDto(token)
                .build();

        return (List<BuyDtoResponse>) restUtil.callGetAllAuth().getBody();
    }
}
