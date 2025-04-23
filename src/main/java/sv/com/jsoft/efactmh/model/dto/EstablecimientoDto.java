package sv.com.jsoft.efactmh.model.dto;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class EstablecimientoDto {

    private String nombreSucursal;
    private String codigoEstable;
    private String codigoEstableMh;
    private String tipoEstablecimiento;
    private Boolean activo;
}
