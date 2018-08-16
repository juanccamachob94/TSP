package a_web.b_backing;

import b_controlador.a_gestion.DefectosControl;
import javax.faces.bean.ManagedProperty;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;

@ManagedBean
@SessionScoped
public class DefectosBean extends GeneralBean {

    @ManagedProperty("#{defectosControl}")
    DefectosControl defectosControl;

    private UIComponent btnGuardarRegistroLOGD;

    public DefectosBean() {
    }

    public void cargarLOGDFiltroParte() {
        try {
            defectosControl.cargarLogdFiltroParte();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato logd. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato logd. " + warn.getMessage(),"warn");
        }
    }

    public boolean esAutor(AtributoCompuesto atributoAutor) {
        try {
            return defectosControl.esAutor(atributoAutor);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al validar el privilegio de edición en logd. " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al validar el privilegio de edición en logd. " + warn.getMessage(),"warn");
            return false;
        }
    }

    public void agregarRegistroLogd() {
        try {
            defectosControl.agregarRegistroLogd();
            ejecutarJS("r_cerrarModalNuevoRegistroLOGD");
            this.enviarMensaje(null,"Registro creado satisfactoriamente","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al agregar el registro al formato logd. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al agregar el registro al formato logd. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void actualizarLogd() {
        try {
            defectosControl.actualizarLogd();
            this.enviarMensaje(null, "Formato LOGD actualizado satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar el formato logd. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar el formato logd. " + warn.getMessage(),"warn");
        }
    }

    public void cargarModalNuevoRegistroLogd() {
        try {
            defectosControl.cargarModalNuevoRegistroLogd();
            ejecutarJS("mostrarModalNuevoRegistroLOGD");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los datos del nuevo registro. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los datos del nuevo registro. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public List getNumerosDefecto() {
        try {
            return defectosControl.getNumerosDefecto();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar la lista de números de defecto. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar la lista de números de defecto. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public FormatoConcreto getLogd() {
        return defectosControl.getLogd();
    }

    public void setLogd(FormatoConcreto logd) {
        defectosControl.setLogd(logd);
    }

    public String getParteFiltro() {
        return defectosControl.getParteFiltro();
    }

    public void setParteFiltro(String parteFiltro) {
        defectosControl.setParteFiltro(parteFiltro);
    }

    public List getEtapas() {
        return defectosControl.getEtapas();
    }

    public List getDefectos() {
        try{
            return defectosControl.getDefectos();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener los defectos. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }
    }

    public AtributoCompuesto getRegistroLOGD() {
        return defectosControl.getRegistroLogd();
    }

    public void setRegistroLOGD(AtributoCompuesto registroLOGD) {
        defectosControl.setRegistroLogd(registroLOGD);
    }

    public UIComponent getBtnGuardarRegistroLOGD() {
        return btnGuardarRegistroLOGD;
    }

    public void setBtnGuardarRegistroLOGD(UIComponent btnGuardarRegistroLOGD) {
        this.btnGuardarRegistroLOGD = btnGuardarRegistroLOGD;
    }

    public DefectosControl getDefectosControl() {
        return defectosControl;
    }

    public void setDefectosControl(DefectosControl defectosControl) {
        this.defectosControl = defectosControl;
    }
    
}