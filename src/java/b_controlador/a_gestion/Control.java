/**
 * Controlador genérico para la utilización de elementos básicos de gestión.
 */
package b_controlador.a_gestion;

import e_utilitaria.ExceptionFatal;
import e_utilitaria.Helper;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class Control implements Serializable {
    public String unir(String cadena) {
        return cadena.replace(" ", "_").replace("%", "").replace("/", "_").replace("(", "").replace(")", "").replace("@", "arroba").replace(".", "_");
    }
    /**
     * Obtiene el controlador requerido
     * @param clase
     * @return 
     * @throws e_utilitaria.ExceptionFatal 
     */
    public Object getControlador(String clase) throws ExceptionFatal {
        try {
            clase = "b_controlador.a_gestion." + clase;
            FacesContext c = FacesContext.getCurrentInstance();
            String[] items = Helper.dividir(clase, ".");
            return c.getApplication().evaluateExpressionGet(c, "#{" + Helper.primeraLetraMinus(items[items.length - 1]) + "}", Class.forName(clase));
        } catch (Exception e) {
            throw new ExceptionFatal("Error fatal al tratar de localizar " + clase + ". " + e.getMessage());
        }
    }
    
    /**
     * Transforma un entero en byte
     * @param n
     * @return 
     */
    public byte b(int n) {
        return (byte) n;
    }
    
    /**
     * Permite saber si la cadena no contiene nada. Muy útil para validaciones
     * @param cadena
     * @return 
     */
    public boolean vacio(String cadena) {
        if (cadena == null) return true;
        return cadena.equals("");
    }
}