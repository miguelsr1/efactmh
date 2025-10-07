package sv.com.jsoft.efactmh.db;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class DataSourceApp {

    @Getter
    @Resource(lookup = "java:/EfactDS")
    private DataSource efactDS;

}
