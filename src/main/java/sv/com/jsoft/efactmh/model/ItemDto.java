package sv.com.jsoft.efactmh.model;

import lombok.Data;

/**
 *
 * @author migue
 */
@Data
public class ItemDto {

    private Integer id;
    private String codigo;
    private String nombre;
    private Integer unidadMedidad;
    private boolean eliminar;
}
