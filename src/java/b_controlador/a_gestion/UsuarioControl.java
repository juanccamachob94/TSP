/**
 * Controlador de datos del usuario.
 */
package b_controlador.a_gestion;

import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import c_negocio.a_relacional.Usuario;
import d_datos.a_dao.UsuarioDAO;
import e_utilitaria.Serial;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;

@ManagedBean
@SessionScoped
public class UsuarioControl extends Control {

    private Usuario usuarioSesion;
    private boolean intentoInicio;
    private String confirmacionClave;
    private String claveValidacion;
    private Usuario usuarioActualizado;


    public UsuarioControl() {
        this.intentoInicio = false;
    }

    /**
     * Asigna una instancia de usuario la primera vez que se pone en marcha la aplicación
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void init() throws ExceptionFatal, ExceptionWarn {
        try{
            this.usuarioSesion = new Usuario();
        }catch(Exception e){
            throw new ExceptionFatal("" + e.getMessage());
        }
    }

    /**
     * Carga los datos de la página de inicio de sesión
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarUsuarioSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            if (!this.intentoInicio) this.usuarioSesion = new Usuario();
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede cargar el usuario de la sesión. " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos de registro y muesta el formulario del mismo alojado en el modal
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalRegistrar() throws ExceptionFatal, ExceptionWarn {
        try{
            this.usuarioSesion = new Usuario();
            setConfirmacionClave("");
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede cargar los datos de registro. " + e.getMessage());
        }
    }

    /**
    * Permite el registro del invitado al sistema.
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void registrar() throws ExceptionFatal, ExceptionWarn {
        try{
            if(UsuarioDAO.consultarUsuarioSistema(this.usuarioSesion) != null) throw new ExceptionWarn("El usuario " + usuarioSesion.getIdentificacion() + " ya está registrado");
            String c = this.usuarioSesion.getClave();
            this.usuarioSesion.setClave(DigestUtils.md5Hex(this.usuarioSesion.getClave()));
            UsuarioDAO.insertarUsuario(this.usuarioSesion);
            this.usuarioSesion.setClave(c);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede registrar al usuario. " + e.getMessage());
        }
    }

    /**
     * Permite al usuario acceder al sistema una vez se encuentra registrado y ha completado los datos.
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void iniciarSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            String identificacion = this.usuarioSesion.getIdentificacion();
            AccesoControl accesoControl = (AccesoControl) getControlador("AccesoControl");
            this.usuarioSesion.setClave(DigestUtils.md5Hex(this.usuarioSesion.getClave()));
            this.usuarioSesion = this.iniciarSesion(this.usuarioSesion);
            if (this.usuarioSesion.getIdentificacion().equals("")) {
                accesoControl.setAccesado(false);
                this.usuarioSesion.setIdentificacion(identificacion);
                throw new ExceptionWarn("Error en usuario y/o contraseña");
            } else {
                this.intentoInicio = true;
                accesoControl.setAccesado(true);
                ProyectoControl pc = ((ProyectoControl) getControlador("ProyectoControl"));
                pc.cargarProyectoSesion();
                pc.cargarRolesSesion();
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede iniciar sesión. " + e.getMessage());
        }
    }

    /**
     * Elimina todos los datos de la sesión y redirecciona a la página inicial.
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cerrarSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            ((AccesoControl) getControlador("AccesoControl")).setAccesado(false);
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede cerrar la sesión. " + e.getMessage());
        }
    }

    /**
     * Sobreescribe el usuario a iniciar sesión por los datos consultados. En caso de no encontrarlo
     * reestablece los valores.
     * @param usuario
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private Usuario iniciarSesion(Usuario usuario) throws ExceptionFatal, ExceptionWarn {
        try{
            usuario = UsuarioDAO.consultarUsuarioInicioSesion(usuario);
            if(usuario == null){
                usuario = new Usuario();
                usuario.setIdentificacion("");
                usuario.setClave("");
            }
            return usuario;
        }catch(Exception e){
            throw new ExceptionFatal("Error al consultar los datos de inicio de sesión. " + e.getMessage());
        }
    }    
    
    
    public void cargarValidacionClave() throws ExceptionFatal, ExceptionWarn {
        try{
            this.claveValidacion = null;
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede cargar los datos de la clave. " + e.getMessage());
        }    
    }
    
    public void validarClaveUsuarioSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            if(!DigestUtils.md5Hex(this.claveValidacion).equals(this.usuarioSesion.getClave())){
                this.claveValidacion = null;
                throw new ExceptionWarn("La clave es incorrecta, intente nuevamente");
            }
            this.confirmacionClave = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede validar la clave del usuario. " + e.getMessage());
        }    
    }
    
    public void cambiarClaveUsuarioSesion() throws ExceptionFatal, ExceptionWarn {
        try{
            if(!this.usuarioActualizado.getClave().equals(this.confirmacionClave)) throw new ExceptionWarn("Las claves no coinciden");
            this.usuarioSesion.setClave(DigestUtils.md5Hex(this.confirmacionClave));
            UsuarioDAO.actualizarUsuario(usuarioSesion);
            this.claveValidacion = null;
            this.usuarioActualizado.setClave(null);
            this.confirmacionClave = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede cambiar la clave del usuario. " + e.getMessage());
        }    
    }
    
    
    public void cargarDatosUsuarioActualizado() throws ExceptionFatal, ExceptionWarn {
        try{
            this.usuarioActualizado = new Usuario();
            this.usuarioActualizado.setIdentificacion(this.usuarioSesion.getIdentificacion());
            this.usuarioActualizado.setNombre(this.usuarioSesion.getNombre());
            this.usuarioActualizado.setApellido(this.usuarioSesion.getApellido());
            this.usuarioActualizado.setEmail(this.usuarioSesion.getEmail());
            this.usuarioActualizado.setTelefono(this.usuarioSesion.getTelefono());
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede cargar los datos del perfil. " + e.getMessage());
        }
    }
    
    public void actualizarDatos() throws ExceptionFatal, ExceptionWarn {
        try{
            if(vacio(this.usuarioActualizado.getNombre())) throw new ExceptionWarn("El nombre es obligatorio");
            if(vacio(this.usuarioActualizado.getApellido())) throw new ExceptionWarn("El apellido es obligatorio");
            if(vacio(this.usuarioActualizado.getEmail())) throw new ExceptionWarn("El email es obligatorio");
            if(!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(this.usuarioActualizado.getEmail()).find())
                throw new ExceptionWarn("Email no válido");
            this.usuarioSesion.setNombre(this.usuarioActualizado.getNombre());
            this.usuarioSesion.setApellido(this.usuarioActualizado.getApellido());
            this.usuarioSesion.setEmail(this.usuarioActualizado.getEmail());
            UsuarioDAO.actualizarUsuario(this.usuarioSesion);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("UsuarioControl no puede actualizar los datos del usuario. " + e.getMessage());
        }
    }
    
    
    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public String getConfirmacionClave() {
        return confirmacionClave;
    }

    public void setConfirmacionClave(String confirmacionClave) {
        this.confirmacionClave = confirmacionClave;
    }

    public String getClaveValidacion() {
        return claveValidacion;
    }

    public void setClaveValidacion(String claveValidacion) {
        this.claveValidacion = claveValidacion;
    }

    public Usuario getUsuarioActualizado() {
        return usuarioActualizado;
    }

    public void setUsuarioActualizado(Usuario usuarioActualizado) {
        this.usuarioActualizado = usuarioActualizado;
    }
    
    
    
    
    
}