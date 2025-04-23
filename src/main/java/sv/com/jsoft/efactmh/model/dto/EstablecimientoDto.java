package sv.com.jsoft.efactmh.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class EstablecimientoDto implements Serializable {

    private Long idEstablecimiento;
    private String nombreSucursal;
    private String codigoEstable;
    private String codigoEstableMh;
    private String tipoEstablecimiento;

    @JsonIgnore
    private Boolean activo;

    public String getStrTipoEstable() {
        switch (tipoEstablecimiento) {
            case "01":
                return "SUCURSAL";
            case "02":
                return "CASA MATRIZ";
            case "04":
                return "BODEGA";
            case "07":
                return "PATIO";
            default:
                return "N/A";
        }
    }
}
