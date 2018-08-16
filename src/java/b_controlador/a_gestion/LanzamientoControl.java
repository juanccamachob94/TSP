/**
 * Permite gestionar la fase de lanzamiento: asginación de roles y definición de metas
 */
package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import c_negocio.a_relacional.Meta;
import c_negocio.a_relacional.MetaId;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import c_negocio.a_relacional.Rol;
import c_negocio.a_relacional.Usuario;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import d_datos.a_dao.ProyectoDAO;
import d_datos.a_dao.UsuarioDAO;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import e_utilitaria.Serial;
import e_utilitaria.TSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class LanzamientoControl extends Control {

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    
    private List usuariosSistema;
    //Lista de listas, donde cada posición representa un rol y su lista los asignados de éste
    private List rolesIntegrantes;
    private boolean rolesIntegrantesRegistados;

    private List metasProyectoSesion;
    private List metasRoles;
    private List metasUsuarios;

    private Meta nuevaMetaProyecto;
    private Meta nuevaMetaRol;
    private Meta nuevaMetaIntegrante;

    private boolean metasProyectoRegistradas;
    private List<Boolean> metasRolesRegistradas;
    private List<Boolean> metasIntegrantesRegistradas;
    
    private List<Meta> metasCicloProyectoElegidas;
    private List<List<Meta>> metasCicloProyectoRlCls;
    



    public LanzamientoControl() {
    }

    /**
     * Carga los datos necesarios para la asignación de roles
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void cargarAsignacionRoles() throws ExceptionFatal, ExceptionWarn {
        try {
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            this.usuariosSistema = UsuarioDAO.consultarTodos();
            rolesIntegrantes = new ArrayList();
            int numRoles = TSP.rolesTSP.size();
            for (int i = 0; i < numRoles; i++) {
                rolesIntegrantes.add(new ArrayList());
            }
            List rolesAsignados = ProyectoDAO.consultarRolesCiclo(proyectoSesion);
            int totalRolesAsignados = rolesAsignados.size();
            RlCl rolCiclo;
            int pos;
            this.rolesIntegrantesRegistados = false;
            for (int i = 0; i < totalRolesAsignados; i++) {
                rolCiclo = (RlCl) rolesAsignados.get(i);
                pos = posicionRolTSP(rolCiclo.getRol().getNombre());
                if (pos != -1) {
                    ((List) rolesIntegrantes.get(pos)).add(UsuarioDAO.consultarUsuarioSistema(rolCiclo.getUsuario()));
                    this.rolesIntegrantesRegistados = true;
                }
            }
            List integrantesRol;
            for (int i = 0; i < numRoles; i++) {
                integrantesRol = (List) rolesIntegrantes.get(i);
                if (integrantesRol.isEmpty()) {
                    integrantesRol.add(new Usuario());
                }
            }

            int numRolesTSP = TSP.rolesTSP.size();
            Rol rol;
            for (int i = 0; i < numRolesTSP; i++) {
                rol = (Rol) TSP.rolesTSP.get(i);
                if (ProyectoDAO.consultarRol(rol.getNombre()) == null) {
                    ProyectoDAO.insertarRolTSP(rol);
                }
            }
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no se pueden cargar los datos de asignación de rol. " + e.getMessage());
        }
    }

    /**
     * Indica la posición del rol en la estructura TSP
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    private int posicionRolTSP(String rol) throws ExceptionFatal, ExceptionWarn {
        try {
            int t = TSP.rolesTSP.size();
            for (int i = 0; i < t; i++) {
                if (rol.equals(((Rol) TSP.rolesTSP.get(i)).getNombre())) {
                    return i;
                }
            }
            return -1;
        } catch (Exception e) {
            throw new ExceptionFatal("Error al posicionarse sobre los roles. " + e.getMessage());
        }
    }

    /**
     * Agrega un integrante al rol especificado
     * @param integrantesRol
     * @param integranteRol
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void agregarIntegrante(List integrantesRol, Usuario integranteRol) throws ExceptionFatal, ExceptionWarn {
        try {
            Usuario usuarioConsultado = UsuarioDAO.consultarUsuarioSistema(integranteRol);
            if (usuarioConsultado == null) {
                integranteRol.setIdentificacion(null);
                throw new ExceptionWarn("No puede localizar al integrante");
            }
            String nuevaIdentificacion = integranteRol.getIdentificacion();
            String identificacion;
            int numApariciones = 0;
            int t = integrantesRol.size();
            for (int i = 0; i < t; i++) {
                identificacion = ((Usuario) integrantesRol.get(i)).getIdentificacion();
                if (identificacion != null) {
                    if (identificacion.equals(nuevaIdentificacion)) {
                        numApariciones += 1;
                    }
                }
            }
            if (numApariciones > 1) {
                integranteRol.setIdentificacion(null);
                throw new ExceptionWarn("No puedes asignar más de una vez al usuario al mismo rol");
            }
            integranteRol.setNombre(usuarioConsultado.getNombre());
            integranteRol.setApellido(usuarioConsultado.getApellido());
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede agregar el integrante. " + e.getMessage());
        }
    }

    /**
     * Agrega un nuevo elemento a la lista del rol
     * @param integrantesRol
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void agregarNuevoListaRol(List integrantesRol) throws ExceptionFatal, ExceptionWarn {
        try {
            integrantesRol.add(new Usuario());
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede agregar un nuevo integrante. " + e.getMessage());
        }
    }

    /**
     * Elimina el elemento de la lista delrol
     * @param integrantesRol
     * @param integranteRol
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void eliminarIntegranteListaRol(List integrantesRol, Usuario integranteRol) throws ExceptionFatal, ExceptionWarn {
        try {
            integrantesRol.remove(integranteRol);
            if (integrantesRol.isEmpty()) {
                integrantesRol.add(new Usuario());
            }
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede eliminar un nuevo integrante. " + e.getMessage());
        }
    }

    /**
     * Registra los roles en forma persistente
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void asignarRoles() throws ExceptionFatal, ExceptionWarn {
        try {
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            byte nCicloActual = b(proyectoSesion.getCicloActual());
            String nombreProyectoSesion = proyectoSesion.getNombre();
            List rlcls = new ArrayList();
            RlCl rlcl;
            RlClId id;
            int numRolesIntegrantes = this.rolesIntegrantes.size();
            List integrantesRol;
            String nombreRol;
            Usuario usuario;
            int numIntegrantesRol;
            for (int i = 0; i < numRolesIntegrantes; i++) {
                nombreRol = ((Rol) TSP.rolesTSP.get(i)).getNombre();
                integrantesRol = (List) this.rolesIntegrantes.get(i);
                numIntegrantesRol = integrantesRol.size();
                for (int j = 0; j < numIntegrantesRol; j++) {
                    usuario = ((Usuario) integrantesRol.get(j));
                    if (usuario.getNombre() == null) {
                        throw new ExceptionWarn("Todos los roles deben tener asociado un integrante");
                    }
                    id = new RlClId();
                    id.setRol(nombreRol);
                    id.setProyecto(nombreProyectoSesion);
                    id.setNCiclo(nCicloActual);
                    id.setUsuario(usuario.getIdentificacion());
                    rlcl = new RlCl();
                    rlcl.setId(id);
                    rlcl.setEstado("Habilitado");
                    rlcls.add(rlcl);
                }
            }
            if (!ProyectoDAO.consultarMetasCiclo(proyectoSesion).isEmpty()) {
                throw new Exception("Una vez creadas las metas no se pueden hacer cambios sobre los roles");
            }
            ProyectoDAO.eliminarRolesTSPCiclo(proyectoSesion);
            ProyectoDAO.insertarRolesCiclo(rlcls);
            ((ProyectoControl) getControlador("ProyectoControl")).cargarRolesSesion();
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede asignar los roles para el ciclo. " + e.getMessage());
        }
    }

    public void cargarMetas() throws ExceptionFatal, ExceptionWarn {
        try {
            Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();

            //Metas de proyecto
            this.metasProyectoSesion = ProyectoDAO.consultarMetasCicloProyecto(proyectoSesion);
            int t = TSP.metasTSPProyecto.size();
            if (this.metasProyectoSesion.isEmpty()) {
                this.metasProyectoRegistradas = false;
                for (int i = 0; i < t; i++) this.metasProyectoSesion.add(Serial.clonar(TSP.metasTSPProyecto.get(i)));
            } else this.metasProyectoRegistradas = true;

            //Metas de rol
            this.metasRoles = new ArrayList();
            this.metasRolesRegistradas = new ArrayList();
            int totalRoles = TSP.rolesTSP.size();
            List metasRol;
            List metasRolTSP;
            for (int i = 0; i < totalRoles; i++) {
                metasRol = ProyectoDAO.consultarMetasRolCicloProyecto(proyectoSesion, ((Rol) TSP.rolesTSP.get(i)).getNombre());
                if (metasRol.isEmpty()) {
                    metasRolTSP = (List) TSP.metasRoles.get(i);
                    t = metasRolTSP.size();
                    for (int j = 0; j < t; j++) {
                        metasRol.add(Serial.clonar(metasRolTSP.get(j)));
                    }
                    this.metasRolesRegistradas.add(false);
                } else this.metasRolesRegistradas.add(true);
                this.metasRoles.add(metasRol);
            }
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede cargar las metas del proyecto. " + e.getMessage());
        }
    }

    /**
     * Elimina la meta de la lista de metas igualmente proporcionado
     * @param metas
     * @param meta
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarMeta(List metas, Meta meta) throws ExceptionFatal, ExceptionWarn {
        try {
            metas.remove(meta);
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede eliminar la meta. " + e.getMessage());
        }
    }

    /**
     * Carga la información del nuevo proyecto
     * @param automatica
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalNuevaMetaProyecto(boolean automatica) throws ExceptionFatal, ExceptionWarn {
        try {
            this.nuevaMetaProyecto = new Meta(new MetaId(), automatica);
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede cargar la información de la nueva meta de proyecto. " + e.getMessage());
        }
    }

    /**
     * Carga la información relacionada a la nueva meta de rol
     * @param automatica
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalNuevaMetaRol(boolean automatica) throws ExceptionFatal, ExceptionWarn {
        try {
            this.nuevaMetaRol = new Meta(new MetaId(), automatica);
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede cargar la información de la nueva meta de proyecto. " + e.getMessage());
        }
    }
    
    /**
     * Carga la información de la meta brindada por tsp de rol
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalAgregarMetaTSPRol() throws ExceptionFatal, ExceptionWarn {
        try {
            this.nuevaMetaRol = new Meta(new MetaId(), true);
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede cargar la información de la nueva meta de rol. " + e.getMessage());
        }
    }

    /**
     * Copia la meta TSP de proyecto a la lista de metas del proyecto en sesión
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void clonarMetaTSPProyecto() throws ExceptionFatal, ExceptionWarn {
        try {
            String descripcion = this.nuevaMetaProyecto.getId().getDescripcion();
            if (descripcion == null) {
                this.nuevaMetaProyecto = new Meta(new MetaId(), false);
            } else {
                int t = TSP.metasTSPProyecto.size();
                Meta metaTSP;
                for (int i = 0; i < t; i++) {
                    metaTSP = (Meta) TSP.metasTSPProyecto.get(i);
                    if (metaTSP.getId().getDescripcion().equals(descripcion)) {
                        this.nuevaMetaProyecto = (Meta) Serial.clonar(metaTSP);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede cargar la información de la nueva meta de proyecto. " + e.getMessage());
        }
    }

    /**
     * Agrega la meta (nueva o de TSP) a la lista de metas del proyecto
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void agregarMetaProyecto() throws ExceptionFatal, ExceptionWarn {
        try {
            String descripcion = this.nuevaMetaProyecto.getId().getDescripcion();
            if (descripcion == null) throw new ExceptionWarn("La meta no es válida");
            if (this.nuevaMetaProyecto.getValor() == null) throw new ExceptionWarn("Debe ingresar el valor para la meta");
            if (vacio(this.nuevaMetaProyecto.getComparativaValor())) throw new ExceptionWarn("Debe especificar la comparativa");
            if (vacio(this.nuevaMetaProyecto.getUnidadValor())) throw new ExceptionWarn("Debe especificar la unidad de medida");
            int t = this.metasProyectoSesion.size();
            for (int i = 0; i < t; i++)
                if (((Meta) this.metasProyectoSesion.get(i)).getId().getDescripcion().equals(descripcion))
                    throw new ExceptionWarn("La meta " + descripcion + " ya está cargada");
            this.metasProyectoSesion.add(this.nuevaMetaProyecto);
            this.nuevaMetaProyecto = null;
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede agregar la meta de proyecto. " + e.getMessage());
        }
    }

    /**
     * A
     * @Agrega la meta (nueva o de TSP) a la lista de metas de rol del proyecto en sesión
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void agregarMetaRol(int indice) throws ExceptionFatal, ExceptionWarn {
        try {
            String descripcion = this.nuevaMetaRol.getId().getDescripcion();
            if (descripcion == null) throw new ExceptionWarn("La meta no es válida");
            List metasRol = (List)this.metasRoles.get(indice);
            int t = metasRol.size();
            for (int i = 0; i < t; i++)
                if (((Meta) metasRol.get(i)).getId().getDescripcion().equals(descripcion))
                    throw new ExceptionWarn("La meta " + descripcion + " ya está cargada");
            metasRol.add(this.nuevaMetaRol);
            this.nuevaMetaRol = null;
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede agregar la meta del rol. " + e.getMessage());
        }
    }
    
    /**
     * Brinda las metas de rol de TSP
     * @param nombreRol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List metasDeRol(String nombreRol) throws ExceptionFatal, ExceptionWarn {
        try {
            int t = TSP.rolesTSP.size();
            for (int i = 0; i < t; i++)
                if (((Rol) TSP.rolesTSP.get(i)).getNombre().equals(nombreRol))
                    return (List) this.metasRoles.get(i);
            return null;
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede obtener las metas del rol " + nombreRol + ". " + e.getMessage());
        }
    }
    
    /**
     * Brinda las metas de rol existentes de TSP
     * @param nombreRol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List metasDeRolExistentes(String nombreRol) throws ExceptionFatal, ExceptionWarn {
        try {
            int t = TSP.rolesTSP.size();
            for (int i = 0; i < t; i++)
                if (((Rol) TSP.rolesTSP.get(i)).getNombre().equals(nombreRol))
                    return (List) this.metasCicloProyectoRlCls.get(i);
            return null;
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede obtener las metas del rol " + nombreRol + ". " + e.getMessage());
        }
    }
    
    /**
     * actualizar logros de las metas
     * @param metas
     * @param tipo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarMetas(List metas, String tipo) throws ExceptionFatal, ExceptionWarn {
        try {
            ProyectoControl pc = (ProyectoControl) getControlador("ProyectoControl");
            Proyecto proyectoSesion = pc.getProyectoSesion();
            int pos = -1;
            int numRolesTSP = this.metasCicloProyectoRlCls.size();
            switch (tipo) {
                case "proyecto":
                    ProyectoDAO.eliminarMetasCicloProyecto(proyectoSesion);
                    break;
                case "rol":
                    for(int i = 0; i < numRolesTSP; i++)
                        if(this.metasCicloProyectoRlCls.get(i).equals(metas)){
                            pos = i;
                            break;
                        }
                    ProyectoDAO.eliminarMetasCicloProyectoRol(proyectoSesion,((Rol)TSP.rolesTSP.get(pos)).getNombre());
                    break;
            }
            int t = metas.size();
            Meta meta;
            MetaId id;
            byte cicloActual = proyectoSesion.getCicloActual();
            Date hoy = new Date();
            String nombreProyecto = proyectoSesion.getNombre();
            String rolUsuario = (String) pc.getRolesUSesion().get(0);
            if(tipo.equals("rol")) rolUsuario = ((Rol)TSP.rolesTSP.get(pos)).getNombre();
            String identificacionUsuarioSesion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            for (int i = 0; i < t; i++) {
                meta = (Meta) metas.get(i);
                if (meta.getFechaCreacion() == null) {
                    id = meta.getId();
                    id.setNCiclo(cicloActual);
                    id.setProyecto(nombreProyecto);
                    id.setRol(rolUsuario);
                    id.setUsuario(identificacionUsuarioSesion);
                    meta.setFechaCreacion(hoy);
                    meta.setTipo(tipo);
                }
                ProyectoDAO.insertarMeta(meta);
            }
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede guardar las metas de " + tipo + ". " + e.getMessage());
        }
    }
    
    
    /**
     * Guarda la lista de metas de acuerdo al tipo de meta
     * @param metas
     * @param tipo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarMetas(List metas, String tipo) throws ExceptionFatal, ExceptionWarn {
        try {
            ProyectoControl pc = (ProyectoControl) getControlador("ProyectoControl");
            Proyecto proyectoSesion = pc.getProyectoSesion();
            int pos = -1;
            int numRolesTSP = this.metasRoles.size();
            switch (tipo) {
                case "proyecto":
                    ProyectoDAO.eliminarMetasCicloProyecto(proyectoSesion);
                    break;
                case "rol":
                    for(int i = 0; i < numRolesTSP; i++)
                        if(this.metasRoles.get(i).equals(metas)){
                            pos = i;
                            break;
                        }
                    ProyectoDAO.eliminarMetasCicloProyectoRol(proyectoSesion,((Rol)TSP.rolesTSP.get(pos)).getNombre());
                    break;
            }
            int t = metas.size();
            Meta meta;
            MetaId id;
            RlCl rlcl;
            byte cicloActual = proyectoSesion.getCicloActual();
            Date hoy = new Date();
            String nombreProyecto = proyectoSesion.getNombre();
            String rolUsuario = (String) pc.getRolesUSesion().get(0);
            if(tipo.equals("rol")) rolUsuario = ((Rol)TSP.rolesTSP.get(pos)).getNombre();
            String identificacionUsuarioSesion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            for (int i = 0; i < t; i++) {
                meta = (Meta) metas.get(i);
                if (meta.getFechaCreacion() == null) {
                    id = meta.getId();
                    id.setNCiclo(cicloActual);
                    id.setProyecto(nombreProyecto);
                    id.setRol(rolUsuario);
                    id.setUsuario(identificacionUsuarioSesion);
                    meta.setFechaCreacion(hoy);
                    meta.setTipo(tipo);
                }
                ProyectoDAO.insertarMeta(meta);
            }
            switch (tipo) {
                case "proyecto":
                    this.metasProyectoRegistradas = true;
                    break;
                case "rol":
                    this.metasRolesRegistradas.set(pos, true);
                    break;
            }
        } catch (Exception e) {
            throw new ExceptionFatal("LanzamientoControl no puede guardar las metas de " + tipo + ". " + e.getMessage());
        }
    }

    public void cargarMetasElegidas() throws ExceptionFatal, ExceptionWarn {
        try{
            Proyecto proyectoSesion = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion();
            //Se llena la lista de metas registradas y de tipo proyecto para el ciclo actual
            this.metasCicloProyectoElegidas = ProyectoDAO.consultarMetasCicloProyecto(proyectoSesion);
            int tMetasTSPProyecto = TSP.metasTSPProyecto.size();
            Meta metaElegida;
            String descripcionMetaElegida;
            String descripcionMetaTSP;
            int tMetasCicloProyectoElegidas = this.metasCicloProyectoElegidas.size();
            //Para cada meta elegida se identifican las automáticas y se asigna el valor de lograda
            for(int i = 0; i < tMetasCicloProyectoElegidas; i++){
                metaElegida = this.metasCicloProyectoElegidas.get(i);
                if(metaElegida.isAutomatica()){
                    descripcionMetaElegida = metaElegida.getId().getDescripcion();
                    for(int j = 0; j < tMetasTSPProyecto; j++){
                        descripcionMetaTSP = ((Meta)TSP.metasTSPProyecto.get(j)).getId().getDescripcion();
                        if(descripcionMetaTSP.equals(descripcionMetaElegida)){
                            determinarLogroMetaProyecto(j,metaElegida);
                            break;
                        }
                    }
                }
            }
            int numRolesTSP = TSP.rolesTSP.size();
            this.metasCicloProyectoRlCls = new ArrayList();
            for(int i = 0; i < numRolesTSP; i++)
                this.metasCicloProyectoRlCls.add(ProyectoDAO.consultarMetasRolCicloProyecto(proyectoSesion, ((Rol)TSP.rolesTSP.get(i)).getNombre()));    
        }catch(Exception e){
            throw new ExceptionFatal("" + e.getMessage());
        }
    }
    
    private void determinarLogroMetaProyecto(int pos, Meta metaElegida) throws ExceptionFatal, ExceptionWarn {
        List<FormatoConcreto> formatos;
        switch(pos){
            //Defectos encontrados en compilación
            case 0:
                formatos = formatoFachada.consultarFormatos("sump");
                if(!formatos.isEmpty()){
                    AtributoCompuesto atrCompilacion = (formatos.get(0)).getAtributo("Defectos inyectados").getSubAtributo((String)TSP.etapas.get(15));
                    double planDefectosCompilacion = Helper.extraerNumero(atrCompilacion.getSubAtributo("Plan").getValor());
                    double realDefectosCompilacion = Helper.extraerNumero(atrCompilacion.getSubAtributo("Real").getValor());
                    double porcentaje = planDefectosCompilacion <= realDefectosCompilacion ? planDefectosCompilacion/realDefectosCompilacion * 100 : realDefectosCompilacion/planDefectosCompilacion * 100;
                    if(porcentaje == metaElegida.getValor().doubleValue()) metaElegida.setLograda(true);
                    else metaElegida.setLograda(false);
                }else metaElegida.setLograda(false);
                break;
            //Defectos encontrados en la prueba de sistema
            case 1:
                formatos = formatoFachada.consultarFormatos("sump");
                if(!formatos.isEmpty()){
                    AtributoCompuesto atrCompilacion = (formatos.get(0)).getAtributo("Defectos inyectados").getSubAtributo((String)TSP.etapas.get(19));
                    double planDefectosCompilacion = Helper.extraerNumero(atrCompilacion.getSubAtributo("Plan").getValor());
                    double realDefectosCompilacion = Helper.extraerNumero(atrCompilacion.getSubAtributo("Real").getValor());
                    double porcentaje = planDefectosCompilacion <= realDefectosCompilacion ? planDefectosCompilacion/realDefectosCompilacion * 100 : realDefectosCompilacion/planDefectosCompilacion * 100;
                    if(porcentaje == metaElegida.getValor().doubleValue()) metaElegida.setLograda(true);
                    else metaElegida.setLograda(false);
                }else metaElegida.setLograda(false);
                break;
            //Error en el estimado de tamaño del producto
            case 3:
                formatos = formatoFachada.consultarFormatos("sump");
                if(!formatos.isEmpty()){
                    AtributoCompuesto atrCompilacion = (formatos.get(0)).getAtributo("Tamaño del producto").getSubAtributo("Código total (T)");
                    double planDefectosCompilacion = Helper.extraerNumero(atrCompilacion.getSubAtributo("Plan").getValor());
                    double realDefectosCompilacion = Helper.extraerNumero(atrCompilacion.getSubAtributo("Real").getValor());
                    double porcentaje = planDefectosCompilacion <= realDefectosCompilacion ? planDefectosCompilacion/realDefectosCompilacion * 100 : realDefectosCompilacion/planDefectosCompilacion * 100;
                    if(porcentaje == metaElegida.getValor().doubleValue()) metaElegida.setLograda(true);
                    else metaElegida.setLograda(false);
                }else metaElegida.setLograda(false);
                break;
            //Información registrada del ciclo
            case 5:
                Proyecto proyectoSesion = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion();
                formatos = formatoFachada.consultarFormatos(null, proyectoSesion.getNombre(), proyectoSesion.getCicloActual(), null, null);
                int t = formatos.size();
                boolean nombres[] = new boolean[10];//sump,sumq,week,schedule,logt,logd,itl,peer,strat
                for(int i = 0; i < t; i++){
                    switch(formatos.get(i).getNombre()){
                        case "sump":
                            nombres[0] = true;
                            break;
                        case "sumq":
                            nombres[1] = true;
                            break;
                        case "week":
                            nombres[2] = true;
                            break;
                        case "schedule":
                            nombres[3] = true;
                            break;
                        case "logt":
                            nombres[4] = true;
                            break;
                        case "logd":
                            nombres[5] = true;
                            break;
                        case "itl":
                            nombres[6] = true;
                            break;
                        case "peer":
                            nombres[7] = true;
                            break;
                        case "strat":
                            nombres[9] = true;
                            break;
                    }
                }
                t = 10;
                boolean faltaFormato = true;
                for(int i = 0; i < t; i++)
                    if(!nombres[i]){
                        faltaFormato = false;
                        break;
                    }
                metaElegida.setLograda(faltaFormato);
                break;
        }
    }

    public List getUsuariosSistema() {
        return usuariosSistema;
    }

    public void setUsuariosSistema(List usuariosSistema) {
        this.usuariosSistema = usuariosSistema;
    }

    public List getRolesIntegrantes() {
        return rolesIntegrantes;
    }

    public void setRolesIntegrantes(List rolesIntegrantes) {
        this.rolesIntegrantes = rolesIntegrantes;
    }

    public List getRolesTSP() {
        return TSP.rolesTSP;
    }

    public boolean isRolesIntegrantesRegistados() {
        return rolesIntegrantesRegistados;
    }

    public void setRolesIntegrantesRegistados(boolean rolesIntegrantesRegistados) {
        this.rolesIntegrantesRegistados = rolesIntegrantesRegistados;
    }

    public List getMetasProyectoSesion() {
        return metasProyectoSesion;
    }

    public void setMetasProyectoSesion(List metasProyectoSesion) {
        this.metasProyectoSesion = metasProyectoSesion;
    }

    public List getMetasRoles() {
        return metasRoles;
    }

    public void setMetasRoles(List metasRoles) {
        this.metasRoles = metasRoles;
    }

    public List getMetasUsuarios() {
        return metasUsuarios;
    }

    public void setMetasUsuarios(List metasUsuarios) {
        this.metasUsuarios = metasUsuarios;
    }

    public List getMetasTSPProyecto() {
        return TSP.metasTSPProyecto;
    }
    
    public List metasTSPRol(int indice){
        return (List)TSP.metasRoles.get(indice);
    }

    public Meta getNuevaMetaProyecto() {
        return nuevaMetaProyecto;
    }

    public void setNuevaMetaProyecto(Meta nuevaMetaProyecto) {
        this.nuevaMetaProyecto = nuevaMetaProyecto;
    }

    public Meta getNuevaMetaRol() {
        return nuevaMetaRol;
    }

    public void setNuevaMetaRol(Meta nuevaMetaRol) {
        this.nuevaMetaRol = nuevaMetaRol;
    }

    public Meta getNuevaMetaIntegrante() {
        return nuevaMetaIntegrante;
    }

    public void setNuevaMetaIntegrante(Meta nuevaMetaIntegrante) {
        this.nuevaMetaIntegrante = nuevaMetaIntegrante;
    }

    public boolean isMetasProyectoRegistradas() {
        return metasProyectoRegistradas;
    }

    public void setMetasProyectoRegistradas(boolean metasProyectoRegistradas) {
        this.metasProyectoRegistradas = metasProyectoRegistradas;
    }

    public List<Boolean> getMetasRolesRegistradas() {
        return metasRolesRegistradas;
    }

    public void setMetasRolesRegistradas(List<Boolean> metasRolesRegistradas) {
        this.metasRolesRegistradas = metasRolesRegistradas;
    }

    public List<Boolean> getMetasIntegrantesRegistradas() {
        return metasIntegrantesRegistradas;
    }

    public void setMetasIntegrantesRegistradas(List<Boolean> metasIntegrantesRegistradas) {
        this.metasIntegrantesRegistradas = metasIntegrantesRegistradas;
    }

    public List<Meta> getMetasCicloProyectoElegidas() {
        return metasCicloProyectoElegidas;
    }

    public void setMetasCicloProyectoElegidas(List<Meta> metasCicloProyectoElegidas) {
        this.metasCicloProyectoElegidas = metasCicloProyectoElegidas;
    }

    public List<List<Meta>> getMetasCicloProyectoRlCls() {
        return metasCicloProyectoRlCls;
    }

    public void setMetasCicloProyectoRlCls(List<List<Meta>> metasCicloProyectoRlCls) {
        this.metasCicloProyectoRlCls = metasCicloProyectoRlCls;
    }

    public FormatoFachada getFormatoFachada() {
        return formatoFachada;
    }

    public void setFormatoFachada(FormatoFachada formatoFachada) {
        this.formatoFachada = formatoFachada;
    }    
}