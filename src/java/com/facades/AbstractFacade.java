/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.facades;

import com.beans.Citas;
import com.beans.Clientes;
import com.beans.Empleados;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Alejandro
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Clientes getCurrentUser (String correo) {
        List<Clientes> lista = new ArrayList<>();
        TypedQuery<Clientes> query = getEntityManager().createQuery("SELECT c FROM Clientes c WHERE c.correo = '" + correo + "'", Clientes.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            if (lista.get(0).getCorreo().equals(correo)) {
                return lista.get(0);
            } else {
                return null;
            }
        }
    }
    
    public Collection<Citas> getCitasCliente (String correo) {
        List<Clientes> lista = new ArrayList<>();
//        List<Citas> lista2 = new ArrayList<>();
        TypedQuery<Clientes> query = getEntityManager().createQuery("SELECT c FROM Clientes c WHERE c.correo = '" + correo + "'", Clientes.class);
//        TypedQuery<Citas> query2 = getEntityManager().createQuery("SELECT k FROM Citas k WHERE k.idClienteFk = " + 1, Citas.class);
        
//        lista2 = query2.getResultList();
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            if (lista.get(0).getCorreo().equals(correo)) {
                return lista.get(0).getCitasCollection();
            } else {
                return null;
            }
        }
    }
    
    public Empleados getEmpleado (BigDecimal id) {
        List<Empleados> lista = new ArrayList<>();
        TypedQuery<Empleados> query = getEntityManager().createQuery("SELECT e FROM Empleados e WHERE e.idEmpleadoPk = " + id, Empleados.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            if (lista.get(0).getIdEmpleadoPk().equals(id)) {
                return lista.get(0);
            } else {
                return null;
            }
        }
    }
    
    public List<Citas> getCitasCliente(BigDecimal id) {
        List<Citas> lista = new ArrayList<>();
        TypedQuery<Citas> query = getEntityManager().createQuery("SELECT c FROM Citas c WHERE c.idClienteFk = " + id, Citas.class);
        lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            return lista;
        }
    }
    
}
