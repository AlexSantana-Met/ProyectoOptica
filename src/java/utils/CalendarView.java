package utils;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 

import org.primefaces.event.SelectEvent;
 
@ManagedBean
public class CalendarView {
         
    private Date fechaCita;
    
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public Date getFechaCita() {
        return fechaCita;
    }
 
    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }
 
  
}