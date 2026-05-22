package sv.com.jsoft.efactmh.repository;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import sv.com.jsoft.efactmh.dao.ClientDao;
import sv.com.jsoft.efactmh.db.DataBaseSupport;
import sv.com.jsoft.efactmh.db.util.NativeQuery;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;

/**
 *
 * @author msanchez
 */
@ApplicationScoped
@Slf4j
public class ClientRepository implements Serializable {

    Jdbi jdbi;

    @Inject
    DataBaseSupport databaseSupport;

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

            return handle
                    .createQuery(NativeQuery.FIND_CLIENT_AUTOCOMPLETE)
                    .bind("query", "%" + query + "%")
                    .bind("correo", correo)
                    .mapTo(ClienteResponse.class)
                    .list();
        } catch (Exception e) {
            log.error("SIN COINCIDENCIAS", e);
            return List.of();
        }
    }

    public Long findContribuyenteByIdFactura(Long idFactura){
        try (Handle handle = jdbi.open()) {
            return handle.createQuery("select id_contribuyente from factura where id_factura = :idFactura")
                    .bind("idFactura", idFactura)
                    .mapTo(Long.class)
                    .one();
        } catch (Exception e) {
            log.error("ERROR AL OBTENER CONTRIBUYENTE POR ID DE FACTURA", e);
            return null;
        }
    }

    public Long findContribuyenteByUser(String user){
        try (Handle handle = jdbi.open()) {
            return handle.createQuery("select id_contribuyente from contribuyente where correo = :correo")
                    .bind("correo", user)
                    .mapTo(Long.class)
                    .one();
        } catch (Exception e) {
            log.error("ERROR AL OBTENER CONTRIBUYENTE POR USER", e);
            return null;
        }
    }
}
