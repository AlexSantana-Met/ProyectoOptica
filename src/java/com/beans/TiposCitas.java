/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alejandro
 */
@Entity
@Table(name = "TIPOS_CITAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposCitas.findAll", query = "SELECT t FROM TiposCitas t")
    , @NamedQuery(name = "TiposCitas.findByIdTipoPk", query = "SELECT t FROM TiposCitas t WHERE t.idTipoPk = :idTipoPk")
    , @NamedQuery(name = "TiposCitas.findByNombre", query = "SELECT t FROM TiposCitas t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "TiposCitas.findByDescripcion", query = "SELECT t FROM TiposCitas t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "TiposCitas.findByStatus", query = "SELECT t FROM TiposCitas t WHERE t.status = :status")})
public class TiposCitas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_PK")
    private BigDecimal idTipoPk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private BigInteger status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCita")
    private Collection<Citas> citasCollection;

    public TiposCitas() {
    }

    public TiposCitas(BigDecimal idTipoPk) {
        this.idTipoPk = idTipoPk;
    }

    public TiposCitas(BigDecimal idTipoPk, String nombre, String descripcion, BigInteger status) {
        this.idTipoPk = idTipoPk;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.status = status;
    }

    public BigDecimal getIdTipoPk() {
        return idTipoPk;
    }

    public void setIdTipoPk(BigDecimal idTipoPk) {
        this.idTipoPk = idTipoPk;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Citas> getCitasCollection() {
        return citasCollection;
    }

    public void setCitasCollection(Collection<Citas> citasCollection) {
        this.citasCollection = citasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoPk != null ? idTipoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposCitas)) {
            return false;
        }
        TiposCitas other = (TiposCitas) object;
        if ((this.idTipoPk == null && other.idTipoPk != null) || (this.idTipoPk != null && !this.idTipoPk.equals(other.idTipoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
