/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.facades;

import com.beans.TiposCitas;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alejandro
 */
@Stateless
public class TiposCitasFacade extends AbstractFacade<TiposCitas> {

    @PersistenceContext(unitName = "ProyectoOpticaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposCitasFacade() {
        super(TiposCitas.class);
    }
    
}
