package sv.com.jsoft.efactmh.db;

import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class DataBaseSupport {

    @Inject
    DataSourceApp dsApp;

    @Getter
    Jdbi jdbi;

    @Getter
    Jdbi jdbiUsuariosPortal;

    @PostConstruct
    public void init() {
        try {
            jdbi = Jdbi.create(dsApp.getEfactDS());
            jdbi.installPlugin(new SqlObjectPlugin());
        } catch (Exception ex) {
            log.error("OCURRIO UN ERROR EN DataBaseSupport.init()", ex);
        }
    }
}
