package com.controllers1;

import com.beans.Citas;
import com.beans.Clientes;
import com.beans.Empleados;
import com.controllers1.util.JsfUtil;
import com.controllers1.util.PaginationHelper;
import com.facades.CitasFacade;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;

@Named("citasController")
@SessionScoped
public class CitasController implements Serializable {

    private Citas current;
    private DataModel items = null;
    private List<String> horasDisponibles;
    private Date dateCita;
    private String hourDate;
    private List<Citas> citasCliente;
    @EJB
    private com.facades.CitasFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public CitasController() {
    }

    public Citas getSelected() {
        if (current == null) {
            current = new Citas();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CitasFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Citas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Citas();
        selectedItemIndex = -1;
        return "Create";
    }

    public void create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        if (session.getAttribute("cliente") == null) {
            try {
                current = new Citas();
                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
            } catch (IOException ex) {
            }
        } else {
            try {
                Clientes cli = (Clientes) session.getAttribute("cliente");
                current.setStatus(new BigInteger("1"));
                current.setIdCitaPk(BigDecimal.valueOf(getFacade().count() + 1));
//                current.setIdCitaPk(BigDecimal.valueOf(getFacade().findAll());
                current.setIdClienteFk(cli);
                current.setFechaCita(dateCita);
                current.setIdEmpleadoFk(getFacade().getEmpleado(BigDecimal.valueOf(1)));
                current.getFechaCita().setHours(Integer.parseInt(hourDate.split(":")[0]));
                current.setHoraCita(current.getFechaCita());
                getFacade().create(current);
                current = null;
                current = new Citas();
                horasDisponibles = new ArrayList<>();
                citasCliente = new ArrayList<>();
                dateCita = null;
                hourDate = "";
                fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agendado la cita correctamente.", null));
                FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("citasController");
                ec.redirect(ec.getRequestContextPath() + "/faces/Citas.xhtml");
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CitasCreated"));
//            return prepareCreate();
            } catch (Exception e) {
                fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error el agendar la cita, consulte con su administrador.", null));
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            return null;
            }
        }
    }

//    public void create() {
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        HttpSession session = (HttpSession) ec.getSession(false);
//        if (session.getAttribute("cliente") == null) {
//            try {
//                current = new Citas();
//                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
//            } catch (IOException ex) {
//            }
//        } else {
//            try {
//                Clientes cli = (Clientes) session.getAttribute("cliente");
//                current.setStatus(new BigInteger("1"));
//                current.setIdCitaPk(BigDecimal.valueOf(getFacade().count() + 1));
//                current.setIdClienteFk(cli);
//                current.setFechaCita(dateCita);
//                current.setIdEmpleadoFk(getFacade().getEmpleado(BigDecimal.valueOf(1)));
//                current.getFechaCita().setHours(Integer.parseInt(hourDate.split(":")[0]));
//                current.getFechaCita().setHours(0);
//                current.setHoraCita(current.getFechaCita());
//                current.getFechaCita().setHours(0);
//                getFacade().create(current);
//                current = null;
//                current = new Citas();
//                dateCita = null;
//                fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agendado la cita correctamente.", null));
//                ec.redirect(ec.getRequestContextPath() + "/faces/Citas.xhtml");
////            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CitasCreated"));
////            return prepareCreate();
//            } catch (Exception e) {
//                fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ha ocurrido un error el agendar la cita, consulte con su administrador.", null));
////            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
////            return null;
//            }
//        }
//    }
    public String prepareEdit() {
        current = (Citas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CitasUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Citas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CitasDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Citas getCitas(java.math.BigDecimal id) {
        return ejbFacade.find(id);
    }

    public void fechaRes() {
        if (dateCita != null) {
            System.out.println(dateCita.toString());
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
//        CitasFacade cf = new CitasFacade();
        if (dateCita != null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
            if (dateCita.compareTo(new java.util.Date()) <= 0) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", null));
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una fecha posterior a la actual.", null));
            } else {
                mostrarDisponibles();
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una fecha por favor.", null));
        }
    }

    public void mostrarDisponibles() {
        List<String> ocupadas = getFacade().getHorasCitas(dateCita);
        horasDisponibles = new ArrayList<>();
        horasDisponibles.add("09:00");
        horasDisponibles.add("10:00");
        horasDisponibles.add("11:00");
        horasDisponibles.add("12:00");
        horasDisponibles.add("15:00");
        horasDisponibles.add("16:00");
        horasDisponibles.add("17:00");
        horasDisponibles.add("18:00");
        for (String ocupada : ocupadas) {
            horasDisponibles.remove(ocupada);
        }
//        System.out.println("XD");
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        mostrarDisponibles();
    }

    public void horasDisponibles() {

    }

    public List<String> getHorasDisponibles() {
        return horasDisponibles;
    }

    public void setHorasDisponibles(List<String> horasDisponibles) {
        this.horasDisponibles = horasDisponibles;
    }

    public Date getDateCita() {
        return dateCita;
    }

    public void setDateCita(Date dateCita) {
        this.dateCita = dateCita;
    }

    public String getHourDate() {
        return hourDate;
    }

    public void setHourDate(String hourDate) {
        this.hourDate = hourDate;
    }

//    public String mostrarCitasClientes() {
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        HttpSession session = (HttpSession) ec.getSession(false);
//        if (session.getAttribute("cliente") == null) {
//            try {
//                current = new Citas();
//                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
//            } catch (IOException ex) {
//            }
//        } else {
//            citasCliente = new ArrayList<>();
//        }
//        return "Nombre";
//    }
    public void mostrarTodasCitas() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
//        citasCliente = new ArrayList<>();
        if (session.getAttribute("cliente") == null) {
            try {
                current = new Citas();
                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
            } catch (IOException ex) {
            }
        } else {
            BigDecimal id = ((Clientes) session.getAttribute("cliente")).getIdClientePk();
            List<Citas> citas = getFacade().findAll();
            citasCliente = new ArrayList<>();
            for (Citas cita : citas) {
                if(cita.getIdClienteFk().getIdClientePk() == id) {
                    citasCliente.add(cita);
                }
            }
        }
    }

    public List<Citas> getCitasCliente() {
        return citasCliente;
    }

    public void setCitasCliente(List<Citas> citasCliente) {
        this.citasCliente = citasCliente;
    }

    public String dateToString(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public String timeToString(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public String statusCita(int status) {
        return status == 1 ? "Pendiente" : "Atendida";
    }

    @FacesConverter(forClass = Citas.class)
    public static class CitasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitasController controller = (CitasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "citasController");
            return controller.getCitas(getKey(value));
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Citas) {
                Citas o = (Citas) object;
                return getStringKey(o.getIdCitaPk());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Citas.class.getName());
            }
        }

    }

}
