/**
 * Permite gestionar lo relacionado al formato strat
 */
package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Ciclo;
import c_negocio.a_relacional.CicloId;
import c_negocio.a_relacional.Parte;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import d_datos.a_dao.ProyectoDAO;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.TSP;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@SessionScoped
public class EstrategiaControl extends Control{

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private Proyecto proyecto;
    private Parte nuevaParte;
    private TreeNode nodoProyecto;

    private int idParteConsultada;
    private Parte parteCopia;

    private FormatoConcreto strat;
    private Map<String, List> headersStrat;

    private List nodosPartes;
    private List registrosStrat;//TODOS. Lista de listas
    
    private List partesProductos;

    public EstrategiaControl() {
    }

    /**
     * Carga el formato strat actual
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarStrat() throws ExceptionFatal, ExceptionWarn {
        try {
            this.idParteConsultada = -1;
            Parte parte;
            this.proyecto = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            cargarHeadersStrat();
            cargarArbolPartes();

            this.registrosStrat = new ArrayList();
            int t = this.nodosPartes.size();
            for (int i = 0; i < t; i++)
                this.registrosStrat.add(new ArrayList());
            this.strat = formatoFachada.cargarFormato("strat");
            List atributos = this.strat.getAtributos();
            AtributoCompuesto atributo;
            int tStrat = atributos.size();
            for (int i = 0; i < tStrat; i++) {
                atributo = ((AtributoCompuesto) atributos.get(i));
                funcionesDeParteString(atributo.getSubAtributo("parte").getValor()).add(atributo);
            }
            for (int i = 0; i < t; i++) {
                if (((List) this.registrosStrat.get(i)).isEmpty()) {
                    parte = (Parte) ((TreeNode) this.nodosPartes.get(i)).getData();
                    if (!tieneHijos(parte)) crearRegistroStrat(parte);
                }
            }
            expandirPartesRecursivamente(this.nodoProyecto);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede cargar el formato strat. " + e.getMessage());
        }
    }

    /**
     * Carga el árbol de partes para el formato strat
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void cargarArbolPartes() throws ExceptionFatal, ExceptionWarn {
        try{
            Parte sistema;
            TreeNode nodo;
            TreeNode padre;
            Ciclo ciclo;
            CicloId id;
            List partes = ProyectoDAO.consultarPartes(this.proyecto);
            this.nodosPartes = new ArrayList();
            int t = partes.size();
            if (t == 0) {
                sistema = new Parte();
                sistema.setNombre(this.proyecto.getNombre());
                sistema.setEstado("Habilitado");
                sistema.setDescripcion("Sistema");
                ciclo = new Ciclo();
                id = new CicloId();
                id.setNCiclo(this.proyecto.getCicloActual());
                id.setProyecto(this.proyecto.getNombre());
                ciclo.setId(id);
                sistema.setCiclo(ciclo);
                sistema.setTipo((String)TSP.tipoPartes.get(0));
                crearParte(sistema,null);
                partes.add(sistema);
                t = 1;
            }
            for (int i = 0; i < t; i++)
                this.nodosPartes.add(new DefaultTreeNode((Parte) partes.get(i), null));
            for (int i = 0; i < t; i++) {
                nodo = (TreeNode) this.nodosPartes.get(i);
                padre = nodoPadre((Parte) nodo.getData());
                if (padre == null) this.nodoProyecto = nodo;
                else padre.getChildren().add(nodo);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede cargar el árbol de partes. " + e.getMessage());
        }
    }

    /**
     * Crea la parte en la base de datos
     * @param parte
     * @param partePadre
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void crearParte(Parte parte, Parte partePadre) throws ExceptionFatal, ExceptionWarn {
        try {
            Parte p = ProyectoDAO.consultarParte(parte);
            if (partePadre != null) parte.setParte(ProyectoDAO.consultarParte(partePadre));
            if (p == null) ProyectoDAO.insertarParte(parte);
            else ProyectoDAO.actualizarParte(parte, parte);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Carga los datos de la nueva parte
     * @param partePadre
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarNuevaParte(Parte partePadre) throws ExceptionFatal, ExceptionWarn {
        try{
            this.idParteConsultada = -1;
            this.nuevaParte = new Parte();
            this.nuevaParte.setTipo(sigTipo(partePadre.getTipo()));
            this.nuevaParte.setParte(partePadre);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede cargar la nueva parte. " + e.getMessage());
        }
    }

    /**
     * Permite consultar la parte
     * @param parte
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void consultarParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            this.idParteConsultada = idParte(parte);
            this.parteCopia = new Parte();
            this.parteCopia.setCiclo(parte.getCiclo());
            this.parteCopia.setDescripcion(parte.getDescripcion());
            this.parteCopia.setEstado(parte.getEstado());
            this.parteCopia.setId(parte.getId());
            this.parteCopia.setNombre(parte.getNombre());
            this.parteCopia.setObservacion(parte.getObservacion());
            this.parteCopia.setParte(parte.getParte());
            this.parteCopia.setTipo(parte.getTipo());
            this.nuevaParte = parte;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("" + e.getMessage());
        }
    }

    /**
     * Indica el tipo que sigue para la parte hija
     * @param tipo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private String sigTipo(String tipo) throws ExceptionFatal, ExceptionWarn {
        try{
            if(tipo.equals((String)TSP.tipoPartes.get(0)))
                return (String)TSP.tipoPartes.get(1);
            else if(tipo.equals((String)TSP.tipoPartes.get(1)))
                return (String)TSP.tipoPartes.get(2);
            else if(tipo.equals((String)TSP.tipoPartes.get(2)))
                return (String)TSP.tipoPartes.get(3);
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede obtener el tipo de parte. " + e.getMessage());
        }
    }

    /**
     * Agrega todos los datos de función ascoiadas a la nueva parte
     * @param parte
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void crearRegistroStrat(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            List funciones = funcionesDeParte(parte);
            funciones.add(fabricaAtributos.crearAtributosStrat(parte.getNombre(),this.proyecto.getNCiclos()));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede crear el registro strat. " + e.getMessage());
        }
    }

    /**
     * Ajusta el contenidode la parte para guardarla en la base de datos
     * @param parte
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void guardarParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            parte.setEstado("Habilitado");
            Ciclo ciclo = new Ciclo();
            CicloId id = new CicloId();
            id.setProyecto(this.proyecto.getNombre());
            id.setNCiclo(this.proyecto.getCicloActual());
            ciclo.setId(id);
            parte.setCiclo(ciclo);
            crearParte(parte,parte.getParte());
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede guardar la parte. " + e.getMessage());
        }
    }

    /**
     * @param parteAnterior
     * @param partePosterior
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void actualizarStrat(Parte parteAnterior, Parte partePosterior)throws ExceptionFatal, ExceptionWarn {
        try{
            String nomPartePos = partePosterior.getNombre();
            String nomParteAnt = parteAnterior.getNombre();
            List atributos = funcionesDeParte(partePosterior);
            AtributoCompuesto atributo;
            int t = atributos.size();
            for (int i = 0; i < t; i++) ((AtributoCompuesto) atributos.get(i)).getSubAtributo("parte").setValor(nomPartePos);
            atributos = this.strat.getAtributos();
            t = atributos.size();
            for (int i = 0; i < t; i++) {
                atributo = ((AtributoCompuesto) atributos.get(i)).getSubAtributo("parte");
                if (atributo.getValor().equals(nomParteAnt)) atributo.setValor(nomPartePos);
            }
            formatoFachada.actualizarFormato(this.strat);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("" + e.getMessage());
        }
    }

    /**
     * Permite expandir las partes en forma recursiva
     * @param node
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void expandirPartesRecursivamente(final TreeNode node) throws ExceptionFatal, ExceptionWarn {
        try{
            for (final TreeNode hijo : node.getChildren()) expandirPartesRecursivamente(hijo);
            node.setExpanded(true);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se pueden expandir las partes recursivamente. " + e.getMessage());
        }
    }

    /**
     * Actualiza una parte del formato strat
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarParteStrat() throws ExceptionFatal, ExceptionWarn {
        try{
            actualizarStrat(this.parteCopia, this.nuevaParte);
            ProyectoDAO.actualizarParte(this.parteCopia, this.nuevaParte);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede actualizar la parte. " + e.getMessage());
        }
    }

    /**
     * Añade una nueva parte al formato strat
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void crearParteStrat() throws ExceptionFatal, ExceptionWarn {
        try {
            if(vacio(this.nuevaParte.getDescripcion())) throw new ExceptionWarn("Debe diligenciar la descripción de la parte");
            int idP = idParte(this.nuevaParte);
            if (idP != -1) eliminarParte((Parte) ((TreeNode) this.nodosPartes.get(idP)).getData());
            TreeNode nodoPadre = nodoPadre(this.nuevaParte);
            Parte padre = (Parte) nodoPadre.getData();
            this.registrosStrat.add(new ArrayList());
            this.nodosPartes.add(new DefaultTreeNode(this.nuevaParte, nodoPadre));
            boolean transferido = false;
            List funcionesPadre = funcionesDeParte(padre);
            List funcionesHijo = funcionesDeParte(this.nuevaParte);
            AtributoCompuesto registro;
            String valorAtr;
            int t = funcionesPadre.size();
            for (int i = t - 1; i >= 0; i--) {
                registro = (AtributoCompuesto) funcionesPadre.get(i);
                valorAtr = registro.getSubAtributo("funcion").getValor();
                if (valorAtr != null)
                    if (!valorAtr.equals("")) {
                        transferido = true;
                        registro.getSubAtributo("parte").setValor(this.nuevaParte.getNombre());
                        funcionesHijo.add(registro);
                    }
                funcionesPadre.remove(i);
            }
            if (!transferido) crearRegistroStrat(this.nuevaParte);
            else crearRegistroStrat(padre);
            this.registrosStrat.set(idParte(padre), new ArrayList());
            guardarParte(this.nuevaParte);
            expandirPartesRecursivamente(nodoPadre);
            this.nuevaParte = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede crear la parte strat. " + e.getMessage());
        }
    }

    /**
     * Devuelve la posición de la parte proporcionada en la que se encuentra dentro de la lista de nodosParte
     * @param parte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int idParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            String nombreParte = parte.getNombre();
            int t = this.nodosPartes.size();
            for (int i = 0; i < t; i++)
                if (((Parte) ((DefaultTreeNode) this.nodosPartes.get(i)).getData()).getNombre().equals(nombreParte))
                    return i;
            return -1;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede obtener el id de la parte" + e.getMessage());
        }
    }

    /**
     * Brinad la lista de funciones de la parte a partir del nombre de ésta
     * @param nomParte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private List funcionesDeParteString(String nomParte) throws ExceptionFatal, ExceptionWarn {
        try{
            Parte parte = new Parte();
            parte.setNombre(nomParte);
            return (List) this.registrosStrat.get(idParte(parte));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se pueden obtener las funciones a partir del nombre de la parte. " + e.getMessage());
        }
    }

    /**
     * Devuelve las funciones de la parte a partir del objeto parte
     * @param parte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List funcionesDeParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            return (List) this.registrosStrat.get(idParte(parte));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede obtener las funciones de la parte. " + e.getMessage());
        }
    }

    /**
     * Identifica el  nodo de la parte
     * @param parte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private TreeNode nodoParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            return (TreeNode) this.nodosPartes.get(idParte(parte));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se localiza el nodo de la parte. " + e.getMessage());
        }

    }

    /**
     * Elimina la parte del ciclo del proyecto
     * @param parte
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            String nombreParte = parte.getNombre();
            TreeNode nodoPadre = nodoPadre(parte);
            Iterator<TreeNode> it = nodoPadre.getChildren().iterator();
            while (it.hasNext()) {
                TreeNode nodoParte = it.next();
                if (((Parte) nodoParte.getData()).getNombre().equals(nombreParte)) {
                    it.remove();
                    eliminacionEncadenada(nodoParte);
                    break;
                }
            }
            crearRegistroStrat((Parte) nodoPadre.getData());
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede eliminar la parte. " + e.getMessage());
        }
    }

    /**
     * Elimina en forma encadenada los nodos de una parte a eliminar
     * @param nodo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void eliminacionEncadenada(TreeNode nodo) throws ExceptionFatal, ExceptionWarn {
        try{
            Parte parte;
            List hijos = nodo.getChildren();
            int t = hijos.size();
            for (int i = 0; i < t; i++)
                eliminacionEncadenada((TreeNode) hijos.get(i));
            parte = (Parte) nodo.getData();
            int id = idParte(parte);
            this.registrosStrat.remove(id);
            this.nodosPartes.remove(id);
            ProyectoDAO.deshabilitarParte(parte);
            if (t == 0)
                if(this.strat.estaGuardado())
                    formatoFachada.eliminarRegistosFormato(this.strat, "parte", parte.getNombre());
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Ocurrió una falla al efectuar la eliminación encadenada. " + e.getMessage());
        }
    }

    /**
     * Permite eliminar una función de una parte
     * @param parte
     * @param atributo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarFuncion(Parte parte, AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            List funciones = funcionesDeParte(parte);
            String nombreAtributo = atributo.toString();
            int t = funciones.size();
            for (int i = 0; i < t; i++)
                if (((AtributoCompuesto) funciones.get(i)).toString().equals(nombreAtributo)) {
                    funciones.remove(i);
                    break;
                }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede eliminar la función. " + e.getMessage());
        }
    }

    /**
     * Identifica el nodo parte de una parte
     * @param parte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private TreeNode nodoPadre(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            Parte partePadre = parte.getParte();
            if (partePadre == null) return null;
            String nombrePartePadre = partePadre.getNombre();
            int t = this.nodosPartes.size();
            Parte pt;
            TreeNode nodo;
            for (int i = 0; i < t; i++) {
                nodo = (TreeNode) this.nodosPartes.get(i);
                pt = (Parte) (nodo.getData());
                if (pt.getNombre().equals(nombrePartePadre)) return nodo;
            }
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("Error al localizar el nodo padre de la parte. " + e.getMessage());
        }
    }

    /**
     * Obtiene las funciones de una parte
     * @param parte
     * @param atributo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List funciones(Parte parte, String atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            List funcs = new ArrayList();
            List atributos = funcionesDeParte(parte);
            int t = atributos.size();
            int t2;
            List atributos2;
            AtributoCompuesto atributo2;
            for (int i = 0; i < t; i++) {
                atributos2 = ((AtributoCompuesto) atributos.get(i)).getAtributos();
                t2 = atributos2.size();
                for (int j = 0; j < t2; j++){
                    atributo2 = ((AtributoCompuesto) atributos2.get(j));
                    if (atributo2.getAtributo().equals(atributo)) {
                        funcs.add(atributo2);
                        break;
                    }
                }
            }
            return funcs;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no localiza las funciones de la parte. " + e.getMessage());
        }
    }

    /**
     * Valida si una parte tiene hijos
     * @param parte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean tieneHijos(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            return nodoParte(parte).getChildCount() > 0;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede determinar si la parte tiene hijos. " + e.getMessage());
        }
    }

    /**
     * Identifica el tamaño de las funciones de la parte
     * @param parte
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public int tamFuncionesParte(Parte parte) throws ExceptionFatal, ExceptionWarn {
        try{
            return funcionesDeParte(parte).size();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede calcular el total de funciones de la parte. " + e.getMessage());
        }
    }

    /**
     * Carga el título del formato strat
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void cargarHeadersStrat() throws ExceptionFatal, ExceptionWarn {
        try{
            int numCiclosProyectoSesion = this.proyecto.getNCiclos();
            List lista;
            this.headersStrat = new HashMap<String, List>();
            lista = new ArrayList();
            for (int i = 0; i < numCiclosProyectoSesion; i++)
                lista.add("C" + Integer.toString(i + 1));
            this.headersStrat.put("tamaño", lista);

            lista = new ArrayList();
            for (int i = 0; i < numCiclosProyectoSesion; i++)
                lista.add("C" + Integer.toString(i + 1));
            this.headersStrat.put("horas", lista);
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede generar el encabezado de la tabla strat. " + e.getMessage());
        }
    }

    /**
     * Guarda el formato strat
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarStrat() throws ExceptionFatal, ExceptionWarn {
        try{
            if(this.nodoProyecto.getChildren().isEmpty()) throw new ExceptionWarn("No hay registros");
            int registroFaltante = validarRegistrosFuncionesNoNulas();
            if(registroFaltante != 0) throw new ExceptionWarn("Debe indicar el nombre de la función " + Integer.toString(registroFaltante));
            List atributos = new ArrayList();
            int t = this.registrosStrat.size();
            int t2;
            List funciones;
            for (int i = 0; i < t; i++) {
                funciones = (List) this.registrosStrat.get(i);
                t2 = funciones.size();
                if (!tieneHijos((Parte) (((TreeNode) this.nodosPartes.get(i)).getData())))
                    for (int j = 0; j < t2; j++)
                        atributos.add((AtributoCompuesto) funciones.get(j));
            }
            this.strat.setAtributos(atributos);
            formatoFachada.actualizarFormato(this.strat);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede guardar el formato Strat. " + e.getMessage());
        }
    }

    /**
     * Calcula el total estimado en tamaño
     * @param ciclo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public double totalTamanio(String ciclo) throws ExceptionFatal, ExceptionWarn {
        try{
            double total = 0;
            int t = this.registrosStrat.size();
            List funciones;
            AtributoCompuesto atributo;
            int t2;
            for (int i = 0; i < t; i++) {
                funciones = (List) this.registrosStrat.get(i);
                t2 = funciones.size();
                for (int j = 0; j < t2; j++) {
                    atributo = (AtributoCompuesto) funciones.get(j);
                    try {
                        total += Double.parseDouble(atributo.getSubAtributo("TAM_Ciclo").getSubAtributo(ciclo).getValor());
                    } catch (Exception e) {}
                }
            }
            return total;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede calcular el total estimado en tamaño. " + e.getMessage());
        }
    }

    private int validarRegistrosFuncionesNoNulas(){
        int numPartes = this.registrosStrat.size();
        int numFunciones;
        List funciones;
        int num = 1;
        for (int i = 0; i < numPartes; i++) {
            funciones = (List) this.registrosStrat.get(i);
            numFunciones = funciones.size();
            for (int j = 0; j < numFunciones; j++) {
                if(vacio(((AtributoCompuesto) funciones.get(j)).getSubAtributo("funcion").getValor()))
                    return num;
                num++;
            }
        }
        return 0;
    }
    
    
    
    /**
     * Calcula el total estimado en horas
     * @param ciclo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public double totalHoras(String ciclo) throws ExceptionFatal, ExceptionWarn {
        try{
            double total = 0;
            int t = this.registrosStrat.size();
            List funciones;
            AtributoCompuesto atributo;
            int t2;
            for (int i = 0; i < t; i++) {
                funciones = (List) this.registrosStrat.get(i);
                t2 = funciones.size();
                for (int j = 0; j < t2; j++) {
                    atributo = (AtributoCompuesto) funciones.get(j);
                    try {
                        total += Double.parseDouble(atributo.getSubAtributo("Horas_Ciclo").getSubAtributo(ciclo).getValor());
                    } catch (Exception e) {}
                }
            }
            return total;
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede calcular el total estimado en horas. " + e.getMessage());
        }
    }

    /**
     * Carga todas las partes que son de tipo producto
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarProductos() throws ExceptionFatal, ExceptionWarn {
        try{
            this.partesProductos = ProyectoDAO.consultarProductos(((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion());
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede cargar los productos. " + e.getMessage());
        }
    }

    /**
     * De acuerdo a la medida que pase por parámetro se obtiene su lista de ciclos involucreados
     * @param nombre
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List listaHeader(String nombre) throws ExceptionFatal, ExceptionWarn {
        try{
            return this.headersStrat.get(nombre);
        }catch(Exception e){
            throw new ExceptionFatal("EstrategiaControl no puede obtener el nombre del elemento del encabezado. " + e.getMessage());
        }
    }

    public int getIdParteConsultada() {
        return idParteConsultada;
    }

    public void setIdParteConsultada(int idParteConsultada) {
        this.idParteConsultada = idParteConsultada;
    }

    public Parte getParteCopia() {
        return parteCopia;
    }

    public void setParteCopia(Parte parteCopia) {
        this.parteCopia = parteCopia;
    }

    public List getPartesProductos() {
        return partesProductos;
    }

    public void setPartesProductos(List partesProductos) {
        this.partesProductos = partesProductos;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Parte getNuevaParte() {
        return nuevaParte;
    }

    public void setNuevaParte(Parte nuevaParte) {
        this.nuevaParte = nuevaParte;
    }

    public TreeNode getNodoProyecto() {
        return nodoProyecto;
    }

    public void setNodoProyecto(TreeNode nodoProyecto) {
        this.nodoProyecto = nodoProyecto;
    }

    public FormatoConcreto getStrat() {
        return strat;
    }

    public void setStrat(FormatoConcreto strat) {
        this.strat = strat;
    }

    public Map<String, List> getHeadersStrat() {
        return headersStrat;
    }

    public void setHeadersStrat(Map<String, List> headersStrat) {
        this.headersStrat = headersStrat;
    }

    public List getNodosPartes() {
        return nodosPartes;
    }

    public void setNodosPartes(List nodosPartes) {
        this.nodosPartes = nodosPartes;
    }

    public List getRegistrosStrat() {
        return registrosStrat;
    }

    public void setRegistrosStrat(List registrosStrat) {
        this.registrosStrat = registrosStrat;
    }

    public FormatoFachada getFormatoFachada() {
        return formatoFachada;
    }

    public void setFormatoFachada(FormatoFachada formatoFachada) {
        this.formatoFachada = formatoFachada;
    }

    public FabricaAtributos getFabricaAtributos() {
        return fabricaAtributos;
    }

    public void setFabricaAtributos(FabricaAtributos fabricaAtributos) {
        this.fabricaAtributos = fabricaAtributos;
    }
}