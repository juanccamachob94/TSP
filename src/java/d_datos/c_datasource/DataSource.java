/**
 * Interfaz para ejecutar operaciones a una base de datos
 */
package d_datos.c_datasource;

import java.util.List;

public interface DataSource {

    /**
     * Consulta un objeto a partir de una sentencia
     * @param query
     * @return
     * @throws Exception 
     */
    public Object consultarObjeto(String query) throws Exception;

    /**
     * Para la consulta a partir de una sentencia
     * @param query
     * @return
     * @throws Exception 
     */
    public List consultarLista(String query) throws Exception;

    /**
     * Una consulta con el número de registros limitado a los primeros n
     * @param query
     * @param n
     * @return
     * @throws Exception 
     */
    public List consultarLista(String query, int n) throws Exception;

    /**
     * Insertar un objeto
     * @param obj
     * @throws Exception 
     */
    public void insertar(Object obj) throws Exception;

    /**
     * Insertar una lista de objetos
     * @param objs
     * @throws Exception 
     */
    public void insertar(List objs) throws Exception;

    /**
     * A diferencia de insertar, se basa en el concepto de localizar el identificador y sobreescribir
     */
    public void guardar(Object obj) throws Exception;

    /**
     * Actualizar con base en una sentencia query
     * @param query
     * @throws Exception 
     */
    public void actualizar(String query) throws Exception;

    /**
     * Actualizar un objeto
     * @param obj
     * @throws Exception 
     */
    public void actualizar(Object obj) throws Exception;

    /**
     * Actualizar una lista de objetos
     * @param objs
     * @throws Exception 
     */
    public void actualizar(List objs) throws Exception;

    /**
     * Eliminar un objeto
     * @param obj
     * @throws Exception 
     */
    public void eliminar(Object obj) throws Exception;
    
    /**
     * Eliminar un objeto a partir de un identificador
     * @param str
     * @throws Exception 
     */    
    public void eliminar(String str) throws Exception;

    /**
     * Eliminar lista de objetos 
     * @param objs
     * @throws Exception 
     */
    public void eliminar(List objs) throws Exception;
}