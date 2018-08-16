package a_web.b_backing;

import b_controlador.a_gestion.PlaneacionControl;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class PlaneacionBean extends GeneralBean {

    @ManagedProperty("#{planeacionControl}")
    private PlaneacionControl planeacionControl;

    public PlaneacionBean() {
    }
    
    public List<String> tareasAntesPlaneacion(String rolSeleccionado) {
        try{
            return planeacionControl.tareasAntesPlaneacion(rolSeleccionado);
        }catch(Exception fatal){
            this.enviarMensaje(null,"Error al cargar las tareas. " + fatal.getMessage(),"fatal");
            return null;
        }
    }
    
    public void cargarTask() {
        try {
            planeacionControl.cargarTask();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato task. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato task. " + warn.getMessage(),"warn");
        }
    }

    public void crearTarea(String etapa) {
        try {
            planeacionControl.crearTarea(etapa);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al crear la tarea. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al crear la tarea. " + warn.getMessage(),"warn");
        }

    }

    public void validarSemanaPlan(List registrosEtapa, AtributoCompuesto atributo) {
        try {
            this.hacerNatural(atributo);
            planeacionControl.validarSemanaPlan(registrosEtapa,atributo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al validar la semana del plan. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al validar la semana del plan. " + warn.getMessage(),"warn");
        }
    }


    public void guardarTask() {
        try {
            planeacionControl.guardarTask();
            this.enviarMensaje(null, "Formato TASK guardado satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al guardar task. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al guardar task. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public List registrosEtapa(String etapa) {
        try {
            return planeacionControl.registrosEtapa(etapa);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener la lista de registros por etapa. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener la lista de registros por etapa. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public List getAtributos2(List registrosEtapa, String atributo) {
        try {
            return planeacionControl.getAtributos2(registrosEtapa,atributo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener el item del registro. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener el item del registro. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }


    public String getHorasAcumuladas() {
        try {
            return planeacionControl.getHorasAcumuladas();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener las horas acumuladas. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener las horas acumuladas. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public void actualizarDatos(List registrosEtapa, AtributoCompuesto atributo2, AtributoCompuesto atributo3) {
        try {
            hacerPositivo(atributo3);
            planeacionControl.actualizarDatos(registrosEtapa,atributo2,atributo3);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar los datos. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar los datos. " + warn.getMessage(),"warn");
        }
    }

    public String getVPsAcumulados() {
        try {
            return planeacionControl.getVPsAcumulados();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener el valor planeado acumulado. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener el valor planeado acumulado. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public String getVPs() {
        try {
            return planeacionControl.getVPs();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener el valor planeado. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener el valor planeado. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public void eliminarTarea(List registrosFase, AtributoCompuesto atributo) {
        try {
            planeacionControl.eliminarTarea(registrosFase,atributo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al eliminar la tarea. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al eliminar la tarea. " + warn.getMessage(),"warn");
        }
    }

    public double[] getTotalRoles() {
        try {
            return planeacionControl.getTotalRoles();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener el total de roles. " + fatal.getMessage(),"fatal");
            return new double[0];
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al obtener el total de roles. " + warn.getMessage(),"warn");
            return new double[0];
        }
    }

    public boolean tieneRol(String rol) {
        try {
            return planeacionControl.tieneRol(rol);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al identificar si el usuario tiene el rol " + rol + ". " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al identificar si el usuario tiene el rol " + rol + ". " + warn.getMessage(),"warn");
            return false;
        }
    }

    public boolean tieneUnicamenteRol(String rol) {
        try {
            return planeacionControl.tieneUnicamenteRol(rol);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al identificar si el usuario tiene el rol " + rol + ". " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al identificar si el usuario tiene el rol " + rol + ". " + warn.getMessage(),"warn");
            return false;
        }
    }    
    
    
    public List getUnidadesTamanio(){
        try{
            return planeacionControl.getUnidadesTamanio();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al obtener las unidades de tamaño para el formato. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }
    }
    
    
    public FormatoConcreto getTask() {
        return planeacionControl.getTask();
    }

    public void setTask(FormatoConcreto task) {
        planeacionControl.setTask(task);
    }
    
    public List getRegistrosEtapas() {
        return planeacionControl.getRegistrosEtapas();
    }

    public void setRegistrosEtapas(List registrosEtapas) {
        planeacionControl.setRegistrosEtapas(registrosEtapas);
    }

    public List getEtapasTSP() {
        return planeacionControl.getEtapasTSP();
    }

    public void setEtapasTSP(List etapasTSP) {
        planeacionControl.setEtapasTSP(etapasTSP);
    }

    public List getRolesUsuarios() {
        return planeacionControl.getRolesUsuarios();
    }

    public void setRolesUsuarios(List rolesUsuarios) {
        planeacionControl.setRolesUsuarios(rolesUsuarios);
    }

    public PlaneacionControl getPlaneacionControl() {
        return planeacionControl;
    }

    public void setPlaneacionControl(PlaneacionControl planeacionControl) {
        this.planeacionControl = planeacionControl;
    }
    
    

}
