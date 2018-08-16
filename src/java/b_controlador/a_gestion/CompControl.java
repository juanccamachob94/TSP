/***
 * Controlador para los formatos PEER, CCR y PIP
 */
package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class CompControl extends Control {

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private FormatoConcreto peer;
    private FormatoConcreto ccr;
    private FormatoConcreto pip;
    
    private FormatoConcreto ccrRevision;
    private FormatoConcreto pipRevision;
    private FormatoConcreto ccrPersonal;
    private FormatoConcreto pipPersonal;

    public CompControl() {
    }

    /**
     * Carga los datos del nuevo formato ccr
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarNuevoCcr() throws ExceptionFatal, ExceptionWarn {
        try {
            ((EstrategiaControl) getControlador("EstrategiaControl")).cargarProductos();
            this.ccr = formatoFachada.crearFormato("ccr");
            fabricaAtributos.crearAtributosCcr(this.ccr);
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede cargar el nuevo CCR. " + e.getMessage());
        }
    }

    /**
     * Carga el formato peer
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarPeer() throws ExceptionFatal, ExceptionWarn {
        try {
            this.peer = formatoFachada.cargarFormato("peer");
            if (!this.peer.estaGuardado()) {
                fabricaAtributos.crearAtributosPeer(this.peer);
            }
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede cargar el nuevo PEER. " + e.getMessage());
        }
    }

    /**
     * Crea una instancia de pip y sus métricas
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarNuevoPip() throws ExceptionFatal, ExceptionWarn {
        try {
            this.pip = formatoFachada.crearFormato("pip");
            fabricaAtributos.crearAtributosPip(this.pip);
            ((EstrategiaControl) getControlador("EstrategiaControl")).cargarProductos();
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede crear un nuevo formato PIP. " + e.getMessage());
        }
    }

    /**
     * Permite insertar el formato ccr en forma perpetua
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarCcr() throws ExceptionFatal, ExceptionWarn {
        try {
            if(formatoCcrValido(this.ccr)) formatoFachada.insertarFormato(this.ccr);
            else throw new ExceptionWarn("Debes completar todos los campos disponibles.");
            cargarNuevoCcr();
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede guardar el formato CCR. " + e.getMessage());
        }
    }
    
    /**
     * Permite registrar la revisión del formato ccr en la base de datos
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void revisarCcr() throws ExceptionFatal, ExceptionWarn {
        try {
            if(this.ccrRevision.getAtributo("aprobación").getValor() == null)
                throw new ExceptionWarn("Debe aprobar o rechazar la solicitud de cambio de configuración.");
            formatoFachada.actualizarFormato(this.ccrRevision);
            this.ccrRevision = null;
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede finalizar la revisión del formato CCR. " + e.getMessage());
        }
    }
    
    /**
     * Permite registrar la revisión del formato PIP en la base de datos
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void revisarPip() throws ExceptionFatal, ExceptionWarn {
        try {
            if(!this.pipRevision.atributosLlenos())
                throw new ExceptionWarn("Debe completar todos los campos disponibles.");
            formatoFachada.actualizarFormato(this.pipRevision);
            this.pipRevision = null;
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede finalizar la revisión del formato PIP. " + e.getMessage());
        }
    }
    
    /**
     * Valida que el formato CCR sea válido
     * @param ccr
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean formatoCcrValido(FormatoConcreto ccr) throws ExceptionFatal, ExceptionWarn{
        try{
            AtributoCompuesto atrInfoCambio = ccr.getAtributo("Información del cambio");
            String parte = ccr.getAtributo("parte").getValor();
            String razonCambio = atrInfoCambio.getSubAtributo("Razón del cambio").getValor();
            String beneficiosCambio = atrInfoCambio.getSubAtributo("Beneficios del cambio").getValor();
            String impactoCambio = atrInfoCambio.getSubAtributo("Impacto del cambio").getValor();
            String descripcionCambio = atrInfoCambio.getSubAtributo("Descripción del cambio").getValor();
            if (parte == null) return false;
            else if (parte.trim().equals("")) return false;
            if (razonCambio == null) return false;
            else if (razonCambio.trim().equals("")) return false;
            if (beneficiosCambio == null) return false;
            else if (beneficiosCambio.trim().equals("")) return false;
            if (impactoCambio == null) return false;
            else if (impactoCambio.trim().equals("")) return false;
            if (descripcionCambio == null) return false;
            else if (descripcionCambio.trim().equals("")) return false;
            return true;
        }catch(Exception e){
            throw new ExceptionFatal("Error al validar el formato CCR. " + e.getMessage());
        }
    }

    /**
     * Permite insertar/actualizar el formato peer
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarPeer() throws ExceptionFatal, ExceptionWarn {
        try {
            if (this.peer.atributosLlenos())
                formatoFachada.actualizarFormato(this.peer);
            else
                throw new ExceptionWarn("El formato PEER debe ser llenado por completo");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede guardar el formato PEER. " + e.getMessage());
        }
    }

    /**
    * Permite guardar el formato PIP en forma persistente
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void insertarPip() throws ExceptionFatal, ExceptionWarn {
        try {
            formatoFachada.insertarFormato(this.pip);
            cargarNuevoPip();
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede insertar el formato PIP. " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista de formatos ccr para el ciclo, pendientes de revisar
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List getCcrsCicloPendientes() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            return formatoFachada.consultarFormatos("ccr",proyectoSesion.getNombre(),proyectoSesion.getCicloActual(),null,null,"aprobación","Pendiente");
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener los ccrs pendientes. " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista de formatos pip para el ciclo, pendientes de revisar
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List getPipsSinRevision() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            return formatoFachada.consultarFormatos("pip",proyectoSesion.getNombre(),proyectoSesion.getCicloActual(),null,null,"Fecha revision",null);
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener los pips pendientes. " + e.getMessage());
        }
    }
    
    public List getPipsPersonales() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion();
            return formatoFachada.consultarFormatos("pip", proyectoSesion.getNombre(), proyectoSesion.getCicloActual(), null, ((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener los pips personales. " + e.getMessage());
        }
    }
    
    public List getCcrsPersonales() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion();
            return formatoFachada.consultarFormatos("ccr", proyectoSesion.getNombre(), proyectoSesion.getCicloActual(), null, ((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener los ccrs personales. " + e.getMessage());
        }
    }

    /**
     * Toma el formato Ccr pendiente y lo pasa a revisión
     * @param ccrPendiente
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarCcrRevision(FormatoConcreto ccrPendiente) throws ExceptionFatal, ExceptionWarn {
        try{
            this.ccrRevision = ccrPendiente;
            this.ccrRevision.getAtributo("Fecha aprobación").setValor(Helper.formatearDate(new Date(), "dd/M/yyyy"));
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener los ccrs pendientes. " + e.getMessage());
        }
    }

    /**
     * Toma el formato Pip pendiente y lo pasa a revisión
     * @param pipRevision
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarPipRevision(FormatoConcreto pipRevision) throws ExceptionFatal, ExceptionWarn {
        try{
            this.pipRevision = pipRevision;
            this.pipRevision.getAtributo("Fecha revision").setValor(Helper.formatearDate(new Date(), "dd/M/yyyy"));
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener el pip a revisar. " + e.getMessage());
        }
    }
    
    public void cargarPipPersonal(FormatoConcreto pipPersonal) throws ExceptionFatal, ExceptionWarn {
        try{
            this.pipPersonal = pipPersonal;
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener el pip personal a revisar. " + e.getMessage());
        }
    }
    
    public void cargarCcrPersonal(FormatoConcreto ccrPersonal) throws ExceptionFatal, ExceptionWarn {
        try{
            this.ccrPersonal = ccrPersonal;
        }catch(Exception e){
            throw new ExceptionFatal("CompControl no puede obtener el ccr personal a revisar. " + e.getMessage());
        }
    }

    /**
     * Permite totalizar los valores del trabajo-dificultad
     * @param atributo3
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarTotalTrabajoDificultadPeer(AtributoCompuesto atributo3) throws ExceptionFatal, ExceptionWarn {
        try {
            boolean cienPorcientoExcedido = false;
            AtributoCompuesto trabajoDificultad = this.peer.getAtributo("Trabajo y dificultad");
            double trabajoTotal = 0;
            double dificultadTotal = 0;
            List atributos2 = trabajoDificultad.getAtributos();
            AtributoCompuesto atributo2;
            int t = atributos2.size();
            for (int i = 0; i < t - 1; i++) {
                atributo2 = (AtributoCompuesto) atributos2.get(i);
                trabajoTotal += Helper.extraerNumero(atributo2.getSubAtributo("Trabajo requerido").getValor());
                dificultadTotal += Helper.extraerNumero(atributo2.getSubAtributo("Dificultad del rol").getValor());
            }
            atributo2 = (AtributoCompuesto) atributos2.get(t - 1);
            if (Helper.redondear(trabajoTotal, 2) > 100) {
                cienPorcientoExcedido = true;
                atributo3.setValor("0");
                this.actualizarTotalTrabajoDificultadPeer(atributo3);
            } else atributo2.getSubAtributo("Trabajo requerido").setValor(Double.toString(trabajoTotal));
            if (Helper.redondear(dificultadTotal, 2) > 100) {
                cienPorcientoExcedido = true;
                atributo3.setValor("0");
                this.actualizarTotalTrabajoDificultadPeer(atributo3);
            } else atributo2.getSubAtributo("Dificultad del rol").setValor(Double.toString(dificultadTotal));
            if (cienPorcientoExcedido) throw new ExceptionWarn("Estás superando el 100%");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CompControl no puede actualizar el total de trabajo y dificultad. " + e.getMessage());
        }
    }

    public FormatoConcreto getCcr() {
        return ccr;
    }

    public void setCcr(FormatoConcreto ccr) {
        this.ccr = ccr;
    }

    public FormatoConcreto getPeer() {
        return peer;
    }

    public void setPeer(FormatoConcreto peer) {
        this.peer = peer;
    }

    public FormatoConcreto getPip() {
        return pip;
    }

    public void setPip(FormatoConcreto pip) {
        this.pip = pip;
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
    
    public FormatoConcreto getCcrRevision() {
        return ccrRevision;
    }

    public void setCcrRevision(FormatoConcreto ccrRevision) {
        this.ccrRevision = ccrRevision;
    }

    public FormatoConcreto getPipRevision() {
        return pipRevision;
    }

    public void setPipRevision(FormatoConcreto pipRevision) {
        this.pipRevision = pipRevision;
    }

    public FormatoConcreto getCcrPersonal() {
        return ccrPersonal;
    }

    public void setCcrPersonal(FormatoConcreto ccrPersonal) {
        this.ccrPersonal = ccrPersonal;
    }

    public FormatoConcreto getPipPersonal() {
        return pipPersonal;
    }

    public void setPipPersonal(FormatoConcreto pipPersonal) {
        this.pipPersonal = pipPersonal;
    }
    
    
    
    
}