/**
 * Gestión de los datos persistentes relacionados con los proyectos
 */
package d_datos.a_dao;

import c_negocio.a_relacional.Ciclo;
import c_negocio.a_relacional.CicloId;
import c_negocio.a_relacional.Documento;
import c_negocio.a_relacional.Fase;
import c_negocio.a_relacional.Meta;
import c_negocio.a_relacional.Parte;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import c_negocio.a_relacional.Rol;
import c_negocio.a_relacional.Usuario;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.TSP;
import java.util.List;
import d_datos.b_fabrica_conexiones.FabricaConexiones;

public class ProyectoDAO {

    //===================================[ROLES|INICIO]===============================================
    
    /**
     * Permite registrar los roles; vínculo entre Rol-Usuario-Ciclo
     * @param rolesCiclo
     * @throws ExceptionFatal 
     */
    public static void insertarRolesCiclo(List rolesCiclo) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(rolesCiclo);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta los usuarioRol para el ciclo actual del proyecto
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarUsuariosRolCicloActual(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select rc from RlCl rc where rc.id.proyecto = '" + proyecto.getNombre() + "' and rc.id.NCiclo = " + proyecto.getCicloActual());
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Obtiene los roles de un usuario en un ciclo
     * @param rc
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarRolesDeUsuario(RlCl rc) throws ExceptionFatal {
        try {
            RlClId id = rc.getId();
            return FabricaConexiones.conectar("postgres").consultarLista("select r.nombre from Rol r, RlCl rc where rc.id.rol = r.nombre and rc.id.proyecto = '" + id.getProyecto() + "' and rc.id.usuario = '" + id.getUsuario() + "' and rc.id.NCiclo = " + Integer.toString(id.getNCiclo()));
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Obtiene los usuarios que tienen un rol específico en el proyecto-ciclo
     * @param rc
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarUsuariosRol(RlCl rc) throws ExceptionFatal {
        try {
            RlClId id = rc.getId();
            return FabricaConexiones.conectar("postgres").consultarLista("select u.identificacion from Usuario u, RlCl rc where rc.id.usuario = u.identificacion and rc.id.proyecto = '" + id.getProyecto() + "' and rc.id.rol = '" + id.getRol() + "' and rc.id.NCiclo = " + Integer.toString(id.getNCiclo()));
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Obtiene los roles asignados para el proyecto en el ciclo actual
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarRolesCiclo(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select rlcl from RlCl rlcl where rlcl.id.NCiclo = " + proyecto.getCicloActual() + " and rlcl.id.proyecto = '" + proyecto.getNombre() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }        
    }

    /**
     * Elimina los roles de TSP para el proyecto en su ciclo actual
     * @param proyecto
     * @throws ExceptionFatal 
     */
    public static void eliminarRolesTSPCiclo(Proyecto proyecto) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").eliminar("delete from RlCl rlcl where rlcl.id.proyecto = '"+proyecto.getNombre()+"' and rlcl.id.NCiclo = " + proyecto.getCicloActual() + " and rlcl.id.rol != '"+TSP.instructor.getNombre()+"'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Obtiene el rol con base en el nombre
     * @param nombreRol
     * @return
     * @throws ExceptionFatal 
     */
    public static Rol consultarRol(String nombreRol) throws ExceptionFatal {
        try {
            return (Rol) FabricaConexiones.conectar("postgres").consultarObjeto("select r from Rol r where r.nombre = '" + nombreRol + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Permite insertar los roles de la metodología TSP
     * @param roles
     * @throws ExceptionFatal 
     */
    public static void insertarRolesTSP(List roles) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(roles);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Inserta un rol de TSP
     * @param rol
     * @throws ExceptionFatal 
     */
    public static void insertarRolTSP(Rol rol) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(rol);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Obtiene los roles de TSP
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarRolesTSP() throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select r from Rol r");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    //===================================[ROLES|FIN]===============================================
    
    //===================================[PROYECTO|INICIO]===============================================

    /**
     * Permite registrar el proyecto en la base de datos
     * @param proyecto
     * @throws ExceptionFatal 
     */
    public static void insertarProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(proyecto);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Sobreescribe el proyecto; depende de la llave primaria (nombre)
     * @param proyecto
     * @throws ExceptionFatal 
     */
    public static void actualizarProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").actualizar(proyecto);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Consulta el último proyecto en el que el usuario es integrante
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static Proyecto consultarUltimoProyectoUsuarioIntegrante(Usuario usuario) throws ExceptionFatal {
        try {
            String identificacion = usuario.getIdentificacion();
            return (Proyecto) FabricaConexiones.conectar("postgres").consultarObjeto("select p from Proyecto p where p.nombre in(select rc.id.proyecto from RlCl rc where rc.id.usuario = '" + identificacion + "' group by rc.id.proyecto) and p.FInicio = (select MAX(p.FInicio) from Proyecto p where p.nombre in(SELECT rc.id.proyecto from RlCl rc where rc.id.usuario = '" + identificacion + "' group by rc.id.proyecto))");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta el último proyecto que ha creado el usuario
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static Proyecto consultarUltimoProyectoCreadoPor(Usuario usuario) throws ExceptionFatal {
        try {
            String identificacion = usuario.getIdentificacion();
            return (Proyecto) FabricaConexiones.conectar("postgres").consultarObjeto("select p from Proyecto p where p.usuario.identificacion = '" + identificacion + "' and p.FInicio = (select max(p.FInicio) from Proyecto p where p.usuario.identificacion = '" + identificacion + "')");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta los proyectos en los que el usuario es integrante
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarProyectosUsuarioIntegrante(Usuario usuario) throws ExceptionFatal {
        try {
            String identificacion = usuario.getIdentificacion();
            return FabricaConexiones.conectar("postgres").consultarLista("select p from Proyecto p where p.nombre in(select rc.id.proyecto from RlCl rc where rc.id.usuario = '" + identificacion + "' group by rc.id.proyecto) order by p.FInicio");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta los proyectos creados por el usuario
     * @param usuario
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarProyectosCreadosPor(Usuario usuario) throws ExceptionFatal {
        try {
            String identificacion = usuario.getIdentificacion();
            return FabricaConexiones.conectar("postgres").consultarLista("select p from Proyecto p where p.usuario.identificacion = '" + identificacion + "' order by p.FInicio");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta el proyecto a partir de su nombre
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static Proyecto consultarProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            return (Proyecto) FabricaConexiones.conectar("postgres").consultarObjeto("select p from Proyecto p where p.nombre = '" + proyecto.getNombre() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    //===================================[PROYECTO|FIN]===============================================

    //===================================[CICLO|INICIO]===============================================

    /**
     * Consulta el ciclo con base en el proyecto y su ciclo actual
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static Ciclo consultarCiclo(Proyecto proyecto) throws ExceptionFatal {
        try {
            return (Ciclo) FabricaConexiones.conectar("postgres").consultarObjeto("select c from Ciclo c where c.id.proyecto = '" + proyecto.getNombre() + "' and c.id.NCiclo = " + proyecto.getCicloActual() + "");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    


    public static void insertarCiclo(Ciclo ciclo) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(ciclo);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static void actualizarCiclo(Ciclo ciclo) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").actualizar(ciclo);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    //===================================[CICLO|FIN]===============================================

    //===================================[FASE|INICIO]===============================================
    
    public static void insertarFase(Fase fase) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(fase);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta la fase con base en el proyecto y su fase actual
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static Fase consultarFase(Proyecto proyecto) throws ExceptionFatal {
        try {
            return (Fase) FabricaConexiones.conectar("postgres").consultarObjeto("select f from Fase f where f.id.proyecto = '" + proyecto.getNombre() + "' and f.id.NCiclo = " + proyecto.getCicloActual() + " and f.id.nombre = '" + proyecto.getFaseActual() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static void actualizarFase(Fase fase) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").actualizar(fase);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    //===================================[FASE|FIN]===============================================

    //===================================[META|INICIO]===============================================

    /**
     * Consulta todas las metas del ciclo,para el proyecto indiferentemente de si es tipo rol,usuario o proyecto
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarMetasCiclo(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select m from Meta m where m.id.proyecto = '" + proyecto.getNombre() + "' and m.id.NCiclo = " + proyecto.getCicloActual());
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static List consultarMetasCicloProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select m from Meta m where m.id.proyecto = '" + proyecto.getNombre() + "' and m.id.NCiclo = " + proyecto.getCicloActual() + " and m.tipo = 'proyecto'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static List consultarMetasRolCicloProyecto(Proyecto proyecto, String nombreRol) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select m from Meta m where m.id.proyecto = '" + proyecto.getNombre() + "' and m.id.NCiclo = " + proyecto.getCicloActual() + " and m.tipo = 'rol' and m.id.rol = '" + nombreRol + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Registra la meta en la base de datos
     * @param meta
     * @throws ExceptionFatal 
     */
    public static void insertarMeta(Meta meta) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(meta);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Elimina la meta
     * @param meta
     * @throws ExceptionFatal 
     */
    public static void eliminarMeta(Meta meta) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").eliminar(meta);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static void eliminarMetasCicloProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").eliminar("delete from Meta m where m.id.proyecto = '" + proyecto.getNombre() + "' and m.id.NCiclo = " + proyecto.getCicloActual() + " and m.tipo = 'proyecto'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static void eliminarMetasCicloProyectoRol(Proyecto proyecto,String nombreRol) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").eliminar("delete from Meta m where m.id.proyecto = '" + proyecto.getNombre() + "' and m.id.NCiclo = " + proyecto.getCicloActual() + " and m.id.rol = '" + nombreRol + "' and m.tipo = 'rol'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    //===================================[META|FIN]===============================================
    
    //===================================[CRITERIOS|INICIO]===============================================
    
    /**
     * Consulta los criterios registrados para todos los proyectos en general
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarCriterios() throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select c from Criterio c");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Consulta todos los criterios de un proyeto específico basado en su nombre
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarCriteriosProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select pc from PyCr pc where pc.id.proyecto = '" + proyecto.getNombre() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    /**
     * Permite insertar los criterios para todos los proyectos en general
     * @param criterios
     * @throws ExceptionFatal 
     */
    public static void insertarCriterios(List criterios) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(criterios);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    //===================================[CRITERIOS|FIN]===============================================

    //===================================[PARTE|INICIO]===============================================

    /**
     * Consulta la parte con base en el nombre, proyecto y ciclo.
     * @param parte
     * @return
     * @throws ExceptionFatal 
     */
    public static Parte consultarParte(Parte parte) throws ExceptionFatal {
        try {
            CicloId id = parte.getCiclo().getId();
            return (Parte) FabricaConexiones.conectar("postgres").consultarObjeto("select pt from Parte pt where pt.nombre = '" + parte.getNombre() + "' and pt.ciclo.id.proyecto = '" + id.getProyecto() + "' and pt.ciclo.id.NCiclo = " + id.getNCiclo());
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Reemplaza la parte anterior por los valores de la parte posterior asegurándose de consvervar el id automáticamente creciente
     * @param parteAnterior
     * @param partePosterior
     * @throws ExceptionFatal 
     */
    public static void actualizarParte(Parte parteAnterior, Parte partePosterior) throws ExceptionFatal {
        try {
            CicloId id = parteAnterior.getCiclo().getId();
            FabricaConexiones.conectar("postgres").actualizar("update Parte pt set pt.nombre = '" + partePosterior.getNombre() + "', pt.tipo = '" + partePosterior.getTipo() + "', pt.descripcion = '" + partePosterior.getDescripcion() + "', pt.observacion = '" + partePosterior.getObservacion() + "', pt.estado = 'Habilitado', pt.parte.id = " + consultarParte(partePosterior.getParte()).getId() + " where pt.ciclo.id.proyecto = '" + id.getProyecto() + "' and pt.ciclo.id.NCiclo = " + id.getNCiclo() + " and pt.nombre = '" + parteAnterior.getNombre() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Obtiene las partes del proyecto
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarPartes(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select pt from Parte pt where pt.ciclo.id.NCiclo = " + proyecto.getCicloActual() + " and  pt.ciclo.id.proyecto = '" + proyecto.getNombre() + "' and pt.estado='Habilitado'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Obtiene todas las partes de tipo producto del proyecto
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarProductos(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select pt from Parte pt where pt.ciclo.id.NCiclo = " + proyecto.getCicloActual() + " and pt.tipo = '"+(String)TSP.tipoPartes.get(1)+"' and pt.ciclo.id.proyecto = '" + proyecto.getNombre() + "' and pt.estado='Habilitado'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Deshabilita la parte para desvinculararla del proyecto únicamente con este estado
     * @param parte
     * @throws ExceptionFatal 
     */
    public static void deshabilitarParte(Parte parte) throws ExceptionFatal {
        try {
            CicloId id = parte.getCiclo().getId();
            FabricaConexiones.conectar("postgres").actualizar("update Parte pt set pt.estado = 'Deshabilitado' where pt.nombre = '" + parte.getNombre() + "' and pt.ciclo.id.proyecto = '" + id.getProyecto() + "' and pt.ciclo.id.NCiclo = " + id.getNCiclo());
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Permite insertar la parte en la base de datos
     * @param parte
     * @throws ExceptionFatal 
     */
    public static void insertarParte(Parte parte) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(parte);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    //===================================[PARTE|FIN]===============================================

    //===================================[REPOSITORIO|INICIO]===============================================
    
    //public static Repositorio getRepositorio() {
        
    //}
    
    //===================================[REPOSITORIO|FIN]===============================================
    
    //===================================[DOCUMENTO|INICIO]===============================================

    /**
     * Permite consultar un documento a partir de su nombre, proyecto y ciclo
     * @param documento
     * @return
     * @throws ExceptionFatal 
     */
    public static Documento consultarDocumento(Documento documento) throws ExceptionFatal {
        try {
            RlClId id = documento.getRlCl().getId();
            return (Documento) FabricaConexiones.conectar("postgres").consultarObjeto("select d from Documento d where d.rlCl.id.proyecto = '" + id.getProyecto() + "' and d.rlCl.id.NCiclo = " + id.getNCiclo() + " and d.nombre = '" + documento.getNombre() + "'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Entrega la lista documentos filtrada por ciclo
     * @param proyecto
     * @param nCiclo
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarDocumentosCiclo(Proyecto proyecto, int nCiclo) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select d from Documento d where d.rlCl.id.proyecto = '" + proyecto.getNombre() + "' and d.rlCl.id.NCiclo = " + nCiclo);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Inserta el documento en la base de datos
     * @param documento
     * @throws ExceptionFatal 
     */
    public static void insertarDocumento(Documento documento) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(documento);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * Carga todos los documentos del proyecto
     * @param proyecto
     * @return
     * @throws ExceptionFatal 
     */
    public static List consultarDocumentosProyecto(Proyecto proyecto) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select d from Documento d where d.rlCl.id.proyecto = '" + proyecto.getNombre() + "' order by d.id");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    /**
     * ELimina del documento de la base de datos
     * @param documento
     * @throws ExceptionFatal 
     */
    public static void eliminarDocumento(Documento documento) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").eliminar(documento);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }

    //===================================[DOCUMENTO|FIN]===============================================

    public static List consultarOpcionesTSP(String tipo) throws ExceptionFatal {
        try {
            return FabricaConexiones.conectar("postgres").consultarLista("select o.nombre from Opciontsp o where o.tipo = '"+tipo+"'");
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
    public static void insertarOpcionesTSP(List opciones) throws ExceptionFatal {
        try {
            FabricaConexiones.conectar("postgres").insertar(opciones);
        } catch (Exception e) {
            throw new ExceptionFatal(e.getMessage());
        }
    }
    
}