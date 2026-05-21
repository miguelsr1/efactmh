package sv.com.jsoft.efactmh.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import lombok.Getter;
import sv.com.jsoft.efactmh.model.Emisor;
import sv.com.jsoft.efactmh.model.PlanMensual;
import sv.com.jsoft.efactmh.model.dto.CatalogoDto;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.model.dto.ParametroDto;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author migue
 */
@Named
@SessionScoped
public class SessionService implements Serializable {

    @Getter
    private String rolUsuario;
    @Getter
    private Emisor emisor;
    @Getter
    private ParametroDto parametroDto;
    @Getter
    private String userName;
    @Getter
    private Long idContribuyente;
    @Getter
    private JwtDto token;
    private String jwtData;
    @Getter
    private List<CatalogoDto> lstEstablecimiento;

    EmisorService emisorService;
    ClientRepository clientRepository;
    SecurityService securityService;

    public SessionService() {
    }

    @Inject
    public SessionService(EmisorService emisorService, ClientRepository clientRepository, SecurityService securityService){
        this.emisorService = emisorService;
        this.clientRepository = clientRepository;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        jwtData = securityService.getJwt();
        userName = new Gson().fromJson(jwtData, JsonObject.class).get("name").getAsString();

        setRol();

        if(rolUsuario.equals("ROLE_EMISOR")) {
            cargarParametrosMh();
            loadEmisor();
        }
    }

    private void setRol() {
        try {
            String[] chunks = jwtData.split("\\.");

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(payload);

            JsonNode rolesNode = jsonNode
                    .path("resource_access")
                    .path("efactura-ws")
                    .path("roles");

            if (rolesNode.isArray()) {

                for (JsonNode role : rolesNode) {
                    rolUsuario = role.asText();
                }
            }
        } catch (JsonProcessingException e) {
            rolUsuario = null;
        }
    }


    private void loadEmisor() {
        ResponseRestApi<Emisor> response = emisorService.getEmisor(token);
        if (response.getCodeHttp() == 200) {
            emisor = response.getBody();

            idContribuyente =  clientRepository.findContribuyenteByUser(emisor.getCorreo());
        }
    }

    public void loadEstablecimiento(List<CatalogoDto> lst) {
        lstEstablecimiento = lst;
    }

    private void cargarParametrosMh() {
        ResponseRestApi rest = RestUtil
                .builder()
                .clazz(ParametroDto.class)
                .jwtDto(token)
                .endpoint("/api/secured/emisor/parametro/all")
                .build()
                .callGetAllAuth();

        if (rest.getCodeHttp() == 200) {
            List<ParametroDto> lst = (List<ParametroDto>) rest.getBody();
            parametroDto = lst.stream().filter(param -> param.getActivo()).findFirst().orElse(null);
        }
    }

    public ResponseRestApi<PlanMensual> getPlanMensual() {
        return RestUtil
                .builder()
                .clazz(PlanMensual.class)
                .jwtDto(token)
                .endpoint("/api/secured/plan")
                .build()
                .callGetOneAuth();
    }
}
