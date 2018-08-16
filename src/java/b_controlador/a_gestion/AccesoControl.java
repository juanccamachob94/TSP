/**
 * Gestiona el acceso a los servicios de un usuario logueado
 */
package b_controlador.a_gestion;

import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AccesoControl extends Control{

    private boolean accesado;

    public AccesoControl(){
        this.accesado = false;
    }

    /**
     * Si el usuario ha iniciado sesión, permite el acceso a inicio
     * @return 
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public boolean permitirAcceso() throws ExceptionFatal, ExceptionWarn {
        try{
            return this.getAccesado();
        }catch(Exception e){
             throw new ExceptionFatal("AccesoControl no puede permitir el acceso. " + e.getMessage());
        }
    }

    /**
     * Si el usuario no ha iniciado sesión, no permite el acceso a inicio
     * @return 
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public boolean denegarAcceso() throws ExceptionFatal, ExceptionWarn {
        try{
            return !this.getAccesado();
        }catch(Exception e){
             throw new ExceptionFatal("AccesoControl no puede denegar el acceso. " + e.getMessage());
        }
    }
    
    public boolean getAccesado(){
        return accesado;
    }

    public void setAccesado(boolean accesado){
        this.accesado = accesado;
    }
}