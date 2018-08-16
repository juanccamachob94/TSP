/**
 * Gestiona todos los elementos relacionados al proyecto
 */
package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import c_negocio.a_relacional.Ciclo;
import c_negocio.a_relacional.CicloId;
import c_negocio.a_relacional.Criterio;
import c_negocio.a_relacional.Fase;
import c_negocio.a_relacional.FaseId;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.PyCr;
import c_negocio.a_relacional.PyCrId;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import c_negocio.a_relacional.Rol;
import c_negocio.a_relacional.Usuario;
import c_negocio.b_no_relacional.atributo.Atributo;
import d_datos.a_dao.ProyectoDAO;
import d_datos.a_dao.UsuarioDAO;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.GestorArchivos;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import java.io.File;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class ProyectoControl extends Control{

    private Proyecto proyectoSesion;
    private Ciclo cicloSesion;
    private Fase faseSesion;
    private List rolesUSesion;
    private List criteriosProyectoSesion;
    
    private Usuario instructor;
    
    private Proyecto nuevoProyecto;
    private RlCl rlClInstructorNuevoProyecto;
    private List criteriosNuevoProyecto;
    private List opcionesCriteriosNuevoProyecto;
    private UploadedFile imagenSubidaNuevoProyecto;
    
    private List proyectosUsuarioSesion;
    private List usuariosRolesCicloActual;
    
    private List<Boolean> validacionCriteriosSalida;
    private List<Atributo> criteriosSalida;
    private String siguienteCicloFase;
    
    private boolean fechaInicioEspecifica;
    private Date fechaEspecificaInicioProyecto;
    
    private List<Boolean> seleccionProyectosUsuarioSesion;
    
    
    public ProyectoControl(){
    }
    
    /**
     * Carga la información de un nuevo proyecto para crearlo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarNuevoProyecto() throws ExceptionFatal, ExceptionWarn {
        try{
            this.nuevoProyecto = new Proyecto();
            this.fechaEspecificaInicioProyecto = new Date();
            this.fechaInicioEspecifica = false;
            this.instructor = new Usuario();
            this.nuevoProyecto.setUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion());
            this.rlClInstructorNuevoProyecto = new RlCl();
            this.opcionesCriteriosNuevoProyecto = new ArrayList();
            this.cargarCriteriosNuevoProyecto(this.cargarCriterios());
            int t = this.criteriosNuevoProyecto.size();
            for (int i = 0; i < t; i++)
                this.opcionesCriteriosNuevoProyecto.add(TSP.opcionesCriterio((PyCr) this.criteriosNuevoProyecto.get(i)));
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede cargar el nuevo proyecto. " + e.getMessage());
        }
    }
    
    
    public void validarFechaEspecificaInicioProyecto() throws ExceptionFatal, ExceptionWarn {
        try{
            if((new Date()).before(this.fechaEspecificaInicioProyecto)){
                this.fechaEspecificaInicioProyecto = new Date();
                throw new ExceptionWarn("La fecha de inicio del proyecto no puede superar la fecha actual");
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede validar la fecha específica del inicio del proyecto " + e.getMessage());
        }
    }
    

    /**
     * Permite actualizar el número de ciclos brindado en el formulario de crear un nuevo proyecto
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarNumeroCiclos() throws ExceptionFatal, ExceptionWarn {
        try{
            int numCiclos = this.nuevoProyecto.getNCiclos();
            if(numCiclos > 3 || numCiclos < 1){
                this.nuevoProyecto.setNCiclos(b(3));
                throw new ExceptionWarn("El número de ciclos debe estar entre 1 y 3.");
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede actualizar el número de ciclos. " + e.getMessage());
        }
    }

    /**
     * Crea el proyecto junto a sus dependencias
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void crearProyecto() throws ExceptionFatal, ExceptionWarn {
        try{
            if(vacio(this.nuevoProyecto.getNombre())) throw new ExceptionWarn("Debe colocar el nombre del proyecto.");
            if(vacio(this.nuevoProyecto.getDescripcion())) throw new ExceptionWarn("Debe colocar una descripción del proyecto.");
            if(this.nuevoProyecto.getNCiclos() <= 0) throw new ExceptionWarn("Debe colocar un número de ciclos válido.");
            if(this.rlClInstructorNuevoProyecto.getUsuario() == null) throw new ExceptionWarn("Debe seleccionar un instructor.");
            if(criteriosNuevoProyecto.get(0) == null) throw new ExceptionWarn("Debe seleccionar el criterio de semana.");
            if(criteriosNuevoProyecto.get(1) == null) throw new ExceptionWarn("Debe seleccionar la forma de medida");
            if(this.imagenSubidaNuevoProyecto == null) throw new ExceptionWarn("No puede crear un proyecto sin logo");
            if(this.nuevoProyecto.getFEstFin() == null) throw new ExceptionWarn("Debe especificar la fecha estimada de finalización del proyecto.");
            if(faltaSeleccionarCriterio()) throw new ExceptionWarn("Todos los criterios deben ser seleccionados");
            if(!GestorArchivos.tipoArchivo(GestorArchivos.extension(this.imagenSubidaNuevoProyecto)).equals("imagen")){
                this.imagenSubidaNuevoProyecto = null;
                throw new ExceptionWarn("Debes subir una imagen.");
            }
            this.nuevoProyecto.setNombre(Helper.soloCaracteresSimples(this.nuevoProyecto.getNombre()));
            Date fechaHoy = this.fechaInicioEspecifica? this.fechaEspecificaInicioProyecto : new Date();
            Date fechaAux;
            if(this.fechaInicioEspecifica){
                fechaAux = new Date();
                fechaHoy.setHours(fechaAux.getHours());
                fechaHoy.setMinutes(fechaAux.getMinutes());
                fechaHoy.setSeconds(fechaAux.getSeconds());
            }
            if(fechaHoy == null) fechaHoy = new Date();
            Usuario usuarioSesion = ((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion();
            Set fases = new LinkedHashSet();
            Fase fase = new Fase();
            fase.setId(new FaseId((String) TSP.fases.get(0), b(1), this.nuevoProyecto.getNombre()));
            fase.setFInicio(fechaHoy);
            fases.add(fase);
            Set ciclos = new LinkedHashSet();
            Ciclo ciclo = new Ciclo();
            CicloId id;
            ciclo.setId(new CicloId(b(1), null));
            ciclo.setFInicio(fechaHoy);
            id = ciclo.getId();
            id.setProyecto(this.nuevoProyecto.getNombre());
            ciclo.setFases(fases);
            ciclos.add(ciclo);
            String extension = GestorArchivos.extension(this.imagenSubidaNuevoProyecto);
            String puntoExtension = ".";
            if(extension == "") puntoExtension = "";
            String nombreProyectoUnido = unir(this.nuevoProyecto.getNombre());
            String rutaCarpetaWebAplicacion = (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/")).replace("\\", "/");
            String rutaLogosProyectos = rutaCarpetaWebAplicacion + "/resources/proyectos";
            String rutaImg = rutaLogosProyectos + "/" + nombreProyectoUnido + "/logo" + puntoExtension + extension;
            this.nuevoProyecto.setCiclos(ciclos);
            this.nuevoProyecto.setFInicio(fechaHoy);
            this.nuevoProyecto.setPyCrs(consolidarCriterios());
            this.nuevoProyecto.setUsuario(usuarioSesion);
            this.nuevoProyecto.setUrlImg(nombreProyectoUnido + "/logo" + puntoExtension + extension);
            ProyectoDAO.insertarProyecto(this.nuevoProyecto);//El trigger debe actualizar los siguientes dos valores en la base de datos
            GestorArchivos.crearCarpeta(rutaLogosProyectos + "/" + nombreProyectoUnido);
            GestorArchivos.crearArchivo(new File(rutaImg), this.imagenSubidaNuevoProyecto);
            //Para mantener un backup y/o alojar los datos en el proyecto durante el desarrollo,también se habilita esta parte para subir una copia de las imágenes
            //GestorArchivos.crearCarpeta(TSP.rootImgsRespaldo + nombreProyectoUnido);
            //GestorArchivos.crearArchivo(new File(TSP.rootImgsRespaldo + nombreProyectoUnido + "/logo" + puntoExtension + extension), this.imagenSubidaNuevoProyecto);
            this.imagenSubidaNuevoProyecto = null;
            this.nuevoProyecto.setCicloActual(b(1));
            this.nuevoProyecto.setFaseActual((String)TSP.fases.get(0));
            this.registrarRolInstructorNuevoProyecto();
            this.rlClInstructorNuevoProyecto = null;
            this.cargarNuevoProyecto();
            cargarProyectoSesion();
            cargarRolesSesion();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede crear el proyecto. " + e.getMessage());
        }
    }

    
    /**
     * Carga la información del último proyecto en el que el usuario está involucrado
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarProyectoSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            this.proyectoSesion = this.cargarUltimoProyectoUSesion(((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion());
            if (this.proyectoSesion != null) {
                this.cicloSesion = ProyectoDAO.consultarCiclo(this.proyectoSesion);
                this.faseSesion = ProyectoDAO.consultarFase(this.proyectoSesion);
                this.criteriosProyectoSesion = ProyectoDAO.consultarCriteriosProyecto(this.proyectoSesion);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede cargar el proyecto de la sesión. " + e.getMessage());
        }
    }
    
    /**
     * Obtiene el último proyecto en el que se encuentra el usuario que ha iniciado sesión
     * @param usuario
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private Proyecto cargarUltimoProyectoUSesion(Usuario usuario) throws ExceptionFatal, ExceptionWarn {
        try {
            Proyecto p1 = ProyectoDAO.consultarUltimoProyectoCreadoPor(usuario);
            Proyecto p2 = ProyectoDAO.consultarUltimoProyectoUsuarioIntegrante(usuario);
            if (p1 == null && p2 == null) return null;
            if (p1 == null && p2 != null) return p2;
            if (p1 != null && p2 == null) return p1;
            if (p1.getNombre().equals(p2.getNombre())) return p1;
            if (p1.getFInicio().getTime() < p2.getFInicio().getTime()) return p2;
            return p1;
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Actualiza los datos de los criterios del nuevo proyecto
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private Set consolidarCriterios() throws ExceptionFatal, ExceptionWarn {
            try{
            Set criterios = new LinkedHashSet();
            int t = this.criteriosNuevoProyecto.size();
            PyCr criterio;
            for (int i = 0; i < t; i++) {
                criterio = (PyCr) this.criteriosNuevoProyecto.get(i);
                criterio.getId().setProyecto(this.nuevoProyecto.getNombre());
                criterios.add(criterio);
            }
            return criterios;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede consolidar los criterios. " + e.getMessage());
        }
    }
    
    /**
     * Carga los criterios disponibles para un nuevo proyecto y los inserta en la base de datos en caso de no localizarlos
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private List cargarCriterios() throws ExceptionFatal, ExceptionWarn {
        try{
            if(ProyectoDAO.consultarCriterios().isEmpty())
                ProyectoDAO.insertarCriterios(TSP.criterios);
            return TSP.criterios;
        }catch(Exception e){
            throw new ExceptionFatal("Error al cargar los criterios. " + e.getMessage());
        }
    }
    
    /**
     * Agrega los criterios seleccionados por el usuario al nuevo proyecto a crear
     * @param criteriosCargados
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void cargarCriteriosNuevoProyecto(List criteriosCargados) throws ExceptionFatal, ExceptionWarn {
        try{
            this.criteriosNuevoProyecto = new ArrayList();
            int t = criteriosCargados.size();
            PyCrId id;
            PyCr pc;
            Criterio criterio;
            for (int i = 0; i < t; i++) {
                id = new PyCrId();
                criterio = ((Criterio) criteriosCargados.get(i));
                id.setCriterio(criterio.getNombre());
                pc = new PyCr(id, criterio, null, null);
                this.criteriosNuevoProyecto.add(pc);
            }            
        }catch(Exception e){
            throw new ExceptionFatal("Error al cargar los criterios del nuevo proyecto. " + e.getMessage());
        }
    }
    
    /**
     *  Brinda las opciones del criterio con base en el nombre
     * @param nomCriterio
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List getOpciones(String nomCriterio) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.criteriosNuevoProyecto.size();
            for (int i = 0; i < t; i++)
                if (((PyCr) this.criteriosNuevoProyecto.get(i)).getCriterio().getNombre().equals(nomCriterio))
                    return (List) this.opcionesCriteriosNuevoProyecto.get(i);
            return new ArrayList();
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede obtener las opciones del criterio " + nomCriterio + ". " + e.getMessage());
        }
    }
    
    /**
     * Carga la lista de proyectos del usuario actualmente en sesión
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarProyectosUsuarioSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            this.proyectosUsuarioSesion = this.consultarProyectosDeUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion());
            int t = this.proyectosUsuarioSesion.size();
            String nomProyectoSesion = this.proyectoSesion.getNombre();
            Proyecto p;
            for (int i = 0; i < t; i++) {
                p = (Proyecto) this.proyectosUsuarioSesion.get(i);
                if (p.getNombre().equals(nomProyectoSesion)) {
                    this.proyectoSesion = p;
                    break;
                }
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede cargar el proyecto del usuario en sesión. " + e.getMessage());
        }
    }

    public void consultarProyectosUsuarioSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            this.proyectosUsuarioSesion = this.consultarProyectosDeUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion());
            this.seleccionProyectosUsuarioSesion = new ArrayList();
            int t = this.proyectosUsuarioSesion.size();
            for(int i = 0; i < t; i++) this.seleccionProyectosUsuarioSesion.add(false);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede cargar el proyecto del usuario en sesión. " + e.getMessage());
        }
    }
    
    public List<Proyecto> proyectosUsuarioSesionSeleccionados() throws ExceptionFatal, ExceptionWarn {
        try{
            List<Proyecto> resultado =  new ArrayList();
            int t = this.proyectosUsuarioSesion.size();
            Boolean seleccionado;
            for(int i = 0; i < t; i++){
                seleccionado = this.seleccionProyectosUsuarioSesion.get(i);
                if(seleccionado != null)
                    if(seleccionado)
                        resultado.add((Proyecto)this.proyectosUsuarioSesion.get(i));
            }
            return resultado;
        }catch(Exception e){
            throw new ExceptionFatal("No se pueden obtener los proyectos del usuario que han sido seleccionados. " + e.getMessage());
        }
        
    }
    
    
    
    /**
     * Consulta los proyectos del usuario actualmente en sesión
     * @param usuario
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private List consultarProyectosDeUsuario(Usuario usuario) throws ExceptionFatal, ExceptionWarn {
        try {
            List p1 = ProyectoDAO.consultarProyectosCreadosPor(usuario);
            List p2 = ProyectoDAO.consultarProyectosUsuarioIntegrante(usuario);
            int t = p1.size();
            int t2 = p2.size();
            Proyecto proyecto;
            boolean encontrado;
            for (int i = 0; i < t2; i++) {
                proyecto = ((Proyecto) p2.get(i));
                encontrado = false;
                for (int j = 0; j < t; j++)
                    if (((Proyecto) p1.get(j)).getNombre().equals(proyecto.getNombre())) {
                        encontrado = true;
                        break;
                    }
                if (!encontrado) p1.add(proyecto);
            }
            return p1;
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Carga los roles asociados al proyecto del usuario en sesión
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarRolesSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            if (this.proyectoSesion != null) {
                this.usuariosRolesCicloActual = ProyectoDAO.consultarUsuariosRolCicloActual(this.proyectoSesion);
                Usuario usuarioSesion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion();
                try {
                    RlCl rc = new RlCl();
                    RlClId id = new RlClId();
                    id.setProyecto(this.proyectoSesion.getNombre());
                    id.setUsuario(usuarioSesion.getIdentificacion());
                    id.setNCiclo(this.proyectoSesion.getCicloActual());
                    rc.setId(id);
                    this.rolesUSesion = ProyectoDAO.consultarRolesDeUsuario(rc);
                } catch (Exception e) {}
            }
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede cargar los roles del usuario en sesión. " + e.getMessage());
        }
    }

    /**
     * Obtiene la opción seleccionada del criterio del proyecto en sesión de acuerdo al criterio a consultar
     * @param criterio
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String valorDeCriterio(String criterio) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.criteriosProyectoSesion.size();
            PyCr pycr;
            for (int i = 0; i < t; i++) {
                pycr = (PyCr) this.criteriosProyectoSesion.get(i);
                if (pycr.getId().getCriterio().equals(criterio))
                    return pycr.getValor();
            }
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede validar el criterio " + criterio + ". " + e.getMessage());
        }
    }
    /**
     * Información de proyecto-ciclo-fase
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String getInformacionProyectoSesion() throws ExceptionFatal, ExceptionWarn {
        try {
            int c = this.proyectoSesion.getCicloActual();
            String f = this.proyectoSesion.getFaseActual();
            //MADNESS
            return Integer.toString(c) + "," + f;
        } catch (Exception e) {
            throw new ExceptionWarn("No has iniciado el proyecto. " + e.getMessage());
        }
    }

    /**
     * Emtrega el color del encabezado de acuerdo a la fase actual del proyecto en sesión
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String getColorFase() throws ExceptionFatal, ExceptionWarn {
        try {
            String faseActual = this.proyectoSesion.getFaseActual();
            int t = TSP.fases.size();
            for (int i = 0; i < t; i++)
                if (((String) TSP.fases.get(i)).equals(faseActual))
                    return (String) TSP.coloresFases.get(i);
            return null;
        } catch (Exception e) {
            throw new ExceptionFatal("Error al identificar el color de la fase actual. " + e.getMessage());
        }
    }
    
    /**
     * Identifica si entre los roles de la sesión se encuentra el rol enviado
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean poseeRolCicloActual(String rol) throws ExceptionFatal, ExceptionWarn {
        try {
            int t = this.rolesUSesion.size();
            for (int i = 0; i < t; i++)
                if (rol.equals((String) this.rolesUSesion.get(i)))
                    return true;
            return false;
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede determinar si el usuario tiene el rol + " + rol + ". " + e.getMessage());
        }
    }
    
    /**
     * Registra el rol de instructor para el nuevo proyecto creado
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void registrarRolInstructorNuevoProyecto() throws ExceptionFatal, ExceptionWarn {
        try{
            if(ProyectoDAO.consultarRol(TSP.instructor.getNombre()) == null)
                ProyectoDAO.insertarRolTSP(TSP.instructor);
            RlClId id = new RlClId();
            id.setNCiclo(this.nuevoProyecto.getCicloActual());
            id.setProyecto(this.nuevoProyecto.getNombre());
            id.setRol(TSP.instructor.getNombre());
            id.setUsuario(this.rlClInstructorNuevoProyecto.getUsuario().getIdentificacion());
            this.rlClInstructorNuevoProyecto.setId(id);
            this.rlClInstructorNuevoProyecto.setEstado("Habilitado");
            List roles = new ArrayList();
            roles.add(this.rlClInstructorNuevoProyecto);
            ProyectoDAO.insertarRolesCiclo(roles);
        }catch(Exception e){
            throw new ExceptionFatal("Error al registrar el rol de instructor. " + e.getMessage());
        }
    }
    
    /**
     * Valida y actualiza la fecha de finalización del proyecto ingresada por el usuario a crear el proyecto
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarFechaEstimadaFinNuevoProyecto() throws ExceptionFatal, ExceptionWarn {
        try{
            Calendar hoyCalendar = Calendar.getInstance();
            hoyCalendar.add(Calendar.DATE, 7);
            Date fechaMinima = hoyCalendar.getTime();
            if(this.nuevoProyecto.getFEstFin().before(fechaMinima)){
                this.nuevoProyecto.setFEstFin(fechaMinima);
                throw new ExceptionWarn("Debe colocar una fecha válida");
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al registrar el rol de instructor. " + e.getMessage());
        }
    }
    
    /**
     * Permite consultar el instructor del nuevo proyecto para validar su existencia
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void consultarInstructor() throws ExceptionFatal, ExceptionWarn {
        try{
            rlClInstructorNuevoProyecto.setUsuario(UsuarioDAO.consultarUsuarioSistema(this.instructor));
            if(rlClInstructorNuevoProyecto.getUsuario() == null){
                String identificacion = this.instructor.getIdentificacion();
                this.instructor.setIdentificacion(null);
                throw new ExceptionWarn("El usuario con la identificación " + identificacion + " no se encuentra registrado.");
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al registrar el rol de instructor. " + e.getMessage());
        }
    }
    
    public void cargarModalPasarSiguienteFaseOCiclo() throws ExceptionFatal, ExceptionWarn {
        try{
            if(!((ProyectoControl)getControlador("ProyectoControl")).poseeRolCicloActual(((Rol)TSP.rolesTSP.get(0)).getNombre()))
                throw new ExceptionWarn("Sólo el Líder de proyecto tiene permitido pasar a la siguiente fase/ciclo");
            String faseActual = this.proyectoSesion.getFaseActual();
            this.criteriosSalida = new ArrayList();
            this.validacionCriteriosSalida = new ArrayList();
            //Se verifica si la fase actual es la última del ciclo para pasar al siguiente o terminar
            if(faseActual.equals(TSP.fases.get(TSP.fases.size() - 1))){
                this.siguienteCicloFase = "ciclo";
                //Si el ciclo actual es el último estimado, el proyecto debe ser finalizado
                if(this.proyectoSesion.getCicloActual() == this.proyectoSesion.getNCiclos()){
                    this.criteriosSalida.add(new Atributo("Todo el proyecto finalizado","manual"));
                    this.validacionCriteriosSalida.add(false);
                }else llenarCriteriosSalida(faseActual);
            }else {
                this.siguienteCicloFase = "fase";
                llenarCriteriosSalida(faseActual);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede cargar el modal de la siguiente fase/ciclo " + e.getMessage());
        }
    }
    
    private void llenarCriteriosSalida(String fase)  throws ExceptionFatal, ExceptionWarn {
        if(((String)TSP.fases.get(0)).equals(fase)){//Fase de lanzamiento
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(0),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(1),"automático"));
            this.validacionCriteriosSalida.add(todosRolesAsignados());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(2),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(3),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(4),"manual"));
            this.validacionCriteriosSalida.add(false);
        } else if(((String)TSP.fases.get(1)).equals(fase)){//Fase de estrategia
            boolean stratGuardado = !(formatoFachada().consultarFormatos("strat").isEmpty());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(5),"automático"));
            this.validacionCriteriosSalida.add(stratGuardado);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(6),"automático"));
            this.validacionCriteriosSalida.add(stratGuardado);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(7),"automático"));
            this.validacionCriteriosSalida.add(stratGuardado);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(8),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(9),"automático"));
            this.validacionCriteriosSalida.add(!(formatoFachada().consultarFormatos("itl").isEmpty()));
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(10),"automático"));
            this.validacionCriteriosSalida.add(stratGuardado);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(11),"manual"));
            this.validacionCriteriosSalida.add(false);
        } else if(((String)TSP.fases.get(2)).equals(fase)){//Fase de planeación
            boolean taskGuardado = !(formatoFachada().consultarFormatos("task").isEmpty());
            boolean scheduleGuardado = !(formatoFachada().consultarFormatos("schedule").isEmpty());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(12),"automático"));
            this.validacionCriteriosSalida.add(taskGuardado && scheduleGuardado);
            boolean sumpGuardado = !(formatoFachada().consultarFormatos("sump").isEmpty());
            boolean sumqGuardado = !(formatoFachada().consultarFormatos("sumq").isEmpty());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(13),"automático"));
            this.validacionCriteriosSalida.add(sumpGuardado && sumqGuardado);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(14),"manual"));
            this.validacionCriteriosSalida.add(false);
        } else if(((String)TSP.fases.get(3)).equals(fase)){//Fase de requerimientos
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(15),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(16),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(17),"manual"));
            this.validacionCriteriosSalida.add(false);
        } else if(((String)TSP.fases.get(4)).equals(fase)){//Fase de diseño
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(18),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(19),"manual"));
            this.validacionCriteriosSalida.add(false);
            boolean sumpGuardado = !(formatoFachada().consultarFormatos("sump").isEmpty());
            boolean sumqGuardado = !(formatoFachada().consultarFormatos("sumq").isEmpty());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(20),"automático"));
            this.validacionCriteriosSalida.add(sumpGuardado && sumqGuardado);
        } else if(((String)TSP.fases.get(5)).equals(fase)){//Fase de implementación
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(21),"manual"));
            this.validacionCriteriosSalida.add(false);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(22),"manual"));
            this.validacionCriteriosSalida.add(false);
            boolean sumpGuardado = !(formatoFachada().consultarFormatos("sump").isEmpty());
            boolean logtGuardado = !(formatoFachada().consultarFormatos("logt").isEmpty());
            boolean logdGuardado = !(formatoFachada().consultarFormatos("logd").isEmpty());
            boolean sumqGuardado = !(formatoFachada().consultarFormatos("sumq").isEmpty());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(23),"automática"));
            this.validacionCriteriosSalida.add(sumpGuardado && logtGuardado && logdGuardado && sumqGuardado);
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(24),"manual"));
                this.validacionCriteriosSalida.add(false);
        } else if(((String)TSP.fases.get(6)).equals(fase)){//Fase de prueba
            boolean logdGuardado = !(formatoFachada().consultarFormatos("logd").isEmpty());
            boolean itlGuardado = !(formatoFachada().consultarFormatos("itl").isEmpty());
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(25),"automática"));
            this.validacionCriteriosSalida.add(logdGuardado && itlGuardado);
        } else if(((String)TSP.fases.get(7)).equals(fase)){//Postmortem
            this.criteriosSalida.add(new Atributo((String)TSP.criteriosSalida.get(26),"automática"));
            this.validacionCriteriosSalida.add(!(formatoFachada().consultarFormatos("peer").isEmpty()));
        }
    }
    
    private boolean todosRolesAsignados() throws ExceptionFatal,ExceptionWarn {
        try{
            List rolesCiclo = ProyectoDAO.consultarRolesCiclo(this.proyectoSesion);
            int t = rolesCiclo.size();
            int t2 =TSP.rolesTSP.size();
            String rol;
            int cantds[] = new int[t2];
            for(int i = 0; i < t; i++){
                rol = ((RlCl)rolesCiclo.get(i)).getId().getRol();
                for(int j = 0; j < t2; j++)
                if((((Rol)TSP.rolesTSP.get(j)).getNombre()).equals(rol)){
                    cantds[j] += 1;
                    break;
                }
            }
            for(int i = 0; i < t2; i++)
                if(cantds[i] == 0)
                    return false;
            return true;
        }catch(Exception e){
            throw new ExceptionFatal("Error al verificar que todos los roles fueran asignados. " + e.getMessage());
        }
    }
    
    public String irSiguienteCicloOFase() throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.validacionCriteriosSalida.size();
            for(int i = 0; i < t; i++)
                if(!this.validacionCriteriosSalida.get(i)) throw new ExceptionWarn("Todos los criterios deben cumplirse");
            Date fechaHoy = new Date();
            String faseActual = this.proyectoSesion.getFaseActual();
            if(this.siguienteCicloFase.equals("fase")){
                Fase nuevaFase = new Fase();
                FaseId id = new FaseId();
                id.setNCiclo(this.proyectoSesion.getCicloActual());
                id.setProyecto(this.proyectoSesion.getNombre());
                int t2 =  TSP.fases.size();
                for(int i = 0; i < t2 - 1; i++)
                    if(faseActual.equals((String)TSP.fases.get(i))){
                        id.setNombre((String)TSP.fases.get(i + 1));
                        break;
                    }
                nuevaFase.setId(id);
                nuevaFase.setFInicio(fechaHoy);
                this.faseSesion.setFFin(fechaHoy);
                ProyectoDAO.actualizarFase(this.faseSesion);
                ProyectoDAO.insertarFase(nuevaFase);
                cargarProyectoSesion();
                cargarRolesSesion();
                return "Has comenzado una nueva fase en forma satisfactoria";
            }else{
                this.cicloSesion.setFFin(fechaHoy);
                this.faseSesion.setFFin(fechaHoy);
                this.proyectoSesion.setFFin(fechaHoy);
                ProyectoDAO.actualizarFase(this.faseSesion);
                ProyectoDAO.actualizarCiclo(this.cicloSesion);
                if(this.cicloSesion.getId().getNCiclo() == this.proyectoSesion.getNCiclos())
                    return "PROYECTO FINALIZADO EXITOSAMENTE!!! Gracias por utilizar la herramienta";
                Fase nuevaFase = new Fase();
                FaseId id = new FaseId();
                id.setNCiclo(b(this.proyectoSesion.getCicloActual() + 1));
                id.setProyecto(this.proyectoSesion.getNombre());
                id.setNombre((String)TSP.fases.get(0));
                nuevaFase.setId(id);
                nuevaFase.setFInicio(fechaHoy);
                Ciclo nuevoCiclo = new Ciclo();
                CicloId idciclo = new CicloId();
                idciclo.setNCiclo(b(proyectoSesion.getCicloActual() + 1));
                idciclo.setProyecto(proyectoSesion.getNombre());
                nuevoCiclo.setId(idciclo);
                nuevoCiclo.setFInicio(fechaHoy);
                Set fases = new LinkedHashSet();
                fases.add(nuevaFase);
                nuevoCiclo.setFases(fases);
                ProyectoDAO.insertarCiclo(nuevoCiclo);
                List rlcls = ProyectoDAO.consultarUsuariosRolCicloActual(proyectoSesion);
                int x = rlcls.size();
                String instructor = null;
                for(int y = 0; y < x; y++)
                    if(((RlCl)rlcls.get(y)).getId().getRol().equals(TSP.instructor.getNombre())){
                        instructor = ((RlCl)rlcls.get(y)).getId().getUsuario();
                        break;
                    }
                RlClId rlclid = new RlClId();
                rlclid.setNCiclo(b(this.proyectoSesion.getCicloActual() + 1));
                rlclid.setProyecto(this.proyectoSesion.getNombre());
                rlclid.setRol(TSP.instructor.getNombre());
                rlclid.setUsuario(instructor);
                RlCl nuevoRlCl = new RlCl();
                nuevoRlCl.setId(rlclid);
                nuevoRlCl.setEstado("Habilitado");
                List roles = new ArrayList();
                roles.add(nuevoRlCl);
                ProyectoDAO.insertarRolesCiclo(roles);
                cargarProyectoSesion();
                cargarRolesSesion();
                return "Has comenzado un nuevo ciclo en forma satisfactoria";
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede dar paso al siguiente ciclo/fase. " + e.getMessage());
        }
    }
    
    
    public Proyecto getProyectoSesion() {
        return proyectoSesion;
    }

    public void setProyectoSesion(Proyecto proyectoSesion) {
        this.proyectoSesion = proyectoSesion;
    }

    public Ciclo getCicloSesion() {
        return cicloSesion;
    }

    public void setCicloSesion(Ciclo cicloSesion) {
        this.cicloSesion = cicloSesion;
    }

    public Fase getFaseSesion() {
        return faseSesion;
    }

    public void setFaseSesion(Fase faseSesion) {
        this.faseSesion = faseSesion;
    }

    public List getRolesUSesion() {
        return rolesUSesion;
    }

    public void setRolesUSesion(List rolesUSesion) {
        this.rolesUSesion = rolesUSesion;
    }

    public List getCriteriosProyectoSesion() {
        return criteriosProyectoSesion;
    }

    public void setCriteriosProyectoSesion(List criteriosProyectoSesion) {
        this.criteriosProyectoSesion = criteriosProyectoSesion;
    }

    public Proyecto getNuevoProyecto() {
        return nuevoProyecto;
    }

    public void setNuevoProyecto(Proyecto nuevoProyecto) {
        this.nuevoProyecto = nuevoProyecto;
    }

    public RlCl getRlClInstructorNuevoProyecto() {
        return rlClInstructorNuevoProyecto;
    }

    public void setRlClInstructorNuevoProyecto(RlCl rlClInstructorNuevoProyecto) {
        this.rlClInstructorNuevoProyecto = rlClInstructorNuevoProyecto;
    }


    public List getCriteriosNuevoProyecto() {
        return criteriosNuevoProyecto;
    }

    public void setCriteriosNuevoProyecto(List criteriosNuevoProyecto) {
        this.criteriosNuevoProyecto = criteriosNuevoProyecto;
    }

    public UploadedFile getImagenSubidaNuevoProyecto() {
        return imagenSubidaNuevoProyecto;
    }

    public void setImagenSubidaNuevoProyecto(UploadedFile imagenSubidaNuevoProyecto) {
        this.imagenSubidaNuevoProyecto = imagenSubidaNuevoProyecto;
    }

    public List getProyectosUsuarioSesion() {
        return proyectosUsuarioSesion;
    }

    public void setProyectosUsuarioSesion(List proyectosUsuarioSesion) {
        this.proyectosUsuarioSesion = proyectosUsuarioSesion;
    }

    public List getUsuariosRolesCicloActual() {
        return usuariosRolesCicloActual;
    }

    public void setUsuariosRolesCicloActual(List usuariosRolesCicloActual) {
        this.usuariosRolesCicloActual = usuariosRolesCicloActual;
    }
    
    public List getFasesTSP() {
        return TSP.fases;
    }

    public List getOpcionesCriteriosNuevoProyecto() {
        return opcionesCriteriosNuevoProyecto;
    }

    public void setOpcionesCriteriosNuevoProyecto(List opcionesCriteriosNuevoProyecto) {
        this.opcionesCriteriosNuevoProyecto = opcionesCriteriosNuevoProyecto;
    }

    public Usuario getInstructor() {
        return instructor;
    }

    public void setInstructor(Usuario instructor) {
        this.instructor = instructor;
    }

    public List<Boolean> getValidacionCriteriosSalida() {
        return validacionCriteriosSalida;
    }

    public void setValidacionCriteriosSalida(List<Boolean> validacionCriteriosSalida) {
        this.validacionCriteriosSalida = validacionCriteriosSalida;
    }

    public List<Atributo> getCriteriosSalida() {
        return criteriosSalida;
    }

    public void setCriteriosSalida(List<Atributo> criteriosSalida) {
        this.criteriosSalida = criteriosSalida;
    }
    
    private FormatoFachada formatoFachada() throws ExceptionFatal {
        try {
            String clase = "b_controlador.b_fachada.FormatoFachada";
            FacesContext c = FacesContext.getCurrentInstance();
            String[] items = Helper.dividir(clase, ".");
            return (FormatoFachada)c.getApplication().evaluateExpressionGet(c, "#{" + Helper.primeraLetraMinus(items[items.length - 1]) + "}", Class.forName(clase));
        } catch (Exception e) {
            throw new ExceptionFatal("Error fatal al tratar de localizar FabricaFormatos. " + e.getMessage());
        }
    }
    
    private boolean faltaSeleccionarCriterio() throws ExceptionFatal {
        try{
            int t = this.criteriosNuevoProyecto.size();
            for(int i = 0; i < t; i++)
                if(((PyCr)this.criteriosNuevoProyecto.get(i)).getValor() == null) return true;
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("Error al verificar la selección de criterios del nuevo proyecto. " + e.getMessage());
        }
    }
    
    public String mensajeTooltipCriterio(String nombreCriterio) throws ExceptionFatal, ExceptionWarn {
        try{
            if(nombreCriterio.equals(((Criterio)TSP.criterios.get(0)).getNombre()))
                return (String) TSP.descripcionCriterios.get(0);
            if(nombreCriterio.equals(((Criterio)TSP.criterios.get(1)).getNombre()))
                return (String) TSP.descripcionCriterios.get(1);
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("Error al consultar la información para el criterio " + nombreCriterio +  ". " + e.getMessage());
        }
    }
    
    
    public boolean isRolesAasignados() throws ExceptionFatal, ExceptionWarn {
        try{
            return ProyectoDAO.consultarRolesCiclo(this.proyectoSesion).size() > 1;
        }catch(Exception e){
            throw new ExceptionFatal("ProyectoControl no puede verificar la asignación de roles para el ciclo. " + e.getMessage());
        }
    }
    
    public Usuario datosInstructor() throws ExceptionFatal, ExceptionWarn {
        try {
            if(this.instructor == null) return null;
            return UsuarioDAO.consultarUsuarioSistema(this.instructor);
        } catch (Exception e) {
            throw new ExceptionFatal("ProyectoControl no puede obtener los datos del instructor. " + e.getMessage());
        }
    }

    public boolean isFechaInicioEspecifica() {
        return fechaInicioEspecifica;
    }

    public void setFechaInicioEspecifica(boolean fechaInicioEspecifica) {
        this.fechaInicioEspecifica = fechaInicioEspecifica;
    }

    public Date getFechaEspecificaInicioProyecto() {
        return fechaEspecificaInicioProyecto;
    }

    public void setFechaEspecificaInicioProyecto(Date fechaEspecificaInicioProyecto) {
        this.fechaEspecificaInicioProyecto = fechaEspecificaInicioProyecto;
    }

    public List<Boolean> getSeleccionProyectosUsuarioSesion() {
        return seleccionProyectosUsuarioSesion;
    }

    public void setSeleccionProyectosUsuarioSesion(List<Boolean> seleccionProyectosUsuarioSesion) {
        this.seleccionProyectosUsuarioSesion = seleccionProyectosUsuarioSesion;
    }
    
    public String getUnidadTamanio(){
        return ((Criterio)TSP.criterios.get(1)).getNombre();
    }
    
    
    
    
}