package b_controlador.c_fabricas.a_fabrica_formatos;

import b_controlador.a_gestion.ProyectoControl;
import b_controlador.a_gestion.UsuarioControl;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FabricaFormatos extends FabricaAbstractaFormatos{

    @ManagedProperty("#{usuarioControl}")
    UsuarioControl usuarioControl;

    @ManagedProperty("#{proyectoControl}")
    ProyectoControl proyectoControl;
    
    /**
     * Permite crear un formato dado el nombre
     * @param nombreFormato
     * @return
     * @throws ExceptionFatal 
     */
    public FormatoConcreto crearFormato(String nombreFormato) throws ExceptionFatal{
        try{
            switch(nombreFormato){
                case "sump":
                case "sumq":
                case "logd":
                case "strat":
                case "task":
                case "schedule":
                case "week":
                case "itl":
                case "logt":
                return crearFormatoGrupal(nombreFormato);
                case "peer":
                case "pip":
                case "ccr":
                return crearFormatoIndividual(nombreFormato);
                default:
                return crearFormatoIndividual(nombreFormato);
            }
        }catch(Exception e){
            throw new ExceptionFatal("FabricaFormatos no puede crear el formato " + nombreFormato + ". " + e.getMessage());
        }
    }

    /**
     * Permite crear un formato dado el nombre y el rol del autor
     * @param nombreFormato
     * @param rolAutor
     * @return
     * @throws ExceptionFatal 
     */
    public FormatoConcreto crearFormato(String nombreFormato, String rolAutor) throws ExceptionFatal{
        try{
            switch(nombreFormato){
                case "logt":
                return crearFormatoIndividualRol(nombreFormato,rolAutor);
            }
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("FabricaFormatos no puede crear el formato " + nombreFormato + ". " + e.getMessage());
        }
    }

    /**
     * Permite crear un formato dados todos los atributos del encabezado
     * @param nombreFormato
     * @param nombreProyecto
     * @param numCiclo
     * @param rolAutor
     * @param identificacionAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto crearFormato(String nombreFormato, String nombreProyecto, int numCiclo, String rolAutor, String identificacionAutor) throws ExceptionFatal, ExceptionWarn {
        try{
        return new FormatoConcreto(nombreFormato, identificacionAutor, rolAutor, nombreProyecto, numCiclo);
        }catch(Exception e){
            throw new ExceptionFatal("FabricaFormatos no puede crear el formato " + nombreFormato + ". " + e.getMessage());
        }
    }

    /**
     * Permite crear un formato individual
     * @param nombreFormato
     * @return
     * @throws ExceptionFatal 
     */
    private FormatoConcreto crearFormatoIndividual(String nombreFormato) throws ExceptionFatal{
        try{
            Proyecto proyectoSesion = proyectoControl.getProyectoSesion();
            return new FormatoConcreto(nombreFormato,proyectoSesion.getNombre(),proyectoSesion.getCicloActual(),usuarioControl.getUsuarioSesion().getIdentificacion());
        }catch(Exception e){
            throw new ExceptionFatal("No se puede crear el formato individual " + nombreFormato + ". " + e.getMessage());
        }
    }

    /**
     * Permite crear un formato de grupo
     */
    private FormatoConcreto crearFormatoGrupal(String nombreFormato) throws ExceptionFatal{
        try{
            Proyecto proyectoSesion = proyectoControl.getProyectoSesion();
            return new FormatoConcreto(nombreFormato,proyectoSesion.getNombre(),proyectoSesion.getCicloActual());
        }catch(Exception e){
            throw new ExceptionFatal("No se puede crear el formato grupal " + nombreFormato + ". " + e.getMessage());
        }
    }

    /**
     * Permite crear un formato individual (de usuario) con rol específico
     * @param nombreFormato
     * @param rolAutor
     * @return
     * @throws ExceptionFatal 
     */
    private FormatoConcreto crearFormatoIndividualRol(String nombreFormato, String rolAutor) throws ExceptionFatal{
        try{
            Proyecto proyectoSesion = proyectoControl.getProyectoSesion();
            return new FormatoConcreto(nombreFormato,usuarioControl.getUsuarioSesion().getIdentificacion(),rolAutor,proyectoSesion.getNombre(),proyectoSesion.getCicloActual());
        }catch(Exception e){
            throw new ExceptionFatal("No se puede crear el formato individual rol " + nombreFormato + ". " + e.getMessage());
        }
    }

    public UsuarioControl getUsuarioControl() {
        return usuarioControl;
    }

    public void setUsuarioControl(UsuarioControl usuarioControl) {
        this.usuarioControl = usuarioControl;
    }

    public ProyectoControl getProyectoControl() {
        return proyectoControl;
    }

    public void setProyectoControl(ProyectoControl proyectoControl) {
        this.proyectoControl = proyectoControl;
    }
}