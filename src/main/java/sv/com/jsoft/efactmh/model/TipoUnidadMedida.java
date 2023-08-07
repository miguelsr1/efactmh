package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author migue
 */
@Entity
@Table(name = "tipo_unidad_medida")
@NamedQueries({
    @NamedQuery(name = "TipoUnidadMedida.findAll", query = "SELECT t FROM TipoUnidadMedida t"),
    @NamedQuery(name = "TipoUnidadMedida.findByIdUnidadMedida", query = "SELECT t FROM TipoUnidadMedida t WHERE t.idUnidadMedida = :idUnidadMedida"),
    @NamedQuery(name = "TipoUnidadMedida.findByCodigo", query = "SELECT t FROM TipoUnidadMedida t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TipoUnidadMedida.findByDescripcion", query = "SELECT t FROM TipoUnidadMedida t WHERE t.descripcion = :descripcion")})
public class TipoUnidadMedida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;

    public TipoUnidadMedida() {
    }

    public TipoUnidadMedida(Integer idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public Integer getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(Integer idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUnidadMedida != null ? idUnidadMedida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoUnidadMedida)) {
            return false;
        }
        TipoUnidadMedida other = (TipoUnidadMedida) object;
        if ((this.idUnidadMedida == null && other.idUnidadMedida != null) || (this.idUnidadMedida != null && !this.idUnidadMedida.equals(other.idUnidadMedida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.jsoft.efactmh.model.TipoUnidadMedida[ idUnidadMedida=" + idUnidadMedida + " ]";
    }
    
}
