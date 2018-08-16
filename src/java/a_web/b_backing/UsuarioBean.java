/**
 * Bean para la gestión de datos del usuario.
 */
package a_web.b_backing;

import b_controlador.a_gestion.UsuarioControl;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import c_negocio.a_relacional.Usuario;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class UsuarioBean extends GeneralBean {

    @ManagedProperty("#{usuarioControl}")
    UsuarioControl usuarioControlNuevo;

    private UIComponent btnIniciarSesion;
    private UIComponent btnRegistrar;
    private UIComponent btnModalRegistrar;


    public UsuarioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            usuarioControlNuevo.init();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al iniciar datos de usuario. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al iniciar datos de usuario. " + warn.getMessage(),"warn");
        }
    }

    public void cargarUsuarioSesion() throws ExceptionFatal{
        try {
            usuarioControlNuevo.cargarUsuarioSesion();
        }catch(ExceptionFatal fatal){
            throw new ExceptionFatal("Error al cargar el usuario de la sesión. " + fatal.getMessage());
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el usuario de la sesión. " + warn.getMessage(),"warn");
        }
    }

    public void cargarModalRegistrar() {
        try {
            usuarioControlNuevo.cargarModalRegistrar();
            ejecutarJS("r_mostrarModalRegistrar");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el registro. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el registro. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void registrar() {
        try {
            usuarioControlNuevo.registrar();
            ejecutarJS("r_registrar");
            iniciarSesion();
        }catch(ExceptionFatal fatal){
            usuarioControlNuevo.getUsuarioSesion().setClave("");
            ejecutarJS("r_limpiarConfirmaClave");
            this.enviarMensaje(this.btnRegistrar,"Error al registrar al usuario. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            usuarioControlNuevo.getUsuarioSesion().setClave("");
            ejecutarJS("r_limpiarConfirmaClave");
            this.enviarMensaje(this.btnRegistrar,"Error al registrar al usuario. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void iniciarSesion() throws ExceptionFatal, ExceptionWarn {
        try {
            usuarioControlNuevo.iniciarSesion();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnIniciarSesion,"Error al iniciar sesión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnIniciarSesion,"Error al iniciar sesión. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void cerrarSesion() {
        try {
            usuarioControlNuevo.cerrarSesion();
            mostrarPagina("inicio");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cerrar sesión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cerrar sesión. " + warn.getMessage(),"warn");
        }
    }

    public void cargarValidacionClave(){
        try {
            usuarioControlNuevo.cargarValidacionClave();
            ejecutarJS("mostrarModalCambioClave");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los datos de validación de clave. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los datos de validación de clave. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void validarClaveUsuarioSesion(){
        try {
            usuarioControlNuevo.validarClaveUsuarioSesion();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al validar la clave. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al validar la clave. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void cambiarClaveUsuarioSesion(){
        try {
            usuarioControlNuevo.cambiarClaveUsuarioSesion();
            ejecutarJS("r_cerrarModalCambioClave");
            this.enviarMensaje(null,"Clave cambiada exitosamente","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cambiar la clave. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cambiar la clave. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void cargarDatosUsuarioActualizado(){
        try {
            usuarioControlNuevo.cargarDatosUsuarioActualizado();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los datos del usuario. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los datos del usuario. " + warn.getMessage(),"warn");
        }
    }    
    public void actualizarDatos(){
        try {
            usuarioControlNuevo.actualizarDatos();
            this.enviarMensaje(null,"Datos personales actualizados satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar los datos del usuario. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar los datos del usuario. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public Usuario getUsuarioSesion() {
        return usuarioControlNuevo.getUsuarioSesion();
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        usuarioControlNuevo.setUsuarioSesion(usuarioSesion);
    }

    public String getConfirmacionClave() {
        return usuarioControlNuevo.getConfirmacionClave();
    }

    public void setConfirmacionClave(String confirmacionClave) {
        usuarioControlNuevo.setConfirmacionClave(confirmacionClave);
    }

    public UIComponent getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public void setBtnIniciarSesion(UIComponent btnIniciarSesion) {
        this.btnIniciarSesion = btnIniciarSesion;
    }

    public UIComponent getBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(UIComponent btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    public UIComponent getBtnModalRegistrar() {
        return btnModalRegistrar;
    }

    public void setBtnModalRegistrar(UIComponent btnModalRegistrar) {
        this.btnModalRegistrar = btnModalRegistrar;
    }

    public UsuarioControl getUsuarioControlNuevo() {
        return usuarioControlNuevo;
    }

    public void setUsuarioControlNuevo(UsuarioControl usuarioControlNuevo) {
        this.usuarioControlNuevo = usuarioControlNuevo;
    }
    
    public String getClaveValidacion() {
        return usuarioControlNuevo.getClaveValidacion();
    }

    public void setClaveValidacion(String claveValidacion) {
        usuarioControlNuevo.setClaveValidacion(claveValidacion);
    }
    
    public Usuario getUsuarioActualizado() {
        return usuarioControlNuevo.getUsuarioActualizado();
    }

    public void setUsuarioActualizado(Usuario usuarioActualizado) {
        usuarioControlNuevo.setUsuarioActualizado(usuarioActualizado);
    }
}