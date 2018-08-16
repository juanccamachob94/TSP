/**
 * Clase auxiliar para operaciones básicas
 */
package e_utilitaria;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Helper {

    /**
     * Cumple la misma función que split pero 4 veces más eficiente
     * @param cadena
     * @param c
     * @return 
     */
    public static String[] dividir(String cadena, String c) {
        cadena += c;
        int i, k, l;
        int t = cadena.length();
        int tc = c.length();
        char cr = c.charAt(0);
        String aux;
        int u = 0;
        int j = 0;
        for (i = 0; i < t; i++)
            if (cadena.charAt(i) == cr) {
                k = i;
                for (l = 0; l < tc; l++) {
                    if (c.charAt(l) == cadena.charAt(i)) i++;
                    if (i == t) break;
                }
                if (i - k == tc) {
                    i--;
                    j++;
                } else i = k;
            }
        String[] resultado = new String[j];
        j = 0;
        for (i = 0; i < t; i++)
            if (cadena.charAt(i) == cr) {
                k = i;
                for (l = 0; l < tc; l++) {
                    if (c.charAt(l) == cadena.charAt(i)) i++;
                    if (i == t) break;
                }
                if (i - k == tc) {
                    aux = "";
                    for (l = u; l < k; l++)
                        aux += cadena.charAt(l) + "";
                    resultado[j++] = aux;
                    u = i;
                    i--;
                } else i = k;
            }
        return resultado;
    }

    /**
     * Permite dar formato a la fecha. Generalmente el formato es dd/M/yyyy como parámetro
     * @param date
     * @param formato
     * @return 
     */
    public static String formatearDate(Date date, String formato) {
        return new SimpleDateFormat(formato).format(new Timestamp(date.getTime()));
    }

    /**
     * Devuelve el número de semanas entre dos fechas sin ser específico con las horas/minutos/segundos
     * @param fechaInicial
     * @param fechaFinal
     * @return 
     */
    public static int semanasEntreFechas(Date fechaInicial, Date fechaFinal) {
        Date fi = new Date();
        fi.setTime(fechaInicial.getTime());
        fi.setHours(0);
        fi.setMinutes(0);
        fi.setSeconds(0);
        return (int) ((fechaFinal.getTime() - fi.getTime()) / (60000 * 1440 * 7));
    }

    /**
     * Devuelve la misma cadena con la primera letra en minúscula
     * @param cadena
     * @return 
     */
    public static String primeraLetraMinus(String cadena) {
        return Character.toLowerCase(cadena.charAt(0)) + cadena.substring(1);
    }

    /**
     * Dada una lista primaria llena de listas secundarias; cada registro secundario es un registro
     * de alguna tabla (metafóricamente), y esta fución brinda el objeto anterior. Un ejemplo de ello
     * es que el registro por parámetro es el primer secudario del último primario; la función le devolverá
     * el ultimo secuendario del pénúltimo primario
     * @param primaria
     * @param secundaria
     * @param registro
     * @return 
     */
    public static Object registroAnterior(List primaria, List secundaria, Object registro) {
        int t = secundaria.size();
        String sec;
        List secun;
        int pos = -1;
        for (int i = 0; i < t; i++) {
            if (((Object) secundaria.get(i)).toString().equals(registro.toString())) {
                pos = i;
                break;
            }
        }

        if (pos == 0) {
            sec = secundaria.toString();
            t = primaria.size();
            for (int i = 0; i < t; i++)
                if (primaria.get(i).toString().equals(sec)) {
                    pos = i;
                    break;
                }
            if (pos == 0) return null;
            else {
                secun = (List) primaria.get(pos - 1);
                return secun.get(secun.size() - 1);
            }
        } else return secundaria.get(pos - 1);
    }

    /**
     * Dada una lista primaria llena de listas secundarias; cada registro secundario es un registro
     * de alguna tabla (metafóricamente), y esta función brinda el objeto siguiente. Un ejemplo de ello
     * es que el registro por parámetro es el último secudario del primer primario; la función le devolverá
     * el primer secuendario del segundo primario
     * @param primaria
     * @param secundaria
     * @param registro
     * @return 
     */
    public static Object registroPosterior(List primaria, List secundaria, Object registro) {
        int t = secundaria.size();
        String sec;
        List secun;
        int pos = -1;
        for (int i = 0; i < t; i++)
            if (((Object) secundaria.get(i)).toString().equals(registro.toString())) {
                pos = i;
                break;
            }

        if (pos == t - 1) {
            sec = secundaria.toString();
            t = primaria.size();
            for (int i = 0; i < t; i++)
                if (primaria.get(i).toString().equals(sec)) {
                    pos = i;
                    break;
                }
            if (pos == t - 1) return null;
            else {
                secun = (List) primaria.get(pos + 1);
                return secun.get(0);
            }
        } else return secundaria.get(pos + 1);
    }

    /**
     * Permite transformar milisegundos a minutos
     * @param miliSegundos
     * @return 
     */
    public static double miliSegAMinutos(Long miliSegundos) {
        return Math.rint((((double) miliSegundos) / 60000) * 10000) / 10000;
    }

    /**
     * A partir del nombre del día de la semana, se devuelve el id
     * @param nombre
     * @return 
     */
    public static int diaSemana(String nombre) {
        int numDias = TSP.diasSemana.size();
        for(int i = 0; i < numDias; i++){
            if(((String)TSP.diasSemana.get(i)).equals(nombre))
                return i;
        }
        return -1;
    }

    /**
     * A partir del id de la semana se devuelve el número
     * @param n
     * @return 
     */
    public static String diaSemana(int n) {
        try{
            return (String) TSP.diasSemana.get(n);
        }catch(Exception e){
            return "[ERROR]";
        }
    }

    /**
     * Obtiene el número de una cadena; de presentar error devolverá cero
     * @param cadena
     * @return 
     */
    public static double extraerNumero(String cadena) {
        try {
            return redondear(Double.parseDouble(cadena),2);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Redondea un valor doble al número de decimales indicado.
     * @param valorInicial
     * @param numeroDecimales
     * @return 
     */
    public static double redondear(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }

    /**
     * Une dos json agregando el segundo como el último elemento del primero.
     * Ejemplo: concatenarJSONS("{'x':'y'}","{'z','w'}") = {'x':'y','z':'w'}
     * @param json1
     * @param json2
     * @return 
     */
    public static String concatenarJSONS(String json1, String json2){
        return json1.substring(0,json1.length() - 1) + "," + json2.substring(0,json2.length()) + "}";
    }

    /**
     * Reemplaza una cadena de sólo caracteres simples.
     * @param cadena
     * @return 
     */
    public static String soloCaracteresSimples(String cadena) {
        cadena = cadena.replaceAll("Á", "A").replaceAll("É", "E").replaceAll("Í", "I").replaceAll("Ó", "O").replaceAll("Ú", "U");
        cadena = cadena.replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "u");
        return cadena.replaceAll("[^\\w\\s]", " ");
    }
}