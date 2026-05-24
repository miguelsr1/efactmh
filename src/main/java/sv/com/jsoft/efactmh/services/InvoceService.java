package sv.com.jsoft.efactmh.services;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;

import jakarta.inject.Inject;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.model.dto.IdDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
public class InvoceService implements Serializable {

    @Inject
    SessionService sessionService;

    public ResponseRestApi<IdDto> saveInvoce(InvoceDto invoceDto) {
        //Persistiendo factura
        return RestUtil
                .builder()
                .clazz(IdDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .body(invoceDto)
                .endpoint("/api/secured/invoce")
                .build()
                .callPostAuth();
    }

    public ResponseRestApi<ClienteResponse> findClient(String queryParam){
        return RestUtil
                .builder()
                .clazz(ClienteResponse.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/client/" + queryParam)
                .build()
                .callGetOneAuth();
    }
}
