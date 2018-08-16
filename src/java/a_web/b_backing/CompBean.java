package a_web.b_backing;

import b_controlador.a_gestion.CompControl;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;

@ManagedBean
@SessionScoped
public class CompBean extends GeneralBean {

    @ManagedProperty("#{compControl}")
    CompControl compControl;

    private UIComponent btnGuardarFormatoCcr;
    private UIComponent btnGuardarFormatoPeer;
    private UIComponent btnGuardarFormatoPip;
    private UIComponent btnGuardarFormatoRevCcr;
    private UIComponent btnGuardarFormatoRevPip;

    public CompBean() {
    }

    public void cargarNuevoCcr() {
        try {
            compControl.cargarNuevoCcr();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el nuevo ccr. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el nuevo ccr. " + warn.getMessage(),"warn");
        }
    }

    public void cargarPeer() {
        try {
            compControl.cargarPeer();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar peer. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
             this.enviarMensaje(null,"Error al cargar peer. " + warn.getMessage(),"warn");
        }
    }

    public void cargarNuevoPip() {
        try {
            compControl.cargarNuevoPip();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el nuevo pip. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el nuevo pip. " + warn.getMessage(),"warn");
        }
    }

    public void actualizarTotalTrabajoDificultadPeer(AtributoCompuesto atributo3) {
        try {
            hacerPositivo(atributo3);
            compControl.actualizarTotalTrabajoDificultadPeer(atributo3);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar el total de trabajo dificultad. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
             this.enviarMensaje(null,"Error al actualizar el total de trabajo dificultad. " + warn.getMessage(),"warn");
        }
    }

