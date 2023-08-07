/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.com.jsoft.efactmh.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author migue
 */
@Entity
@Table(name = "efactura")
@NamedQueries({
    @NamedQuery(name = "Efactura.findAll", query = "SELECT e FROM Efactura e")})
public class Efactura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_efactura")
    private Integer idEfactura;
    @Size(max = 32)
    @Column(name = "codigo_generacion")
    private String codigoGeneracion;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Size(max = 2)
    @Column(name = "tipo_dte")
    private String tipoDte;
    @Size(max = 45)
    @Column(name = "estado")
    private String estado;

    public Efactura() {
    }

    public Efactura(Integer idEfactura) {
        this.idEfactura = idEfactura;
    }

    public Integer getIdEfactura() {
        return idEfactura;
    }

    public void setIdEfactura(Integer idEfactura) {
        this.idEfactura = idEfactura;
    }

    public String getCodigoGeneracion() {
        return codigoGeneracion;
    }

    public void setCodigoGeneracion(String codigoGeneracion) {
        this.codigoGeneracion = codigoGeneracion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoDte() {
        return tipoDte;
    }

    public void setTipoDte(String tipoDte) {
        this.tipoDte = tipoDte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEfactura != null ? idEfactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Efactura)) {
            return false;
        }
        Efactura other = (Efactura) object;
        if ((this.idEfactura == null && other.idEfactura != null) || (this.idEfactura != null && !this.idEfactura.equals(other.idEfactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.jsoft.efactmh.model.Efactura[ idEfactura=" + idEfactura + " ]";
    }
    
}
