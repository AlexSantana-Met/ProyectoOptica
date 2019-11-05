/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.facades;

import com.beans.Citas;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class CitasFacade extends AbstractFacade<Citas> {

    @PersistenceContext(unitName = "ProyectoOpticaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitasFacade() {
        super(Citas.class);
    }

    public List<String> getHorasCitas(Date date) {
        EntityManager em = getEntityManager();
        List<Date> lista2 = new ArrayList<>();
        List<String> result = new ArrayList<>();
        String dateSelected = new SimpleDateFormat("dd/MM/yyyy").format(date);
//        String fechaSelect = date.getDate() < 10 ? "0" + String.valueOf(date.getDate()) : String.valueOf(date.getDate()) + "/"
//                + date.getMonth() < 10 ? "0" + String.valueOf(date.getMonth()) : String.valueOf(date.getMonth());
//        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c WHERE c.fechaCita = '" + dateSelected + "'", Citas.class);
        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c", Citas.class);
//        TypedQuery<Citas> query = em.createQuery("SELECT c FROM Citas c WHERE TO_CHAR(c.fechaCita,'DD/MM/YYYY') = '" + dateSelected + "'", Citas.class);
        List<Citas> lista = query.getResultList();
        if (lista.isEmpty()) {
            return null;
        } else {
            for (Citas cita : lista) {
                if (new SimpleDateFormat("dd/MM/yyyy").format(cita.getFechaCita()).equals(dateSelected)) {
                    lista2.add(cita.getHoraCita());
                }
            }
            for (Date fecha : lista2) {
                String horas = fecha.getHours() < 10 ? "0" + String.valueOf(fecha.getHours()) : String.valueOf(fecha.getHours());
                result.add(horas + ":00");
            }
            return result;
        }
    }

//    public List<Citas> obtenerCitas(int idCliente) {
//        List<Citas> listaRet = new ArrayList<>();
//        List<Citas> listaCitas = 
//        if (listaCitas == null) {
//            return null;
//        } else {
//            for (Citas cita : listaCitas) {
//                String horas = cita.getHora().getHours() < 10 ? "0" + String.valueOf(cita.getHora().getHours()) : String.valueOf(cita.getHora().getHours());
//
//                listaRet.add(new CitaBean(0, cita.getAnio(), cita.getDia(), cita.getMes(),
//                        idCliente, cita.getEmpleadosId().getIdEmpleado(), horas + ":00",
//                        cita.getDescripcion(), cita.getEstado(), cita.getRespuesta()));
//            }
//            return listaRet;
//        }
//    }
}
