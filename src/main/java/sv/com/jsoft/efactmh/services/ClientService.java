package sv.com.jsoft.efactmh.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;
import sv.com.jsoft.efactmh.repository.ClientRepository;

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
}
