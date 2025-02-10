package sv.com.jsoft.efactmh.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sv.com.jsoft.efactmh.model.dto.EmisorDto;

/**
 *
 * @author migue
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Emisor extends EmisorDto{

    private String codigoDepartamento;
    private String codigoMunicipio;
}