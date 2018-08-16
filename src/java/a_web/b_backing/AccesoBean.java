/**
 * Bean para la gestión de acceso al sistema
 */
package a_web.b_backing;

import b_controlador.a_gestion.AccesoControl;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AccesoBean extends GeneralBean{
    
    @ManagedProperty("#{accesoControl}")
    AccesoControl accesoControl;

    public AccesoBean() {
    }
    
    /**
     * Permite el acceso a los servicios de proyecto si el usuario está logueado
     */
    public void permitirAcceso(){
        try{
            if(accesoControl.permitirAcceso()) mostrarPagina("inicio");
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal al permitir el acceso. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal al permitir el acceso. " + warn.getMessage(),"warn");
        }
    }

    /**
     * Deniega el acceso a los servicios de proyecto si el usuario no está logueado
     */
    public void denegarAcceso(){
        try{
            if(accesoControl.denegarAcceso()) mostrarPagina("usuario");
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal al denegar el acceso. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal al denegar el acceso. " + warn.getMessage(),"warn");
        }
    }
    
    public boolean getAccesado(){
        return accesoControl.getAccesado();
    }
    
    public void setAccesado(boolean accesado){
        accesoControl.setAccesado(accesado);
    }

    public AccesoControl getAccesoControl() {
        return accesoControl;
    }

    public void setAccesoControl(AccesoControl accesoControl) {
        this.accesoControl = accesoControl;
    }
}