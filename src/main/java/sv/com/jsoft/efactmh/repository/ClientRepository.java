package sv.com.jsoft.efactmh.repository;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import sv.com.jsoft.efactmh.dao.ClientDao;
import sv.com.jsoft.efactmh.db.DataBaseSupport;
import sv.com.jsoft.efactmh.db.DataSourceApp;
import sv.com.jsoft.efactmh.db.util.NativeQuery;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;

/**
 *
 * @author msanchez
 */
@Stateless
@Slf4j
public class ClientRepository {

    Jdbi jdbi;

    @Inject
    private DataSourceApp dsApp;
    @Inject
    private DataBaseSupport databaseSupport;

    private ClientDao clientDao;

    @PostConstruct
    public void init() {
        jdbi = databaseSupport.getJdbi();

        clientDao = jdbi.onDemand(ClientDao.class);
    }

    public ClienteResponse get(Long idCliente) {
        return clientDao.get(idCliente);
    }

    public List<ClienteResponse> findClientBySearch(String query, String correo) {
        try (Handle handle = jdbi.open()) {
            handle.registerRowMapper(ConstructorMapper.factory(ClienteResponse.class));

            List<ClienteResponse> lst = handle
                    .createQuery(NativeQuery.FIND_CLIENT_AUTOCOMPLETE)
                    .bind("query", "%" + query + "%")
                    .bind("correo", correo)
                    .mapTo(ClienteResponse.class)
                    .list();

            return lst;
        } catch (Exception e) {
            log.error("OCURRIO UN ERROR", e);
            return List.of();
        }
    }
}
