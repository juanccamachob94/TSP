/**
 * Estructura para la gestión de formatos TSP
 */
package c_negocio.b_no_relacional.formato;

import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import e_utilitaria.Serial;
import java.util.ArrayList;
import java.util.List;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class FormatoConcreto extends Formato{

    @MongoObjectId
    private String _id;
    private String autor;
    private String rol;
    private List<AtributoCompuesto> atributos;

    public FormatoConcreto() {
        super();
    }

    public FormatoConcreto(String nombre, String proyecto, int ciclo) {
        super(nombre,proyecto,ciclo);
        this.atributos = new ArrayList<AtributoCompuesto>();
    }

    public FormatoConcreto(String nombre, String rol, String proyecto, int ciclo) {
        super(nombre,proyecto,ciclo);
        this.rol = rol;
        this.atributos = new ArrayList<AtributoCompuesto>();
    }

    public FormatoConcreto(String nombre, String proyecto, int ciclo, String autor) {
        super(nombre,proyecto,ciclo);
        this.autor = autor;
        this.atributos = new ArrayList<AtributoCompuesto>();
    }

    public FormatoConcreto(String nombre, String autor, String rol, String proyecto, int ciclo) {
        super(nombre,proyecto,ciclo);
        this.autor = autor;
        this.rol = rol;
        this.atributos = new ArrayList<AtributoCompuesto>();
    }

    /**
     * Permite adicionar un atributo al formato
     * @param atributo 
     */
    public void agregarAtributo(AtributoCompuesto atributo) {
        this.atributos.add(atributo);
    }

    /**
     * Permite obtener un atributo a partir de su nombre
     * @param atributo
     * @return 
     */
    public AtributoCompuesto getAtributo(String atributo) {
        int t = this.atributos.size();
        AtributoCompuesto atr;
        String nomAtributo;
        for (int i = 0; i < t; i++) {
            atr = this.atributos.get(i);
            nomAtributo = atr.getAtributo();
            if (nomAtributo != null)
                if (nomAtributo.equals(atributo))
                    return atr;
        }
        return null;
    }

    /**
     * Si el formato tiene id nulo, éste no se ha consultado de la base de datos
     * @return 
     */
    public boolean estaGuardado(){
        return this._id != null;
    }

    /**
     * Serializa todo del formato, especificando si incluir el id en el json resultante
     * @param conId
     * @return 
     */
    public String toSerial(boolean conId){
        FormatoConcreto miFormato = (FormatoConcreto)Serial.clonar(this);
        miFormato.setFecha(null);
        if(conId) return Serial.serializar(miFormato);
        String id = this._id;
        this._id = null;
        String serial = Serial.serializar(this);
        this._id = id;
        return serial;
    }

    /**
     * Serializa el encabezado del formato, especificando si incluir el id en el json resultante
     * @param conId
     * @return 
     */
    public String serialEncabezado(boolean conId){
        FormatoConcreto miFormato = (FormatoConcreto)Serial.clonar(this);
        miFormato.setFecha(null);
        if(!conId) miFormato.setId(null);
        miFormato.setAtributos(null);
        return Serial.serializar(miFormato);
    }

    /**
     * Valida si todos los atributos poseen un valor diferente de nulo y de vacío.
     * @return 
     */
    public boolean atributosLlenos() {
        if(this.atributos != null)
            return numAtributoVacios(this.atributos, 0) == 0;
        return false;
    }

    /**
     * Identifica el número de atributos con valor nulo o vacío en una lista de atributos.
     * @param atributos
     * @param vaciosAcumulados
     * @return 
     */
    private int numAtributoVacios(List atributos, int vaciosAcumulados) {
        int t = atributos.size();
        AtributoCompuesto atributo;
        List subAtributos;
        String valor;
        for (int i = 0; i < t; i++) {
            atributo = (AtributoCompuesto) atributos.get(i);
            subAtributos = atributo.getAtributos();
            if (subAtributos == null) {
                valor = atributo.getValor();
                if (valor == null) vaciosAcumulados += 1;
                else if (valor.trim().equals(""))
                        vaciosAcumulados += 1;
            } else vaciosAcumulados += numAtributoVacios(subAtributos, vaciosAcumulados);
        }
        return vaciosAcumulados;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<AtributoCompuesto> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<AtributoCompuesto> atributos) {
        this.atributos = atributos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}