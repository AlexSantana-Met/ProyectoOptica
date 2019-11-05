/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alejandro
 */
@Entity
@Table(name = "CITAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citas.findAll", query = "SELECT c FROM Citas c")
    , @NamedQuery(name = "Citas.findByIdCitaPk", query = "SELECT c FROM Citas c WHERE c.idCitaPk = :idCitaPk")
    , @NamedQuery(name = "Citas.findByFechaCita", query = "SELECT c FROM Citas c WHERE c.fechaCita = :fechaCita")
    , @NamedQuery(name = "Citas.findByHoraCita", query = "SELECT c FROM Citas c WHERE c.horaCita = :horaCita")
    , @NamedQuery(name = "Citas.findByDescripcion", query = "SELECT c FROM Citas c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Citas.findByRespuestaCita", query = "SELECT c FROM Citas c WHERE c.respuestaCita = :respuestaCita")
    , @NamedQuery(name = "Citas.findByStatus", query = "SELECT c FROM Citas c WHERE c.status = :status")})
public class Citas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CITA_PK")
    private BigDecimal idCitaPk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CITA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORA_CITA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaCita;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 200)
    @Column(name = "RESPUESTA_CITA")
    private String respuestaCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private BigInteger status;
    @JoinColumn(name = "ID_CLIENTE_FK", referencedColumnName = "ID_CLIENTE_PK")
    @ManyToOne(optional = false)
    private Clientes idClienteFk;
    @JoinColumn(name = "ID_EMPLEADO_FK", referencedColumnName = "ID_EMPLEADO_PK")
    @ManyToOne(optional = false)
    private Empleados idEmpleadoFk;
    @JoinColumn(name = "TIPO_CITA", referencedColumnName = "ID_TIPO_PK")
    @ManyToOne(optional = false)
    private TiposCitas tipoCita;

    public Citas() {
    }

    public Citas(BigDecimal idCitaPk) {
        this.idCitaPk = idCitaPk;
    }

    public Citas(BigDecimal idCitaPk, Date fechaCita, Date horaCita, String descripcion, BigInteger status) {
        this.idCitaPk = idCitaPk;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.descripcion = descripcion;
        this.status = status;
    }

    public BigDecimal getIdCitaPk() {
        return idCitaPk;
    }

    public void setIdCitaPk(BigDecimal idCitaPk) {
        this.idCitaPk = idCitaPk;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Date getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Date horaCita) {
        this.horaCita = horaCita;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRespuestaCita() {
        return respuestaCita;
    }

    public void setRespuestaCita(String respuestaCita) {
        this.respuestaCita = respuestaCita;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public Clientes getIdClienteFk() {
        return idClienteFk;
    }

    public void setIdClienteFk(Clientes idClienteFk) {
        this.idClienteFk = idClienteFk;
    }

    public Empleados getIdEmpleadoFk() {
        return idEmpleadoFk;
    }

    public void setIdEmpleadoFk(Empleados idEmpleadoFk) {
        this.idEmpleadoFk = idEmpleadoFk;
    }

    public TiposCitas getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(TiposCitas tipoCita) {
        this.tipoCita = tipoCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCitaPk != null ? idCitaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Citas)) {
            return false;
        }
        Citas other = (Citas) object;
        if ((this.idCitaPk == null && other.idCitaPk != null) || (this.idCitaPk != null && !this.idCitaPk.equals(other.idCitaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.beans.Citas[ idCitaPk=" + idCitaPk + " ]";
    }
    
}
