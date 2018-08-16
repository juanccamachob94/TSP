package a_web.b_backing;

import b_controlador.a_gestion.ProyectoControl;
import c_negocio.a_relacional.Usuario;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.Ciclo;
import c_negocio.a_relacional.Fase;
import c_negocio.a_relacional.RlCl;
import c_negocio.b_no_relacional.atributo.Atributo;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class ProyectoBean extends GeneralBean {

    @ManagedProperty("#{proyectoControl}")
    private ProyectoControl proyectoControl;

    private UIComponent btnSeleccionarProyectoSesion;
    private UIComponent btnPasarSiguienteCicloFase;

    public ProyectoBean() {
    }

    public void cargarNuevoProyecto() throws ExceptionFatal {
        try{
            proyectoControl.cargarNuevoProyecto();
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null,"Error al cargar el nuevo proyecto. " + warn.getMessage(),"warn");
        }
    }    
    
    public void actualizarNumeroCiclos() {
        try {
            proyectoControl.actualizarNumeroCiclos();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null,"Error al actualizar el número de ciclos. " + fatal.getMessage(),"fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null,"\"Error al actualizar el número de ciclos. " + warn.getMessage(),"warn");
        }
    }
    
    public void actualizarFechaEstimadaFinNuevoProyecto() {
        try {
            proyectoControl.actualizarFechaEstimadaFinNuevoProyecto();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null,"Error al validar la fecha estimada para la finalización del proyecto. " + fatal.getMessage(),"fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null,"Error al validar la fecha estimada para la finalización del proyecto. " + warn.getMessage(),"warn");
        }
    }
    
    public String valorDeCriterio(String criterio) {
        try {
            return proyectoControl.valorDeCriterio(criterio);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener el valir del criterio. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener el valir del criterio. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public void cargarProyectoSesion() {
        try {
            proyectoControl.cargarProyectoSesion();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el proyecto de sesión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el proyecto de sesión. " + warn.getMessage(),"warn");
        }
    }
    
    public void consultarInstructor() {
        try {
            proyectoControl.consultarInstructor();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar al instructor. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar al instructor. " + warn.getMessage(),"warn");
        }
    }
    

    public void cargarRolesSesion() {
        try {
            proyectoControl.cargarRolesSesion();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los roles de sesión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los roles de sesión. " + warn.getMessage(),"warn");
        }
    }
    
    public boolean isRolesAasignados(){
        try {
            return proyectoControl.isRolesAasignados();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al identificar si los roles han sido asignados. " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al identificar si los roles han sido asignados. " + warn.getMessage(),"warn");
            return false;
        }
    }
    

    public void crearProyecto() {
        try {
            proyectoControl.crearProyecto();
            this.mostrarPagina("asignacion_roles");
            this.enviarMensaje(null, "Proyecto creado satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al crear el proyecto. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al crear el proyecto. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void cargarProyectosUsuarioSesion() {
        try {
            proyectoControl.cargarProyectosUsuarioSesion();
            ejecutarJS("r_mostrarModalProyectosUsuarioSesion");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar proyectos del usuario en sesión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar proyectos del usuario en sesión. " + warn.getMessage(),"warn");
        }
    }

    public void seleccionarProyectoSesion() {
        try {
            proyectoControl.cargarRolesSesion();
            mostrarPagina("inicio");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al seleccionar el proyecto de sesión. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al seleccionar el proyecto de sesión. " + warn.getMessage(),"warn");
        }
    }

    
    public String getInformacionProyectoSesion() {
        try {
            return proyectoControl.getInformacionProyectoSesion();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener información del prouyecto en sesión. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener información del prouyecto en sesión. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public String getColorFase() {
        try {
            return proyectoControl.getColorFase();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener el color de la fase actual. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener el color de la fase actual. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public void denegarAccesoRol(String rol){
        try{
            if(!proyectoControl.poseeRolCicloActual(rol)) mostrarPagina("inicio");
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal al denegar el acceso. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal al denegar el acceso. " + warn.getMessage(),"warn");
        }
    }
    
    public List getOpciones(String nomCriterio) {
        try{
            return proyectoControl.getOpciones(nomCriterio);
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal al denegar el acceso. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal al denegar el acceso. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }
    
    public boolean poseeRolCicloActual(String rol) {
        try{
            return proyectoControl.poseeRolCicloActual(rol);
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal al verificar los roles asignados para el usuario. " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal al verificar los roles asignados para el usuario. " + warn.getMessage(),"warn");
            return false;
        }
    }
    
    public void cargarModalPasarSiguienteFaseOCiclo(){
        try{
            proyectoControl.cargarModalPasarSiguienteFaseOCiclo();
            ejecutarJS("mostrarModalSigFase");
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal cargar los criterios de salida. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal cargar los criterios de salida. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void irSiguienteCicloOFase(){
        try{
            String resultado = proyectoControl.irSiguienteCicloOFase();
            mostrarPagina("inicio");
            this.enviarMensaje(null, resultado, "info");
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error fatal al ir al siguiente ciclo/fase. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error fatal al ir al siguiente ciclo/fase. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void validarFechaEspecificaInicioProyecto(){
        try{
            proyectoControl.validarFechaEspecificaInicioProyecto();
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error al validar la fecha de inicio del proyecto. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error al validar la fecha de inicio del proyecto. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    

    public Usuario getDatosInstructor(){
        try{
            return proyectoControl.datosInstructor();
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error al validar la fecha de inicio del proyecto. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error al validar la fecha de inicio del proyecto. " + warn.getMessage(),"warn");
            return null;
        }
    }
    
    public void consultarProyectosUsuarioSesion(){
        try{
            proyectoControl.consultarProyectosUsuarioSesion();
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error al consultar los proyectos del usuario. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error al consultar los proyectos del usuario. " + warn.getMessage(),"warn");
        }
    }
    
    public String mensajeTooltipCriterio(String nombreCriterio){
        try{
            return proyectoControl.mensajeTooltipCriterio(nombreCriterio);
        }catch(ExceptionWarn warn){
            enviarMensaje(null,"Error al consultar los detalles de los criterios. " + warn.getMessage(),"warn");
            return null;
        }catch(ExceptionFatal fatal){
            enviarMensaje(null,"Error al consultar los detalles de los criterios. " + fatal.getMessage(),"fatal");
            return null;
        }
    }
    
    public Proyecto getProyectoSesion() {
        return proyectoControl.getProyectoSesion();
    }

    public void setProyectoSesion(Proyecto proyectoSesion) {
        proyectoControl.setProyectoSesion(proyectoSesion);
    }

    public List getProyectosUsuarioSesion() {
        return proyectoControl.getProyectosUsuarioSesion();
    }

    public void setProyectosUsuarioSesion(List proyectosUsuarioSesion) {
        proyectoControl.setProyectosUsuarioSesion(proyectosUsuarioSesion);
    }

    public UIComponent getBtnSeleccionarProyectoSesion() {
        return btnSeleccionarProyectoSesion;
    }

    public void setBtnSeleccionarProyectoSesion(UIComponent btnSeleccionarProyectoSesion) {
        this.btnSeleccionarProyectoSesion = btnSeleccionarProyectoSesion;
    }

    public List getRolesUSesion() {
        return proyectoControl.getRolesUSesion();
    }

    public void setRolesUSesion(List rolesUSesion) {
        proyectoControl.setRolesUSesion(rolesUSesion);
    }

    public List getFasesTSP() {
        return proyectoControl.getFasesTSP();
    }

    public List getUsuariosRolesCicloActual() {
        return proyectoControl.getUsuariosRolesCicloActual();
    }

    public Ciclo getCicloSesion() {
        return proyectoControl.getCicloSesion();
    }

    public void setCicloSesion(Ciclo cicloSesion) {
        proyectoControl.setCicloSesion(cicloSesion);
    }

    public Fase getFaseSesion() {
        return proyectoControl.getFaseSesion();
    }

    public void setFaseSesion(Fase faseSesion) {
        proyectoControl.setFaseSesion(faseSesion);
    }

    public List getCriteriosProyectoSesion() {
        return proyectoControl.getCriteriosProyectoSesion();
    }

    public void setCriteriosProyectoSesion(List criteriosProyectoSesion) {
        proyectoControl.setCriteriosProyectoSesion(criteriosProyectoSesion);
    }

    public Proyecto getNuevoProyecto() {
        return proyectoControl.getNuevoProyecto();
    }

    public void setNuevoProyecto(Proyecto nuevoProyecto) {
        proyectoControl.setNuevoProyecto(nuevoProyecto);
    }


    public List getCriteriosNuevoProyecto() {
        return proyectoControl.getCriteriosNuevoProyecto();
    }

    public void setCriteriosNuevoProyecto(List criteriosNuevoProyecto) {
        proyectoControl.setCriteriosNuevoProyecto(criteriosNuevoProyecto);
    }

    public ProyectoControl getProyectoControl() {
        return proyectoControl;
    }

    public void setProyectoControl(ProyectoControl proyectoControl) {
        this.proyectoControl = proyectoControl;
    }
    
    public UploadedFile getImagenSubidaNuevoProyecto() {
        return proyectoControl.getImagenSubidaNuevoProyecto();
    }

    public void setImagenSubidaNuevoProyecto(UploadedFile imagenSubidaNuevoProyecto) {
        proyectoControl.setImagenSubidaNuevoProyecto(imagenSubidaNuevoProyecto);
    }
    
    public RlCl getRlClInstructorNuevoProyecto() {
        return proyectoControl.getRlClInstructorNuevoProyecto();
    }

    public void setRlClInstructorNuevoProyecto(RlCl rlClInstructorNuevoProyecto) {
        proyectoControl.setRlClInstructorNuevoProyecto(rlClInstructorNuevoProyecto);
    }
    
    
    public List getOpcionesCriteriosNuevoProyecto() {
        return proyectoControl.getOpcionesCriteriosNuevoProyecto();
    }

    public void setOpcionesCriteriosNuevoProyecto(List opcionesCriteriosNuevoProyecto) {
        proyectoControl.setOpcionesCriteriosNuevoProyecto(opcionesCriteriosNuevoProyecto);
    }

    public Usuario getInstructor() {
        return proyectoControl.getInstructor();
    }

    public void setInstructor(Usuario instructor) {
        proyectoControl.setInstructor(instructor);
    }
    
    public List<Boolean> getValidacionCriteriosSalida() {
        return proyectoControl.getValidacionCriteriosSalida();
    }

    public void setValidacionCriteriosSalida(List<Boolean> validacionCriteriosSalida) {
        proyectoControl.setValidacionCriteriosSalida(validacionCriteriosSalida);
    }

    public List<Atributo> getCriteriosSalida() {
        return proyectoControl.getCriteriosSalida();
    }

    public void setCriteriosSalida(List<Atributo> criteriosSalida) {
        proyectoControl.setCriteriosSalida(criteriosSalida);
    }

    public UIComponent getBtnPasarSiguienteCicloFase() {
        return btnPasarSiguienteCicloFase;
    }

    public void setBtnPasarSiguienteCicloFase(UIComponent btnPasarSiguienteCicloFase) {
        this.btnPasarSiguienteCicloFase = btnPasarSiguienteCicloFase;
    }
    
    public boolean isFechaInicioEspecifica() {
        return proyectoControl.isFechaInicioEspecifica();
    }

    public void setFechaInicioEspecifica(boolean fechaInicioEspecifica) {
        proyectoControl.setFechaInicioEspecifica(fechaInicioEspecifica);
    }

    public Date getFechaEspecificaInicioProyecto() {
        return proyectoControl.getFechaEspecificaInicioProyecto();
    }

    public void setFechaEspecificaInicioProyecto(Date fechaEspecificaInicioProyecto) {
        proyectoControl.setFechaEspecificaInicioProyecto(fechaEspecificaInicioProyecto);
    }
    
    public List<Boolean> getSeleccionProyectosUsuarioSesion() {
        return proyectoControl.getSeleccionProyectosUsuarioSesion();
    }

    public void setSeleccionProyectosUsuarioSesion(List<Boolean> seleccionProyectosUsuarioSesion) {
        proyectoControl.setSeleccionProyectosUsuarioSesion(seleccionProyectosUsuarioSesion);
    }
    
    public String getUnidadTamanio(){
        return proyectoControl.getUnidadTamanio();
    }
    
}