/**
 * Bean para la gestión de los formatos SUMQ y SUMP
 */
package a_web.b_backing;

import b_controlador.a_gestion.CalidadControl;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;

@ManagedBean
@SessionScoped
public class CalidadBean extends GeneralBean {

    @ManagedProperty("#{calidadControl}")
    CalidadControl calidadControl;
    
    private UIComponent btnGuardarSump;
    private UIComponent btnGuardarSumq;
    

    public CalidadBean() {
    }

    /**
     * Carga el formato SUMP
     * @param pagSump
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarSump(boolean pagSump) throws ExceptionFatal, ExceptionWarn {
        try{
            calidadControl.cargarSump(pagSump);
        }catch(ExceptionFatal fatal){
                throw fatal;
        }catch(ExceptionWarn warn){
            if(pagSump) this.enviarMensaje(null,"Error al cargar Sump." + warn.getMessage(),"warn");
            else throw warn;
        }
    }

    /**
     * Carga el formato SUMQ
     * @param pagSumq
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarSumq(boolean pagSumq) throws ExceptionFatal, ExceptionWarn {
        try{
            calidadControl.cargarSumq(pagSumq);
        }catch(ExceptionFatal fatal){
                throw fatal;
        }catch(ExceptionWarn warn){
            if(pagSumq) enviarMensaje(null,"Error al cargar Sumq. " + warn.getMessage(),"warn");
            else throw warn;
        }
    }
    
    /**
     * Guarda el formato SUMP
     */
    public void guardarFormatoSump(){
        try{
            calidadControl.guardarFormatoSump();
            this.enviarMensaje(this.btnGuardarSump,"Formato Sump guardado satisfactoriamente.","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarSump,"Error al guardar el formato Sump. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarSump,"Error al guardar el formato Sump. " + warn.getMessage(),"warn");
        }
    }
    
    /**
     * Guarda el formato SUMQ
     */
    public void guardarFormatoSumq(){
        try{
            calidadControl.guardarFormatoSumq();
            this.enviarMensaje(this.btnGuardarSump,"Formato Sumq guardado satisfactoriamente.","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarSump,"Error al guardar el formato Sumq. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarSump,"Error al guardar el formato Sumq. " + warn.getMessage(),"warn");
        }
    }
    
    /**
     * Totaliza el código para el formato SUMP
     * @param atrCodigo 
     */
    public void totalizarCodigoSump(AtributoCompuesto atrCodigo){
        try{
            this.hacerPositivo(atrCodigo);
            calidadControl.totalizarCodigoSump(atrCodigo);
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(this.btnGuardarSump,"Error al guardar totalizar el código. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(this.btnGuardarSump,"Error al guardar totalizar el código. " + warn.getMessage(),"warn");
        }
    }
    
    public FormatoConcreto getSump() {
        return calidadControl.getSump();
    }

    public void setSump(FormatoConcreto sump) {
        calidadControl.setSump(sump);
    }

    public FormatoConcreto getSumq() {
        return calidadControl.getSumq();
    }

    public void setSumq(FormatoConcreto sumq) {
        calidadControl.setSumq(sumq);
    }

    public CalidadControl getCalidadControl() {
        return calidadControl;
    }

    public void setCalidadControl(CalidadControl calidadControl) {
        this.calidadControl = calidadControl;
    }

    public UIComponent getBtnGuardarSump() {
        return btnGuardarSump;
    }

    public void setBtnGuardarSump(UIComponent btnGuardarSump) {
        this.btnGuardarSump = btnGuardarSump;
    }

    public UIComponent getBtnGuardarSumq() {
        return btnGuardarSumq;
    }

    public void setBtnGuardarSumq(UIComponent btnGuardarSumq) {
        this.btnGuardarSumq = btnGuardarSumq;
    }
}