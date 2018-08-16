/**
 * Revisor de código para el lenguaje Java
 */
package c_negocio.c_otros;

import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import java.util.List;

public class RevisorCodigoJava extends RevisorCodigo {

    private static RevisorCodigoJava instancia;

    /**
     * Devuelve la instancia en caso de existir o la crea y devuelve en caso contrario.
     * @return 
     */
    public static RevisorCodigoJava getInstancia() {
        if (instancia == null)
            instancia = new RevisorCodigoJava();
        return instancia;
    }

    private RevisorCodigoJava(){
    }

    @Override
    public boolean esAbstracta(List<String> lineasArchivo) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = lineasArchivo.size();
            String concatenadoLineas = "";
            String linea;
            for (int i = 0; i < t; i++) {
                linea = lineasArchivo.get(i);
                concatenadoLineas += linea.trim();
                if (linea.contains("{")) break;
            }
            return concatenadoLineas.contains("abstract") || concatenadoLineas.contains("interface");
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar si la clase Java es abstracta. " + e.getMessage());
        }
    }

    @Override
    public String nombreFuncion(String linea) throws ExceptionFatal, ExceptionWarn {
        try{
            char[] chs = linea.toCharArray();
            int j = 0;
            String nombre;
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] == '(') {
                    nombre = "";
                    for (j += 1; j < i; j++)
                        nombre += chs[j];
                    return nombre;
                }
                if (chs[i] == ' ') j = i;
            }
            return linea;
        }catch(Exception e){
            throw new ExceptionFatal("Error al obtener el nombre de la función Java. " + e.getMessage());
        }
    }

    @Override
    public String cadenaParametros(String linea) throws ExceptionFatal, ExceptionWarn {
        try{
            char[] chs = linea.toCharArray();
            String nombre;
            for (int i = 0; i < chs.length; i++)
                if (chs[i] == '(')
                    for (int j = chs.length - 1; j > i; j--)
                        if (chs[j] == ')') {
                            nombre = "";
                            for (int k = i + 1; k < j; k++)
                                nombre += chs[k];
                            return nombre;
                        }
            return "";
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar la cadena de parámetros de la función Java. " + e.getMessage());
        }
    }

    @Override
    public boolean contieneLogica(String cadena) throws ExceptionFatal, ExceptionWarn {
        try{
            if (cadena.contains("!")) return true;
            if (cadena.contains("&")) return true;
            if (cadena.contains("|")) return true;
            if (cadena.contains("?")) return true;
            if (cadena.contains("=")) return true;
            if (cadena.contains("<")) return true;
            if (cadena.contains(">")) return true;
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar si la línea Java contiene lógica. " + e.getMessage());
        }
    }

    @Override
    public boolean esReservada(String cadena) throws ExceptionFatal, ExceptionWarn {
        try{
            if (cadena.contains("if ")) return true;
            if (cadena.contains("else ")) return true;
            if (cadena.contains("for ")) return true;
            if (cadena.contains("while ")) return true;
            if (cadena.contains("switch ")) return true;
            if (cadena.contains("catch ")) return true;
            if (cadena.contains("if else ")) return true;
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("Error la identificar las palabras reservadas para el lenguaje Java. " + e.getMessage());
        }
    }

    @Override
    public boolean comienzaFuncion(String linea) throws ExceptionFatal, ExceptionWarn {
        try{
            linea = linea.trim();
            if (linea.contains("void")) return true;
            char[] chs = linea.toCharArray();
            int k;
            String antesParentesis;
            String lineaPegada;
            if (contieneLogica(linea)) return false;
            if(esReservada(linea)) return false;
            if (esComentario(linea)) return false;
            for (int i = 0; i < chs.length; i++)
                if (chs[i] == '(') {
                    //Se valida que sea la creación de una función y no un llamado
                    antesParentesis = "";
                    for (int j = 0; j < i; j++)
                        antesParentesis += chs[j];
                    if (Helper.dividir(antesParentesis, " ").length == 1) return false;
                    else {
                        lineaPegada = linea.replaceAll(" ", "");
                        if ((lineaPegada.endsWith("{") || lineaPegada.endsWith(")")) && !lineaPegada.endsWith(";"))
                            return true;
                    }
                }
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar el comienzo de la función Java. " + e.getMessage());
        }
    }

    @Override
    public boolean esComentario(String linea) throws ExceptionFatal, ExceptionWarn {
        try{
            return linea.startsWith("//") || linea.startsWith("/*");
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar si la línea es un comentario Java. " + e.getMessage());
        }
    }

    @Override
    public boolean extensionCorrecta(String nombreArchivo) throws ExceptionFatal, ExceptionWarn {
        try{
            return nombreArchivo.endsWith(".java");
        }catch(Exception e){
            throw new ExceptionFatal("Error al verificar si la extensión del archivo pertenece al lenguaje Java. " + e.getMessage());
        }
    }

    @Override
    public String informacionFuncion(int i, List<String> lineasArchivo) throws ExceptionFatal, ExceptionWarn {
        try {
            int fnTerminada = 0;
            int t = lineasArchivo.size();
            String linea = "";
            String lineaTrim;
            char[] chs;
            int numLineasCodigoRealFunc = 0;
            boolean llaveInicioFuncion = false;
            String funcion = "";
            boolean comienzaCadena;
            int j;
            for (j = i; j < t; j++) {
                linea = lineasArchivo.get(j).replaceAll(" ", "");
                chs = linea.toCharArray();
                comienzaCadena = false;
                for (int k = 0; k < chs.length; k++) {
                    if(chs[k] == '"')
                        comienzaCadena = !comienzaCadena;
                    if (chs[k] == '{')
                        if(!comienzaCadena){
                            fnTerminada += 1;
                            llaveInicioFuncion = true;
                        }
                    if (chs[k] == '}')
                        if (j != t - 1)
                            if(!comienzaCadena)
                                fnTerminada -= 1;
                }
                if (llaveInicioFuncion)
                    if (fnTerminada == 0)
                        break;
                lineaTrim = linea.trim();
                if (!lineaTrim.equals("") && !lineaTrim.startsWith("//") && !lineaTrim.equals("{") && !lineaTrim.equals("}")) {
                    numLineasCodigoRealFunc += 1;
                }
                funcion += linea.replaceAll(" ", "");
            }
            int posUltimLineaFuncion = j;
            chs = funcion.toCharArray();
            String contenido = "";
            for (j = 0; j < chs.length; j++)
                if (chs[j] == '{') {
                    contenido = funcion.substring(j + 1, chs.length);
                    break;
                }
            if (!llaveInicioFuncion)
                throw new ExceptionWarn("Inicio incorrecto en la función " + this.nombreFuncion(lineasArchivo.get(i)) + " sobre la línea " + Integer.toString(i + 1));
            if (fnTerminada != 0)
                throw new ExceptionWarn("Finalización incorrecta de la función " + this.nombreFuncion(lineasArchivo.get(i)) + " sobre la línea " + Integer.toString(i + 1));
            this.setNumLineasFuncion(posUltimLineaFuncion - i);
            this.setNumLineasCodigoRealFuncion(numLineasCodigoRealFunc - 1);//Se le resta el título de la función
            return contenido;
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("No es posible capturar la información de la función " + this.nombreFuncion(lineasArchivo.get(i)) + " sobre la línea " + Integer.toString(i + 1));
        }
    }
}