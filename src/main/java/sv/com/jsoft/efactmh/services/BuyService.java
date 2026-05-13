package sv.com.jsoft.efactmh.services;

import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import sv.com.jsoft.efactmh.model.dto.*;
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

    public ResponseRestApi<ApiMhDteResponse> save(String json, LocalDate buyDate, int idTipoDocumento, String numDocumento, JwtDto token) {

        BuyContriDtoRequest request = new BuyContriDtoRequest();
        request.setJson(json);
        request.setAnho((short) buyDate.getYear());
        request.setMes((short) buyDate.getMonthValue());
        request.setIdTipoDocumento(idTipoDocumento);
        request.setNumDocumento(numDocumento);

        RestUtil restUtil = RestUtil.builder()
                .endpoint("/api/secured/buy/contri")
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

    public List<BuyDtoResponse> getListContri(LocalDate buyDate, int idTipoDocumento, String numDocumento, JwtDto token) {
        RestUtil restUtil = RestUtil.builder()
                .endpoint("/api/secured/buy/contri/" + idTipoDocumento + "/" + numDocumento + "/" + buyDate.getYear() + "/" + buyDate.getMonthValue() + "/")
                .clazz(BuyDtoResponse.class)
                .jwtDto(token)
                .build();

        return (List<BuyDtoResponse>) restUtil.callGetAllAuth().getBody();
    }
}
