package com.controllers1;

import com.beans.Citas;
import com.beans.Clientes;
import com.controllers1.util.JsfUtil;
import com.controllers1.util.PaginationHelper;
import com.facades.ClientesFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

@Named("clientesController")
@SessionScoped
public class ClientesController implements Serializable {

    private Clientes current;
    private DataModel items = null;
    private List<Citas> citasCliente;
    private Date birthday;
    private UploadedFile file;
    private String ruta;
    private String nombreArchivo = "";
    @EJB
    private com.facades.ClientesFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ClientesController() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Clientes cl = (Clientes) session.getAttribute("cliente");
        if (cl != null) {
            current.setNombre(cl.getNombre());
            current.setApellidoPaterno(cl.getApellidoPaterno());
            current.setApellidoMaterno(cl.getApellidoMaterno());
            current.setCorreo(cl.getCorreo());
            current.setDireccion(cl.getDireccion());
            current.setFechaNacimiento(cl.getFechaNacimiento());
            current.setTelefono(cl.getTelefono());
            current.setIdClientePk(cl.getIdClientePk());
            current.setNivelUsuario(cl.getNivelUsuario());
        }
    }

    public Clientes getSelected() {
        if (current == null) {
            current = new Clientes();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ClientesFacade getFacade() {
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
        current = (Clientes) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Clientes();
        selectedItemIndex = -1;
        return "Registro";
    }

    public void create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            current.setStatus(new BigInteger("1"));
            current.setNivelUsuario(new BigInteger("1"));
            current.setIdClientePk(BigDecimal.valueOf(getFacade().count() + 1));
            current.setImagen("no-user-image.png");
            getFacade().create(current);
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha registrado correctamente.", null));
            current = new Clientes();
            selectedItemIndex = -1;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en registro, contactese con el administrador.", null));
        }
    }

    public String prepareEdit() {
        current = (Clientes) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public void update() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        if (session.getAttribute("cliente") == null) {
            try {
                current = new Clientes();
                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
            } catch (IOException ex) {

            }
        }
        try {
            if (current.getImagen().isEmpty()) {
                current.setImagen("no-user-image.png");
            }
            getFacade().edit(current);
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Los datos se han guardado correctamente.", null));
            session.setAttribute("cliente", current);
            ruta = "";
            nombreArchivo = "";
            file = null;
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClientesUpdated"));
//            return "View";
        } catch (Exception e) {
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el guardado de los datos.", null));
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            return null;
        }
    }

    public String destroy() {
        current = (Clientes) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClientesDeleted"));
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

    public Clientes getClientes(java.math.BigDecimal id) {
        return ejbFacade.find(id);
    }

    public void login() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext fc = FacesContext.getCurrentInstance();
        if (getFacade().validaLogin(current.getCorreo(), current.getPass())) {
            HttpSession session = (HttpSession) ec.getSession(false);
            session.setAttribute("user", current.getCorreo());
            session.setAttribute("pass", current.getPass());
            Clientes cliente = getFacade().getCurrentUser(current.getCorreo());
            current = cliente;
            session.setAttribute("cliente", cliente);
            String result = muestraPerfil();
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

    public String muestraPerfil() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            Clientes c = (Clientes) session.getAttribute("cliente");
            ec.redirect(ec.getRequestContextPath() + "/faces/Citas.xhtml");
            return "EXITO";
        } catch (IOException ex) {
            return "ERROR";
        }
    }

    public void logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("cliente");
        session.removeAttribute("pass");
        session.removeAttribute("user");
        current = new Clientes();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
        } catch (IOException ex) {

        }
    }

    public String getNombreCompleto() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        if (session.getAttribute("cliente") == null) {
            try {
                current = new Clientes();
                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
            } catch (IOException ex) {

            }
            return "";
        } else {
            return current.getNombre() + " " + current.getApellidoPaterno() + " " + current.getApellidoMaterno();
        }
    }

    public void mostrarCitasCliente() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        if (session.getAttribute("cliente") == null) {
            try {
                current = new Clientes();
                ec.redirect(ec.getRequestContextPath() + "/faces/Login.xhtml");
            } catch (IOException ex) {

            }
        } else {
            Clientes cliente2 = getFacade().find(current.getIdClientePk());
            recreateModel();
            citasCliente = null;
            citasCliente = new ArrayList<>();
            String correo = current.getCorreo();
            current = null;
            current = new Clientes();
            Clientes cliente = getFacade().getCurrentUser(correo);
            current = cliente;
            Collection<Citas> prb = getFacade().getCitasCliente(current.getCorreo());
            for (Citas citas : prb) {
                citasCliente.add(citas);
            }
        }
//        System.out.println(current.getApellidoMaterno());
    }

    public List<Citas> getCitasCliente() {
        return citasCliente;
    }

    public void setCitasCliente(List<Citas> citasCliente) {
        this.citasCliente = citasCliente;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void NuevoDocumento() {
        if (!file.getFileName().isEmpty()) {
            if (SubirArchivo()) {
                current.setImagen(nombreArchivo);
                ruta = "";
            }
        }
        update();
    }

    public Boolean SubirArchivo() {
        try {
            ruta = "web/resources/images";
            if (current.getImagen().equals("no-user-image.png")) {
                nombreArchivo = String.valueOf(getFacade().count() + 1) + current.getApellidoPaterno() + random() + "."
                        + getFile().getContentType().substring(getFile().getContentType().indexOf("/") + 1);
            } else {
                nombreArchivo = current.getImagen();
            }
            copiar_archivo(nombreArchivo, getFile().getInputstream());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String random() {
        String cadena = "";
        while (cadena.length() < 5) {
            cadena = cadena.concat(String.valueOf((int) (Math.random() * 10)));
        }
        return cadena;
    }

    public void copiar_archivo(String nombre_archivo, InputStream in) {
        try {
            ruta = ruta + "/" + nombre_archivo;
            String ruta2 = getPath();
            System.out.println(ruta2.length() + ", " + ruta2.indexOf("\\build\\web\\"));
            ruta2 = ruta2.substring(0, ruta2.indexOf("build\\web\\"));
            File ar = new File(ruta2 + ruta);
            if (ar.exists()) {
                ar.delete();
            }
            ar = null;
            OutputStream out = new FileOutputStream(new File(ruta2 + ruta));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
//            out.write(contenido);
            out.flush();
            out.close();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("Error al subir el archivo"));
        }
    }

    public static String getPath() {
        try {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            return ctx.getRealPath("/");
        } catch (Exception e) {
            System.out.println("Error al obtener la ruta");
            return null;
        }
    }

    public String nombrePaciente(java.math.BigDecimal id) {
        Clientes em = getClientes(id);
        return em.getNombre() + " " + em.getApellidoPaterno() + " " + em.getApellidoMaterno();
    }

    public String statusCliente(java.math.BigInteger status) {
        return status.intValue() == 1 ? "Activo" : "Inactivo";
    }

    public void verificaSesion() {
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

    @FacesConverter(forClass = Clientes.class)
    public static class ClientesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClientesController controller = (ClientesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clientesController");
            return controller.getClientes(getKey(value));
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
            if (object instanceof Clientes) {
                Clientes o = (Clientes) object;
                return getStringKey(o.getIdClientePk());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Clientes.class.getName());
            }
        }

    }

}
