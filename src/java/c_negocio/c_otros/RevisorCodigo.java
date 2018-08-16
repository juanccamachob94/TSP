/**
 * Revisar de código con los elementos generales de revisión de código
 */
package c_negocio.c_otros;

import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.List;

public class RevisorCodigo {

    protected int numLineasCodigoRealFuncion;
    protected int numLineasFuncion;

    public int getNumLineasRealCodigoFuncion() {
        return this.numLineasCodigoRealFuncion;
    }

    protected void setNumLineasCodigoRealFuncion(int numLineasCodigoRealFuncion) {
        this.numLineasCodigoRealFuncion = numLineasCodigoRealFuncion;
    }

    public int getNumLineasFuncion() {
        return this.numLineasFuncion;
    }

    protected void setNumLineasFuncion(int numLineasFuncion) {
        this.numLineasFuncion = numLineasFuncion;
    }

    /**
     * Permite indicar si un archivo no posee implementación de funciones
     * @param lineasArchivo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean esAbstracta(List<String> lineasArchivo) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al verificar si la clase es abstracta.");
    }

    /**
     * Brinda el nombre de la función alojada en una línea del archivo
     * @param linea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String nombreFuncion(String linea) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al obtener el nombre de la función.");
    }

    /**
     * Devuelve la cadena encontrada dentro de los paréntesis de la función
     * @param linea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String cadenaParametros(String linea) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al obtener la cadena de parámetros de la función.");
    }

    /**
     * Valida si la cadena recibida contiene comparadores lógicos como >, < &, |, etc.
     * @param cadena
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean contieneLogica(String cadena) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al identificar si contiene o no lógica.");
    }

    /**
     * Valida si la cadena recibida contiene valores reservados de if, while, etc.
     * @param cadena
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean esReservada(String cadena) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al identificar elementos reservados del lenguaje.");
    }

    /**
     * Indica si la línea recibida como parámetro es una función o no
     * @param linea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean comienzaFuncion(String linea) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al validar si comienza la función.");
    }

    /**
     * Indica si la línea recibida como parámetro es un comentario o no
     * @param linea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean esComentario(String linea) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al identificar si la línea es o no un comentario.");
    }

    /**
     * Indica si la extensión del archivo es válido para el lenguaje
     * @param nombreArchivo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean extensionCorrecta(String nombreArchivo) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al validar si la extensión es correcta.");
    }

    /**
     * Provee el contenido de la función y sobreescribe el valor intero del tamaño del función
     * @param i
     * @param lineasArchivo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String informacionFuncion(int i, List<String> lineasArchivo) throws ExceptionFatal, ExceptionWarn {
        throw new ExceptionFatal("Error al obtener información concerniente a la función.");
    }
}
