package sv.com.jsoft.efactmh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author msanchez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadEconomica {

    private String codigoActividad;
    private String actividadEconomica;
    private int orden;
}
