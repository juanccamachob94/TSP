package a_web.b_backing;

import b_controlador.a_gestion.DocumentoControl;
import c_negocio.a_relacional.Documento;
import javax.faces.bean.ManagedProperty;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.TreeNode;

@ManagedBean
@SessionScoped
public class DocumentoBean extends GeneralBean {

    @ManagedProperty("#{documentoControl}")
    DocumentoControl documentoControl;

    public DocumentoBean() {
    }

    public void cargarCarpetaProyecto() {
        try {
            documentoControl.cargarCarpetaProyecto();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar la carpeta del proyecto. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar la carpeta del proyecto. " + warn.getMessage(), "warn");
        } finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public void cargarModalSeleccionCarpetaArchivo(Documento carpetaPadre) {
        try {
            documentoControl.cargarModalSeleccionCarpetaArchivo(carpetaPadre);
            ejecutarJS("r_mostrarModalSeleccionCarpetaArchivo");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al cargar el menú para subir documentos. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al cargar el menú para subir documentos. " + warn.getMessage(), "warn");
        } finally {
            ejecutarJS("cuadrarLabels");
        }
    }

    public String cadenaIdDocumento(Documento doc) {
        try {
            return documentoControl.cadenaIdDocumento(doc);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al generar los elementos html del directorio. " + fatal.getMessage(), "fatal");
            return null;
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al generar los elementos html del directorio. " + warn.getMessage(), "warn");
            return null;
        }
    }
    
    public void crearCarpeta() throws ExceptionFatal {
        try {
            String nombreNuevaCarpeta = documentoControl.crearCarpeta();
            this.enviarMensaje(null, "Carpeta " + nombreNuevaCarpeta + " creada sastisfactoriamente", "info");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al crear la carpeta. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "" + warn.getMessage(), "warn");
        }
    }

    public void subirArchivo(FileUploadEvent event) {
        try {
            documentoControl.subirArchivo(event);
            this.enviarMensaje(null, "Archivo " + event.getFile().getFileName() + " creado satisfactoriamente", "info");
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al subir el archivo. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al subir el archivo. " + warn.getMessage(), "warn");
        } finally {
            ejecutarJS("cerrarMensaje");
        }
    }

    public String nombreOriginalDocumento(Documento documento) {
        try {
            return documentoControl.nombreOriginalDocumento(documento);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al identificar el nombre del documento" + fatal.getMessage(), "fatal");
            return null;
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al identificar el nombre del documento" + warn.getMessage(), "warn");
            return null;
        }
    }

    public void eliminarDocumento(Documento documento) {
        try {
            documentoControl.eliminarDocumento(documento);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al eliminar el documento. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al eliminar el documento. " + warn.getMessage(), "warn");
        }
    }

    public boolean tienePermiso(Documento documento) {
        try {
            return documentoControl.tienePermiso(documento);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al evaluar los permisos sobre el directorio. " + fatal.getMessage(), "fatal");
            return false;
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al evaluar los permisos sobre el directorio. " + warn.getMessage(), "warn");
            return false;
        }
    }

    public void descargarDocumento(Documento documento) {
        try {
            documentoControl.descargarDocumento(documento);
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al descargar el documento. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al descargar el documento. " + warn.getMessage(), "warn");
        }
    }

    public DefaultStreamedContent getDocumentoDescarga() {
        return documentoControl.getDocumentoDescarga();
    }

    public void setDocumentoDescarga(DefaultStreamedContent documentoDescarga) {
        documentoControl.setDocumentoDescarga(documentoDescarga);
    }

    public TreeNode getRootProyecto() {
        return documentoControl.getRootProyecto();
    }

    public void setRootProyecto(TreeNode rootProyecto) {
        documentoControl.setRootProyecto(rootProyecto);
    }

    public List getNodosDocumentos() {
        return documentoControl.getNodosDocumentos();
    }

    public void setNodosDocumentos(List nodosDocumentos) {
        documentoControl.setNodosDocumentos(nodosDocumentos);
    }

    public TreeNode getNodoDocumentoSeleccionado() {
        return documentoControl.getNodoDocumentoSeleccionado();
    }

    public void setNodoDocumentoSeleccionado(TreeNode nodoDocumentoSeleccionado) {
        documentoControl.setNodoDocumentoSeleccionado(nodoDocumentoSeleccionado);
    }

    public List getDocumentosProyecto() {
        return documentoControl.getDocumentosProyecto();
    }

    public void setDocumentosProyecto(List documentosProyecto) {
        documentoControl.setDocumentosProyecto(documentosProyecto);
    }

    public Documento getCarpetaSeleccionada() {
        return documentoControl.getCarpetaSeleccionada();
    }

    public void setCarpetaSeleccionada(Documento carpetaSeleccionada) {
        documentoControl.setCarpetaSeleccionada(carpetaSeleccionada);
    }

    public String getNombreNuevaCarpeta() {
        return documentoControl.getNombreNuevaCarpeta();
    }

    public void setNombreNuevaCarpeta(String nombreNuevaCarpeta) {
        documentoControl.setNombreNuevaCarpeta(nombreNuevaCarpeta);
    }

    public DocumentoControl getDocumentoControl() {
        return documentoControl;
    }

    public void setDocumentoControl(DocumentoControl documentoControl) {
        this.documentoControl = documentoControl;
    }

}
