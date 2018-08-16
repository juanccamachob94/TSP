/**
 * Permite la serialización y deserialización de objetos mediante JSON
 */
package e_utilitaria;
import com.google.gson.*;

public class Serial{

    /**
     * Convierte un objeto a una cadena json
     * @param obj
     * @return 
     */
    public static String serializar(Object obj){
            return new Gson().toJson(obj);
    }

    /**
     * Convierte una cadena json a un objeto
     * @param cadena
     * @param obj
     * @return 
     */
    public static Object deserializar(String cadena, Object obj){
            return new Gson().fromJson(cadena,obj.getClass());
    }

    /**
     * Entrega una instancia diferente del objeto enviado
     * @param obj
     * @return 
     */
    public static Object clonar(Object obj){
            return deserializar(serializar(obj),obj);
    }
}