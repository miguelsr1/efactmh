package sv.com.jsoft.efactmh.repository;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import sv.com.jsoft.efactmh.db.DataBaseSupport;

@Stateless
@Slf4j
public class ComprasRepository {

    Jdbi jdbi;

    @Inject
    private DataBaseSupport databaseSupport;

    @PostConstruct
    public void init() {
        jdbi = databaseSupport.getJdbi();
    }

    public String getCsvCompras(Integer idTipoDocumento, String numeroDocumento, Integer anio, Integer mes) {
        String sql = "SELECT * FROM public.fn_rpt_csv_compras_contribuyente(:p_id_tipo_documento, :p_numero_documento, :p_anio, :p_mes)";
        try (Handle handle = jdbi.open()) {
            List<String> lines = handle.createQuery(sql)
                    .bind("p_id_tipo_documento", idTipoDocumento)
                    .bind("p_numero_documento", numeroDocumento)
                    .bind("p_anio", anio)
                    .bind("p_mes", mes)
                    .mapTo(String.class)
                    .list();
            return String.join("\n", lines);
        } catch (Exception e) {
            log.error("Error al ejecutar la función fn_rpt_csv_compras_contribuyente", e);
            return null; // O retornar un string de error CSV, ej: "error;ocurrio un problema"
        }
    }

    public String getCsvCompras(Long idContribuyente, Integer anio, Integer mes) {
        String sql = "SELECT * FROM public.fn_rpt_csv_compras(:p_id_contribuyente, :p_anio, :p_mes)";
        try (Handle handle = jdbi.open()) {
            List<String> lines = handle.createQuery(sql)
                    .bind("p_id_contribuyente", idContribuyente.intValue())
                    .bind("p_anio", anio)
                    .bind("p_mes", mes)
                    .mapTo(String.class)
                    .list();
            return String.join("\n", lines);
        } catch (Exception e) {
            log.error("Error al ejecutar la función fn_rpt_csv_compras", e);
            return null;
        }
    }

    public String getCsvConsumidorFinal(Integer idContribuyente, Integer anio, Integer mes) {
        String sql = "SELECT * FROM public.fn_rpt_csv_consumidor_final(:p_id_contribuyente, :p_year, :p_month)";
        try (Handle handle = jdbi.open()) {
            List<String> lines = handle.createQuery(sql)
                    .bind("p_id_contribuyente", idContribuyente)
                    .bind("p_year", anio)
                    .bind("p_month", mes)
                    .mapTo(String.class)
                    .list();
            return String.join("\n", lines);
        } catch (Exception e) {
            log.error("Error al ejecutar la función fn_rpt_csv_consumidor_final", e);
            return null;
        }
    }

    public String getCsvContribuyente(Integer idContribuyente, Integer anio, Integer mes) {
        String sql = "SELECT * FROM public.fn_rpt_csv_contribuyente(:p_id_contribuyente, :p_anho, :p_mes)";
        try (Handle handle = jdbi.open()) {
            List<String> lines = handle.createQuery(sql)
                    .bind("p_id_contribuyente", idContribuyente)
                    .bind("p_anho", anio)
                    .bind("p_mes", mes)
                    .mapTo(String.class)
                    .list();
            return String.join("\n", lines);
        } catch (Exception e) {
            log.error("Error al ejecutar la función fn_rpt_csv_contribuyente", e);
            return null;
        }
    }
}
