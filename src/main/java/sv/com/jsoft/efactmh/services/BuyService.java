package sv.com.jsoft.efactmh.services;

import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;

import lombok.Getter;
import sv.com.jsoft.efactmh.model.dto.*;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class BuyService implements Serializable {

    @Getter
    private List<CostClassificationDto> lstCost;
    
    @Getter
    private List<MesDto> lstMeses;
    
    @Getter
    private List<AnioDto> lstAnios;

    @PostConstruct
    public void init(){
        lstCost = new ArrayList();

        lstCost.add(new CostClassificationDto(1, "COSTO", "pi pi-sync"));
        lstCost.add(new CostClassificationDto(2, "GASTO", "pi pi-wallet"));
        
        lstMeses = new ArrayList();
        lstMeses.add(new MesDto(1, "Enero"));
        lstMeses.add(new MesDto(2, "Febrero"));
        lstMeses.add(new MesDto(3, "Marzo"));
        lstMeses.add(new MesDto(4, "Abril"));
        lstMeses.add(new MesDto(5, "Mayo"));
        lstMeses.add(new MesDto(6, "Junio"));
        lstMeses.add(new MesDto(7, "Julio"));
        lstMeses.add(new MesDto(8, "Agosto"));
        lstMeses.add(new MesDto(9, "Septiembre"));
        lstMeses.add(new MesDto(10, "Octubre"));
        lstMeses.add(new MesDto(11, "Noviembre"));
        lstMeses.add(new MesDto(12, "Diciembre"));
        
        lstAnios = new ArrayList();
        int currentYear = LocalDate.now().getYear();
        lstAnios.add(new AnioDto(currentYear, String.valueOf(currentYear)));
        lstAnios.add(new AnioDto(currentYear - 1, String.valueOf(currentYear - 1)));
    }

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
