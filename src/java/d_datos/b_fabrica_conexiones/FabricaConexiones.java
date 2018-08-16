/**
 * Fabrica que entrega una instancia de la conexión a la base de datos solicitada
 */
package d_datos.b_fabrica_conexiones;

import d_datos.c_datasource.DataSource;
import d_datos.c_datasource.SourceMongo;
import d_datos.c_datasource.SourcePostgres;

public class FabricaConexiones extends FabricaAbstConexiones {
    
    /**
     * Permite conectar al motor especificado en el parámetro
     * @param motor
     * @return
     * @throws ClassNotFoundException 
     */
    public static DataSource conectar(String motor) throws ClassNotFoundException{
        switch(motor){
            case "postgres": return SourcePostgres.getInstancia();
            case "mongo": return SourceMongo.getInstancia();
        }
        return null;
    }
}