package a_web.b_backing;

import b_controlador.a_gestion.CodigoControl;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.io.File;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class CodigoBean extends GeneralBean {

    @ManagedProperty("#{codigoControl}")
    CodigoControl codigoControl;

    public CodigoBean() {
    }

    public void reiniciarVariables() {
        try{
            codigoControl.reiniciarVariables();
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al reiniciar las variables. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al reiniciar las variables. " + warn.getMessage(),"warn");
        }
    }

    public void subirCodigoAntes() {
        try{
            codigoControl.subirCodigoAntes();
            this.enviarMensaje(null, "Archivo " + codigoControl.getArchivoSubidoAntes().getFileName() + " subido satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al subir el código anterior. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al subir el código anterior. " + warn.getMessage(),"warn");
        }
    }

    public void subirCodigoDespues() {
        try{
            codigoControl.subirCodigoDespues();
            this.enviarMensaje(null, "Archivo " + codigoControl.getArchivoSubidoDespues().getFileName() + " subido satisfactoriamente", "info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al subir el código posterior. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al subir el código posterior. " + warn.getMessage(),"warn");
        }
    }

    public void validarCodigoAntes() {
        try{
            codigoControl.validarCodigoAntes();
            this.enviarMensaje(null,"Código " + codigoControl.getArchivoSubidoAntes().getFileName() + " validado satisfactoriamente","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al validar el código anterior. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al validar el código anterior. " + warn.getMessage(),"warn");
        }
    }

    public void validarCodigoDespues() {
        try{
            codigoControl.validarCodigoDespues();
            this.enviarMensaje(null,"Código " + codigoControl.getArchivoSubidoDespues().getFileName() + " validado satisfactoriamente","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al validar el código posterior. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al validar el código posterior. " + warn.getMessage(),"warn");
        }
    }

    public void analizar() {
        try{
            codigoControl.analizar();
            this.enviarMensaje(null,"Analisis finalizado satisfactoriamente","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al analizar los códigos. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al analizar los códigos. " + warn.getMessage(),"warn");
        }
    }

    public UploadedFile getArchivoSubidoAntes() {
        return codigoControl.getArchivoSubidoAntes();
    }

    public void setArchivoSubidoAntes(UploadedFile archivoSubidoAntes) {
        codigoControl.setArchivoSubidoAntes(archivoSubidoAntes);
    }

    public UploadedFile getArchivoSubidoDespues() {
        return codigoControl.getArchivoSubidoDespues();
    }

    public void setArchivoSubidoDespues(UploadedFile archivoSubidoDespues) {
        codigoControl.setArchivoSubidoDespues(archivoSubidoDespues);
    }

    public File getArchivoAntes() {
        return codigoControl.getArchivoAntes();
    }

    public void setArchivoAntes(File archivoAntes) {
        codigoControl.setArchivoAntes(archivoAntes);
    }

    public List getArchivosAntes() {
        return codigoControl.getArchivosAntes();
    }

    public void setArchivosAntes(List archivosAntes) {
        codigoControl.setArchivosAntes(archivosAntes);
    }

    public List getArchivosDespues() {
        return codigoControl.getArchivosDespues();
    }

    public void setArchivosDespues(List archivosDespues) {
        codigoControl.setArchivosDespues(archivosDespues);
    }

    public File getArchivoDespues() {
        return codigoControl.getArchivoDespues();
    }

    public void setArchivoDespues(File archivoDespues) {
        codigoControl.setArchivoDespues(archivoDespues);
    }

    public boolean isCodigoAntesValido() {
        return codigoControl.isCodigoAntesValido();
    }

    public void setCodigoAntesValido(boolean codigoAntesValido) {
        codigoControl.setCodigoAntesValido(codigoAntesValido);
    }

    public boolean isCodigoDespuesValido() {
        return codigoControl.isCodigoDespuesValido();
    }

    public void setCodigoDespuesValido(boolean codigoDespuesValido) {
        codigoControl.setCodigoDespuesValido(codigoDespuesValido);
    }

    public FormatoConcreto getFormatoComparacionCodigo() {
        return codigoControl.getFormatoComparacionCodigo();
    }

    public void setFormatoComparacionCodigo(FormatoConcreto formatoComparacionCodigo) {
        codigoControl.setFormatoComparacionCodigo(formatoComparacionCodigo);
    }

    public int getNumAgregados() {
        return codigoControl.getNumAgregados();
    }

    public void setNumAgregados(int numAgregados) {
        codigoControl.setNumAgregados(numAgregados);
    }

    public int getNumIguales() {
        return codigoControl.getNumIguales();
    }

    public void setNumIguales(int numIguales) {
        codigoControl.setNumIguales(numIguales);
    }

    public int getNumEliminados() {
        return codigoControl.getNumEliminados();
    }

    public void setNumEliminados(int numEliminados) {
        codigoControl.setNumEliminados(numEliminados);
    }

    public int getNumModificados() {
        return codigoControl.getNumModificados();
    }

    public void setNumModificados(int numModificados) {
        codigoControl.setNumModificados(numModificados);
    }

    public CodigoControl getCodigoControl() {
        return codigoControl;
    }

    public void setCodigoControl(CodigoControl codigoControl) {
        this.codigoControl = codigoControl;
    }
    
    
}