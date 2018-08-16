package a_web.b_backing;

import b_controlador.a_gestion.SemanalControl;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
@ManagedBean
@SessionScoped
public class SemanalBean extends GeneralBean {

    @ManagedProperty("#{semanalControl}")
    SemanalControl semanalControl;

    private UIComponent btnActualizarSchedule;
    private UIComponent btnGenerarWeek;

    public SemanalBean() {
    }

    public void cargarSchedule() {
        try {
            semanalControl.cargarSchedule();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato schedule. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato schedule. " + warn.getMessage(),"warn");
        }
    }
    
    
    public void actualizarSchedule() {
        try {
            String mensaje = semanalControl.actualizarSchedule();
            this.enviarMensaje(this.btnActualizarSchedule,mensaje,"info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar el formato schedule. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar el formato schedule. " + warn.getMessage(),"warn");
        }
    }

    public void cargarWeek() {
        try {
            semanalControl.cargarWeek();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato week. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato week. " + warn.getMessage(),"warn");
        }
    }

    public boolean isExisteWeek() {
        try {
            return semanalControl.isExisteWeek();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al verificar la existencia del formato week. " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al verificar la existencia del formato week. " + warn.getMessage(),"warn");
            return false;
        }
    }

    public void generarWeekSemanaActual() {
        try {
            String semana = semanalControl.generarWeekSemanaActual();
            this.enviarMensaje(this.btnGenerarWeek, "Formato WEEK de esta semana (semana " + semana + " creado satisfactoriamente)", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al generar el formato week de la semana actual. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al generar el formato week de la semana actual. " + warn.getMessage(),"warn");
        }
            
    }

    private void consolidarScheduleSemana(int numSemana, AtributoCompuesto atributo2HorasProyectoSemana, AtributoCompuesto atributo2HorasProyectoCiclo, AtributoCompuesto atributo2VGSemana, AtributoCompuesto atributo2VGCiclo) {
        try {
            semanalControl.consolidarScheduleSemana(numSemana,atributo2HorasProyectoSemana,atributo2HorasProyectoCiclo,atributo2VGSemana,atributo2VGCiclo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consolidar la información semanal en el formato schedule. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consolidar la información semanal en el formato schedule. " + warn.getMessage(),"warn");
        }
    }

    public FormatoConcreto getWeek() {
        return semanalControl.getWeek();
    }

    public void setWeek(FormatoConcreto week) {
        semanalControl.setWeek(week);
    }

    public UIComponent getBtnGenerarWeek() {
        return btnGenerarWeek;
    }

    public void setBtnGenerarWeek(UIComponent btnGenerarWeek) {
        this.btnGenerarWeek = btnGenerarWeek;
    }

    public int getNumSemanaUltimoFormatoWeek() {
        return semanalControl.getNumSemanaUltimoFormatoWeek();
    }

    public void setNumSemanaUltimoFormatoWeek(int numSemanaUltimoFormatoWeek) {
        semanalControl.setNumSemanaUltimoFormatoWeek(numSemanaUltimoFormatoWeek);
    }
    
    public FormatoConcreto getSchedule() {
        return semanalControl.getSchedule();
    }

    public void setSchedule(FormatoConcreto schedule) {
        semanalControl.setSchedule(schedule);
    }

    public List getRegistrosSchedule() {
        return semanalControl.getRegistrosSchedule();
    }

    public void setRegistrosSchedule(List registrosSchedule) {
        semanalControl.setRegistrosSchedule(registrosSchedule);
    }

    public int getDiaReferencia() {
        return semanalControl.getDiaReferencia();
    }

    public void setDiaReferencia(int diaReferencia) {
        semanalControl.setDiaReferencia(diaReferencia);
    }

    public UIComponent getBtnActualizarSchedule() {
        return btnActualizarSchedule;
    }

    public void setBtnActualizarSchedule(UIComponent btnActualizarSchedule) {
        this.btnActualizarSchedule = btnActualizarSchedule;
    }

    public SemanalControl getSemanalControl() {
        return semanalControl;
    }

    public void setSemanalControl(SemanalControl semanalControl) {
        this.semanalControl = semanalControl;
    }
    
    

}
