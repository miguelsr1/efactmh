package sv.com.jsoft.efactmh.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import sv.com.jsoft.efactmh.model.Cliente;
import sv.com.jsoft.efactmh.model.PerNaturalRequest;
import sv.com.jsoft.efactmh.model.Personeria;
import sv.com.jsoft.efactmh.model.dto.ClienteDto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@Named
@ApplicationScoped
public class ClientService implements Serializable {

    @Inject
    SessionService sessionService;

    @Inject
    ClientRepository clientRepository;

    public ClienteResponse get(Long idCliente) {
        return clientRepository.get(idCliente);
    }

    public ResponseRestApi<List<ClienteDto>>findAllClient() {
        return RestUtil
                .builder()
                .clazz(ClienteDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/client/")
                .build()
                .callGetAllAuth();
    }

    public ResponseRestApi<Cliente> findClientById(Long idClient) {
        return RestUtil.builder()
                .clazz(Cliente.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/client/update/" + idClient)
                .build()
                .callGetOneAuth();
    }

    public int updClient(Long idClient, Personeria p) {
        return RestUtil
                .builder()
                .clazz(ClienteDto.class)
                .accessToken(sessionService.getAccessTokenString())
                .endpoint("/api/secured/client/" + ((p instanceof PerNaturalRequest) ? "pn" : "pj") + "/")
                .build()
                .callUpdClient(idClient, p);
    }

    public int insClient(Long idClient, Personeria p) {
        return RestUtil
                .builder()
                .clazz(String.class)
                .accessToken(sessionService.getAccessTokenString())
                .body(p)
                .endpoint("/api/secured/client/" + ((p instanceof PerNaturalRequest) ? "pn" : "pj") + "/")
                .build()
                .callPostAuth().getCodeHttp();
    }

}
