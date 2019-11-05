/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.facades;

import com.beans.Clientes;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Alejandro
 */
@Stateless
public class ClientesFacade extends AbstractFacade<Clientes> {

    @PersistenceContext(unitName = "ProyectoOpticaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesFacade() {
        super(Clientes.class);
    }

    public boolean validaLogin(String user, String pass) {
        List<Clientes> lis = new ArrayList<>();
        TypedQuery<Clientes> query = em.createQuery("SELECT c FROM Clientes c where c.correo = '" + user + "'", Clientes.class);
        lis = query.getResultList();
        if (!lis.isEmpty()) {
            Clientes aux = lis.get(0);
            return aux.getPass().equals(pass);
        } else {
            return false;
        }
    }

}
