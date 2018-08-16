/**
 * Conecta con la base de datos no relacional y ejecuta operaciones básicas con la base de datos.
 */
package d_datos.c_datasource;

import e_utilitaria.ExceptionFatal;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import com.mongodb.MongoClient;
import e_utilitaria.Helper;
import e_utilitaria.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SourceMongo implements DataSource {

    private static DataSource instancia;
    private MongoCollection coleccion;
    
    private SourceMongo() throws ClassNotFoundException {
        this.coleccion = (new Jongo(new MongoClient("localhost", 27017).getDB("tsp"))).getCollection("formatos");
    }

    /**
     * Devuelve la instancia en caso de exitir. En caso contrario, crea otra.
     * @return
     * @throws ClassNotFoundException 
     */
    public static DataSource getInstancia() throws ClassNotFoundException {
        if (instancia == null) instancia = new SourceMongo();
        return instancia;
    }

    @Override
    public Object consultarObjeto(String query) throws ExceptionFatal {
        try{
            return this.coleccion.findOne(query).as(FormatoConcreto.class);
        }catch(Exception e){
            try{
                String mensaje = e.getMessage();
                String jsonOriginal = "";
                char m[] = mensaje.toCharArray();
                int i;
                int k;
                char json[];
                for(i = 0; i < m.length; i++)
                    if(m[i] == '{'){
                        jsonOriginal = mensaje.substring(i,mensaje.length());
                        json = jsonOriginal.toCharArray();
                        for(int j = 1; j < json.length; j++)
                            if(json[j] == '$'){
                                for(k = j; k < json.length; k++)
                                    if(json[k] == '}'){
                                        jsonOriginal = jsonOriginal.substring(0,j - 3) + jsonOriginal.substring(j + 8,k) + jsonOriginal.substring(k + 1,jsonOriginal.length());
                                        break;
                                    }
                                break;
                            }
                        break;
                    }
                return Serial.deserializar(jsonOriginal, new FormatoConcreto());
            }catch(Exception subE){
                throw new ExceptionFatal("Error al consultar el objeto en el motor mongo. " + subE.getMessage());
            }
        }
    }

    @Override
    public List consultarLista(String query) throws ExceptionFatal {
        List documentos = new ArrayList();
        try{
            Iterable resultado = this.coleccion.find(query).as(FormatoConcreto.class);
            for (Iterator it = resultado.iterator(); it.hasNext();)
                documentos.add(it.next());
            return documentos;
        }catch(Exception e){
            try{
                String objs[] = Helper.dividir(e.getMessage(),"{ \"_id\" : { \"$oid\"");
                char m[];
                for(int i = 1; i < objs.length; i+=2){
                    if(objs[i].endsWith("]")) objs[i] = objs[i].substring(0,objs[i].length());
                    m = objs[i].toCharArray();
                    for(int j = 0; j < m.length; j++)
                        if(m[j] == '}'){
                            documentos.add(Serial.deserializar("{ \"_id\" " + objs[i].substring(0, j) + objs[i].substring(j + 1, objs[i].length()), new FormatoConcreto()));   
                            break;
                        }
                }
                return documentos;
            }catch(Exception subE){
                throw new ExceptionFatal("Error al consultar el objeto en el motor mongo. " + subE.getMessage());
            }
        }
    }

    @Override
    public List consultarLista(String query, int n) throws ExceptionFatal {
        throw new ExceptionFatal("Error al consultar la lista de datos en el motor mongo.");
    }


    @Override
    public void insertar(Object obj) throws ExceptionFatal {
        try{
            this.coleccion.insert(obj);
        }catch(Exception e){
            throw new ExceptionFatal("Error al insertar los datos en el motor mongo. " + e.getMessage());
        }
    }

    /**
     * Sólo inserta registros nuevos
     * @param objs
     * @throws ExceptionFatal 
     */
    @Override
    public void insertar(List objs) throws ExceptionFatal {
        try{
            int t = objs.size();
            for (int i = 0; i < t; i++)
                insertar(objs.get(i));
        }catch(Exception e){
            throw new ExceptionFatal("Error al insertar los datos en el motor mongo. " + e.getMessage());
        }
    }

    /**
     * Actualiza o guarda o un registro
     * @param obj
     * @throws ExceptionFatal 
     */
    @Override
    public void guardar(Object obj) throws ExceptionFatal {
        try{
            this.coleccion.save(obj);
        }catch(Exception e){
            throw new ExceptionFatal("Error al guardar los datos en el motor mongo. " + e.getMessage());
        }
    }

    @Override
    public void actualizar(String query) throws ExceptionFatal {
        try{
            String[] jsons = query.split("\\|");
            if (jsons[0].split(":").length == 1)
                this.coleccion.update("{_id:#}", new ObjectId(jsons[0])).with(jsons[1]);
            else this.coleccion.update(jsons[0]).with(jsons[1]);
        }catch(Exception e){
            throw new ExceptionFatal("Error al actualizar los datos en el motor mongo. " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Object obj) throws ExceptionFatal {
        throw new ExceptionFatal("Error al actualizar los datos en el motor mongo.");
    }

    @Override
    public void actualizar(List objs) throws ExceptionFatal {
        try{
            int t = objs.size();
            for (int i = 0; i < t; i++)
                actualizar(objs.get(i));
        }catch(Exception e){
            throw new ExceptionFatal("Error al actualizar los datos en el motor mongo. " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String str) throws ExceptionFatal {
        try{
            this.coleccion.remove(new ObjectId(str));
        }catch(Exception e){
            throw new ExceptionFatal("Error al eliminar los datos en el motor mongo. " + e.getMessage());
        }        
    }
    
    @Override
    public void eliminar(Object obj) throws ExceptionFatal {
        throw new ExceptionFatal("Error al eliminar los datos en el motor mongo.");
    }

    @Override
    public void eliminar(List objs) throws ExceptionFatal {
        try{
            int t = objs.size();
            for (int i = 0; i < t; i++)
                eliminar(objs.get(i));
        }catch(Exception e){
            throw new ExceptionFatal("Error al eliminar los datos en el motor mongo. " + e.getMessage());
        }
    }
}
