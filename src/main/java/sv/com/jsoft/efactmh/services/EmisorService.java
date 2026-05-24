package sv.com.jsoft.efactmh.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
@Slf4j
public class EmisorService {

    @Inject
    SessionService sessionService;

    public ResponseRestApi<Emisor> getEmisor() {
        return RestUtil
                .builder()
                .clazz(Emisor.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/emisor")
                .build()
                .callGetOneAuth();
    }
}
