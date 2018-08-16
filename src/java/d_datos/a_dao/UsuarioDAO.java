/**
 * Gestión de los datos persistentes relacionados con los usuarios del sistema
 */
package d_datos.a_dao;

import java.util.List;
import c_negocio.a_relacional.Usuario;
import d_datos.b_fabrica_conexiones.FabricaConexiones;
import e_utilitaria.ExceptionFatal;

public class UsuarioDAO {

    /**
     * Consulta un usuario basado en su identificacion y su clave.
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static Usuario consultarUsuarioInicioSesion(Usuario usuario) throws ExceptionFatal {
        try {
            return (Usuario) FabricaConexiones.conectar("postgres").consultarObjeto("select u from Usuario u where u.identificacion = '" + usuario.getIdentificacion() + "' and u.clave = '" + usuario.getClave() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal("Error al consutar el usuario para el inicio de sesión. " + e.getMessage());
        }
    }
    
    /**
     * Consulta el usuario con base en su identificación
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static Usuario consultarUsuarioSistema(Usuario usuario) throws ExceptionFatal {
        try {
            return (Usuario) FabricaConexiones.conectar("postgres").consultarObjeto("select u from Usuario u where u.identificacion = '" + usuario.getIdentificacion() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal("Error al cosultar los usuarios del sistema. " + e.getMessage());
        }
    }

    /**
     * Añade el usuario al sistema persistente
     * @param usuario
     * @throws ExceptionFatal 
     */
    public static void insertarUsuario(Usuario usuario) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(usuario);
        } catch (Exception e) {
            throw new ExceptionFatal("Error al insertar el usuario al sistema. " + e.getMessage());
        }

    }

    public static void actualizarUsuario(Usuario usuario) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").actualizar(usuario);
        } catch (Exception e) {
            throw new ExceptionFatal("Error al actualizar el usuario. " + e.getMessage());
        }
    }
    
    /**
     * Consulta todos los usuarios registrados en el sistema
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarTodos() throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select u from Usuario u");
        } catch (Exception e) {
            throw new ExceptionFatal("Error al consultar todos los usuarios. " + e.getMessage());
        }
    }


}
