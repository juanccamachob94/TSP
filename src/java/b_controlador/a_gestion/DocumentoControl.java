/**
 * Controlador para los documentos y la carpeta del proyecto
 */
package b_controlador.a_gestion;

import e_utilitaria.GestorArchivos;
import c_negocio.a_relacional.Documento;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import c_negocio.a_relacional.Rol;
import c_negocio.a_relacional.Usuario;
import d_datos.a_dao.ProyectoDAO;
import e_utilitaria.Helper;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.TSP;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class DocumentoControl extends Control{
    
    private String root;

    private TreeNode rootProyecto;
    private List nodosDocumentos;
    private List documentosProyecto;

    //Carpeta que se elige al presionar el botón de + para añadirle carpetas o archivos
    private Documento carpetaSeleccionada;
    //Nombre de la carpeta que se va a crear
    private String nombreNuevaCarpeta;
    private DefaultStreamedContent documentoDescarga;

    private TreeNode nodoDocumentoSeleccionado;

    public DocumentoControl() {
    }
    
    @PostConstruct
    public void init() {
        this.root = "C:/tsp/";
    }
    /**
     * carga la carpeta del proyecto y administra las carpetas del ciclo actual
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarCarpetaProyecto() throws ExceptionFatal, ExceptionWarn{
        try {
            //Se crea la carpeta para almacenar los proyectos en caso de que no exista
            if(!GestorArchivos.existe(this.root)) GestorArchivos.crearCarpeta(this.root);
            //Se instancia el root de directorios
            this.rootProyecto = new DefaultTreeNode("folder", crearDocumento("root", "folder", null, null, false, null, null, 0, null, null), null);
            ProyectoControl pb = ((ProyectoControl) getControlador("ProyectoControl"));
            Usuario usuarioSesion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion();
            Proyecto proyectoSesion = pb.getProyectoSesion();
            int cicloActual = proyectoSesion.getCicloActual();
            String nombreProyectoSesion = proyectoSesion.getNombre();
            String identificacionUsuario = usuarioSesion.getIdentificacion();
            String rolUsuario = null;
            if(!proyectoSesion.getFaseActual().equals(TSP.fases.get(0)))
                rolUsuario = (String) pb.getRolesUSesion().get(0);
            //Se consultan los documentos del proyecto desde la base de datos
            this.documentosProyecto = ProyectoDAO.consultarDocumentosProyecto(proyectoSesion);
            //Se carga la lista de nodos a partir de la lista de documentos de la base de atos:
            //Se instancia la lista de los nodos que van a contener los documentos
            this.nodosDocumentos = new ArrayList();
            //Se declara el tamaño de la lista de documentos (directorios y archivos) a fin de reducir procesamiento
            int t = this.documentosProyecto.size();
            Documento doc;
            //Se crea un nodo, sin padre, por cada documento, y se añade a la lista de nodosDocumentos
            for (int i = 0; i < t; i++) {
                doc = (Documento) documentosProyecto.get(i);
                nodosDocumentos.add(new DefaultTreeNode(doc.getTipo(), doc, null));
            }
            //Si no existe la carpeta del proyecto, se crea automáticamente
            if (documentosProyecto.isEmpty())
                crearCarpetaProyecto(nombreProyectoSesion, cicloActual, identificacionUsuario, rolUsuario);
            //Se revisa si existe carpeta para el ciclo actual. Si la carpeta no existe, se crea y sus subdirectorios con las carpetas por rol
            Documento carpetaCicloActual = carpetaCiclo(nombreProyectoSesion, cicloActual);
            if(!((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion().getFaseActual().equals(TSP.fases.get(0)))
                if (carpetaCicloActual == null)
                    crearCarpetaCiclo((Documento) (((TreeNode) this.nodosDocumentos.get(0)).getData()), nombreProyectoSesion, cicloActual, identificacionUsuario, rolUsuario);
            //Se actualiza el t para el tamaño de la lista de nodosDocumentos a fin de iterar sobre nodosDocumentos.
            //Cabe menionar que la lita de nodosDocumentos puede sufrir cambios durante la revisión de carpetas de proyecto y de ciclo
            t = this.nodosDocumentos.size();
            //Con todos los nodos creados e independientes, se procede a establecer las jerarquías de directorios con
            //el apuntador de cada nodo hacia su directorio padre
            TreeNode nodo;
            TreeNode nodoPadre;
            for (int i = 0; i < t; i++) {
                nodo = (TreeNode) this.nodosDocumentos.get(i);
                nodoPadre = nodoPadre((Documento) nodo.getData());
                if (nodoPadre == null) this.rootProyecto.getChildren().add(nodo);
                else nodoPadre.getChildren().add(nodo);
            }
            expandirDirectorios(TSP.ciclo + " " + Integer.toString(cicloActual));
        } catch (Exception e) {
            throw new ExceptionFatal("DocumentoControl no puede cargar la carpeta del proyecto. " + e.getMessage());
        }
    }

    /**
     * Crea la carpeta del proyecto
     * @param nombreProyecto
     * @param nCiclo
     * @param identificacion
     * @param rol
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void crearCarpetaProyecto(String nombreProyecto, int nCiclo, String identificacion, String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            String nombreCarpetaProyecto = Helper.soloCaracteresSimples(nombreProyecto);
            nombreCarpetaProyecto = nombreCarpetaProyecto.substring(0,Math.min(nombreCarpetaProyecto.length(), 50));
            Documento carpetaProyecto = crearDocumento(nombreCarpetaProyecto, "folder", "", "", false, null, nombreProyecto, nCiclo, identificacion, rol);
            this.documentosProyecto.add(carpetaProyecto);
            this.nodosDocumentos.add(new DefaultTreeNode("folder", carpetaProyecto, null));
            GestorArchivos.crearCarpeta(this.root + nombreCarpetaProyecto);
            registrarDocumento(carpetaProyecto, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede crear la carpeta del proyecto. " + e.getMessage());
        }
    }

    /**
     * Identifica el nodo documento padre del enviado como parámetro
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private TreeNode nodoPadre(Documento documento) throws ExceptionFatal, ExceptionWarn{
        try{
            Documento carpetaPadre = documento.getDocumento();
            if (carpetaPadre == null) return null;
            int numCicloPadre = carpetaPadre.getRlCl().getId().getNCiclo();
            String nombreCarpetaPadre = carpetaPadre.getNombre();
            int t = this.nodosDocumentos.size();
            Documento doc;
            TreeNode nodo;
            for (int i = 0; i < t; i++) {
                nodo = (TreeNode) this.nodosDocumentos.get(i);
                doc = (Documento) nodo.getData();
                if (doc.getNombre().equals(nombreCarpetaPadre) && doc.getRlCl().getId().getNCiclo() == numCicloPadre)
                    return nodo;
            }
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl falló al tratar de localizar un documento padre. " + e.getMessage());
        }
    }

    /**
     * Crea la carpeta del ciclo
     * @param carpetaProyecto
     * @param nombreProyecto
     * @param nCiclo
     * @param identificacion
     * @param rol
     * @throws ExceptionFatal 
     */
    private void crearCarpetaCiclo(Documento carpetaProyecto, String nombreProyecto, int nCiclo, String identificacion, String rol) throws ExceptionFatal {
        try{
            String localizacion = carpetaProyecto.getUrl() + carpetaProyecto.getNombre() + "/";
            String nombreCarpetaCiclo = Helper.soloCaracteresSimples(TSP.ciclo + " " + Integer.toString(nCiclo));
            Documento carpetaCiclo = crearDocumento(nombreCarpetaCiclo, "folder", localizacion, "", false, carpetaProyecto, nombreProyecto, nCiclo, identificacion, rol);
            registrarDocumento(carpetaCiclo, carpetaProyecto);
            this.nodosDocumentos.add(new DefaultTreeNode("folder", carpetaCiclo, null));
            this.documentosProyecto.add(carpetaCiclo);
            this.crearCarpetasRoles(carpetaCiclo, nombreProyecto, nCiclo, identificacion, rol);
            GestorArchivos.crearCarpeta(this.root + localizacion + nombreCarpetaCiclo);
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede crear la carpeta del ciclo. " + e.getMessage());
        }
    }

    /**
     * Crea las carpetas de los roles pertenecientes a un ciclo en específico
     * @param carpetaCiclo
     * @param nombreProyecto
     * @param nCiclo
     * @param identificacion
     * @param rol
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void crearCarpetasRoles(Documento carpetaCiclo, String nombreProyecto, int nCiclo, String identificacion, String rol) throws ExceptionFatal, ExceptionWarn{
        try{
            int t = TSP.rolesTSP.size();
            Documento carpetaRol;
            String localizacion = carpetaCiclo.getUrl() + carpetaCiclo.getNombre() + "/";
            String nombreCarpetaRol;
            for (int i = 0; i < t; i++) {
                nombreCarpetaRol = Helper.soloCaracteresSimples(((Rol) TSP.rolesTSP.get(i)).getNombre());
                carpetaRol = crearDocumento(nombreCarpetaRol, "folder", localizacion, "", false, carpetaCiclo, nombreProyecto, nCiclo, identificacion, rol);
                registrarDocumento(carpetaRol, carpetaCiclo);
                this.documentosProyecto.add(carpetaRol);
                this.nodosDocumentos.add(new DefaultTreeNode("folder", carpetaRol, null));
                GestorArchivos.crearCarpeta(this.root + localizacion + nombreCarpetaRol);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede crear la carpeta del rol. " + e.getMessage());
        }
    }

    /**
     * Consulta la carpeta del ciclo a partir del nombre del proyecto y número del ciclo
     * @param nombreProyecto
     * @param nCiclo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private Documento carpetaCiclo(String nombreProyecto, int nCiclo) throws ExceptionFatal, ExceptionWarn{
        try{
            return ProyectoDAO.consultarDocumento(crearDocumento(TSP.ciclo + " " + Integer.toString(nCiclo), "folder", null, null, false, null, nombreProyecto, nCiclo, null, null));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede consultar la carpeta del ciclo " + nCiclo + ". " + e.getMessage());
        }
    }

    /**
     * Crea un documento con todos los parámetros enviados
     * @param nombre
     * @param tipo
     * @param url
     * @param extension
     * @param editable
     * @param docPadre
     * @param nombreProyecto
     * @param nCiclo
     * @param identificacion
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private Documento crearDocumento(String nombre, String tipo, String url, String extension, boolean editable, Documento docPadre, String nombreProyecto, int nCiclo, String identificacion, String rol) throws ExceptionFatal, ExceptionWarn{
        try{
            Documento documento = new Documento();
            documento.setNombre(nombre);
            documento.setTipo(tipo);
            documento.setFechaCreacion(new Date());
            RlCl rlCl = new RlCl();
            RlClId id = new RlClId();
            id.setProyecto(nombreProyecto);
            id.setNCiclo((byte) nCiclo);
            id.setRol(tipo);
            id.setUsuario(identificacion);
            id.setRol(rol);
            rlCl.setId(id);
            documento.setRlCl(rlCl);
            documento.setDocumento(docPadre);
            documento.setExtension(extension);
            documento.setEditable(editable);
            documento.setUrl(url);
            return documento;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede crear el documento " + nombre + ". " + e.getMessage());
        }
    }

    /**
     * Permite expandir las carpetas del proyecto hasta el nivel del ciclo
     * actual
     * @param nombreCarpetaCiclo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void expandirDirectorios(String nombreCarpetaCiclo) throws ExceptionFatal, ExceptionWarn{
        try{
            //Se expande la carpeta de proyecto
            TreeNode nodoCarpetaProyecto = this.rootProyecto.getChildren().get(0);
            nodoCarpetaProyecto.setExpanded(true);
            //Se expanden las carpetas del ciclo
            List<TreeNode> nodosCiclos = nodoCarpetaProyecto.getChildren();
            int t = nodosCiclos.size();
            TreeNode nodoCiclo;
            for (int i = 0; i < t; i++) {
                nodoCiclo = nodosCiclos.get(i);
                //Sólo se expande la carpeta de ciclo que corresponde al ciclo actual
                if (((Documento) nodoCiclo.getData()).getNombre().equals(nombreCarpetaCiclo))
                    nodoCiclo.setExpanded(true);
            }
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl falló al tratar de expandir los directorios. " + e.getMessage());
        }
    }

    /**
     * Carga el modal con los datos para la subida de archivos o creación de carpeta
     * @param carpetaPadre
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalSeleccionCarpetaArchivo(Documento carpetaPadre) throws ExceptionFatal, ExceptionWarn{
        try{
            this.carpetaSeleccionada = carpetaPadre;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede cargar el menú de creación de documento. " + e.getMessage());
        }
    }

    /**
     * Concatena el nombre del documento, el proyecto y el ciclo que son su
     * llave primaria. Se utiliza esta función para construir solicitudes
     * específicas sobre los documentos en el archivo xhtml
     * @param doc
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String cadenaIdDocumento(Documento doc) throws ExceptionFatal, ExceptionWarn{
        try{
            RlClId id = doc.getRlCl().getId();
            return doc.getNombre() + "_" + id.getProyecto() + "_" + Integer.toString(id.getNCiclo());
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede crear la cadena identificadora de documento. " + e.getMessage());
        }
    }

    /**
     * Identifica si el documento ya se encuentra o no registrado en la base de datos
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean documentoRegistrado(Documento documento) throws ExceptionFatal, ExceptionWarn{
        try{
            String idDocumento = cadenaIdDocumento(documento);
            int t = this.documentosProyecto.size();
            for (int i = 0; i < t; i++)
                if (cadenaIdDocumento((Documento) this.documentosProyecto.get(i)).equals(idDocumento))
                    return true;
            return false;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede validar si el documento ya se encuentra registrado. " + e.getMessage());
        }
    }

    /**
     * Crea una nueva carpeta vinculándola al sistema de directorios
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String crearCarpeta() throws ExceptionFatal, ExceptionWarn{
        try{
            ProyectoControl pb = ((ProyectoControl) getControlador("ProyectoControl"));
            Usuario usuarioSesion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion();
            Proyecto proyectoSesion = pb.getProyectoSesion();
            String rol = rolDeURL(this.carpetaSeleccionada.getUrl());
            if (rol == null) rol = (String) pb.getRolesUSesion().get(0);
            String localizacion = this.carpetaSeleccionada.getUrl() + this.carpetaSeleccionada.getNombre() + "/";
            this.nombreNuevaCarpeta = Helper.soloCaracteresSimples(this.nombreNuevaCarpeta);
            Documento nuevaCarpeta = crearDocumento(this.nombreNuevaCarpeta, "folder", localizacion, "", true, this.carpetaSeleccionada, proyectoSesion.getNombre(), proyectoSesion.getCicloActual(), usuarioSesion.getIdentificacion(), rol);
            if (!documentoRegistrado(nuevaCarpeta)) {
                registrarDocumento(nuevaCarpeta, this.carpetaSeleccionada);
                this.documentosProyecto.add(nuevaCarpeta);
                TreeNode nodoPadre = nodoPadre(nuevaCarpeta);
                TreeNode nodoDocumento = new DefaultTreeNode("folder", nuevaCarpeta, null);
                nodoPadre.getChildren().add(nodoDocumento);
                this.nodosDocumentos.add(nodoDocumento);
                nodoPadre.setExpanded(true);
                GestorArchivos.crearCarpeta(this.root + localizacion + this.nombreNuevaCarpeta);
                this.nombreNuevaCarpeta = null;
            } else
                throw new ExceptionWarn("No está permitido crear una carpeta del mismo nombre dentro del ciclo");
            return nuevaCarpeta.getNombre();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede crear la carpeta. " + e.getMessage());
        }
    }

    /**
     * Extrae e identifica el rol dentro de una url de documento
     * @param url
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private String rolDeURL(String url) throws ExceptionFatal, ExceptionWarn{
        try{
            int t = TSP.rolesTSP.size();
            Rol rol;
            for (int i = 0; i < t; i++) {
                rol = (Rol) TSP.rolesTSP.get(i);
                if (url.contains(Helper.soloCaracteresSimples(rol.getNombre())))
                    return rol.getNombre();
            }
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede extraer el rol TSP de las rutas de archivo. " + e.getMessage());
        }
    }

    /**
     * Permite subir un archivoal directorio del proyecto y anexarlo al directorio del mismo
     * @param event
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void subirArchivo(FileUploadEvent event) throws ExceptionFatal, ExceptionWarn {
        try {
            UploadedFile archivoSubido = event.getFile();
            String nombreOriginalArchivo = archivoSubido.getFileName();
            String extension = GestorArchivos.extension(archivoSubido);
            String puntoExtension = extension == ""? "":".";
            String nombreArchivo = Helper.soloCaracteresSimples(GestorArchivos.nombreArchivo(nombreOriginalArchivo, extension));
            //Type está definido por el nodo para el ícono a representar
            String type = GestorArchivos.tipoArchivo(extension);
            String localizacion = this.carpetaSeleccionada.getUrl() + this.carpetaSeleccionada.getNombre() + "/";
            File archivo = new File(this.root + localizacion + "/" + nombreArchivo + puntoExtension + extension);
            if (!archivo.exists()) {
                Proyecto proyectoSesion = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
                Usuario usuarioSesion = ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion();
                Documento nuevoDocumento = crearDocumento(nombreArchivo, type, localizacion, extension, true, this.carpetaSeleccionada, proyectoSesion.getNombre(), proyectoSesion.getCicloActual(), usuarioSesion.getIdentificacion(), rolDeURL(localizacion));
                if (!documentoRegistrado(nuevoDocumento)) {
                    registrarDocumento(nuevoDocumento, this.carpetaSeleccionada);
                    this.documentosProyecto.add(nuevoDocumento);
                    TreeNode nodoPadre = nodoPadre(nuevoDocumento);
                    TreeNode nuevoNodo = new DefaultTreeNode(type, nuevoDocumento, null);
                    nodoPadre.getChildren().add(nuevoNodo);
                    nodoPadre.setExpanded(true);
                    this.nodosDocumentos.add(nuevoNodo);
                } else
                    throw new ExceptionWarn("No está permitido subir archivos del mismo nombre dentro del ciclo");
                GestorArchivos.crearArchivo(archivo, archivoSubido);
            } else
                throw new ExceptionWarn("El archivo " + nombreOriginalArchivo + " ya se encuentra en el directorio");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("DocumentoControl no puede subir el archivo. " + e.getMessage());
        }
    }

    /**
     * Identifica si un documento es carpoeta o no.
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean esCarpeta(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try{
            return documento.getTipo().equals("folder");
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede determinar si es carpeta o no. " + e.getMessage());
        }
    }

    /**
     * Identifica el nombre original del documento a partir de su nombre y su extensión
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String nombreOriginalDocumento(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try{
            if (esCarpeta(documento)) return documento.getNombre();
            return documento.getNombre() + "." + documento.getExtension();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede determinar el nombre originarl del documento. " + e.getMessage());
        }
    }

    /**
     * Obtiene la posición del documento en la lista de documentos del proyecto
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int idDocumento(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try{
            String strDocumento = documento.toString();
            int t = this.documentosProyecto.size();
            for (int i = 0; i < t; i++)
                if (((Documento) this.documentosProyecto.get(i)).toString().equals(strDocumento))
                    return i;
            return -1;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no localiza el documento. " + e.getMessage());
        }
    }

    /**
     * Brinda la ruta del documento; si es archivo debe concatenarse con la extensión
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private String rutaDocumento(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try{
            return esCarpeta(documento) ? this.root + documento.getUrl() + documento.getNombre() : this.root + documento.getUrl() + documento.getNombre() + "." + documento.getExtension();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede determinar la ruta del documento. " + e.getMessage());
        }
    }

    /**
     * ELimina un documento del directorio del proyecto
     * @param documento
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarDocumento(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try {
            //Se identifica al nodo padre a fin de romper el enlace
            if (GestorArchivos.existe(rutaDocumento(documento))) {

                TreeNode nodoPadre = nodoPadre(documento);
                Iterator<TreeNode> it = nodoPadre.getChildren().iterator();
                while (it.hasNext()) {
                    TreeNode nodoDocumento = it.next();
                    //De entre todos los hijos del padre del documento a eliminar
                    //se identifica el documento a eliminar y se elimina dicho nodo
                    if (((Documento) nodoDocumento.getData()).equals(documento)) {
                        it.remove();
                        //Se eliminan los nodos y documentos de las dos listas respectivas
                        eliminacionEncadenada(nodoDocumento);
                        break;
                    }
                }
            } else
                throw new ExceptionFatal("No se localiza el recurso " + documento.getNombre());
        } catch (Exception e) {
            throw new ExceptionFatal("DocumentoControl no puede eliminar el documento. " + e.getMessage());
        }
    }

    /**
     * En forma recursiva elimina el elemento actual de las dos listas (nodos y
     * documentos).
     * @param nodoDocumento
     * @param nodo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void eliminacionEncadenada(TreeNode nodo) throws ExceptionFatal, ExceptionWarn {
        try{
            Documento documento;
            String ruta;
            boolean esCarpeta = false;
            List hijos = nodo.getChildren();
            int t = hijos.size();
            for (int i = 0; i < t; i++)
                eliminacionEncadenada((TreeNode) hijos.get(i));
            documento = (Documento) nodo.getData();
            ruta = rutaDocumento(documento);
            if (GestorArchivos.existe(ruta)) {
                int id = idDocumento(documento);
                ProyectoDAO.eliminarDocumento(documento);
                eliminarDocumentoDisco(ruta, esCarpeta);
                this.documentosProyecto.remove(id);
                this.nodosDocumentos.remove(id);
                if (esCarpeta(documento)) esCarpeta = true;
        } else
            throw new ExceptionFatal("No se localiza el recurso " + documento.getNombre());
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede eliminar en forma encadenada. " + e.getMessage());
        }
    }

    /**
     * Valida si el usuario tiene permisos para editar el documento
     * @param documento
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean tienePermiso(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try {
            String rol = rolDeURL(documento.getUrl() + documento.getNombre());
            if (rol == null) return false;
            return ((ProyectoControl) getControlador("ProyectoControl")).poseeRolCicloActual(rol);
        } catch (Exception e) {
            throw new ExceptionFatal("DocumentoControl no puede validar si el usuario tiene permiso sobre los documentos");
        }
    }

    /**
     * Permite descargar el documento solicitado
     * @param documento
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void descargarDocumento(Documento documento) throws ExceptionFatal, ExceptionWarn {
        try{
            File archivo = new File(rutaDocumento(documento));
            InputStream input = new FileInputStream(archivo);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            this.documentoDescarga = new DefaultStreamedContent(input, externalContext.getMimeType(archivo.getName()), archivo.getName());
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede descargar el documento. " + e.getMessage());
        }
    }

    /**
     * Elimina el archivo o carpeta del directorio en disco
     * @param ruta
     * @param esCarpeta
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void eliminarDocumentoDisco(String ruta, boolean esCarpeta) throws ExceptionFatal, ExceptionWarn{
        try{
            if (esCarpeta) GestorArchivos.eliminarCarpeta(ruta);
            else GestorArchivos.eliminarArchivo(ruta);
        }catch(Exception e){
            throw new ExceptionFatal("DocumentoControl no puede eliminar el documento. " + e.getMessage());
        }
    }

    /**
     * Inserta el documento en la base de datos y actualiza su identificador
     * @param documento
     * @param documentoPadre
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void registrarDocumento(Documento documento, Documento documentoPadre) throws ExceptionFatal, ExceptionWarn {
        try{
            if (documento.getDocumento() != null) documento.setDocumento(ProyectoDAO.consultarDocumento(documentoPadre));
            ProyectoDAO.insertarDocumento(documento);
            documento.setId(ProyectoDAO.consultarDocumento(documento).getId());
        }catch(Exception e){
            throw e;
        }

    }

    public TreeNode getRootProyecto() {
        return rootProyecto;
    }

    public void setRootProyecto(TreeNode rootProyecto) {
        this.rootProyecto = rootProyecto;
    }

    public List getNodosDocumentos() {
        return nodosDocumentos;
    }

    public void setNodosDocumentos(List nodosDocumentos) {
        this.nodosDocumentos = nodosDocumentos;
    }

    public TreeNode getNodoDocumentoSeleccionado() {
        return nodoDocumentoSeleccionado;
    }

    public void setNodoDocumentoSeleccionado(TreeNode nodoDocumentoSeleccionado) {
        this.nodoDocumentoSeleccionado = nodoDocumentoSeleccionado;
    }

    public List getDocumentosProyecto() {
        return documentosProyecto;
    }

    public void setDocumentosProyecto(List documentosProyecto) {
        this.documentosProyecto = documentosProyecto;
    }

    public Documento getCarpetaSeleccionada() {
        return carpetaSeleccionada;
    }

    public void setCarpetaSeleccionada(Documento carpetaSeleccionada) {
        this.carpetaSeleccionada = carpetaSeleccionada;
    }

    public String getNombreNuevaCarpeta() {
        return nombreNuevaCarpeta;
    }

    public void setNombreNuevaCarpeta(String nombreNuevaCarpeta) {
        this.nombreNuevaCarpeta = nombreNuevaCarpeta;
    }

    public DefaultStreamedContent getDocumentoDescarga() {
        return documentoDescarga;
    }

    public void setDocumentoDescarga(DefaultStreamedContent documentoDescarga) {
        this.documentoDescarga = documentoDescarga;
    }
}