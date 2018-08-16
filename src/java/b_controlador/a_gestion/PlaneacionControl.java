/**
 * Controlador enfocado en el formato TASK
 */
package b_controlador.a_gestion;

import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import b_controlador.b_fachada.FormatoFachada;
import c_negocio.a_relacional.Opciontsp;
import c_negocio.a_relacional.Rol;
import d_datos.a_dao.ProyectoDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class PlaneacionControl extends Control{

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private List registrosEtapas;
    private List etapasTSP;
    private List rolesUsuarios;
    private FormatoConcreto task;
    private List rolesUSesion;

    public PlaneacionControl() {
        etapasTSP = TSP.etapas;
    }
    
    public List<String> tareasAntesPlaneacion(String rolSeleccionado) throws ExceptionFatal {
        try{
            String nombreRolInstructor = TSP.instructor.getNombre();
            if(rolSeleccionado != null && rolSeleccionado.equals(nombreRolInstructor)) return null;
            ProyectoControl pc = ((ProyectoControl) getControlador("ProyectoControl"));
            List<String> rolesUsuarioSesion = pc.getRolesUSesion();
            int numRolesUsuarioSesion = rolesUsuarioSesion.size();
            for(int i = 0; i < numRolesUsuarioSesion; i++)
                if(rolesUsuarioSesion.equals(nombreRolInstructor) && numRolesUsuarioSesion == 1)
                    return null;
            //Se carga el formato TASK para obtener las tareas de acuerdo con su estado
            this.cargarTask();
            //Se obtienen las tareas para la primera etapa de la metodología: Estrategia y planeación
            List<AtributoCompuesto> tareasEtapa = this.registrosEtapa((String)TSP.etapas.get(0));
            int numTareasEtapa = tareasEtapa.size();
            //Se identifica la fase actual para obtener las tareas de la etapa que corresponden a dicha fase
            String faseActual = pc.getProyectoSesion().getFaseActual();
            //Se obtiene la identificación del usuario en sesión para capturar el estado de la tarea del formato task
            String identificacionUsuarioSesion = ((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            
            //Se almacenan las tareas de la etaa correspondientes a la fase actual
            List<String> tareasFase = null;
            if(faseActual.equals((String)TSP.fases.get(0)))
                tareasFase = (List)TSP.tareasAntesPlaneacion.get(0);
            else if(faseActual.equals((String)TSP.fases.get(1)))
                tareasFase = (List)TSP.tareasAntesPlaneacion.get(1);
            else tareasFase = new ArrayList();
            int numTareasFase = tareasFase.size();
            int i, j, k;
            List<String> tareasNoTerminadas = new ArrayList();//Variable que almacena las tareas no terminadas a mostrar al usuario, y con ello, pueda ejecutarlas hasta terminarlas
            AtributoCompuesto tarea;//Variable auxiliar para almacenar las tareas de la etapa correspondientes al formamto TASK
            String nombreTarea;//Nombre de la tarea obtenida de la etapa del formato TASK
            List<AtributoCompuesto> estadosTarea;//Atributo que indica la terminación o no de una tarea; tarea de la etapa de lanzamiento y estrategia del formato TASK
            int numEstadosTarea;
            AtributoCompuesto estadoTarea;
            //Se recorren todas las tareas de la etapa de lanzamiento y estrategia
            for(i = 0; i < numTareasEtapa; i++){
                tarea = tareasEtapa.get(i);
                nombreTarea = tarea.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").getValor();
                //Por cada tarea de la etapa de estrategia se recorren las tareas que corresponden a la fase actual del ciclo del proyecto
                for(j = 0; j < numTareasFase; j++)
                    //Si la tarea de la etapa es igual a una de las tareas a trabajar sobre la fase actual, se procede a mirar si el usuario en sesión ya la ha terminado o no para cargarla
                    if(nombreTarea.equals(tareasFase.get(j))) {
                        try{
                            if(!tarea.getSubAtributo("Estado de la tarea").getSubAtributo(rolSeleccionado).getSubAtributo(identificacionUsuarioSesion).getValor().equals("Terminada"))
                                    tareasNoTerminadas.add(nombreTarea);
                        } catch(Exception e) {
                            tareasNoTerminadas.add(nombreTarea);
                        }
                        break;
                    }
            }
            return tareasNoTerminadas;
        }catch(Exception e){
            throw new ExceptionFatal("Error fatal al obtener las tareas para registrar el tiempo. " + e.getMessage());
        }
    }

    private void agregarTareasNoPlaneadas(String etapa) throws ExceptionFatal {
        try{
            List registrosEtapa = registrosEtapa(etapa);
            AtributoCompuesto registro;
            int numFasesNoPlaneadas = TSP.tareasAntesPlaneacion.size();
            int numTareasFaseNoPlaneada;
            List tareasFaseNoPlaneadas;
            for(int i = 0; i < numFasesNoPlaneadas; i++){
                tareasFaseNoPlaneadas = (List) TSP.tareasAntesPlaneacion.get(i);
                numTareasFaseNoPlaneada = tareasFaseNoPlaneadas.size();
                for(int j = 0; j < numTareasFaseNoPlaneada; j++){
                    registro = fabricaAtributos.crearAtributosTask(etapa);
                    registro.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").setValor((String)tareasFaseNoPlaneadas.get(j));
                    registrosEtapa.add(registro);
                    float totalHoras = actualizarNIntegrantesyHoras(registrosEtapa, registro);
                    if (totalHoras > 0) actualizarVPsYVPsAcumulados(totalHoras);
                }
            }
        }catch(Exception e) {
            throw new ExceptionFatal("PlaneacionControl no puede adicionar las tareas de lanzamiento y estrategia. " + e.getMessage());
        }
    }

    /**
     * Carga el formato actual TASK
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarTask() throws ExceptionFatal, ExceptionWarn {
        try{
            ProyectoControl pb = (ProyectoControl) getControlador("ProyectoControl");
            ((EstrategiaControl)getControlador("EstrategiaControl")).cargarProductos();
            this.rolesUSesion = pb.getRolesUSesion();
            this.rolesUsuarios = pb.getUsuariosRolesCicloActual();
            this.registrosEtapas = new ArrayList();
            this.task = formatoFachada.cargarFormato("task");
            List atributos = this.task.getAtributos();
            int totalAtributos = atributos.size();
            int totalEtapas = this.etapasTSP.size();
            AtributoCompuesto atributo;
            if (totalAtributos == 0) {
                this.registrosEtapas.add(new ArrayList());
                agregarTareasNoPlaneadas((String) this.etapasTSP.get(0));
                for (int i = 1; i < totalEtapas; i++) {
                    this.registrosEtapas.add(new ArrayList());
                    crearTarea((String) this.etapasTSP.get(i));
                }
            } else {
                for (int i = 0; i < totalEtapas; i++)
                    this.registrosEtapas.add(new ArrayList());
                for (int i = 0; i < totalAtributos; i++) {
                    atributo = (AtributoCompuesto) atributos.get(i);
                    registrosEtapa(atributo.getSubAtributo("Tarea").getSubAtributo("Etapa").getValor()).add(atributo);
                }
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede cargar el formato task. " + e.getMessage());
        }
    }

    /**
     * Crea la tarea nueva para el formato task
     * @param etapa
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void crearTarea(String etapa) throws ExceptionFatal, ExceptionWarn {
        try{
            List registrosEtapa = registrosEtapa(etapa);
            AtributoCompuesto registro = fabricaAtributos.crearAtributosTask(etapa);
            registrosEtapa.add(registro);
            float totalHoras = actualizarNIntegrantesyHoras(registrosEtapa, registro);
            if (totalHoras > 0) actualizarVPsYVPsAcumulados(totalHoras);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede crear la tarea. " + e.getMessage());
        }
    }

    /**
     * Valida que la semana del plan esté dentro del valor estimado
     * @param registrosEtapa
     * @param atributo2
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void validarSemanaPlan(List registrosEtapa, AtributoCompuesto atributo2) throws ExceptionFatal, ExceptionWarn {
        try{
            int n;
            AtributoCompuesto atrNSemana = atributo2.getSubAtributo("N. Semana");
            AtributoCompuesto atrAnterior = (AtributoCompuesto)Helper.registroAnterior(this.registrosEtapas, registrosEtapa, atributoPadre(registrosEtapa, atributo2));
            if(atrAnterior == null)
                atrNSemana.setValor("1");
            else{
                n = (int)Helper.extraerNumero(atrAnterior.getSubAtributo("Tamaño del plan").getSubAtributo("N. Semana").getValor());
                if(n > (int)Helper.extraerNumero(atrNSemana.getValor()) || (int)Helper.extraerNumero(atrNSemana.getValor()) > n + 1)
                    atrNSemana.setValor(Integer.toString(n));
                }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede validar la semana del plan. " + e.getMessage());
        }
    }

    
    /**
     * Permite guardar el formato task persistentemente
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarTaskNoPlaneada() throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.etapasTSP.size();
            int t2;
            List atributos = new ArrayList();
            List registrosEtapa;
            int i;
            for (i = 0; i < t; i++) {
                registrosEtapa = (List) this.registrosEtapas.get(i);
                t2 = registrosEtapa.size();
                for (int j = 0; j < t2; j++)
                    atributos.add((AtributoCompuesto) registrosEtapa.get(j));
            }
            this.task.setAtributos(atributos);
            formatoFachada.actualizarFormato(this.task);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede guardar el formato task. " + e.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    /**
     * Permite guardar el formato task persistentemente
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void guardarTask() throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.etapasTSP.size();
            int t2;
            List atributos = new ArrayList();
            AtributoCompuesto atributo;
            AtributoCompuesto atributo2;
            AtributoCompuesto atributo2_2;
            List registrosEtapa;
            String mensaje = null;
            int i;
            int nSemana = 1;
            int numSemana;
            for (i = 0; i < t; i++) {
                registrosEtapa = (List) this.registrosEtapas.get(i);
                t2 = registrosEtapa.size();
                for (int j = 0; j < t2; j++) {
                    atributo = (AtributoCompuesto) registrosEtapa.get(j);
                    atributo2 = atributo.getSubAtributo("Tarea");
                    atributo2_2 = atributo.getSubAtributo("Tamaño del plan");
                    if (!vacio(atributo2.getSubAtributo("Parte").getValor())) {
                        if (!vacio(atributo2.getSubAtributo("Nombre de la tarea").getValor())) {
                            if (!vacio(atributo2_2.getSubAtributo("Unidad de tamaño").getValor())) {
                                numSemana = (int)Helper.extraerNumero(atributo2_2.getSubAtributo("N. Semana").getValor());
                                if(nSemana == numSemana) atributos.add(atributo);
                                else{
                                    if(numSemana == nSemana + 1){
                                        nSemana += 1;
                                            atributos.add(atributo);
                                    }else mensaje = "El número de semana del registro " + Integer.toString(i + 1) + " no es secuencial";
                                }
                            } else mensaje = "La unidad de tamaño del registro " + Integer.toString(i + 1) + " está vacía";
                        } else mensaje = "El nombre de la tarea del registro " + Integer.toString(i + 1) + " está vacía";
                    } else mensaje = "La parte del registro " + Integer.toString(i + 1) + " está vacía";
                    if (mensaje != null) throw new ExceptionWarn(mensaje + ". No se ha guardado el formato");
                }
            }
            this.task.setAtributos(atributos);
            formatoFachada.actualizarFormato(this.task);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede guardar el formato task. " + e.getMessage());
        }
    }

    /**
     * Crea los registros correspondientes a una etapa de TSP
     * @param etapa
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List registrosEtapa(String etapa) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.etapasTSP.size();
            for (int i = 0; i < t; i++)
                if (((String) this.etapasTSP.get(i)).equals(etapa))
                    return (List) this.registrosEtapas.get(i);
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede registrar la etapa. " + e.getMessage());
        }
    }

    /**
     * Obtiene los registros de uina etapa con base en un atributo
     * @param registrosEtapa
     * @param atributo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List getAtributos2(List registrosEtapa, String atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = registrosEtapa.size();
            List atributos2 = new ArrayList();
            for (int i = 0; i < t; i++) 
                atributos2.add(((AtributoCompuesto) registrosEtapa.get(i)).getSubAtributo(atributo));
            return atributos2;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede obtener el atributo del registro task. " + e.getMessage());
        }
    }

    /**
     * Actualiza el número de integrantes y horas
     * @param registrosFase
     * @param atributo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private float actualizarNIntegrantesyHoras(List registrosFase, AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            int t;
            float acumuladoHoras;
            AtributoCompuesto atr;
            AtributoCompuesto atr2_1;
            AtributoCompuesto atr2_2;
            AtributoCompuesto atr3;
            AtributoCompuesto atrAnterior;
            atr2_1 = atributo.getSubAtributo("Tarea");
            atr2_2 = atributo.getSubAtributo("Horas del plan");
            List integrantes = new ArrayList();
            List atrs3_2 = new ArrayList();
            atrs3_2.add(atr2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(0)).getNombre()));
            atrs3_2.add(atr2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(1)).getNombre()));
            atrs3_2.add(atr2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(4)).getNombre()));
            atrs3_2.add(atr2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(2)).getNombre()));
            atrs3_2.add(atr2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(3)).getNombre()));

            t = atrs3_2.size();
            acumuladoHoras = 0;
            for (int i = 0; i < t; i++) {
                atr3 = (AtributoCompuesto) atrs3_2.get(i);
                if (Float.parseFloat(atr3.getValor()) > 0)
                    agregarIntegrantesRol(integrantes, integrantesPorRol(atr3.getAtributo()));
                acumuladoHoras += Float.parseFloat(atr3.getValor());
            }
            atr2_2.getSubAtributo("Total horas").setValor(Float.toString(acumuladoHoras));
            atr2_1.getSubAtributo("N. Integrts.").setValor(Integer.toString(integrantes.size()));

            atrAnterior = (AtributoCompuesto) Helper.registroAnterior(this.registrosEtapas, registrosFase, atributo);
            if (atrAnterior == null)
                atr2_2.getSubAtributo("Acumulado horas").setValor(Float.toString(acumuladoHoras));
            else {
                acumuladoHoras += Float.parseFloat(atrAnterior.getSubAtributo("Horas del plan").getSubAtributo("Acumulado horas").getValor());
                atr2_2.getSubAtributo("Acumulado horas").setValor(Float.toString(acumuladoHoras));
            }
            atr = (AtributoCompuesto) Helper.registroPosterior(this.registrosEtapas, registrosFase, atributo);
            while (atr != null) {
                atr2_2 = atr.getSubAtributo("Horas del plan");
                atr2_2.getSubAtributo("Acumulado horas").setValor(Float.toString((acumuladoHoras += Float.parseFloat(atr2_2.getSubAtributo("Total horas").getValor()))));
                atr = (AtributoCompuesto) Helper.registroPosterior(this.registrosEtapas, registrosEtapa(atr.getSubAtributo("Tarea").getSubAtributo("Etapa").getValor()), atr);
            }
            return acumuladoHoras;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede actualizar el número de integrantes y las horas del formato. " + e.getMessage());
        }
    }

    /**
     * Brinda la lista de integrantes que tiene un rol para el ciclo
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private List integrantesPorRol(String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = rolesUsuarios.size();
            List integrantes = new ArrayList();
            RlClId rcId;
            for (int i = 0; i < t; i++) {
                rcId = ((RlCl) rolesUsuarios.get(i)).getId();
                if (rcId.getRol().equals(rol))
                    integrantes.add(rcId.getUsuario());
            }
            return integrantes;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede obtener la lista de integrantes por rol. " + e.getMessage());
        }
    }

    /**
     * Indica el atributo padre del atributo recibido
     * @param atributos
     * @param hijo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private AtributoCompuesto atributoPadre(List atributos, AtributoCompuesto hijo) throws ExceptionFatal, ExceptionWarn {
        try{
            String hijoString = hijo.toString();
            int t = atributos.size();
            AtributoCompuesto atributo;
            List atributosSig;
            int t2;
            for (int i = 0; i < t; i++) {
                atributo = (AtributoCompuesto) atributos.get(i);
                atributosSig = atributo.getAtributos();
                t2 = atributosSig.size();
                for (int j = 0; j < t2; j++)
                    if (hijoString.equals(((AtributoCompuesto) atributosSig.get(j)).toString())) return atributo;
            }
            return null;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede obtener el atributo padre del elemento. " + e.getMessage());
        }
    }

    /**
     * Sólo a los usuarios que no se encuentran en la lista de integrantes los
     * agrega a la lista
     * @param integrantes
     * @param integrantesPorRol
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void agregarIntegrantesRol(List integrantes, List integrantesPorRol) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = integrantes.size();
            int t2 = integrantesPorRol.size();
            String usuario;
            boolean encontrado;
            for (int i = 0; i < t2; i++) {
                usuario = (String) integrantesPorRol.get(i);
                encontrado = false;
                for (int j = 0; j < t; j++)
                    if (usuario.equals((String) integrantes.get(j))) {
                        encontrado = true;
                        break;
                    }
                if (!encontrado) integrantes.add(usuario);
            }
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede agregar los integrantes a la tarea. " + e.getMessage());
        }
    }

    /**
     * Obtiene las horas acumuladas de una tarea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String getHorasAcumuladas() throws ExceptionFatal, ExceptionWarn {
        try{
            String r = "";
            int t = this.registrosEtapas.size();
            int t2;
            for (int i = 0; i < t; i++) {
                t2 = ((List) this.registrosEtapas.get(i)).size();
                for (int j = 0; j < t2; j++)
                    r += "formTask:tableTask:" + Integer.toString(i) + ":repeatTotalHorasAcumuladas:" + Integer.toString(j) + ":totalHorasAcumuladas ";
            }
            return r;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede cacular el total de horas acumuladas. " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos reales del formato TASK
     * @param registrosEtapa
     * @param atributo2
     * @param atributo3
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarDatos(List registrosEtapa, AtributoCompuesto atributo2, AtributoCompuesto atributo3) throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atributoPadre = atributoPadre(registrosEtapa, atributo2);
            actualizarVPsYVPsAcumulados(actualizarNIntegrantesyHoras(registrosEtapa, atributoPadre));
            String nombreRol;
            List atributos4;
            List integrantesRol;
            AtributoCompuesto atr3;
            List atributosX;
            int t;
            int y;
            boolean rolYainsertado;
            AtributoCompuesto atr2_3 = atributoPadre.getSubAtributo("Estado de la tarea");
            nombreRol = atributo3.getAtributo();
            if (Float.parseFloat(atributo3.getValor()) > 0) {
                atributosX = atr2_3.getAtributos();
                y = atributosX.size();
                rolYainsertado = false;
                for (int o = 0; o < y; o++)
                    if (((AtributoCompuesto) atributosX.get(o)).getAtributo().equals(nombreRol)) {
                        rolYainsertado = true;
                        break;
                    }
                if (!rolYainsertado) {
                    atributos4 = new ArrayList();
                    integrantesRol = integrantesPorRol(nombreRol);
                    t = integrantesRol.size();
                    for (int i = 0; i < t; i++)
                        atributos4.add(new AtributoCompuesto((String) integrantesRol.get(i), "Sin terminar", null));
                    atributosX.add(new AtributoCompuesto(nombreRol, atributos4));
                }
            } else {
                atr3 = atr2_3.getSubAtributo(nombreRol);
                if (atr3 != null) atr2_3.getAtributos().remove(atr3);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede actualizar el formato task. " + e.getMessage());
        }
    }

    /**
     * Construye la cadena de actualización de valores planeados acumulados
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String getVPsAcumulados() throws ExceptionFatal, ExceptionWarn {
        try{
            String r = "";
            int t = this.registrosEtapas.size();
            int t2;
            for (int i = 0; i < t; i++) {
                t2 = ((List) this.registrosEtapas.get(i)).size();
                for (int j = 0; j < t2; j++)
                    r += "formTask:tableTask:" + Integer.toString(i) + ":repeatVpAcumulado:" + Integer.toString(j) + ":vpAcumulado ";
            }
            return r;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede calcular valores planeados acumulados. " + e.getMessage());
        }
    }

    /**
     * Construye la cadena de actualización de valores planeados
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String getVPs() throws ExceptionFatal, ExceptionWarn {
        try{
            String r = "";
            int t = this.registrosEtapas.size();
            int t2;
            for (int i = 0; i < t; i++) {
                t2 = ((List) this.registrosEtapas.get(i)).size();
                for (int j = 0; j < t2; j++)
                    r += "formTask:tableTask:" + Integer.toString(i) + ":repeatValorPlaneado:" + Integer.toString(j) + ":valorPlaneado ";
            }
            return r;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede calcular el valor planeado. " + e.getMessage());
        }
    }

    /**
     * Permite actualizar los valores planeados y valores planeados acumulados
     * @param totalHoras
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void actualizarVPsYVPsAcumulados(float totalHoras) throws ExceptionFatal, ExceptionWarn {
        try{
            List registrosEtapa;
            registrosEtapa = (List) this.registrosEtapas.get(0);
            AtributoCompuesto primerAtributo = (AtributoCompuesto) (registrosEtapa).get(0);
            AtributoCompuesto atributo2;
            AtributoCompuesto atributo3;
            float vpAcumuladoAnterior;
            atributo2 = primerAtributo.getSubAtributo("Tamaño del plan");
            atributo3 = atributo2.getSubAtributo("VP");
            atributo3.setValor(Float.toString((float) Float.parseFloat(primerAtributo.getSubAtributo("Horas del plan").getSubAtributo("Total horas").getValor()) / totalHoras));
            atributo2.getSubAtributo("VP Acumulado").setValor(atributo3.getValor());
            vpAcumuladoAnterior = Float.parseFloat(atributo3.getValor());
            AtributoCompuesto atributo = (AtributoCompuesto) Helper.registroPosterior(this.registrosEtapas, registrosEtapa, primerAtributo);
            while (atributo != null) {
                atributo2 = atributo.getSubAtributo("Tamaño del plan");
                atributo3 = atributo2.getSubAtributo("VP");
                atributo3.setValor(Float.toString((float) Float.parseFloat(atributo.getSubAtributo("Horas del plan").getSubAtributo("Total horas").getValor()) / totalHoras));
                atributo2.getSubAtributo("VP Acumulado").setValor(Float.toString((float) (vpAcumuladoAnterior += Float.parseFloat(atributo3.getValor()))));
                atributo = (AtributoCompuesto) Helper.registroPosterior(this.registrosEtapas, registrosEtapa(atributo.getSubAtributo("Tarea").getSubAtributo("Etapa").getValor()), atributo);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede actualizar los valores planeados. " + e.getMessage());
        }

    }

    /**
     * Permite actualizar el total de horas reales acumuladas
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void actualizarHorasRealesAcumuladas() throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atr;
            AtributoCompuesto atr2;
            AtributoCompuesto atr3;
            float valor;
            atr = (AtributoCompuesto) ((List) this.registrosEtapas.get(0)).get(0);
            atr2 = atr.getSubAtributo("Real");
            atr3 = atr2.getSubAtributo("Horas");
            atr2.getSubAtributo("Horas acumuladas").setValor(atr3.getValor());
            valor = Float.parseFloat(atr3.getValor());
            atr = (AtributoCompuesto) Helper.registroPosterior(this.registrosEtapas, registrosEtapa(atr.getSubAtributo("Tarea").getSubAtributo("Etapa").getValor()), atr);
            while (atr != null) {
                atr2 = atr.getSubAtributo("Real");
                atr2.getSubAtributo("Horas acumuladas").setValor(Float.toString(valor += Float.parseFloat(atr2.getSubAtributo("Horas").getValor())));
                atr = (AtributoCompuesto) Helper.registroPosterior(this.registrosEtapas, registrosEtapa(atr.getSubAtributo("Tarea").getSubAtributo("Etapa").getValor()), atr);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede actualizar las horas reales acumuladas. " + e.getMessage());
        }
    }

    /**
     * Elimina una tara del formato task
     * @param registrosFase
     * @param atributo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarTarea(List registrosFase, AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            int t = registrosFase.size();
            for (int i = 0; i < t; i++)
                if (atributo.toString().equals(((AtributoCompuesto) registrosFase.get(i)).toString())) {
                    registrosFase.remove(i);
                    break;
                }
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede eliminar la tarea. " + e.getMessage());
        }
    }

    /**
     * Calcula el total de horas por cada rol en un arreglo de double
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public double[] getTotalRoles() throws ExceptionFatal, ExceptionWarn {
        try{
            int t = this.registrosEtapas.size();
            List registrosEtapa;
            AtributoCompuesto atributo2;
            int t2;
            double totales[] = new double[5];
            for (int i = 0; i < t; i++) {
                registrosEtapa = (List) this.registrosEtapas.get(i);
                t2 = registrosEtapa.size();
                for (int j = 0; j < t2; j++) {
                    atributo2 = ((AtributoCompuesto) registrosEtapa.get(j)).getSubAtributo("Horas del plan");
                    totales[0] += Double.parseDouble(atributo2.getSubAtributo(((Rol)TSP.rolesTSP.get(0)).getNombre()).getValor());
                    totales[1] += Double.parseDouble(atributo2.getSubAtributo(((Rol)TSP.rolesTSP.get(1)).getNombre()).getValor());
                    totales[2] += Double.parseDouble(atributo2.getSubAtributo(((Rol)TSP.rolesTSP.get(4)).getNombre()).getValor());
                    totales[3] += Double.parseDouble(atributo2.getSubAtributo(((Rol)TSP.rolesTSP.get(2)).getNombre()).getValor());
                    totales[4] += Double.parseDouble(atributo2.getSubAtributo(((Rol)TSP.rolesTSP.get(3)).getNombre()).getValor());
                }
            }
            return totales;
        }catch(Exception e){
            throw new ExceptionFatal("Planeación control no puede obtener el total por rol. " + e.getMessage());
        }
    }

    /**
     * Identifica si el usuario tiene el rol asignado para editar parte del task
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean tieneRol(String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            ProyectoControl pc = ((ProyectoControl) getControlador("ProyectoControl"));
            pc.cargarRolesSesion();
            this.rolesUSesion = pc.getRolesUSesion();
            if(this.rolesUSesion == null) return false;
            int t = this.rolesUSesion.size();
            for (int i = 0; i < t; i++)
                if (rol.equals((String) this.rolesUSesion.get(i)))
                    return true;
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede identificar la adquisición del rol. " + e.getMessage());
        }
    }
    
    /**
     * Identifica si el usuario tiene el rol asignado para editar parte del task
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean tieneUnicamenteRol(String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            ProyectoControl pc = ((ProyectoControl) getControlador("ProyectoControl"));
            pc.cargarRolesSesion();
            this.rolesUSesion = pc.getRolesUSesion();
            if(this.rolesUSesion == null) return false;
            int t = this.rolesUSesion.size();
            for (int i = 0; i < t; i++){
                if (rol.equals((String) this.rolesUSesion.get(i)))
                    return t == 1;
            }
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede identificar la adquisición del rol. " + e.getMessage());
        }
    }

    /**
     * Actualiza el estado de la tarea con base en los avances reales de ésta
     * @param proyecto
     * @param nombreTarea
     * @param rol
     * @param identificacion
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarEstadoTarea(Proyecto proyecto, String nombreTarea, String rol, String identificacion) throws ExceptionFatal, ExceptionWarn {
        try{
            this.cargarTask();
            List atributos = task.getAtributos();
            AtributoCompuesto atributo;
            AtributoCompuesto atributo2;
            int t = atributos.size();
            for (int i = 0; i < t; i++) {
                atributo = (AtributoCompuesto) atributos.get(i);
                if (atributo.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").getValor().equals(nombreTarea)) {
                    atributo2 = atributo.getSubAtributo("Estado de la tarea");
                    atributo2.getSubAtributo(rol).getSubAtributo(identificacion).setValor("Terminada");
                    if (tareaFinalizada(atributo2)) actualizarDatosReales(atributo);
                    formatoFachada.actualizarFormato(this.task);
                    break;
                }
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede actualizar el estado de la tarea. " + e.getMessage());
        }
    }
    
    
    
    public void agregarEstadoTareaRol(Proyecto proyecto, String nombreTarea, String rol, String identificacion) throws ExceptionFatal, ExceptionWarn {
        this.cargarTask();
        List registrosEtapa = registrosEtapa((String)TSP.etapas.get(0));
        int totalRegistrosEtapaEstLanz = registrosEtapa.size();
        AtributoCompuesto registroTask;
        AtributoCompuesto estadoTarea;
        AtributoCompuesto atrRol;
        for(int i = 0; i < totalRegistrosEtapaEstLanz; i++){
            registroTask = (AtributoCompuesto)registrosEtapa.get(i);
            if(registroTask.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").getValor().equals(nombreTarea)){
                estadoTarea = registroTask.getSubAtributo("Estado de la tarea");
                try{
                    atrRol = estadoTarea.getSubAtributo(rol);
                    try{
                        atrRol.getSubAtributo(identificacion);
                    }catch(Exception e){
                        atrRol.getAtributos().add(new AtributoCompuesto(identificacion,"No terminada",null));
                    }
                }catch(Exception e){
                    estadoTarea.getAtributos().add(new AtributoCompuesto(rol,null,new ArrayList(){{add(new AtributoCompuesto(identificacion,"No terminada",null));}}));
                }
                guardarTaskNoPlaneada();
                break;
            }
        }
    }

    /**
     * Indica si la tarea se encuentra finalizada o no
     * @param atributo2
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean tareaFinalizada(AtributoCompuesto atributo2) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos3 = atributo2.getAtributos();
            List atributos4;
            int t = atributos3.size();
            int t2;
            for (int i = 0; i < t; i++) {
                atributos4 = ((AtributoCompuesto) atributos3.get(i)).getAtributos();
                t2 = atributos4.size();
                for (int j = 0; j < t2; j++)
                    if (((AtributoCompuesto) atributos4.get(j)).getValor().equals("Sin terminar"))
                        return false;

            }
            return true;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede identificar si la tarea fue finalizada. " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos reales del formato TASK
     * @param atributo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void actualizarDatosReales(AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atributo2;
            ProyectoControl pb = ((ProyectoControl) getControlador("ProyectoControl"));
            Date fechaInicioCiclo = pb.getCicloSesion().getFInicio();
            Date fechaHoy = new Date();
            atributo2 = atributo.getSubAtributo("Real");
            atributo2.getSubAtributo("N. Semana").setValor(Integer.toString(Helper.semanasEntreFechas(fechaInicioCiclo, fechaHoy) + 1));
            double horasReales = ((TiempoControl) getControlador("TiempoControl")).horasRealesTarea(pb.getProyectoSesion(), atributo.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").getValor());
            atributo2.getSubAtributo("Horas").setValor(Double.toString(horasReales));
            this.actualizarHorasRealesAcumuladas();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede actualizar los datos reales del formato task. " + e.getMessage());
        }
    }

    /**
     * Calcula el número de horas por semana de la tarea
     * @param numSemana
     * @param atributo3HorasPlan
     * @param atributo3HorasReal
     * @param atributo3VPAcumulado
     * @param atributo3VGSemanal
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void horasPorSemana(int numSemana, AtributoCompuesto atributo3HorasPlan, AtributoCompuesto atributo3HorasReal, AtributoCompuesto atributo3VPAcumulado, AtributoCompuesto atributo3VGSemanal) throws ExceptionFatal, ExceptionWarn {
        try{
        this.cargarTask();
        List atributos = this.task.getAtributos();
        int t = atributos.size();
        AtributoCompuesto atributo;
        AtributoCompuesto atributo2;
        double horasReales = 0;
        double horasPlaneadas = 0;
        String vp = "0";
        double vgSemana = 0;
        for (int i = 0; i < t; i++) {
            atributo = (AtributoCompuesto) atributos.get(i);
            atributo2 = atributo.getSubAtributo("Tamaño del plan");
            if (Integer.parseInt(atributo2.getSubAtributo("N. Semana").getValor()) == numSemana) {
                horasPlaneadas += Double.parseDouble(atributo.getSubAtributo("Horas del plan").getSubAtributo("Total horas").getValor());
                vp = atributo2.getSubAtributo("VP Acumulado").getValor();
            }
            atributo2 = atributo.getSubAtributo("Real");
            if (Integer.parseInt(atributo2.getSubAtributo("N. Semana").getValor()) == numSemana) {
                horasReales += Double.parseDouble(atributo2.getSubAtributo("Horas").getValor());
                vgSemana += Double.parseDouble(atributo.getSubAtributo("Tamaño del plan").getSubAtributo("VP").getValor());
            }
        }

        atributo3HorasPlan.setValor(Double.toString(horasPlaneadas));
        atributo3HorasReal.setValor(Double.toString(horasReales));
        atributo3VPAcumulado.setValor(vp);
        atributo3VGSemanal.setValor(Double.toString(vgSemana));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede calcular el total de horas por semana. " + e.getMessage());
        }
    }

    /**
     * Consolida la información semanal para el formato SCHEDULE
     * @param fechaHoy
     * @param numSemana
     * @param atributo2HorasTareasTermFaseActual
     * @param atributo2LiderProyecto
     * @param atributo2GerenteDesarrollo
     * @param atributo2GerentePlaneacion
     * @param atributo2GerenteCalidadProceso
     * @param atributo2GerenteSoporte
     * @param artibuto2TotalesInfSemanalIntegrts
     * @param atributos2
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void consolidarDatosSemana(Date fechaHoy, int numSemana, AtributoCompuesto atributo2HorasTareasTermFaseActual, AtributoCompuesto atributo2LiderProyecto, AtributoCompuesto atributo2GerenteDesarrollo, AtributoCompuesto atributo2GerentePlaneacion, AtributoCompuesto atributo2GerenteCalidadProceso, AtributoCompuesto atributo2GerenteSoporte, AtributoCompuesto artibuto2TotalesInfSemanalIntegrts, List atributos2) throws ExceptionFatal, ExceptionWarn {
        try{
            this.cargarTask();
            Proyecto proyecto = ((ProyectoControl) getControlador("ProyectoControl")).getProyectoSesion();
            TiempoControl tb = ((TiempoControl) getControlador("TiempoControl"));
            List atributos = this.task.getAtributos();
            int t = atributos.size();
            //if(t == 0) throw new ExceptionWarn("No pueden consolidar los datos de la semana sin registrar los datos de planeación. Complete y guarde el formato TASK.");
            int numSemanasFaseActual = Helper.semanasEntreFechas(((ProyectoControl) getControlador("ProyectoControl")).getFaseSesion().getFInicio(), fechaHoy);
            int numSemanaInicioFaseActual = numSemana - numSemanasFaseActual;
            int nSemana;
            List atrs3;

            AtributoCompuesto atributo;
            AtributoCompuesto atributo2;
            AtributoCompuesto atributo2_1;
            AtributoCompuesto atributo2_2;
            AtributoCompuesto atributo2_3;
            String nombreTarea;
            AtributoCompuesto atributo3;
            AtributoCompuesto atributo3_1;
            AtributoCompuesto atributo3_2;
            double horasPlanLiderProyecto = 0;
            double horasPlanGerenteDesarrollo = 0;
            double horasPlanGerentePlaneacion = 0;
            double horasPlanGerenteCalidadProceso = 0;
            double horasPlanGerenteSoporte = 0;

            double horasRealLiderProyecto = 0;
            double horasRealGerenteDesarrollo = 0;
            double horasRealGerentePlaneacion = 0;
            double horasRealGerenteCalidadProceso = 0;
            double horasRealGerenteSoporte = 0;

            double VPLiderProyecto = 0;
            double VPGerenteDesarrollo = 0;
            double VPGerentePlaneacion = 0;
            double VPGerenteCalidadProceso = 0;
            double VPGerenteSoporte = 0;

            double VGLiderProyecto = 0;
            double VGGerenteDesarrollo = 0;
            double VGGerentePlaneacion = 0;
            double VGGerenteCalidadProceso = 0;
            double VGGerenteSoporte = 0;

            int numRolesAsignadosATarea;
            double totalTareasHorasPlaneadas = 0;
            double totalTareasHorasReales = 0;
            double totalTareasVG = 0;

            double horasTareasTermFaseActualPlan = 0;
            double horasTareasTermFaseActualReal = 0;
            for (int i = 0; i < t; i++) {
                atributo = (AtributoCompuesto) atributos.get(i);
                nombreTarea = atributo.getSubAtributo("Tarea").getSubAtributo("Nombre de la tarea").getValor();
                atributo2_1 = atributo.getSubAtributo("Horas del plan");
                atributo3_1 = atributo2_1.getSubAtributo("Total horas");
                atributo2_2 = atributo.getSubAtributo("Estado de la tarea");
                atributo2 = atributo.getSubAtributo("Real");
                atributo3_2 = atributo2.getSubAtributo("Horas");
                numRolesAsignadosATarea = atributo2_2.getAtributos().size();
                atributo2_3 = atributo.getSubAtributo("Tamaño del plan");
                nSemana = Integer.parseInt(atributo2_3.getSubAtributo("N. Semana").getValor());
                atributo3 = atributo2_3.getSubAtributo("VP");
                if (nSemana == numSemana) {
                    horasPlanLiderProyecto += Double.parseDouble(atributo2_1.getSubAtributo(((Rol)TSP.rolesTSP.get(0)).getNombre()).getValor());
                    horasPlanGerenteDesarrollo += Double.parseDouble(atributo2_1.getSubAtributo(((Rol)TSP.rolesTSP.get(1)).getNombre()).getValor());
                    horasPlanGerentePlaneacion += Double.parseDouble(atributo2_1.getSubAtributo(((Rol)TSP.rolesTSP.get(4)).getNombre()).getValor());
                    horasPlanGerenteCalidadProceso += Double.parseDouble(atributo2_1.getSubAtributo(((Rol)TSP.rolesTSP.get(2)).getNombre()).getValor());
                    horasPlanGerenteSoporte += Double.parseDouble(atributo2_1.getSubAtributo(((Rol)TSP.rolesTSP.get(3)).getNombre()).getValor());
                    if (atributo2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(0)).getNombre()) != null)
                        VPLiderProyecto += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (atributo2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(1)).getNombre()) != null)
                        VPGerenteDesarrollo += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (atributo2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(4)).getNombre()) != null)
                        VPGerentePlaneacion += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (atributo2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(2)).getNombre()) != null)
                        VPGerenteCalidadProceso += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (atributo2_2.getSubAtributo(((Rol)TSP.rolesTSP.get(3)).getNombre()) != null)
                        VPGerenteSoporte += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                }
                if (nSemana >= numSemanaInicioFaseActual && nSemana <= numSemana)
                    horasTareasTermFaseActualPlan += Double.parseDouble(atributo3_1.getValor());
                nSemana = Integer.parseInt(atributo2.getSubAtributo("N. Semana").getValor());
                if (nSemana == numSemana) {
                    horasRealLiderProyecto += tb.horasRolTarea(proyecto, nombreTarea, ((Rol)TSP.rolesTSP.get(0)).getNombre());
                    horasRealGerenteDesarrollo += tb.horasRolTarea(proyecto, nombreTarea, ((Rol)TSP.rolesTSP.get(1)).getNombre());
                    horasRealGerentePlaneacion += tb.horasRolTarea(proyecto, nombreTarea, ((Rol)TSP.rolesTSP.get(4)).getNombre());
                    horasRealGerenteCalidadProceso += tb.horasRolTarea(proyecto, nombreTarea, ((Rol)TSP.rolesTSP.get(2)).getNombre());
                    horasRealGerenteSoporte += tb.horasRolTarea(proyecto, nombreTarea, ((Rol)TSP.rolesTSP.get(3)).getNombre());

                    if (tareaFinalizadaPorRol(atributo2_2, ((Rol)TSP.rolesTSP.get(0)).getNombre()))
                        VGLiderProyecto += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (tareaFinalizadaPorRol(atributo2_2, ((Rol)TSP.rolesTSP.get(1)).getNombre()))
                        VGGerenteDesarrollo += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (tareaFinalizadaPorRol(atributo2_2, ((Rol)TSP.rolesTSP.get(4)).getNombre()))
                        VGGerentePlaneacion += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (tareaFinalizadaPorRol(atributo2_2, ((Rol)TSP.rolesTSP.get(2)).getNombre()))
                        VGGerenteCalidadProceso += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;
                    if (tareaFinalizadaPorRol(atributo2_2, ((Rol)TSP.rolesTSP.get(3)).getNombre()))
                        VGGerenteSoporte += Double.parseDouble(atributo3.getValor()) / numRolesAsignadosATarea;

                    if (tareaFinalizada(atributo2_2)) {
                        atrs3 = new ArrayList();
                        totalTareasHorasPlaneadas += Double.parseDouble(atributo3_1.getValor());
                        totalTareasHorasReales += Double.parseDouble(atributo3_2.getValor());
                        totalTareasVG += Double.parseDouble(atributo3.getValor());
                        atrs3.add(new AtributoCompuesto("Horas planeadas", atributo3_1.getValor(), null));
                        atrs3.add(new AtributoCompuesto("Horas reales", atributo3_2.getValor(), null));
                        atrs3.add(new AtributoCompuesto("VG", atributo3.getValor(), null));
                        atrs3.add(new AtributoCompuesto("Semana planeada", atributo2_3.getSubAtributo("N. Semana").getValor(), null));
                        atrs3.add(new AtributoCompuesto("Nombre tarea", nombreTarea, null));
                        atributos2.add(new AtributoCompuesto("Tarea", atrs3));
                    }

                }
                if (nSemana >= numSemanaInicioFaseActual && nSemana <= numSemana) horasTareasTermFaseActualReal += Double.parseDouble(atributo3_2.getValor());

            }
            atributo2HorasTareasTermFaseActual.getSubAtributo("Plan").setValor(Double.toString(horasTareasTermFaseActualPlan));
            atributo2HorasTareasTermFaseActual.getSubAtributo("Real").setValor(Double.toString(horasTareasTermFaseActualReal));
            atributo2LiderProyecto.getSubAtributo("Horas planeadas").setValor(Double.toString(horasPlanLiderProyecto));
            atributo2GerenteDesarrollo.getSubAtributo("Horas planeadas").setValor(Double.toString(horasPlanGerenteDesarrollo));
            atributo2GerentePlaneacion.getSubAtributo("Horas planeadas").setValor(Double.toString(horasPlanGerentePlaneacion));
            atributo2GerenteCalidadProceso.getSubAtributo("Horas planeadas").setValor(Double.toString(horasPlanGerenteCalidadProceso));
            atributo2GerenteSoporte.getSubAtributo("Horas planeadas").setValor(Double.toString(horasPlanGerenteSoporte));

            atributo2LiderProyecto.getSubAtributo("Horas reales").setValor(Double.toString(horasRealLiderProyecto));
            atributo2GerenteDesarrollo.getSubAtributo("Horas reales").setValor(Double.toString(horasRealGerenteDesarrollo));
            atributo2GerentePlaneacion.getSubAtributo("Horas reales").setValor(Double.toString(horasRealGerentePlaneacion));
            atributo2GerenteCalidadProceso.getSubAtributo("Horas reales").setValor(Double.toString(horasRealGerenteCalidadProceso));
            atributo2GerenteSoporte.getSubAtributo("Horas reales").setValor(Double.toString(horasRealGerenteSoporte));

            atributo2LiderProyecto.getSubAtributo("VP").setValor(Double.toString(VPLiderProyecto));
            atributo2GerenteDesarrollo.getSubAtributo("VP").setValor(Double.toString(VPGerenteDesarrollo));
            atributo2GerentePlaneacion.getSubAtributo("VP").setValor(Double.toString(VPGerentePlaneacion));
            atributo2GerenteCalidadProceso.getSubAtributo("VP").setValor(Double.toString(VPGerenteCalidadProceso));
            atributo2GerenteSoporte.getSubAtributo("VP").setValor(Double.toString(VPGerenteSoporte));

            atributo2LiderProyecto.getSubAtributo("VG").setValor(Double.toString(VGLiderProyecto));
            atributo2GerenteDesarrollo.getSubAtributo("VG").setValor(Double.toString(VGGerenteDesarrollo));
            atributo2GerentePlaneacion.getSubAtributo("VG").setValor(Double.toString(VGGerentePlaneacion));
            atributo2GerenteCalidadProceso.getSubAtributo("VG").setValor(Double.toString(VGGerenteCalidadProceso));
            atributo2GerenteSoporte.getSubAtributo("VG").setValor(Double.toString(VGGerenteSoporte));

            artibuto2TotalesInfSemanalIntegrts.getSubAtributo("Horas reales").setValor(Double.toString(horasRealLiderProyecto + horasRealGerenteDesarrollo + horasRealGerentePlaneacion + horasRealGerenteCalidadProceso + horasRealGerenteSoporte));
            artibuto2TotalesInfSemanalIntegrts.getSubAtributo("Horas planeadas").setValor(Double.toString(horasPlanLiderProyecto + horasPlanGerenteDesarrollo + horasPlanGerentePlaneacion + horasPlanGerenteCalidadProceso + horasPlanGerenteSoporte));
            artibuto2TotalesInfSemanalIntegrts.getSubAtributo("VG").setValor(Double.toString(VPLiderProyecto + VPGerenteDesarrollo + VPGerentePlaneacion + VPGerenteCalidadProceso + VPGerenteSoporte));

            atrs3 = new ArrayList();
            atrs3.add(new AtributoCompuesto("Horas planeadas", Double.toString(totalTareasHorasPlaneadas), null));
            atrs3.add(new AtributoCompuesto("Horas reales", Double.toString(totalTareasHorasReales), null));
            atrs3.add(new AtributoCompuesto("VG", Double.toString(totalTareasVG), null));
            atrs3.add(new AtributoCompuesto("Nombre tarea", "Totales", null));
            atributos2.add(new AtributoCompuesto("Total", atrs3));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede consolidar los datos de la semana. " + e.getMessage());
        }
    }
    /**
     * Indica si la tarea ha sido finalizada por el rol
     * @param atributo2
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean tareaFinalizadaPorRol(AtributoCompuesto atributo2, String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos3 = atributo2.getAtributos();
            List atributos4;
            AtributoCompuesto atributo3;
            boolean encontrado = false;
            int t = atributos3.size();
            int t2;
            for (int i = 0; i < t; i++) {
                atributo3 = (AtributoCompuesto) atributos3.get(i);
                if (atributo3.getAtributo().equals(rol)) {
                    atributos4 = atributo3.getAtributos();
                    encontrado = true;
                    t2 = atributos4.size();
                    for (int j = 0; j < t2; j++)
                        if (((AtributoCompuesto) atributos4.get(j)).getValor().equals("Sin terminar"))
                            return false;
                    break;
                }

            }
            return encontrado;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede determinar si la tarea fue finalizada por el rol " + rol + ". " + e.getMessage());
        }
    }

    /**
     * Brinda un resumen del plan para el formato SUMP
     * @param atributos2TiemposFase
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void resumenPlaneacion(List atributos2TiemposFase) throws ExceptionFatal, ExceptionWarn {
        try{
            this.cargarTask();
            List atributosTask = this.task.getAtributos();
            int t = atributos2TiemposFase.size();
            int j = 0;
            int t2 = atributosTask.size();
            if(t2 == 0) throw new ExceptionWarn("No existe planeación de la cuál extraer los datos. Por favor complete y registre el formato TASK para continuar.");
            AtributoCompuesto atributo2TiempoEtapa;
            double totalHorasPlanEtapa;
            double totalHorasRealEtapa;
            double totalHorasPlan = 0;
            double totalHorasReal = 0;
            AtributoCompuesto atributoTask;
            for (int i = 0; i < t - 1; i++) {
                totalHorasPlanEtapa = 0;
                totalHorasRealEtapa = 0;
                atributo2TiempoEtapa = (AtributoCompuesto) atributos2TiemposFase.get(i);
                atributoTask = (AtributoCompuesto) atributosTask.get(j);
                while (atributoTask.getSubAtributo("Tarea").getSubAtributo("Etapa").getValor().equals(atributo2TiempoEtapa.getAtributo())) {
                    totalHorasPlanEtapa += Double.parseDouble(atributoTask.getSubAtributo("Horas del plan").getSubAtributo("Total horas").getValor());
                    totalHorasRealEtapa += Double.parseDouble(atributoTask.getSubAtributo("Real").getSubAtributo("Horas").getValor());
                    if (++j >= t2) break;
                    atributoTask = (AtributoCompuesto) atributosTask.get(j);
                }
                totalHorasPlan += totalHorasPlanEtapa;
                totalHorasReal += totalHorasRealEtapa;
                atributo2TiempoEtapa.getSubAtributo("Plan").setValor(Double.toString(totalHorasPlanEtapa));
                atributo2TiempoEtapa.getSubAtributo("Real").setValor(Double.toString(totalHorasRealEtapa));
            }

            atributo2TiempoEtapa = (AtributoCompuesto) atributos2TiemposFase.get(t - 1);
            atributo2TiempoEtapa.getSubAtributo("Plan").setValor(Double.toString(totalHorasPlan));
            atributo2TiempoEtapa.getSubAtributo("Real").setValor(Double.toString(totalHorasReal));

            double totalPorcentajes = 0;
            double totalReal;
            if(totalHorasReal != 0)
                for (int i = 0; i < t - 1; i++) {
                    atributo2TiempoEtapa = (AtributoCompuesto) atributos2TiemposFase.get(i);
                    totalReal = Helper.extraerNumero(atributo2TiempoEtapa.getSubAtributo("Real").getValor()) / totalHorasReal * 100;
                    atributo2TiempoEtapa.getSubAtributo("Real porcentaje").setValor(Double.toString(totalReal));
                    totalPorcentajes += totalReal;
                }
            ((AtributoCompuesto) atributos2TiemposFase.get(t - 1)).getSubAtributo("Real porcentaje").setValor(Double.toString(totalPorcentajes));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede obtener un resumen de planeación. " + e.getMessage());
        }
    }

    public List getUnidadesTamanio() throws ExceptionFatal {
        try{
            List unidadesTamanio = ProyectoDAO.consultarOpcionesTSP("unidades tamaño");
            if(unidadesTamanio.isEmpty()){
                Opciontsp o;
                int t = TSP.unidadesTamanio.size();
                for(int i = 0; i < t; i++){
                    o = new Opciontsp();
                    o.setNombre((String)TSP.unidadesTamanio.get(i));
                    o.setTipo("unidades tamaño");
                    unidadesTamanio.add(o);
                }
                ProyectoDAO.insertarOpcionesTSP(unidadesTamanio);
                return TSP.unidadesTamanio;
            }
            return unidadesTamanio;
        }catch(Exception e){
            throw new ExceptionFatal("PlaneacionControl no puede cargar las unidades de tamaño. " + e.getMessage());
        }
    }
    
    
    public List getRegistrosEtapas() {
        return registrosEtapas;
    }

    public void setRegistrosEtapas(List registrosEtapas) {
        this.registrosEtapas = registrosEtapas;
    }

    public List getEtapasTSP() {
        return etapasTSP;
    }

    public void setEtapasTSP(List etapasTSP) {
        this.etapasTSP = etapasTSP;
    }

    public List getRolesUsuarios() {
        return rolesUsuarios;
    }

    public void setRolesUsuarios(List rolesUsuarios) {
        this.rolesUsuarios = rolesUsuarios;
    }


    public FormatoConcreto getTask() {
        return task;
    }

    public void setTask(FormatoConcreto task) {
        this.task = task;
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