package sv.com.jsoft.efactmh.dao;

import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import sv.com.jsoft.efactmh.db.util.NativeQuery;
import sv.com.jsoft.efactmh.model.dto.ClienteResponse;

/**
 *
 * @author msanchez
 */
public interface ClientDao {

    @SqlQuery(NativeQuery.FIND_CLIENT_AUTOCOMPLETE)
    @RegisterBeanMapper(ClienteResponse.class)
    List<ClienteResponse> getImagenesPorSolicitud(@Bind("query") String query,
            @Bind("queryName") String queryName,
            @Bind("correo") String correo);

    @SqlQuery(NativeQuery.FIND_CLIENT)
    @RegisterBeanMapper(ClienteResponse.class)
    ClienteResponse get(Long idCliente);
}
