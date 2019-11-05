/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alejandro
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean {
    private String correo;
    private String passw;

    public LoginBean(String correo, String passw) {
        this.correo = correo;
        this.passw = passw;
    }

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

//    public void iniciarSesion() {
//        LoginFacade lf = new LoginFacade();
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        FacesContext fc = FacesContext.getCurrentInstance();
//        if (lf.iniciarSesion(correo, passw)) {
//            HttpSession session = (HttpSession) ec.getSession(false);
//            session.setAttribute("user", this.getCorreo());
//            session.setAttribute("pass", this.getPassw());
//            ClienteBean cliente = new ClientesFacade().getCliente(correo);
//            session.setAttribute("cliente", cliente);
//            if (cliente != null) {
//                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inicio de sesión correcto", null));
//                return "perfil.xhtml";
//            } else {
//                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en inicio de sesión.", null));
//                return null;
//            }
//            String result = cliente.muestraPerfil();
//            switch (result) {
//                case "ERROR":
//                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en inicio de sesión.", null));
//                    break;
//                case "EXITO":
//                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inicio de sesión correcto", null));
//                    break;
//            }
//            try {
//                Client
////                ec.redirect(ec.getRequestContextPath() + "/faces/perfil.xhtml");
//            } catch (IOException ex) {
//                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en inicio de sesión.", null));
//            }
//        } else {
//            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña o correo incorrectos.", null));
//        }
//    }
}
