package sv.com.jsoft.efactmh.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sv.com.jsoft.efactmh.model.DetalleFacturaDto;
import sv.com.jsoft.efactmh.model.InvoceDto;
import sv.com.jsoft.efactmh.model.dto.ApiMhDteResponse;
import sv.com.jsoft.efactmh.model.dto.ParametroDto;
import sv.com.jsoft.efactmh.model.dto.SendDteRequest;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@ApplicationScoped
@Slf4j
public class DteService implements Serializable {

    private final static ResourceBundle VARIABLES = ResourceBundle.getBundle("variables");

    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {

    }

    public ResponseRestApi<ApiMhDteResponse> getSendMh(SendDteRequest send) {
        RestUtil restUtil = RestUtil.builder()
                .endpoint("/api/secured/dte/send")
                .clazz(ApiMhDteResponse.class)
                .accessToken(sessionService.getAccessTokenString())
                .body(send)
                .build();

        return restUtil.callPostAuth();
    }

    public ResponseRestApi<String> sendMail(Long idFactura) {
        return RestUtil.builder()
                .endpoint("/api/secured/dte/send-mail/" + idFactura)
                .clazz(String.class)
                .accessToken(sessionService.getAccessTokenString())
                .build()
                .callGetOneAuth();
    }
}
