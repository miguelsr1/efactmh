package sv.com.jsoft.efactmh.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import sv.com.jsoft.efactmh.model.MunicipioDto;
import sv.com.jsoft.efactmh.model.Producto;
import sv.com.jsoft.efactmh.model.TipoUnidadMedida;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@ApplicationScoped
public class CatalogoService {

    @Getter
    private List<Producto> lstProducto;
    @Getter
    private List<TipoUnidadMedida> lstTipoUnidadMedida;

    @Getter
    private List<CatalogoDto> lstDepartamentos;
    @Getter
    private List<MunicipioDto> lstMunicipios;
    @Getter
    private List<CatalogoDto> lstGiros;

    @Inject
    SessionService securityService;

    {
        lstProducto = new ArrayList<>();
        lstTipoUnidadMedida = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        //loadProduct();
        //loadTipoUnidadMedida();
        loadDatosUbicacion();
        loadGiros();
    }

    private void loadTipoUnidadMedida() {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("item")
                .clazz(TipoUnidadMedida.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();

        if (response.getCodeHttp() == 200) {
            lstTipoUnidadMedida = (List<TipoUnidadMedida>) response.getBody();
        }
    }

    private void loadDatosUbicacion() {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/departamento")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();

        if (response.getCodeHttp() == 200) {
            lstDepartamentos = (List<CatalogoDto>) response.getBody();
        }

        response = RestUtil.builder()
                .endpoint("/api/catalogo/municipio")
                .clazz(MunicipioDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();

        if (response.getCodeHttp() == 200) {
            lstMunicipios = (List<MunicipioDto>) response.getBody();
        }
    }

    public List<MunicipioDto> getMunicipioDtoByCodDepa(String codDepa) {
        return lstMunicipios
                .stream()
                .filter(mun -> mun.getCodDepartamento().compareTo(codDepa) == 0)
                .sorted(Comparator.comparing(MunicipioDto::getNombre))
                .collect(Collectors.toList());
    }

    private void loadGiros() {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/giro")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();
        if (response.getCodeHttp() == 200) {
            lstGiros = (List<CatalogoDto>) response.getBody();
        }
    }

    private void loadProduct() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8090/hello/all")).GET().build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Type lst = new TypeToken<List<Producto>>() {
            }.getType();

            Gson gson = new Gson();

            lstProducto = gson.fromJson(response.body(), lst);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Producto getProductoByCodigo(String codigo) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8090/hello/producto/" + codigo + "/")).GET().build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();

            return gson.fromJson(response.body(), Producto.class);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            Logger.getLogger(CatalogoService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<CatalogoDto> getLstEstablecimiento(JwtDto token) {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/establecimiento")
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();
        return (response.getCodeHttp() == 200) ? (List<CatalogoDto>) response.getBody() : new ArrayList<>();
    }

    public List<CatalogoDto> getLstPuntoVentaByEstablecimiento(JwtDto token, Long idEstablecimiento) {
        ResponseRestApi response = RestUtil.builder()
                .endpoint("/api/catalogo/punto-venta/" + idEstablecimiento)
                .clazz(CatalogoDto.class)
                .jwtDto(securityService.getToken())
                .build()
                .callGet();
        return (response.getCodeHttp() == 200) ? (List<CatalogoDto>) response.getBody() : new ArrayList<>();
    }
}
