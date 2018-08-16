package a_web.b_backing;

import b_controlador.a_gestion.RiesgosControl;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class RiesgosBean extends GeneralBean {

    @ManagedProperty("#{riesgosControl}")
    RiesgosControl riesgosControl;
    
    UIComponent btnGuardarRegistroItl;

    public RiesgosBean() {
    }
    public void cargarItlFiltroParte() {
        try {
            riesgosControl.cargarItlFiltroParte();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el itl. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el itl. " + warn.getMessage(),"warn");
        }
    }

    public boolean esAutor(AtributoCompuesto atributoAutor) {
        try {
            return riesgosControl.esAutor(atributoAutor);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al validar el dominio sobre los registros del formato itl. " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al validar el dominio sobre los registros del formato itl. " + warn.getMessage(),"warn");
            return false;
        }
    }

    public void agregarRegistroItl() {
        try {
            riesgosControl.agregarRegistroItl();
            ejecutarJS("r_cerrarModalNuevoRegistroItl");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al agregar el registro itl. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al agregar el registro itl. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void actualizarItl() {
        try {
            riesgosControl.actualizarItl();
            this.enviarMensaje(null, "Formato ITL actualizado satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar el formato itl. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar el formato itl. " + warn.getMessage(),"warn");
        }
    }

    public void cargarModalNuevoRegistroItl() {
        try {
            riesgosControl.cargarModalNuevoRegistroItl();
            ejecutarJS("mostrarModalNuevoRegistroItl");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los datos del nuevo registro itl. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los datos del nuevo registro itl. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public List getNumerosRiesgoProblema() throws ExceptionFatal {
        try {
            return riesgosControl.getNumerosRiesgoProblema();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar los números de riesgo de los registros itl. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar los números de riesgo de los registros itl. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public FormatoConcreto getItl() {
        return riesgosControl.getItl();
    }

    public void setItl(FormatoConcreto itl) {
        riesgosControl.setItl(itl);
    }

    public String getParteFiltro() {
        return riesgosControl.getParteFiltro();
    }

    public void setParteFiltro(String parteFiltro) {
        riesgosControl.setParteFiltro(parteFiltro);
    }

    public AtributoCompuesto getRegistroItl() {
        return riesgosControl.getRegistroItl();
    }

    public void setRegistroItl(AtributoCompuesto registroItl) {
        riesgosControl.setRegistroItl(registroItl);
    }

    public UIComponent getBtnGuardarRegistroItl() {
        return btnGuardarRegistroItl;
    }

    public void setBtnGuardarRegistroItl(UIComponent btnGuardarRegistroItl) {
        this.btnGuardarRegistroItl = btnGuardarRegistroItl;
    }

    public List getRiesgosProblemas() {
        return riesgosControl.getRiesgosProblemas();
    }

    public List getPrioridades() {
        return riesgosControl.getPrioridades();
    }

    public RiesgosControl getRiesgosControl() {
        return riesgosControl;
    }

    public void setRiesgosControl(RiesgosControl riesgosControl) {
        this.riesgosControl = riesgosControl;
    }

    
}
