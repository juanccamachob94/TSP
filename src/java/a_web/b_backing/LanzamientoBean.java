package a_web.b_backing;

import b_controlador.a_gestion.LanzamientoControl;
import c_negocio.a_relacional.Meta;
import c_negocio.a_relacional.Usuario;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;


@ManagedBean
@SessionScoped
public class LanzamientoBean extends GeneralBean {

    @ManagedProperty("#{lanzamientoControl}")
    LanzamientoControl lanzamientoControl;
    
    private UIComponent btnAsignarRoles;
    
    public LanzamientoBean() {
    }

    public void cargarAsignacionRoles(){
        try{
            lanzamientoControl.cargarAsignacionRoles();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar los datos de asignación de roles. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar los datos de asignación de roles. " + warn.getMessage(), "warn");
        } 
    }
    
    public void agregarIntegrante(List integrantesRol,Usuario integranteRol){
        try{
            lanzamientoControl.agregarIntegrante(integrantesRol,integranteRol);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al agregar el integrante. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al agregar el integrante. " + warn.getMessage(), "warn");
        } 
    }
    
    public void agregarNuevoListaRol(List integrantesRol){
        try{
            lanzamientoControl.agregarNuevoListaRol(integrantesRol);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al agregar el integrante. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al agregar el integrante. " + warn.getMessage(), "warn");
        } 
    }
    
    public void eliminarIntegranteListaRol(List integrantesRol, Usuario integranteRol){
        try{
            lanzamientoControl.eliminarIntegranteListaRol(integrantesRol,integranteRol);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al eliminar el integrante. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al eliminar el integrante. " + warn.getMessage(), "warn");
        } 
    }
    
