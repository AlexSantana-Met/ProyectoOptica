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
@Table(name = "PUESTOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puestos.findAll", query = "SELECT p FROM Puestos p")
    , @NamedQuery(name = "Puestos.findByIdPuestoPk", query = "SELECT p FROM Puestos p WHERE p.idPuestoPk = :idPuestoPk")
    , @NamedQuery(name = "Puestos.findByNombrePuesto", query = "SELECT p FROM Puestos p WHERE p.nombrePuesto = :nombrePuesto")
    , @NamedQuery(name = "Puestos.findByArea", query = "SELECT p FROM Puestos p WHERE p.area = :area")
    , @NamedQuery(name = "Puestos.findByDescripcion", query = "SELECT p FROM Puestos p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "Puestos.findByStatus", query = "SELECT p FROM Puestos p WHERE p.status = :status")})
public class Puestos implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PUESTO_PK")
    private BigDecimal idPuestoPk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NOMBRE_PUESTO")
    private String nombrePuesto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AREA")
    private String area;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private BigInteger status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPuestoFk")
    private Collection<Empleados> empleadosCollection;

    public Puestos() {
    }

    public Puestos(BigDecimal idPuestoPk) {
        this.idPuestoPk = idPuestoPk;
    }

    public Puestos(BigDecimal idPuestoPk, String nombrePuesto, String area, String descripcion, BigInteger status) {
        this.idPuestoPk = idPuestoPk;
        this.nombrePuesto = nombrePuesto;
        this.area = area;
        this.descripcion = descripcion;
        this.status = status;
    }

    public BigDecimal getIdPuestoPk() {
        return idPuestoPk;
    }

    public void setIdPuestoPk(BigDecimal idPuestoPk) {
        this.idPuestoPk = idPuestoPk;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
    public Collection<Empleados> getEmpleadosCollection() {
        return empleadosCollection;
    }

    public void setEmpleadosCollection(Collection<Empleados> empleadosCollection) {
        this.empleadosCollection = empleadosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPuestoPk != null ? idPuestoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puestos)) {
            return false;
        }
        Puestos other = (Puestos) object;
        if ((this.idPuestoPk == null && other.idPuestoPk != null) || (this.idPuestoPk != null && !this.idPuestoPk.equals(other.idPuestoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombrePuesto;
    }
    
}
