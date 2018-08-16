package a_web.b_backing;

import b_controlador.a_gestion.TiempoControl;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;

@ManagedBean
@SessionScoped
public class TiempoBean extends GeneralBean {

    @ManagedProperty("#{tiempoControl}")
    TiempoControl tiempoControl;

    private UIComponent btnGuardarActividad;
    private UIComponent btnInterrupcionPrevista;
    private UIComponent btnInterrupcionNoPrevista;

    public TiempoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            tiempoControl.init();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al iniciar. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al iniciar. " + warn.getMessage(),"warn");
        }
    }

    public void cargarTareasRolActivo() {
        try {
            tiempoControl.cargarTareasRolActivo();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar las tareas del rol activo. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar las tareas del rol activo. " + warn.getMessage(),"warn");
        }
    }

    public List cargarTareasRol(String rol) {
        try {
            return tiempoControl.cargarTareasRol(rol);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar las tareas del rol. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar las tareas del rol. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }
    
    
    public void iniciarActividad() {
        try {
            tiempoControl.iniciarActividad();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al iniciar la actividad. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al iniciar la actividad. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void iniciarActividadNoPlaneada() {
        try {
            tiempoControl.iniciarActividadNoPlaneada();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al iniciar la actividad. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al iniciar la actividad. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }
    
    
    public void pararActividad() throws ExceptionFatal, ExceptionWarn {
        try {
            tiempoControl.pararActividad();
            ejecutarJS("r_mostrarModalPararActividad");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al para la actividad. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            mostrarPagina("inicio");
            this.enviarMensaje(null,"Error al para la actividad. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void guardarActividad() {
        try {
            tiempoControl.guardarActividad();
            this.enviarMensaje(null, "Actividad guardada satisfactoriamente", "info");
            ejecutarJS("r_cerrarModalPararActividad");
            ejecutarJS("cerrarMensaje");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarActividad,"Error al guardar la actividad. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarActividad,"Error al guardar la actividad. " + warn.getMessage(),"warn");
        } finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void guardarActividadNoPlaneada() {
        try {
            tiempoControl.guardarActividadNoPlaneada();
            this.enviarMensaje(null, "Actividad guardada satisfactoriamente", "info");
            ejecutarJS("r_cerrarModalPararActividad");
            ejecutarJS("cerrarMensaje");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarActividad,"Error al guardar la actividad. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarActividad,"Error al guardar la actividad. " + warn.getMessage(),"warn");
        } finally {
            ejecutarJS("cuadrarLabels");
        }
    }
    
    
    
    public void cargarModalInterrupcionPrevista() {
        try {
            tiempoControl.cargarModalInterrupcionPrevista();
            ejecutarJS("r_mostrarModalInterrnPrev");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar la interrupción prevista. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar la interrupción prevista. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void cargarModalInterrupcionNoPrevista() {
        try {
            tiempoControl.cargarModalInterrupcionNoPrevista();
            ejecutarJS("r_mostrarModalInterrupcionNoPrevista");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar la interrupción no prevista. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar la interrupción no prevista. " + warn.getMessage(),"warn");
        } finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void agregarTiempoInterrupcion() {
        try {
            tiempoControl.agregarTiempoInterrupcion();
            ejecutarJS("r_cerrarModalInterrNoPrev");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al agregar el tiempo de interrupción. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al agregar el tiempo de interrupción. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void actualizarTiempoInterrupcion() {
        try {
            tiempoControl.actualizarTiempoInterrupcion();
            ejecutarJS("r_cerrarModalInterrPrev");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar el tiempo de interrupción. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar el tiempo de interrupción. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void cancelarActividad() {
        try {
            tiempoControl.cancelarActividad();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cancelar la actividad" + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            ejecutarJS("r_cerrarModalPararActividad");
            this.enviarMensaje(null, warn.getMessage(),"warn");
        }
    }

    public double horasRealesTarea(Proyecto proyecto, String nombreTarea) {
        try {
            return tiempoControl.horasRealesTarea(proyecto,nombreTarea);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al calcular las horas reales de la tarea. " + fatal.getMessage(),"fatal");
            return 0;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al calcular las horas reales de la tarea. " + warn.getMessage(),"warn");
            return 0;
        }
    }
    
    public List<String> getIntegrantesRol(){
        try{
            return tiempoControl.getIntegrantesRol();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener los integrantes del rol seleccionado. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener los integrantes del rol seleccionado. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public void cargarLogtUsuarioRol() {
        try{
            tiempoControl.cargarLogtUsuarioRol();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato logt. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato logt. " + warn.getMessage(),"warn");
        }
    }
    
    public void cancelarInterrupcionPrevista(){
        try{
            tiempoControl.cancelarInterrupcionPrevista();
            ejecutarJS("r_cerrarModalInterrPrev");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato logt. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato logt. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void cancelarInterrupcionNoPrevista(){
        try{
            tiempoControl.cancelarInterrupcionNoPrevista();
            ejecutarJS("r_cerrarModalInterrNoPrev");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato logt. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato logt. " + warn.getMessage(),"warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    
    public List getTareasRolActivo() {
        return tiempoControl.getTareasRolActivo();
    }

    public void setTareasRolActivo(List tareasRolActivo) {
        tiempoControl.setTareasRolActivo(tareasRolActivo);
    }

    public AtributoCompuesto getRegistroLogt() {
        return tiempoControl.getRegistroLogt();
    }

    public void setRegistroLogt(AtributoCompuesto registroLogt) {
        tiempoControl.setRegistroLogt(registroLogt);
    }

    public UIComponent getBtnGuardarActividad() {
        return btnGuardarActividad;
    }

    public void setBtnGuardarActividad(UIComponent btnGuardarActividad) {
        this.btnGuardarActividad = btnGuardarActividad;
    }

    public Boolean getTareaFinalizada() {
        return tiempoControl.getTareaFinalizada();
    }

    public void setTareaFinalizada(Boolean tareaFinalizada) {
        tiempoControl.setTareaFinalizada(tareaFinalizada);
    }

    public boolean isActividadIniciada() {
        return tiempoControl.isActividadIniciada();
    }

    public void setActividadIniciada(boolean actividadIniciada) {
        tiempoControl.setActividadIniciada(actividadIniciada);
    }

    public String getRolSeleccionado() {
        return tiempoControl.getRolSeleccionado();
    }

    public void setRolSeleccionado(String rolSeleccionado) {
        tiempoControl.setRolSeleccionado(rolSeleccionado);
    }

    public String getTareaSeleccionada() {
        return tiempoControl.getTareaSeleccionada();
    }

    public void setTareaSeleccionada(String tareaSeleccionada) {
        tiempoControl.setTareaSeleccionada(tareaSeleccionada);
    }

    public UIComponent getBtnInterrupcionNoPrevista() {
        return btnInterrupcionNoPrevista;
    }

    public void setBtnInterrupcionNoPrevista(UIComponent btnInterrupcionNoPrevista) {
        this.btnInterrupcionNoPrevista = btnInterrupcionNoPrevista;
    }

    public double getTiempoInterrupcion() {
        return tiempoControl.getTiempoInterrupcion();
    }

    public void setTiempoInterrupcion(double tiempoInterrupcion) {
        tiempoControl.setTiempoInterrupcion(tiempoInterrupcion);
    }

    public String getFechaHoraInterrupcion() {
        return tiempoControl.getFechaHoraInterrupcion();
    }

    public void setFechaHoraInterrupcion(String fechaHoraInterrupcion) {
        tiempoControl.setFechaHoraInterrupcion(fechaHoraInterrupcion);
    }


    public FormatoConcreto getLogt() {
        return tiempoControl.getLogt();
    }

    public void setLogt(FormatoConcreto logt) {
        tiempoControl.setLogt(logt);
    }

    public TiempoControl getTiempoControl() {
        return tiempoControl;
    }

    public void setTiempoControl(TiempoControl tiempoControl) {
        this.tiempoControl = tiempoControl;
    }

    public UIComponent getBtnInterrupcionPrevista() {
        return btnInterrupcionPrevista;
    }

    public void setBtnInterrupcionPrevista(UIComponent btnInterrupcionPrevista) {
        this.btnInterrupcionPrevista = btnInterrupcionPrevista;
    }

    public String getRolFiltro() {
        return tiempoControl.getRolFiltro();
    }

    public void setRolFiltro(String rolFiltro) {
        tiempoControl.setRolFiltro(rolFiltro);
    }

    public String getIntegranteFiltro() {
        return tiempoControl.getIntegranteFiltro();
    }

    public void setIntegranteFiltro(String integranteFiltro) {
        tiempoControl.setIntegranteFiltro(integranteFiltro);
    }
    
}
