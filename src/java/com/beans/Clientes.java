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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alejandro
 */
@Entity
@Table(name = "CLIENTES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c")
    , @NamedQuery(name = "Clientes.findByIdClientePk", query = "SELECT c FROM Clientes c WHERE c.idClientePk = :idClientePk")
    , @NamedQuery(name = "Clientes.findByCorreo", query = "SELECT c FROM Clientes c WHERE c.correo = :correo")
    , @NamedQuery(name = "Clientes.findByPass", query = "SELECT c FROM Clientes c WHERE c.pass = :pass")
    , @NamedQuery(name = "Clientes.findByNombre", query = "SELECT c FROM Clientes c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Clientes.findByApellidoPaterno", query = "SELECT c FROM Clientes c WHERE c.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "Clientes.findByApellidoMaterno", query = "SELECT c FROM Clientes c WHERE c.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "Clientes.findByFechaNacimiento", query = "SELECT c FROM Clientes c WHERE c.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Clientes.findByDireccion", query = "SELECT c FROM Clientes c WHERE c.direccion = :direccion")
    , @NamedQuery(name = "Clientes.findByTelefono", query = "SELECT c FROM Clientes c WHERE c.telefono = :telefono")
    , @NamedQuery(name = "Clientes.findByStatus", query = "SELECT c FROM Clientes c WHERE c.status = :status")
    , @NamedQuery(name = "Clientes.findByImagen", query = "SELECT c FROM Clientes c WHERE c.imagen = :imagen")
    , @NamedQuery(name = "Clientes.findByNivelUsuario", query = "SELECT c FROM Clientes c WHERE c.nivelUsuario = :nivelUsuario")})
public class Clientes implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CLIENTE_PK")
    private BigDecimal idClientePk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CORREO")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "PASS")
    private String pass;
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
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Size(min = 0, max = 50)
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "TELEFONO")
    private BigInteger telefono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private BigInteger status;
    @Column(name = "IMAGEN")
    private String imagen;
    @Column(name = "NIVEL_USUARIO")
    private BigInteger nivelUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idClienteFk")
    private Collection<Citas> citasCollection;

    public Clientes() {
    }

    public Clientes(BigDecimal idClientePk) {
        this.idClientePk = idClientePk;
    }

    public Clientes(BigDecimal idClientePk, String correo, String pass, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion, BigInteger status) {
        this.idClientePk = idClientePk;
        this.correo = correo;
        this.pass = pass;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.direccion = direccion;
        this.status = status;
    }

    public BigDecimal getIdClientePk() {
        return idClientePk;
    }

    public void setIdClientePk(BigDecimal idClientePk) {
        this.idClientePk = idClientePk;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClientePk != null ? idClientePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.idClientePk == null && other.idClientePk != null) || (this.idClientePk != null && !this.idClientePk.equals(other.idClientePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return correo;
//        return "\n\nnombre: " + this.nombre + "\n"
//                + "app: " + this.apellidoPaterno + "\n"
//                + "app: " + this.apellidoMaterno + "\n"
//                + "correo: " + this.correo + "\n"
//                + "pass: " + this.pass + "\n"
//                + "fecha: " + this.fechaNacimiento + "\n"
//                + "dir: " + this.direccion + "\n"
//                + "tel: " + this.telefono + "\n"
//                + "status: " + this.status + "\n"
//                + "id: " + this.idClientePk + "\n\n\n";
    }

}
