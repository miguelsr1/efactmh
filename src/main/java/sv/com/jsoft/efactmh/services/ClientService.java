package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.Cliente;
import sv.com.jsoft.efactmh.model.PerNaturalRequest;
import sv.com.jsoft.efactmh.model.Personeria;
import sv.com.jsoft.efactmh.model.dto.ClienteDto;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.model.dto.JwtDto;
import sv.com.jsoft.efactmh.repository.ClientRepository;
import sv.com.jsoft.efactmh.util.ResponseRestApi;
import sv.com.jsoft.efactmh.util.RestUtil;

/**
 *
 * @author msanchez
 */
@Named
@ApplicationScoped
public class ClientService {

    @Inject
    ClientRepository clientRepository;

    public ClienteResponse get(Long idCliente) {
        return clientRepository.get(idCliente);
    }

    public ResponseRestApi<ClienteDto> findAllClient(JwtDto jwt) {
        return RestUtil
                .builder()
                .clazz(ClienteDto.class)
                .jwtDto(jwt)
                .endpoint("/api/secured/client/")
                .build()
                .callGetAllAuth();
    }

    public ResponseRestApi<Cliente> findClientById(JwtDto jwt, Long idClient) {
        return RestUtil.builder()
                .clazz(Cliente.class)
                .jwtDto(jwt)
                .endpoint("/api/secured/client/update/" + idClient)
                .build()
                .callGetOneAuth();
    }

    public int updClient(JwtDto jwt, Long idClient, Personeria p) {
        return RestUtil
                .builder()
                .clazz(ClienteDto.class)
                .jwtDto(jwt)
                .endpoint("/api/secured/client/" + ((p instanceof PerNaturalRequest) ? "pn" : "pj") + "/")
                .build()
                .callUpdClient(idClient, p);
    }

    public int insClient(JwtDto jwt, Long idClient, Personeria p) {
        return RestUtil
                .builder()
                .clazz(String.class)
                .jwtDto(jwt)
                .body(p)
                .endpoint("/api/secured/client/" + ((p instanceof PerNaturalRequest) ? "pn" : "pj") + "/")
                .build()
                .callPostAuth().getCodeHttp();
    }

}
