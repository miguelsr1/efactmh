package sv.com.jsoft.efactmh.model;

import lombok.Data;

/**
 *
 * @author admin
 */
@Data
public class PlanMensual {

    private Long id;
    private Long idContribuyente;
    private Short tipoPlan;
    private Boolean activo;
    private String fechaCreacion;
    private Boolean pagado;
}