    public void asignarRoles(){
        try{
            lanzamientoControl.asignarRoles();
            this.enviarMensaje(this.btnAsignarRoles, "Roles asignados satisfactoriamente.", "info");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(this.btnAsignarRoles, "Error al asignar los roles. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(this.btnAsignarRoles, "Error al asignar los roles. " + warn.getMessage(), "warn");
        } finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void cargarMetas(){
        try{
            lanzamientoControl.cargarMetas();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar las metas. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar las metas. " + warn.getMessage(), "warn");
        } 
    }
    
    public void eliminarMeta(List metas, Meta meta){
        try{
            lanzamientoControl.eliminarMeta(metas,meta);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al eliminar las meta. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al eliminar las meta. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void cargarModalNuevaMetaProyecto(boolean automatica){
        try{
            lanzamientoControl.cargarModalNuevaMetaProyecto(automatica);
            if(automatica) ejecutarJS("mostrarModalAgregarMetaProyecto");
            else ejecutarJS("mostrarModalNuevaMetaProyecto");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar la meta de proyecto. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar la meta de proyecto. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }

    
    public void cargarModalNuevaMetaRol(int indice, boolean automatica){
        try{
            lanzamientoControl.cargarModalNuevaMetaRol(automatica);
            if(automatica) ejecutarJS("mostrarModalAgregarMetaRol",new String[]{"n:"+Integer.toString(indice)+""});
            else ejecutarJS("mostrarModalNuevaMetaRol",new String[]{"n:"+Integer.toString(indice)+""});
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar la meta de proyecto. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar la meta de proyecto. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
        
        
    public void clonarMetaTSPProyecto(){
        try{
            lanzamientoControl.clonarMetaTSPProyecto();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al generar la meta de proyecto. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al generar la meta de proyecto. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void agregarMetaProyecto(){
        try{
            lanzamientoControl.agregarMetaProyecto();
            ejecutarJS("r_cerrarModalAgregarMetaProyecto");
            ejecutarJS("r_cerrarModalNuevaMetaProyecto");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al agregar la meta de proyecto. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al agregar la meta de proyecto. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    
    
    public void agregarMetaRol(int indice){
        try{
            lanzamientoControl.agregarMetaRol(indice);
            ejecutarJS("r_cerrarModalAgregarMetaRol",new String[]{"n:"+Integer.toString(indice)+""});
            ejecutarJS("r_cerrarModalNuevaMetaRol",new String[]{"n:"+Integer.toString(indice)+""});
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al agregar la meta de proyecto. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al agregar la meta de proyecto. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }

    
    public void actualizarMetas(List metas, String tipo){
        try{
            lanzamientoControl.actualizarMetas(metas,tipo);
            this.enviarMensaje(null,"Metas de " + tipo + " actualizadas satisfactoriamente","info");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al actualizar las metas. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al actualizar las metas. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public void guardarMetas(List metas, String tipo){
        try{
            lanzamientoControl.guardarMetas(metas,tipo);
            this.enviarMensaje(null,"Metas de " + tipo + " guardas satisfactoriamente","info");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al guardar las metas. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al guardar las metas. " + warn.getMessage(), "warn");
        }finally{
            ejecutarJS("cuadrarLabels");
        }
    }
    
    public List metasDeRol(String nombreRol){
        try{
            return lanzamientoControl.metasDeRol(nombreRol);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al obtener las metas del rol. " + fatal.getMessage(), "fatal");
            return new ArrayList();
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al obtener las metas del rol. " + warn.getMessage(), "warn");
            return new ArrayList();
        }
    }
    
    public List metasDeRolExistentes(String nombreRol){
        try{
            return lanzamientoControl.metasDeRolExistentes(nombreRol);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al obtener las metas del rol. " + fatal.getMessage(), "fatal");
            return new ArrayList();
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al obtener las metas del rol. " + warn.getMessage(), "warn");
            return new ArrayList();
        }
    }
    
    public void cargarMetasElegidas(){
        try{
            lanzamientoControl.cargarMetasElegidas();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar las metas. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar las metas. " + warn.getMessage(), "warn");
        }
    }
    
    
    public LanzamientoControl getLanzamientoControl() {
        return lanzamientoControl;
    }

    public void setLanzamientoControl(LanzamientoControl lanzamientoControl) {
        this.lanzamientoControl = lanzamientoControl;
    }

    public List getUsuariosSistema() {
        return lanzamientoControl.getUsuariosSistema();
    }

    public void setUsuariosSistema(List usuariosSistema) {
        lanzamientoControl.setUsuariosSistema(usuariosSistema);
    }

    public List getRolesIntegrantes() {
        return lanzamientoControl.getRolesIntegrantes();
    }

    public void setRolesIntegrantes(List rolesIntegrantes) {
        lanzamientoControl.setRolesIntegrantes(rolesIntegrantes);
    }

    public List getRolesTSP(){
        return lanzamientoControl.getRolesTSP();
    }

    public UIComponent getBtnAsignarRoles() {
        return btnAsignarRoles;
    }

    public void setBtnAsignarRoles(UIComponent btnAsignarRoles) {
        this.btnAsignarRoles = btnAsignarRoles;
    }

    public boolean isRolesIntegrantesRegistados() {
        return lanzamientoControl.isRolesIntegrantesRegistados();
    }

    public void setRolesIntegrantesRegistados(boolean rolesIntegrantesRegistados) {
        lanzamientoControl.setRolesIntegrantesRegistados(rolesIntegrantesRegistados);
    } 
 
    public List getMetasProyectoSesion() {
        return lanzamientoControl.getMetasProyectoSesion();
    }

    public void setMetasProyectoSesion(List metasProyectoSesion) {
        lanzamientoControl.setMetasProyectoSesion(metasProyectoSesion);
    }

    public List getMetasRoles() {
        return lanzamientoControl.getMetasProyectoSesion();
    }

    public void setMetasRoles(List metasRoles) {
        lanzamientoControl.setMetasRoles(metasRoles);
    }

    public List getMetasUsuarios() {
        return lanzamientoControl.getMetasUsuarios();
    }

    public void setMetasUsuarios(List metasUsuarios) {
        lanzamientoControl.setMetasUsuarios(metasUsuarios);
    }
    
    public List getMetasTSPProyecto(){
        return lanzamientoControl.getMetasTSPProyecto();
    }
    
    public List metasTSPRol(int indice){
        return lanzamientoControl.metasTSPRol(indice);
    }

  public Meta getNuevaMetaProyecto() {
        return lanzamientoControl.getNuevaMetaProyecto();
    }

    public void setNuevaMetaProyecto(Meta nuevaMetaProyecto) {
        lanzamientoControl.setNuevaMetaProyecto(nuevaMetaProyecto);
    }

    public Meta getNuevaMetaRol() {
        return lanzamientoControl.getNuevaMetaRol();
    }

    public void setNuevaMetaRol(Meta nuevaMetaRol) {
        lanzamientoControl.setNuevaMetaRol(nuevaMetaRol);
    }

    public Meta getNuevaMetaIntegrante() {
        return lanzamientoControl.getNuevaMetaIntegrante();
    }

    public void setNuevaMetaIntegrante(Meta nuevaMetaIntegrante) {
        lanzamientoControl.setNuevaMetaIntegrante(nuevaMetaIntegrante);
    }

    public boolean isMetasProyectoRegistradas() {
        return lanzamientoControl.isMetasProyectoRegistradas();
    }

    public void setMetasProyectoRegistradas(boolean metasProyectoRegistradas) {
        lanzamientoControl.setMetasProyectoRegistradas(metasProyectoRegistradas);
    }

    public List<Boolean> getMetasRolesRegistradas() {
        return lanzamientoControl.getMetasRolesRegistradas();
    }

    public void setMetasRolesRegistradas(List<Boolean> metasRolesRegistradas) {
        lanzamientoControl.setMetasRolesRegistradas(metasRolesRegistradas);
    }

    public List<Boolean> getMetasIntegrantesRegistradas() {
        return lanzamientoControl.getMetasIntegrantesRegistradas();
    }

    public void setMetasIntegrantesRegistradas(List<Boolean> metasIntegrantesRegistradas) {
        lanzamientoControl.setMetasIntegrantesRegistradas(metasIntegrantesRegistradas);
    }
    

    public List<Meta> getMetasCicloProyectoElegidas() {
        return lanzamientoControl.getMetasCicloProyectoElegidas();
    }

    public void setMetasCicloProyectoElegidas(List<Meta> metasCicloProyectoElegidas) {
        lanzamientoControl.setMetasCicloProyectoElegidas(metasCicloProyectoElegidas);
    }

    public List<List<Meta>> getMetasCicloProyectoRolesUsuarioSesion() {
        return lanzamientoControl.getMetasCicloProyectoRlCls();
    }

    public void setMetasCicloProyectoRolesUsuarioSesion(List<List<Meta>> metasCicloProyectoRolesUsuarioSesion) {
        lanzamientoControl.setMetasCicloProyectoRlCls(metasCicloProyectoRolesUsuarioSesion);
    }
    
    
    
}
