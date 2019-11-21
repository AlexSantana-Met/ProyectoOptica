package com.controllers1;

import com.beans.Contacto;
import com.controllers1.util.JsfUtil;
import com.controllers1.util.PaginationHelper;
import com.facades.ContactoFacade;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

@Named("contactoController")
@SessionScoped
public class ContactoController implements Serializable {

    private Contacto current;
    private DataModel items = null;
    @EJB
    private com.facades.ContactoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ContactoController() {
    }

    public Contacto getSelected() {
        if (current == null) {
            current = new Contacto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ContactoFacade getFacade() {
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
        current = (Contacto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Contacto();
        selectedItemIndex = -1;
        return "Create";
    }

    public void create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            current.setStatus(new BigInteger("1"));
            current.setIdContactoPk(BigDecimal.valueOf(getFacade().count() + 1));
            getFacade().create(current);
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Gracias por su opinión.", null));
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ContactoCreated"));
//            return prepareCreate();
            current = new Contacto();
            selectedItemIndex = -1;
        } catch (Exception e) {
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", null));
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            return null;
        }
    }

    public String prepareEdit() {
        current = (Contacto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ContactoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Contacto) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ContactoDeleted"));
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

    public Contacto getContacto(java.math.BigDecimal id) {
        return ejbFacade.find(id);
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
//        return "admin";
    }
    

    @FacesConverter(forClass = Contacto.class)
    public static class ContactoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContactoController controller = (ContactoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contactoController");
            return controller.getContacto(getKey(value));
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
            if (object instanceof Contacto) {
                Contacto o = (Contacto) object;
                return getStringKey(o.getIdContactoPk());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Contacto.class.getName());
            }
        }

    }

}
