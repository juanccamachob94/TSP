package a_web.b_backing;

import c_negocio.a_relacional.Meta;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class GeneralBean implements Serializable {

    //Permite enviar respuestas al cliente
    public void enviarMensaje(UIComponent boton, String mensaje, String tipo) {
        FacesContext context = FacesContext.getCurrentInstance();
        Severity s = null;
        switch (tipo) {
            case "info":
                s = FacesMessage.SEVERITY_INFO;
                break;
            case "warn":
                s = FacesMessage.SEVERITY_WARN;
                break;
            case "error":
                s = FacesMessage.SEVERITY_ERROR;
                break;
            case "fatal":
                s = FacesMessage.SEVERITY_FATAL;
                break;

        }
        if (boton == null)
            context.addMessage(null, new FacesMessage(s, mensaje, mensaje));
        else
            context.addMessage(boton.getClientId(context), new FacesMessage(s, mensaje, mensaje));
    }

    //Permite redireccionar a una página web
    protected void mostrarPagina(String nombrePagina) throws ExceptionFatal, ExceptionWarn {
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect(nombrePagina + ".xhtml");    
        }catch(Exception e){
            throw new ExceptionFatal("Error fatal al cargar la página " + nombrePagina + ".");
        }
    }

    //Permite ejecutar una función javascript sin parámetros
    protected void ejecutarJS(String funcion) {
        RequestContext.getCurrentInstance().execute(funcion + "()");
    }

    /*Permite ejecutar una función javascript con parámetros
    Cada parámetro debe acompañarse de una letra y dos puntos (:) para enclarecer el tipo de dato*/
    protected void ejecutarJS(String funcion, String... arreglo) {
        String parametros = "";
        String[] aux;
        for (int i = 0; i < arreglo.length; i++) {
            aux = arreglo[i].split(":");
            parametros += aux[0] == "c" ? "'" + aux[1] + "'," : aux[1] + ",";
        }
        parametros = parametros.substring(0, parametros.length() - 1);
        RequestContext.getCurrentInstance().execute(funcion + "(" + parametros + ")");
    }

    //Devuelve el Bean del paquete backing a partir del nombre de la clase literalmente escrito en cadena
    protected Object getManagedBean(String clase) throws ExceptionFatal {
        try{
        clase = "a_web.b_backing." + clase;
        FacesContext c = FacesContext.getCurrentInstance();
        String[] items = Helper.dividir(clase, ".");
        return c.getApplication().evaluateExpressionGet(c, "#{" + Helper.primeraLetraMinus(items[items.length - 1]) + "}", Class.forName(clase));
        }catch(Exception fatal){
            throw new ExceptionFatal("Error fatal al tratar de localizar " + clase + ".");
        }
    }

    //Para la creación de identificadores en xhtml se requieren caracteres simples y unidos
    public String unir(String cadena) {
        return cadena.replace(" ", "_").replace("%", "").replace("/", "_").replace("(", "").replace(")", "").replace("@", "arroba").replace(".", "_");
    }

    //Brinda el mapeo de los valores Java; acción por defecto de JSF.
    public void actualizar() {
        this.ejecutarJS("cuadrarLabels");
    }

    //Devuelve un objeto transformado a cadena para gestiones xhtml
    public String toString(Object o) {
        try {
            return o.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public List getLista() {
        List lista = new ArrayList();
        lista.add("");
        return lista;
    }

    //Permite sobreescribir el valor de un atributo como valor absoluto de lo que contenía
    public void hacerPositivo(AtributoCompuesto atributo) {
        try {
            atributo.setValor(Double.toString(Math.abs(Double.parseDouble(atributo.getValor()))));
        } catch (Exception e) {
            atributo.setValor("0.0");
        }
    }
    
    //Permite sobreescribir el valor de una meta como valor absoluto de lo que contenía
    public void hacerMetaPositiva(Meta meta) {
        try {
            meta.setValor(new BigDecimal(Math.abs(meta.getValor().doubleValue())));
        } catch (Exception e) {
            meta.setValor(new BigDecimal(0));
        }
    }

    //Permite sobreesscribir el valo de un atributo a un enterno mayor o igual a 0
    public void hacerNatural(AtributoCompuesto atributo) {
        try {
            atributo.setValor(Integer.toString((int) Math.abs(Double.parseDouble(atributo.getValor()))));
        } catch (Exception e) {
            atributo.setValor("0");
        }
    }
    
    public void hacerPorcentual(AtributoCompuesto atributo){
        try {
            double valor = Math.abs(Double.parseDouble(atributo.getValor()));
            if(valor > 100) valor = 100;
            atributo.setValor(Double.toString(valor));
        } catch (Exception e) {
            atributo.setValor("0.0");
        }   
    }

    public void hacerPorcentualDecimal(AtributoCompuesto atributo){
        try {
            double valor = Math.abs(Double.parseDouble(atributo.getValor()));
            if(valor > 1) valor = 1;
            atributo.setValor(Double.toString(valor));
        } catch (Exception e) {
            atributo.setValor("0.0");
        }   
    }
    
    public String formatearDate(Date fecha){
        return Helper.formatearDate(fecha, "dd/M/yyyy");
    }
    
    //Permite gestionar en la iterfaz las cadenas muy largas, evitando desencajar elementos
    public String cadenaIncompleta(String cadena, int numCaracteres) {
        int t = cadena.length();
        if (t > numCaracteres + 3) return cadena.substring(0, numCaracteres) + "...";
        return cadena;
    }
    
    public String getMensajeSeleccion() {
        return TSP.mensajeSeleccion;
    }

}