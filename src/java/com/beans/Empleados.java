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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "EMPLEADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e")
    , @NamedQuery(name = "Empleados.findByIdEmpleadoPk", query = "SELECT e FROM Empleados e WHERE e.idEmpleadoPk = :idEmpleadoPk")
    , @NamedQuery(name = "Empleados.findByNombre", query = "SELECT e FROM Empleados e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Empleados.findByApellidoPaterno", query = "SELECT e FROM Empleados e WHERE e.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "Empleados.findByApellidoMaterno", query = "SELECT e FROM Empleados e WHERE e.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "Empleados.findByCorreo", query = "SELECT e FROM Empleados e WHERE e.correo = :correo")
    , @NamedQuery(name = "Empleados.findByTelefono", query = "SELECT e FROM Empleados e WHERE e.telefono = :telefono")
    , @NamedQuery(name = "Empleados.findByStatus", query = "SELECT e FROM Empleados e WHERE e.status = :status")
    , @NamedQuery(name = "Empleados.findByNivelUsuario", query = "SELECT e FROM Empleados e WHERE e.nivelUsuario = :nivelUsuario")})
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EMPLEADO_PK")
    private BigDecimal idEmpleadoPk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "APELLIDO_PATERNO")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "APELLIDO_MATERNO")
    private String apellidoMaterno;
    @Size(max = 30)
    @Column(name = "CORREO")
    private String correo;
    @Column(name = "TELEFONO")
    private BigInteger telefono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private BigInteger status;
    @Column(name = "NIVEL_USUARIO")
    private BigInteger nivelUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleadoFk")
    private Collection<Citas> citasCollection;
    @JoinColumn(name = "ID_PUESTO_FK", referencedColumnName = "ID_PUESTO_PK")
    @ManyToOne(optional = false)
    private Puestos idPuestoFk;

    public Empleados() {
    }

    public Empleados(BigDecimal idEmpleadoPk) {
        this.idEmpleadoPk = idEmpleadoPk;
    }

    public Empleados(BigDecimal idEmpleadoPk, String nombre, String apellidoPaterno, String apellidoMaterno, BigInteger status) {
        this.idEmpleadoPk = idEmpleadoPk;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.status = status;
    }

    public BigDecimal getIdEmpleadoPk() {
        return idEmpleadoPk;
    }

    public void setIdEmpleadoPk(BigDecimal idEmpleadoPk) {
        this.idEmpleadoPk = idEmpleadoPk;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public BigInteger getTelefono() {
        return telefono;
    }

    public void setTelefono(BigInteger telefono) {
        this.telefono = telefono;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public BigInteger getNivelUsuario() {
        return nivelUsuario;
    }

    public void setNivelUsuario(BigInteger nivelUsuario) {
        this.nivelUsuario = nivelUsuario;
    }

    @XmlTransient
    public Collection<Citas> getCitasCollection() {
        return citasCollection;
    }

    public void setCitasCollection(Collection<Citas> citasCollection) {
        this.citasCollection = citasCollection;
    }

    public Puestos getIdPuestoFk() {
        return idPuestoFk;
    }

    public void setIdPuestoFk(Puestos idPuestoFk) {
        this.idPuestoFk = idPuestoFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpleadoPk != null ? idEmpleadoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.idEmpleadoPk == null && other.idEmpleadoPk != null) || (this.idEmpleadoPk != null && !this.idEmpleadoPk.equals(other.idEmpleadoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }

}
