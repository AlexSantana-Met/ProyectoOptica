/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alejandro
 */
@Entity
@Table(name = "CONTACTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contacto.findAll", query = "SELECT c FROM Contacto c")
    , @NamedQuery(name = "Contacto.findByIdContactoPk", query = "SELECT c FROM Contacto c WHERE c.idContactoPk = :idContactoPk")
    , @NamedQuery(name = "Contacto.findByCorreo", query = "SELECT c FROM Contacto c WHERE c.correo = :correo")
    , @NamedQuery(name = "Contacto.findByNombre", query = "SELECT c FROM Contacto c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Contacto.findByMensaje", query = "SELECT c FROM Contacto c WHERE c.mensaje = :mensaje")
    , @NamedQuery(name = "Contacto.findByStatus", query = "SELECT c FROM Contacto c WHERE c.status = :status")})
public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CONTACTO_PK")
    private BigDecimal idContactoPk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CORREO")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "MENSAJE")
    private String mensaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private BigInteger status;

    public Contacto() {
    }

    public Contacto(BigDecimal idContactoPk) {
        this.idContactoPk = idContactoPk;
    }

    public Contacto(BigDecimal idContactoPk, String correo, String nombre, String mensaje, BigInteger status) {
        this.idContactoPk = idContactoPk;
        this.correo = correo;
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.status = status;
    }

    public BigDecimal getIdContactoPk() {
        return idContactoPk;
    }

    public void setIdContactoPk(BigDecimal idContactoPk) {
        this.idContactoPk = idContactoPk;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContactoPk != null ? idContactoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.idContactoPk == null && other.idContactoPk != null) || (this.idContactoPk != null && !this.idContactoPk.equals(other.idContactoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.beans.Contacto[ idContactoPk=" + idContactoPk + " ]";
    }

}
