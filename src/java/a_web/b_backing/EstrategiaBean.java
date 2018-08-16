package a_web.b_backing;
import b_controlador.a_gestion.EstrategiaControl;
import c_negocio.a_relacional.Parte;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import org.primefaces.model.TreeNode;

@ManagedBean
@SessionScoped
public class EstrategiaBean extends GeneralBean {

    @ManagedProperty("#{estrategiaControl}")
    EstrategiaControl estrategiaControl;

    private UIComponent btnCrearParte;
    private UIComponent btnActualizarParte;

    public EstrategiaBean() {
    }

    public void cargarStrat() {
        try {
            estrategiaControl.cargarStrat();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar el formato strat. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar el formato strat. " + warn.getMessage(),"warn");
        }
    }

    public void cargarNuevaParte(Parte partePadre) {
        try {
            estrategiaControl.cargarNuevaParte(partePadre);
            ejecutarJS("r_mostrarModalNuevaParte");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los datos de la nueva parte. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los datos de la nueva parte. " + warn.getMessage(),"warn");
        }
    }

    public void consultarParte(Parte parte) {
        try {
            estrategiaControl.consultarParte(parte);
            ejecutarJS("r_mostrarModalNuevaParte");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar los datos de la parte. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar los datos de la parte. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void crearRegistroStrat(Parte parte) {
        try {
            estrategiaControl.crearRegistroStrat(parte);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al crear el registro strat. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al crear el registro strat. " + warn.getMessage(),"warn");
        }
    }
    
    public void actualizarParteStrat() throws ExceptionFatal {
        try {
            estrategiaControl.actualizarParteStrat();
            ejecutarJS("r_cerrarModalNuevaParte");
            this.enviarMensaje(this.btnCrearParte, estrategiaControl.getNuevaParte().getNombre() + " actualizado exitosamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al actualizar la parte. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al actualizar la parte. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void crearParteStrat() {
        try {
            estrategiaControl.crearParteStrat();
            ejecutarJS("r_cerrarModalNuevaParte");
            this.enviarMensaje(null, "Parte creada satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al crear la parte strat. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al crear la parte strat. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public List funcionesDeParte(Parte parte) {
        try {
            return estrategiaControl.funcionesDeParte(parte);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar las funciones de la parte. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar las funciones de la parte. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public void eliminarParte(Parte parte) {
        try {
            estrategiaControl.eliminarParte(parte);
            this.enviarMensaje(null, parte.getNombre() + " eliminada satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al eliminar la parte. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al eliminar la parte. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void eliminarFuncion(Parte parte, AtributoCompuesto atributo) {
        try {
            estrategiaControl.eliminarFuncion(parte,atributo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al eliminar la función. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al eliminar la función. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public List funciones(Parte parte, String atributo) {
        try {
            return estrategiaControl.funciones(parte,atributo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar las funciones de parte. " + fatal.getMessage(),"fatal");
            return new ArrayList();
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar las funciones de parte. " + warn.getMessage(),"warn");
            return new ArrayList();
        }
    }

    public boolean tieneHijos(Parte parte) {
        try {
            return estrategiaControl.tieneHijos(parte);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al verificar si la parte tiene hijos. " + fatal.getMessage(),"fatal");
            return false;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al verificar si la parte tiene hijos. " + warn.getMessage(),"warn");
            return false;
        }
    }

    public int tamFuncionesParte(Parte parte) {
        try {
            return estrategiaControl.tamFuncionesParte(parte);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar el tamaño de las funciones de parte. " + fatal.getMessage(),"fatal");
            return 0;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar el tamaño de las funciones de parte. " + warn.getMessage(),"warn");
            return 0;
        }
    }
    
    public void guardarStrat() throws ExceptionFatal {
        try {
            estrategiaControl.guardarStrat();
            this.enviarMensaje(null, "Formato STRAT guardado satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al guardar el formato strat. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al guardar el formato strat. " + warn.getMessage(),"warn");
        }finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public double totalTamanio(String ciclo) {
        try {
            return estrategiaControl.totalTamanio(ciclo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al totalizar el tamaño estimado. " + fatal.getMessage(),"fatal");
            return 0;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al totalizar el tamaño estimado. " + warn.getMessage(),"warn");
            return 0;
        }
    }

    public double totalHoras(String ciclo) {
        try {
            return estrategiaControl.totalHoras(ciclo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al totalizar el tiempo estimado. " + fatal.getMessage(),"fatal");
            return 0;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al totalizar el tiempo estimado. " + warn.getMessage(),"warn");
            return 0;
        }
    }

    public void cargarProductos() {
        try {
            estrategiaControl.cargarProductos();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al cargar los productos. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al cargar los productos. " + warn.getMessage(),"warn");
        }
    }

    public List listaHeader(String nombre) {
        try {
            return estrategiaControl.listaHeader(nombre);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al consultar elementos del encabezado strat. " + fatal.getMessage(),"fatal");
            return null;
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al consultar elementos del encabezado strat. " + warn.getMessage(),"warn");
            return null;
        }
    }

    public Proyecto getProyecto() {
        return estrategiaControl.getProyecto();
    }

    public void setProyecto(Proyecto proyecto) {
        estrategiaControl.setProyecto(proyecto);
    }

    public Parte getNuevaParte() {
        return estrategiaControl.getNuevaParte();
    }

    public void setNuevaParte(Parte nuevaParte) {
        estrategiaControl.setNuevaParte(nuevaParte);
    }

    public TreeNode getNodoProyecto() {
        return estrategiaControl.getNodoProyecto();
    }

    public void setNodoProyecto(TreeNode nodoProyecto) {
        estrategiaControl.setNodoProyecto(nodoProyecto);
    }

    public UIComponent getBtnCrearParte() {
        return btnCrearParte;
    }

    public void setBtnCrearParte(UIComponent btnCrearParte) {
        this.btnCrearParte = btnCrearParte;
    }

    public FormatoConcreto getStrat() {
        return estrategiaControl.getStrat();
    }

    public void setStrat(FormatoConcreto strat) {
        estrategiaControl.setStrat(strat);
    }

    public Map<String, List> getHeadersStrat() {
        return estrategiaControl.getHeadersStrat();
    }

    public void setHeadersStrat(Map<String, List> headersStrat) {
        estrategiaControl.setHeadersStrat(headersStrat);
    }

    public List getNodosPartes() {
        return estrategiaControl.getNodosPartes();
    }

    public void setNodosPartes(List nodosPartes) {
       estrategiaControl.setNodosPartes(nodosPartes);
    }

    public List getRegistrosStrat() {
        return estrategiaControl.getRegistrosStrat();
    }

    public void setRegistrosStrat(List registrosStrat) {
        estrategiaControl.setRegistrosStrat(registrosStrat);
    }

    public int getIdParteConsultada() {
        return estrategiaControl.getIdParteConsultada();
    }

    public void setIdParteConsultada(int idParteConsultada) {
        estrategiaControl.setIdParteConsultada(idParteConsultada);
    }

    public Parte getParteCopia() {
        return estrategiaControl.getParteCopia();
    }

    public void setParteCopia(Parte parteCopia) {
        estrategiaControl.setParteCopia(parteCopia);
    }

    public UIComponent getBtnActualizarParte() {
        return btnActualizarParte;
    }

    public void setBtnActualizarParte(UIComponent btnActualizarParte) {
        this.btnActualizarParte = btnActualizarParte;
    }

    public List getPartesProductos() {
        return estrategiaControl.getPartesProductos();
    }

    public void setPartesProductos(List partesProductos) {
        estrategiaControl.setPartesProductos(partesProductos);
    }    

    public EstrategiaControl getEstrategiaControl() {
        return estrategiaControl;
    }

    public void setEstrategiaControl(EstrategiaControl estrategiaControl) {
        this.estrategiaControl = estrategiaControl;
    }

}
