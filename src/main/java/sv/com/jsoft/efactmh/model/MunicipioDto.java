package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class MunicipioDto implements Serializable {

    private String nombre;
    private String codDepartamento;
    private String codigoMunicipio;
}