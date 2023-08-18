package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author migue
 */
public class Efactura implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEfactura;
    private String codigoGeneracion;
    private Date fechaEmision;
    private String tipoDte;
    private String estado;

    public Efactura() {
    }
}