    public void guardarCcr() {
        try {
            compControl.guardarCcr();
            enviarMensaje(this.btnGuardarFormatoCcr, "Formato CCR guardado satisfactóricamente. Queda en espera de la revisión del gerente de planeación", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoCcr,"Error al insertar el formato ccr. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
             this.enviarMensaje(this.btnGuardarFormatoCcr,"Error al insertar el formato ccr. " + warn.getMessage(),"warn");
        }
    }
    
    public void revisarCcr() {
        try {
            compControl.revisarCcr();
            enviarMensaje(this.btnGuardarFormatoRevCcr, "Formato ccr revisado satisfactoriamente.", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoRevCcr,"Error al revisar el formato ccr. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoRevCcr,"Error al revisar el formato ccr. " + warn.getMessage(),"warn");
        }
    } 

    public void revisarPip() {
        try {
            compControl.revisarPip();
            enviarMensaje(this.btnGuardarFormatoRevPip, "Formato pip revisado satisfactoriamente.", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoRevPip,"Error al revisar el formato pip. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoRevPip,"Error al revisar el formato pip. " + warn.getMessage(),"warn");
        }
    }    
    
    public void actualizarPeer() {
        try {
            compControl.actualizarPeer();
            enviarMensaje(this.btnGuardarFormatoPeer, "Formato PEER guardado satisfactóricamente. Puedes actualizar este formato siempre que no termine el ciclo actual", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoPeer,"Error al guardar el formato peer. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoPeer,"Error al guardar el formato peer. " + warn.getMessage(),"warn");
        }
    }

    public void insertarPip() {
        try {
            compControl.insertarPip();
            enviarMensaje(this.btnGuardarFormatoPip, "Formato PIP guardado satisfactóricamente. Queda en espera de revisión", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoPip,"Error al insertar el formato pip. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoPip,"Error al isertar el formato pip. " + warn.getMessage(),"warn");
        }
    }
    
    public List getCcrsCicloPendientes(){
        try {
            return compControl.getCcrsCicloPendientes();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoRevCcr,"Error al consultar los formatos ccr pendientes del ciclo. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoRevCcr,"Error al consultar los formatos ccr pendientes del ciclo. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }
    
    public void cargarCcrRevision(FormatoConcreto ccrPendiente){
        try {
            compControl.cargarCcrRevision(ccrPendiente);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoRevCcr,"Error al cargar el ccr de revisión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoRevCcr,"Error al cargar el ccr de revisión. " + warn.getMessage(),"warn");
        }
    }
    
    
    public void cargarPipRevision(FormatoConcreto pipRevision){
        try {
            compControl.cargarPipRevision(pipRevision);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoRevPip,"Error al cargar el pip de revisión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoRevPip,"Error al cargar el pip de revisión. " + warn.getMessage(),"warn");
        }
    }
    
    public void cargarPipPersonal(FormatoConcreto pipPersonal){
        try {
            compControl.cargarPipPersonal(pipPersonal);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el pip de personal. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el pip de personal. " + warn.getMessage(),"warn");
        }
    }
    
    public void cargarCcrPersonal(FormatoConcreto ccrPersonal){
        try {
            compControl.cargarCcrPersonal(ccrPersonal);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el pip de personal. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el pip de personal. " + warn.getMessage(),"warn");
        }
    }
    
    public List getPipsSinRevision(){
        try {
            return compControl.getPipsSinRevision();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarFormatoRevPip,"Error al consultar los formatos pip pendientes del ciclo. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarFormatoRevPip,"Error al consultar los formatos pip pendientes del ciclo. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }
    
    public List getPipsPersonales(){
        try {
            return compControl.getPipsPersonales();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar los formatos pip personales del ciclo. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar los formatos pip personales del ciclo. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }
    
    public List getCcrsPersonales(){
        try {
            return compControl.getCcrsPersonales();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar los formatos ccr personales del ciclo. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar los formatos ccr personales del ciclo. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public UIComponent getBtnGuardarFormatoCcr() {
        return btnGuardarFormatoCcr;
    }

    public void setBtnGuardarFormatoCcr(UIComponent btnGuardarFormatoCcr) {
        this.btnGuardarFormatoCcr = btnGuardarFormatoCcr;
    }

    public FormatoConcreto getCcr() {
        return compControl.getCcr();
    }

    public void setCcr(FormatoConcreto ccr) {
        compControl.setCcr(ccr);
    }

    public FormatoConcreto getPeer() {
        return compControl.getPeer();
    }

    public void setPeer(FormatoConcreto peer) {
        compControl.setPeer(peer);
    }

    public FormatoConcreto getPip() {
        return compControl.getPip();
    }

    public void setPip(FormatoConcreto pip) {
        compControl.setPip(pip);
    }

    public CompControl getCompControl() {
        return compControl;
    }

    public void setCompControl(CompControl compControl) {
        this.compControl = compControl;
    }
    
    public FormatoConcreto getCcrRevision() {
        return compControl.getCcrRevision();
    }

    public void setCcrRevision(FormatoConcreto ccrRevision) {
        compControl.setCcrRevision(ccrRevision);
    }
    
    public FormatoConcreto getPipRevision() {
        return compControl.getPipRevision();
    }

    public void setPipRevision(FormatoConcreto pipRevision) {
        compControl.setPipRevision(pipRevision);
    }
    
    public FormatoConcreto getCcrPersonal() {
        return compControl.getCcrPersonal();
    }

    public void setCcrPersonal(FormatoConcreto ccrPersonal) {
        compControl.setCcrPersonal(ccrPersonal);
    }

    public FormatoConcreto getPipPersonal() {
        return compControl.getPipPersonal();
    }

    public void setPipPersonal(FormatoConcreto pipPersonal) {
        compControl.setPipPersonal(pipPersonal);
    }

    public UIComponent getBtnGuardarFormatoPeer() {
        return btnGuardarFormatoPeer;
    }

    public void setBtnGuardarFormatoPeer(UIComponent btnGuardarFormatoPeer) {
        this.btnGuardarFormatoPeer = btnGuardarFormatoPeer;
    }

    public UIComponent getBtnGuardarFormatoPip() {
        return btnGuardarFormatoPip;
    }

    public void setBtnGuardarFormatoPip(UIComponent btnGuardarFormatoPip) {
        this.btnGuardarFormatoPip = btnGuardarFormatoPip;
    }

    public UIComponent getBtnGuardarFormatoRevCcr() {
        return btnGuardarFormatoRevCcr;
    }

    public void setBtnGuardarFormatoRevCcr(UIComponent btnGuardarFormatoRevCcr) {
        this.btnGuardarFormatoRevCcr = btnGuardarFormatoRevCcr;
    }

    public UIComponent getBtnGuardarFormatoRevPip() {
        return btnGuardarFormatoRevPip;
    }

    public void setBtnGuardarFormatoRevPip(UIComponent btnGuardarFormatoRevPip) {
        this.btnGuardarFormatoRevPip = btnGuardarFormatoRevPip;
    }

}