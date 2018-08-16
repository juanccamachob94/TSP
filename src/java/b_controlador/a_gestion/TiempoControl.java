/**
 * Controlador del tiempo: LOGT
 */
package b_controlador.a_gestion;

import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.Helper;
import java.sql.Timestamp;
import b_controlador.b_fachada.FormatoFachada;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import d_datos.a_dao.ProyectoDAO;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class TiempoControl extends Control {

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private List tareasRolActivo;

    private AtributoCompuesto registroLogt;

    private String rolSeleccionado;
    private String tareaSeleccionada;
    private Boolean tareaFinalizada;
    private boolean actividadIniciada;

    private double tiempoInterrupcion;

    private UIComponent btnInterrupcionPrevista;
    private String fechaHoraInterrupcion;

    private FormatoConcreto logt;
    private String rolFiltro;
    private String integranteFiltro;

    public TiempoControl() {
    }

    /**
     * Inicializa las variables de tarea con la sesión
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void init() throws ExceptionFatal, ExceptionWarn {
        try {
            this.tiempoInterrupcion = 0;
            this.actividadIniciada = false;
        } catch (Exception e) {
            throw new ExceptionFatal("TiempoControl no puede iniciar los valores de gestión de tiempo. " + e.getMessage());
        }
    }

    /**
     * Permite cargar las tareas del rol activo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarTareasNoPlaneadasRolActivo() throws ExceptionFatal {
        try{
            this.tareaSeleccionada = null;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cargar las tareas del rol activo. " + e.getMessage());
        }
    }
    
    
    /**
     * Permite cargar las tareas del rol activo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarTareasRolActivo() throws ExceptionFatal, ExceptionWarn {
        try{
            if (this.rolSeleccionado != null)
                this.tareasRolActivo = cargarTareasRol(this.rolSeleccionado);
            this.tareaSeleccionada = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cargar las tareas del rol activo. " + e.getMessage());
        }
    }

    /**
     * carga las tareas para el rol selecccionado
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List cargarTareasRol(String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            List tareasRol = new ArrayList();
            Proyecto proyecto = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            String identificacion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            FormatoConcreto task = formatoFachada.consultarFormato("task", proyecto.getNombre(), proyecto.getCicloActual(),null,null);
            if (task == null) return null;
            List tareas = task.getAtributos();
            int t = tareas.size();
            AtributoCompuesto atributo;
            for (int i = 0; i < t; i++) {
                atributo = (AtributoCompuesto) tareas.get(i);
                if (Float.parseFloat(atributo.getSubAtributo("Horas del plan").getSubAtributo(rol).getValor()) != 0)
                    if (!atributo.getSubAtributo("Estado de la tarea").getSubAtributo(this.rolSeleccionado).getSubAtributo(identificacion).getValor().equals("Terminada"))
                        tareasRol.add(atributo.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").getValor());
            }
            return tareasRol;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cargar las tareas del rol " + rol + ". " + e.getMessage());
        }
    }    
    
    /**
     * Inicia la actividad/tarea para las fases antes de la fase de planeación
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void iniciarActividadNoPlaneada() throws ExceptionFatal, ExceptionWarn {
        try{
            List valido = validarDatosIniciarActividadNoPlaneada();
            if ((boolean) valido.get(0)) {
                this.registroLogt = fabricaAtributos.crearAtributosLogt();
                this.registroLogt.getSubAtributo("Fecha-hora inicio").setValor((new Timestamp(Calendar.getInstance().getTimeInMillis())).toString());
                this.registroLogt.getSubAtributo("Tarea").setValor(this.tareaSeleccionada);
                this.actividadIniciada = true;
                this.tareaFinalizada = null;
            } else throw new ExceptionWarn((String) valido.get(1));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede iniciar la actividad. " + e.getMessage());
        }
    }
    
    /**
     * Inicia la actividad/tarea
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void iniciarActividad() throws ExceptionFatal, ExceptionWarn {
        try{
            List valido = validarDatosIniciarActividad();
            if ((boolean) valido.get(0)) {
                this.registroLogt = fabricaAtributos.crearAtributosLogt();
                this.registroLogt.getSubAtributo("Fecha-hora inicio").setValor((new Timestamp(Calendar.getInstance().getTimeInMillis())).toString());
                this.registroLogt.getSubAtributo("Tarea").setValor(this.tareaSeleccionada);
                this.actividadIniciada = true;
                this.tareaFinalizada = null;
            } else throw new ExceptionWarn((String) valido.get(1));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede iniciar la actividad. " + e.getMessage());
        }
    }

    /**
     * Valida los datos para iniciar la actividad
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private List validarDatosIniciarActividad() throws ExceptionFatal, ExceptionWarn {
        try{
            List valido = new ArrayList();
            if (this.rolSeleccionado == null) {
                valido.add(false);
                valido.add("Debe seleccionar un rol");
            } else {
                if (this.tareaSeleccionada == null) {
                    valido.add(false);
                    valido.add("Debe seleccionar una tarea");
                } else valido.add(true);
            }
            return valido;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede validar los datos para iniciar la actividad. " + e.getMessage());
        }
    }
    
    
    
    /**
     * Valida los datos para iniciar la actividad
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private List validarDatosIniciarActividadNoPlaneada() throws ExceptionFatal, ExceptionWarn {
        try{
            List valido = new ArrayList();
            if (this.rolSeleccionado == null) {
                if(((ProyectoControl)getControlador("ProyectoControl")).getRolesUSesion().isEmpty()){
                    if (this.tareaSeleccionada == null) {
                        valido.add(false);
                        valido.add("Debe seleccionar una tarea");
                    } else valido.add(true);
                }
                else{
                    valido.add(false);
                    valido.add("Debe seleccionar un rol");
                }
            } else {
                if (this.tareaSeleccionada == null) {
                    valido.add(false);
                    valido.add("Debe seleccionar una tarea");
                } else valido.add(true);
            }
            return valido;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede validar los datos para iniciar la actividad. " + e.getMessage());
        }
    }

    /**
     * Detiene la actividad
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void pararActividad() throws ExceptionFatal, ExceptionWarn {
        try{
            if (this.actividadIniciada) {
                Timestamp tiempoFinal;
                tiempoFinal = new Timestamp(Calendar.getInstance().getTimeInMillis());
                this.registroLogt.getSubAtributo("Fecha-hora finalización").setValor((tiempoFinal).toString());
                this.registroLogt.getSubAtributo("Tiempo dedicado").setValor(Double.toString(Helper.redondear(Helper.miliSegAMinutos(tiempoFinal.getTime() - Timestamp.valueOf(this.registroLogt.getSubAtributo("Fecha-hora inicio").getValor()).getTime()) - Double.parseDouble(this.registroLogt.getSubAtributo("Tiempo interrumpido").getValor()),2)));
            } else throw new ExceptionWarn("La actividad no ha iniciado");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede parar la actividad. " + e.getMessage());
        }
    }

    /**
     * Permite guardar la actividad y actualizar TASK en caso de terminada
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarActividad() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion;
            this.tareaSeleccionada = null;
            String nombreTarea = this.registroLogt.getSubAtributo("Tarea").getValor();
            String identificacion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            if (this.tareaFinalizada == null)
                throw new ExceptionWarn("Debe especificar si ha finalizado o no la tarea");
            else {
                proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
                FormatoConcreto formato = formatoFachada.consultarFormato("logt", "Provisional");
                if(formato == null) formato = formatoFachada.cargarFormato("logt", this.rolSeleccionado);
                else formato.setRol(this.rolSeleccionado);
                formato.agregarAtributo(this.registroLogt);
                formatoFachada.actualizarFormato(formato);
                if (this.tareaFinalizada) {
                    ((PlaneacionControl) getControlador("PlaneacionControl")).actualizarEstadoTarea(proyectoSesion, nombreTarea, this.rolSeleccionado, identificacion);
                    cargarTareasRolActivo();
                }
                this.actividadIniciada = false;
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede guardar la actividad. " + e.getMessage());
        }
    }
    
    /**
     * Permite guardar la actividad y actualizar TASK en caso de terminada
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarActividadNoPlaneada() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            PlaneacionControl pc = ((PlaneacionControl) getControlador("PlaneacionControl"));
            this.tareaSeleccionada = null;
            String nombreTarea = this.registroLogt.getSubAtributo("Tarea").getValor();
            String identificacion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            if (this.tareaFinalizada == null)
                throw new ExceptionWarn("Debe especificar si ha finalizado o no la tarea");
            else {
                    if(this.rolSeleccionado == null) {
                       if (this.tareaFinalizada) throw new ExceptionWarn("No puede finalizar una tarea que no tiene rol asignado");
                    }else pc.agregarEstadoTareaRol(proyectoSesion, nombreTarea, this.rolSeleccionado, identificacion);
                FormatoConcreto formato = formatoFachada.consultarFormato("logt", "Provisional");
                if(this.rolSeleccionado == null) this.rolSeleccionado = "Provisional";
                if(formato == null) formato = formatoFachada.cargarFormato("logt", this.rolSeleccionado);
                else formato.setRol(this.rolSeleccionado);
                formato.agregarAtributo(this.registroLogt);
                formatoFachada.actualizarFormato(formato);
                if (this.tareaFinalizada)
                    pc.actualizarEstadoTarea(proyectoSesion, nombreTarea, this.rolSeleccionado, identificacion);
                this.actividadIniciada = false;
                if(this.rolSeleccionado.equals("Provisional")) this.rolSeleccionado = null;
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede guardar la actividad. " + e.getMessage());
        }
    }
    
    
    
    
    /**
     * Carga el modal de interrupción prevista
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalInterrupcionPrevista() throws ExceptionFatal, ExceptionWarn {
        try{
            if(this.fechaHoraInterrupcion == null){
                if(this.tiempoInterrupcion == 0) this.fechaHoraInterrupcion = (new Timestamp(Calendar.getInstance().getTimeInMillis())).toString();
                else throw new ExceptionWarn("Ya existe una interrupción no prevista en ejecución");
            }else throw new ExceptionWarn("Ya existe una interrupción prevista en ejecución");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cargar los datos de interrupción prevista. " + e.getMessage());
        }
    }

    /**
     * Carga el modal de interrupción no prevista
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalInterrupcionNoPrevista() throws ExceptionFatal, ExceptionWarn {
        try{
            if(this.fechaHoraInterrupcion == null){
                if(this.tiempoInterrupcion == 0) this.tiempoInterrupcion = 1;
                else throw new ExceptionWarn("Ya existe una interrupción no prevista en ejecución");
            }
            else throw new ExceptionWarn("Ya existe una interrupción prevista en ejecución");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cargar los datos de interrupción no prevista. " + e.getMessage());
        }
    }

    /**
     * Agrega el tiempo de interrupción al interrumpido total
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void agregarTiempoInterrupcion() throws ExceptionFatal, ExceptionWarn {
        try{
            if (this.tiempoInterrupcion > 0) {
                double diferencia = Helper.miliSegAMinutos((new Timestamp(Calendar.getInstance().getTimeInMillis())).getTime() - Timestamp.valueOf(this.registroLogt.getSubAtributo("Fecha-hora inicio").getValor()).getTime()) - Double.parseDouble(this.registroLogt.getSubAtributo("Tiempo interrumpido").getValor()) - this.tiempoInterrupcion;
                if (diferencia > 0) {
                    AtributoCompuesto tiempo = this.registroLogt.getSubAtributo("Tiempo interrumpido");
                    tiempo.setValor(Double.toString(Double.parseDouble(tiempo.getValor()) + this.tiempoInterrupcion));
                    this.tiempoInterrupcion = 0;
                } else throw new ExceptionWarn("La interrupción sobrepasa el tiempo transcurrido en " + Math.abs(diferencia) + " minutos");
            } else throw new ExceptionWarn("Número inválido");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede agregar el tiempo de interrupción. " + e.getMessage());
        }
    }

    /**
     * Actualiza el tiempo de interrupción
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarTiempoInterrupcion() throws ExceptionFatal, ExceptionWarn {
        try{
            this.registroLogt.getSubAtributo("Tiempo interrumpido").setValor(Double.toString(Helper.miliSegAMinutos((new Timestamp(Calendar.getInstance().getTimeInMillis())).getTime() - Timestamp.valueOf(this.fechaHoraInterrupcion).getTime())));
            this.fechaHoraInterrupcion = null;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede actualizar el tiempo de interrupción. " + e.getMessage());
        }
    }

    /**
     * Cancela la ejecución de la actividad actual
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cancelarActividad() throws ExceptionFatal, ExceptionWarn {
        try{
            this.tareaFinalizada = null;
            this.actividadIniciada = false;
            throw new ExceptionWarn("Has perdido el registro de esta actividad");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cancelar la actividad. " + e.getMessage());
        }
    }

    /**
     * Indica las horas reales de la tarea total - interrupciones
     * @param proyecto
     * @param nombreTarea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public double horasRealesTarea(Proyecto proyecto, String nombreTarea) throws ExceptionFatal, ExceptionWarn {
        try{
            double tiempoReal = 0;
            List formulariosLogt = formatoFachada.consultarFormatos("logt");
            int t = formulariosLogt.size();
            int t2;
            AtributoCompuesto atributo;
            List atributos;
            for (int i = 0; i < t; i++) {
                atributos = ((FormatoConcreto) formulariosLogt.get(i)).getAtributos();
                t2 = atributos.size();
                for (int j = 0; j < t2; j++) {
                    atributo = (AtributoCompuesto) atributos.get(j);
                    if (atributo.getSubAtributo("Tarea").getValor().equals(nombreTarea))
                        tiempoReal += Double.parseDouble(atributo.getSubAtributo("Tiempo dedicado").getValor())/60;
                }
            }
            return tiempoReal;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede indicar la cantidad de horas reales transcurridas de la tarea. " + e.getMessage());
        }
    }

    /**
     * Indica las horas dedicadas en total por rol a partir de los formatos de los integrantes que tienen dicho rol
     * @param proyecto
     * @param nombreTarea
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public double horasRolTarea(Proyecto proyecto, String nombreTarea, String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            List formatosLogtIntegrantesRol =  formatoFachada.consultarFormatos("logt", proyecto.getNombre(), proyecto.getCicloActual(),rol,null);
            int numFormatos = formatosLogtIntegrantesRol.size();
            if(formatosLogtIntegrantesRol.isEmpty()) return 0;
            double horas = 0;
            int t;
            FormatoConcreto logtRol;
            for(int x = 0; x < numFormatos; x++){
                logtRol = (FormatoConcreto) formatosLogtIntegrantesRol.get(x);
                List atributos = logtRol.getAtributos();
                if(atributos != null){
                    t = atributos.size();
                    AtributoCompuesto atributo;
                    for(int i = 0; i < t; i++){
                        atributo = (AtributoCompuesto) atributos.get(i);
                        if(atributo.getSubAtributo("Tarea").getValor().equals(nombreTarea))
                            horas += Double.parseDouble(atributo.getSubAtributo("Tiempo dedicado").getValor())/60;
                    }
                }
            }
            return horas;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede obtener las horas dedicadas por el rol a la tarea. " + e.getMessage());
        }
    }
    
    public List getIntegrantesRol() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion();
            if(this.rolFiltro == null) return null;
            RlCl rc = new RlCl();
            RlClId id = new RlClId();
            id.setRol(this.rolFiltro);
            id.setProyecto(proyectoSesion.getNombre());
            id.setNCiclo(proyectoSesion.getCicloActual());
            rc.setId(id);
            this.integranteFiltro = null;
            List usuariosRol = ProyectoDAO.consultarUsuariosRol(rc);
            if(usuariosRol.isEmpty()) throw new ExceptionWarn("No existe rol asignado para el rol " + this.rolFiltro);
            return usuariosRol;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al obtener los integrantes del rol " + this.rolFiltro + ". " + e.getMessage());
        }
    }
    
    public void cargarLogtUsuarioRol() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            this.logt = formatoFachada.consultarFormato("logt",proyectoSesion.getNombre(), proyectoSesion.getCicloActual(), this.rolFiltro, this.integranteFiltro);
            if(logt == null) throw new ExceptionWarn("No existe formato logt para " + this.integranteFiltro + " con el rol " + this.rolFiltro);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al cargar el formato logt para el usuario " + this.integranteFiltro + " de rol " + this.rolFiltro + ". " + e.getMessage());
        }    
    }
    
    public void cancelarInterrupcionPrevista() throws ExceptionFatal, ExceptionWarn {
        try{
            this.fechaHoraInterrupcion = null;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cancelar la interrupción prevista. " + e.getMessage());
        }
    }
    
    public void cancelarInterrupcionNoPrevista() throws ExceptionFatal, ExceptionWarn {
        try{
            this.tiempoInterrupcion = 0;
        }catch(Exception e){
            throw new ExceptionFatal("TiempoControl no puede cancelar la interrupción no prevista. " + e.getMessage());
        }
    }

    public List getTareasRolActivo() {
        return tareasRolActivo;
    }

    public void setTareasRolActivo(List tareasRolActivo) {
        this.tareasRolActivo = tareasRolActivo;
    }

    public AtributoCompuesto getRegistroLogt() {
        return registroLogt;
    }

    public void setRegistroLogt(AtributoCompuesto registroLogt) {
        this.registroLogt = registroLogt;
    }

    public Boolean getTareaFinalizada() {
        return tareaFinalizada;
    }

    public void setTareaFinalizada(Boolean tareaFinalizada) {
        this.tareaFinalizada = tareaFinalizada;
    }

    public boolean isActividadIniciada() {
        return actividadIniciada;
    }

    public void setActividadIniciada(boolean actividadIniciada) {
        this.actividadIniciada = actividadIniciada;
    }

    public String getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(String rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public String getTareaSeleccionada() {
        return tareaSeleccionada;
    }

    public void setTareaSeleccionada(String tareaSeleccionada) {
        this.tareaSeleccionada = tareaSeleccionada;
    }

    public double getTiempoInterrupcion() {
        return tiempoInterrupcion;
    }

    public void setTiempoInterrupcion(double tiempoInterrupcion) {
        this.tiempoInterrupcion = tiempoInterrupcion;
    }

    public UIComponent getBtnInterrupcionPrevista() {
        return btnInterrupcionPrevista;
    }

    public void setBtnInterrupcionPrevista(UIComponent btnInterrupcionPrevista) {
        this.btnInterrupcionPrevista = btnInterrupcionPrevista;
    }

    public String getFechaHoraInterrupcion() {
        return fechaHoraInterrupcion;
    }

    public void setFechaHoraInterrupcion(String fechaHoraInterrupcion) {
        this.fechaHoraInterrupcion = fechaHoraInterrupcion;
    }

    public FormatoConcreto getLogt() {
        return logt;
    }

    public void setLogt(FormatoConcreto logt) {
        this.logt = logt;
    }

    public FormatoFachada getFormatoFachada() {
        return formatoFachada;
    }

    public void setFormatoFachada(FormatoFachada formatoFachada) {
        this.formatoFachada = formatoFachada;
    }

    public FabricaAtributos getFabricaAtributos() {
        return fabricaAtributos;
    }

    public void setFabricaAtributos(FabricaAtributos fabricaAtributos) {
        this.fabricaAtributos = fabricaAtributos;
    }

    public String getRolFiltro() {
        return rolFiltro;
    }

    public void setRolFiltro(String rolFiltro) {
        this.rolFiltro = rolFiltro;
    }

    public String getIntegranteFiltro() {
        return integranteFiltro;
    }

    public void setIntegranteFiltro(String integranteFiltro) {
        this.integranteFiltro = integranteFiltro;
    }
}