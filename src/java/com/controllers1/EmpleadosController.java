package com.controllers1;

import com.beans.Clientes;
import com.beans.Empleados;
import com.controllers1.util.JsfUtil;
import com.controllers1.util.PaginationHelper;
import com.facades.EmpleadosFacade;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
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

@Named("empleadosController")
@SessionScoped
public class EmpleadosController implements Serializable {

    private Empleados current;
    private DataModel items = null;
    private String pass;
    @EJB
    private com.facades.EmpleadosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public EmpleadosController() {
    }

    public Empleados getSelected() {
        if (current == null) {
            current = new Empleados();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EmpleadosFacade getFacade() {
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
        current = (Empleados) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Empleados();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EmpleadosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Empleados) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EmpleadosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Empleados) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EmpleadosDeleted"));
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

    public Empleados getEmpleados(java.math.BigDecimal id) {
        return ejbFacade.find(id);
    }

    public String nombreEmpleado(java.math.BigDecimal id) {
        Empleados em = getEmpleados(id);
        return em.getNombre() + " " + em.getApellidoPaterno() + " " + em.getApellidoMaterno();
    }

    public String statusEmpleado(java.math.BigInteger status) {
        return status.intValue() == 1 ? "Activo" : "Inactivo";
    }

    public SelectItem[] getEstadoEmpleado() {
        SelectItem[] tipos = {
            new SelectItem(new BigDecimal(1), "Activo"),
            new SelectItem(new BigDecimal(0), "Inactivo")
        };
        return tipos;
    }

    public void login() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext fc = FacesContext.getCurrentInstance();
        if (getFacade().validaLogin(current.getCorreo(), pass)) {
            HttpSession session = (HttpSession) ec.getSession(false);
            Empleados empleado = new Empleados();
            empleado.setCorreo(current.getCorreo());
            current = new Empleados();
            session.setAttribute("admin", empleado);
            String result = muestraAdminPanel();
            switch (result) {
                case "ERROR":
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en inicio de sesión.", null));
                    break;
                case "EXITO":
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inicio de sesión correcto", null));
                    break;
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña o correo incorrectos.", null));
        }
    }

    public String muestraAdminPanel() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            ec.redirect(ec.getRequestContextPath() + "/faces/pages/citas/List.xhtml");
            return "EXITO";
        } catch (IOException ex) {
            return "ERROR";
        }
    }

    public void logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("admin");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath() + "/faces/index-admin.xhtml");
        } catch (IOException ex) {

        }
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void verificaSesion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        if (session.getAttribute("admin") == null) {
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/index-admin.xhtml");
            } catch (IOException ex) {

            }
        } else {
            try {
                if (session.getAttribute("cliente") != null) {
                    ec.redirect(ec.getRequestContextPath() + "/faces/index-admin.xhtml");
                }
            } catch (IOException ex) {

            }
        }
        
    }

    @FacesConverter(forClass = Empleados.class)
    public static class EmpleadosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmpleadosController controller = (EmpleadosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "empleadosController");
            return controller.getEmpleados(getKey(value));
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
            if (object instanceof Empleados) {
                Empleados o = (Empleados) object;
                return getStringKey(o.getIdEmpleadoPk());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Empleados.class.getName());
            }
        }

    }

}
