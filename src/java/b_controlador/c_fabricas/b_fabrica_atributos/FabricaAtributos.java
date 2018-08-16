/**
 * Fabrica concreta de atributos para formato
 */
package b_controlador.c_fabricas.b_fabrica_atributos;

import b_controlador.a_gestion.DefectosControl;
import b_controlador.a_gestion.PlaneacionControl;
import b_controlador.a_gestion.SemanalControl;
import c_negocio.a_relacional.Rol;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class FabricaAtributos extends FabricaAbstractaAtributos{

    public FabricaAtributos(){
    }
    /**
     * Crea los atributos para el formato LOGT
     * @return
     * @throws ExceptionFatal 
     */
    public AtributoCompuesto crearAtributosLogt() throws ExceptionFatal {
        try{
            AtributoCompuesto registroLogt = new AtributoCompuesto(new ArrayList());
            List atributos = registroLogt.getAtributos();
            atributos.add(new AtributoCompuesto("Fecha-hora inicio"));
            atributos.add(new AtributoCompuesto("Fecha-hora finalización"));
            atributos.add(new AtributoCompuesto("Tiempo dedicado", "0", null));
            atributos.add(new AtributoCompuesto("Tiempo interrumpido", "0", null));
            atributos.add(new AtributoCompuesto("Tarea"));
            atributos.add(new AtributoCompuesto("Comentarios"));
            return registroLogt;
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos logt");
        }
    }

    /**
     * Crea los atributos para el formato WEEK
     * @param sc
     * @param week
     * @param fechaHoy
     * @param numSemana
     * @throws ExceptionFatal 
     */
    public void crearAtributosWeek(SemanalControl sc, FormatoConcreto week, Date fechaHoy, int numSemana) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = week.getAtributos();
            atributos.add(new AtributoCompuesto("semana", Integer.toString(numSemana), null));
            List atributos2 = new ArrayList();
            List atributos3;
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan"));
            atributos3.add(new AtributoCompuesto("Real"));
            AtributoCompuesto atributo2HorasProyectoSemana = new AtributoCompuesto("Horas Semana", atributos3);
            atributos2.add(atributo2HorasProyectoSemana);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan"));
            atributos3.add(new AtributoCompuesto("Real"));
            AtributoCompuesto atributo2HorasProyectoCiclo = new AtributoCompuesto("Horas Ciclo", atributos3);
            atributos2.add(atributo2HorasProyectoCiclo);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan"));
            atributos3.add(new AtributoCompuesto("Real"));
            AtributoCompuesto atributo2VGSemana = new AtributoCompuesto("VG Semana", atributos3);
            atributos2.add(atributo2VGSemana);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan"));
            atributos3.add(new AtributoCompuesto("Real"));
            AtributoCompuesto atributo2VGCiclo = new AtributoCompuesto("VG Ciclo", atributos3);
            atributos2.add(atributo2VGCiclo);
            sc.consolidarScheduleSemana(numSemana, atributo2HorasProyectoSemana, atributo2HorasProyectoCiclo, atributo2VGSemana, atributo2VGCiclo);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan"));
            atributos3.add(new AtributoCompuesto("Real"));
            AtributoCompuesto atributo2HorasTareasTermFaseActual = new AtributoCompuesto("Total horas Fase Actual", atributos3);
            atributos2.add(atributo2HorasTareasTermFaseActual);
            atributos.add(new AtributoCompuesto("Información semanal", atributos2));
            atributos2 = new ArrayList();
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas planeadas"));
            atributos3.add(new AtributoCompuesto("Horas reales"));
            atributos3.add(new AtributoCompuesto("VG"));
            atributos3.add(new AtributoCompuesto("VP"));
            AtributoCompuesto atributo2LiderProyecto = new AtributoCompuesto(((Rol)TSP.rolesTSP.get(0)).getNombre(), atributos3);
            atributos2.add(atributo2LiderProyecto);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas planeadas"));
            atributos3.add(new AtributoCompuesto("Horas reales"));
            atributos3.add(new AtributoCompuesto("VG"));
            atributos3.add(new AtributoCompuesto("VP"));
            AtributoCompuesto atributo2GerenteDesarrollo = new AtributoCompuesto(((Rol)TSP.rolesTSP.get(1)).getNombre(), atributos3);
            atributos2.add(atributo2GerenteDesarrollo);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas planeadas"));
            atributos3.add(new AtributoCompuesto("Horas reales"));
            atributos3.add(new AtributoCompuesto("VG"));
            atributos3.add(new AtributoCompuesto("VP"));
            AtributoCompuesto atributo2GerentePlaneacion = new AtributoCompuesto(((Rol)TSP.rolesTSP.get(4)).getNombre(), atributos3);
            atributos2.add(atributo2GerentePlaneacion);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas planeadas"));
            atributos3.add(new AtributoCompuesto("Horas reales"));
            atributos3.add(new AtributoCompuesto("VG"));
            atributos3.add(new AtributoCompuesto("VP"));
            AtributoCompuesto atributo2GerenteCalidadProceso = new AtributoCompuesto(((Rol)TSP.rolesTSP.get(2)).getNombre(), atributos3);
            atributos2.add(atributo2GerenteCalidadProceso);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas planeadas"));
            atributos3.add(new AtributoCompuesto("Horas reales"));
            atributos3.add(new AtributoCompuesto("VG"));
            atributos3.add(new AtributoCompuesto("VP"));
            AtributoCompuesto atributo2GerenteSoporte = new AtributoCompuesto(((Rol)TSP.rolesTSP.get(3)).getNombre(), atributos3);
            atributos2.add(atributo2GerenteSoporte);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas planeadas"));
            atributos3.add(new AtributoCompuesto("Horas reales"));
            atributos3.add(new AtributoCompuesto("VG"));
            AtributoCompuesto artibuto2TotalesInfSemanalIntegrts = new AtributoCompuesto("Totales integrantes", atributos3);
            atributos2.add(artibuto2TotalesInfSemanalIntegrts);
            atributos.add(new AtributoCompuesto("Información semanal integrantes", atributos2));
            atributos2 = new ArrayList();
            ((PlaneacionControl) getControlador("PlaneacionControl")).consolidarDatosSemana(fechaHoy, numSemana, atributo2HorasTareasTermFaseActual, atributo2LiderProyecto, atributo2GerenteDesarrollo, atributo2GerentePlaneacion, atributo2GerenteCalidadProceso, atributo2GerenteSoporte, artibuto2TotalesInfSemanalIntegrts, atributos2);
            atributos.add(new AtributoCompuesto("Tareas completas", atributos2));
            atributos.add(new AtributoCompuesto("Comentarios"));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos week");
        }
    }

    /**
     * Crea los atributos para el formato SCHEDULE
     * @param numSemana
     * @return
     * @throws ExceptionFatal 
     */
    public AtributoCompuesto crearAtributosSchedule(int numSemana) throws ExceptionFatal {
        try{
            List atributos2 = new ArrayList();
            List atributos3;
            atributos2.add(new AtributoCompuesto("N. Semana", Integer.toString(numSemana), null));
            atributos2.add(new AtributoCompuesto("Fecha", Helper.formatearDate(new Date(), "dd/MM/yyyy"), null));
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas directas", "0", null));
            atributos3.add(new AtributoCompuesto("Horas acumuladas", "0", null));
            atributos3.add(new AtributoCompuesto("VP Acumulado", "0", null));
            atributos2.add(new AtributoCompuesto("Plan", atributos3));
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Horas de equipo", "0", null));
            atributos3.add(new AtributoCompuesto("Horas acumuladas", "0", null));
            atributos3.add(new AtributoCompuesto("VG Semanal", "0", null));
            atributos3.add(new AtributoCompuesto("VG Acumulado", "0", null));
            atributos2.add(new AtributoCompuesto("Real", atributos3));
            return new AtributoCompuesto(atributos2);
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos schedule");
        }
    }
    
    /**
     * Crea los atributos para el formato ITL
     * @return
     * @throws ExceptionFatal 
     */
    public AtributoCompuesto crearAtributosItl() throws ExceptionFatal {
        try{
            List atributos = new ArrayList();
            atributos.add(new AtributoCompuesto("fecha"));
            atributos.add(new AtributoCompuesto("riesgo-problema"));
            atributos.add(new AtributoCompuesto("numero"));
            atributos.add(new AtributoCompuesto("prioridad"));
            atributos.add(new AtributoCompuesto("autor"));
            atributos.add(new AtributoCompuesto("fecha seguimiento"));
            atributos.add(new AtributoCompuesto("fecha resolución"));
            atributos.add(new AtributoCompuesto("descripción"));
            return new AtributoCompuesto(atributos);
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos itl");
        }
    }
    
    /**
     * Crea los atributos para el formato TASK
     * @param etapa
     * @return
     * @throws ExceptionFatal 
     */
    public AtributoCompuesto crearAtributosTask(String etapa) throws ExceptionFatal {
        try{
            AtributoCompuesto atributo;
            AtributoCompuesto atributo2;
            List atributos2;
            List atributos3;
            atributo = new AtributoCompuesto(new ArrayList());
            atributos2 = atributo.getAtributos();
            
            atributo2 = new AtributoCompuesto("Tarea", new ArrayList());
            atributos3 = atributo2.getAtributos();
            atributos3.add(new AtributoCompuesto("Etapa", etapa, null));
            atributos3.add(new AtributoCompuesto("Parte"));
            atributos3.add(new AtributoCompuesto("Nombre de la tarea",etapa,null));
            atributos3.add(new AtributoCompuesto("N. Integrts.", "0", null));
            atributos2.add(atributo2);
            
            atributo2 = new AtributoCompuesto("Horas del plan", new ArrayList());
            atributos3 = atributo2.getAtributos();
            atributos3.add(new AtributoCompuesto(((Rol)TSP.rolesTSP.get(0)).getNombre(), "0", null));
            atributos3.add(new AtributoCompuesto(((Rol)TSP.rolesTSP.get(1)).getNombre(), "0", null));
            atributos3.add(new AtributoCompuesto(((Rol)TSP.rolesTSP.get(4)).getNombre(), "0", null));
            atributos3.add(new AtributoCompuesto(((Rol)TSP.rolesTSP.get(2)).getNombre(), "0", null));
            atributos3.add(new AtributoCompuesto(((Rol)TSP.rolesTSP.get(3)).getNombre(), "0", null));
            atributos3.add(new AtributoCompuesto("Total horas", "0", null));
            atributos3.add(new AtributoCompuesto("Acumulado horas", "0", null));
            atributos2.add(atributo2);

            atributo2 = new AtributoCompuesto("Tamaño del plan", new ArrayList());
            atributos3 = atributo2.getAtributos();
            atributos3.add(new AtributoCompuesto("Unidad de tamaño"));
            atributos3.add(new AtributoCompuesto("Tamaño", "0", null));
            atributos3.add(new AtributoCompuesto("N. Semana", "0", null));
            atributos3.add(new AtributoCompuesto("VP", "0", null));
            atributos3.add(new AtributoCompuesto("VP Acumulado", "0", null));
            atributos2.add(atributo2);

            atributo2 = new AtributoCompuesto("Real", new ArrayList());
            atributos3 = atributo2.getAtributos();
            atributos3.add(new AtributoCompuesto("Horas", "0", null));
            atributos3.add(new AtributoCompuesto("Horas acumuladas", "0", null));
            atributos3.add(new AtributoCompuesto("N. Semana", "0", null));
            atributos2.add(atributo2);

            atributos2.add(new AtributoCompuesto("Estado de la tarea", new ArrayList()));
            return atributo;
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos task");
        }
    }

    /**
     * Crea los atributos para el formato STRAT
     * @param nombreParte
     * @param numCiclos
     * @return
     * @throws ExceptionFatal 
     */
    public AtributoCompuesto crearAtributosStrat(String nombreParte, int numCiclos) throws ExceptionFatal {
      try{
            AtributoCompuesto atributo;
            AtributoCompuesto atributo2;
            List atributos2;
            List atributos3;
            atributo = new AtributoCompuesto(new ArrayList());
            atributos2 = atributo.getAtributos();
            atributos2.add(new AtributoCompuesto("parte", nombreParte, null));
            atributos2.add(new AtributoCompuesto("funcion"));
            atributo2 = new AtributoCompuesto(new ArrayList());
            atributo2.setAtributo("TAM_Ciclo");
            atributos3 = atributo2.getAtributos();
            for (int i = 0; i < numCiclos; i++)
                atributos3.add(new AtributoCompuesto("C" + Integer.toString(i + 1),"0",null));
            atributos2.add(atributo2);
            atributo2 = new AtributoCompuesto(new ArrayList());
            atributo2.setAtributo("Horas_Ciclo");
            atributos3 = atributo2.getAtributos();
            for (int i = 0; i < numCiclos; i++)
                atributos3.add(new AtributoCompuesto("C" + Integer.toString(i + 1),"0",null));
            atributos2.add(atributo2);
            return atributo;
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos strat");
        }
    }

    /**
     * Crea los atributos para el formato LOGD
     */
    public AtributoCompuesto crearAtributosLogd() throws ExceptionFatal {
        try{
            List atributos = new ArrayList();
            atributos.add(new AtributoCompuesto("fecha"));
            atributos.add(new AtributoCompuesto("numero"));
            atributos.add(new AtributoCompuesto("tipo"));
            atributos.add(new AtributoCompuesto("etapa inyección"));
            atributos.add(new AtributoCompuesto("etapa remoción"));
            atributos.add(new AtributoCompuesto("tiempo arreglo"));
            atributos.add(new AtributoCompuesto("defecto arreglado"));
            atributos.add(new AtributoCompuesto("descripción"));
            atributos.add(new AtributoCompuesto("autor"));
            return new AtributoCompuesto(atributos);
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos logd");
        }
    }
    
    /**
     * Crea los atributos para el formato PIP
     * @param pip
     * @throws ExceptionFatal 
     */
    public void crearAtributosPip(FormatoConcreto pip) throws ExceptionFatal {
        try{
            List atributos = pip.getAtributos();
            atributos.add(new AtributoCompuesto("parte"));
            atributos.add(new AtributoCompuesto("Prioridad"));
            atributos.add(new AtributoCompuesto("Descripción del problema"));
            atributos.add(new AtributoCompuesto("Descripción de la propuesta"));
            atributos.add(new AtributoCompuesto("Fecha revision"));
            atributos.add(new AtributoCompuesto("Cambios"));
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos pip");
        }
    }

    /**
     * Crea los atributos para el formato PEER
     * @param peer
     * @throws ExceptionFatal 
     */
    public void crearAtributosPeer(FormatoConcreto peer) throws ExceptionFatal {
        try{
            List atributos = peer.getAtributos();
            List atributos2;
            List atributos3;
            int t = TSP.rolesTSP.size();
            atributos2 = new ArrayList();
            for (int i = 0; i < t; i++) {
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Trabajo requerido", "0", null));
                atributos3.add(new AtributoCompuesto("Dificultad del rol", "0", null));
                atributos2.add(new AtributoCompuesto(((Rol) TSP.rolesTSP.get(i)).getNombre(), atributos3));
            }

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Trabajo requerido", "0", null));
            atributos3.add(new AtributoCompuesto("Dificultad del rol", "0", null));
            atributos2.add(new AtributoCompuesto("Total", atributos3));

            atributos.add(new AtributoCompuesto("Trabajo y dificultad", atributos2));

            atributos2 = new ArrayList();
            atributos2.add(new AtributoCompuesto("Espíritu de equipo"));
            atributos2.add(new AtributoCompuesto("Efectividad general"));
            atributos2.add(new AtributoCompuesto("Experiencia gratificante"));
            atributos2.add(new AtributoCompuesto("Productividad de equipo"));
            atributos2.add(new AtributoCompuesto("Calidad del proceso"));
            atributos2.add(new AtributoCompuesto("Calidad del producto"));

            atributos.add(new AtributoCompuesto("Criterios equipo", atributos2));

            atributos2 = new ArrayList();
            for (int i = 0; i < t; i++)
                atributos2.add(new AtributoCompuesto(((Rol) TSP.rolesTSP.get(i)).getNombre()));
            atributos.add(new AtributoCompuesto("Contribución general", atributos2));
            atributos2 = new ArrayList();
            for (int i = 0; i < t; i++)
                atributos2.add(new AtributoCompuesto(((Rol) TSP.rolesTSP.get(i)).getNombre()));
            atributos.add(new AtributoCompuesto("Ayuda y soporte", atributos2));
            atributos2 = new ArrayList();
            for (int i = 0; i < t; i++)
                atributos2.add(new AtributoCompuesto(((Rol) TSP.rolesTSP.get(i)).getNombre()));
            atributos.add(new AtributoCompuesto("Desempeño", atributos2));
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear los atributos Peer");
        }
    }
    
    /**
     * Crea los atributos para el formato CCR
     */
    public void crearAtributosCcr(FormatoConcreto ccr) throws ExceptionFatal {
        try{
            AtributoCompuesto atributo;
            ccr.agregarAtributo(new AtributoCompuesto("parte"));
            ccr.agregarAtributo(new AtributoCompuesto("aprobación", "Pendiente", null));
            ccr.agregarAtributo(new AtributoCompuesto("Fecha aprobación", "", null));
            List atributos2 = new ArrayList();
            atributos2.add(new AtributoCompuesto("Razón del cambio", null));
            atributos2.add(new AtributoCompuesto("Beneficios del cambio", null));
            atributos2.add(new AtributoCompuesto("Impacto del cambio", null));
            atributos2.add(new AtributoCompuesto("Descripción del cambio", null));
            atributo = new AtributoCompuesto("Información del cambio", atributos2);
            ccr.agregarAtributo(atributo);
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear crear los atributos ccr");
        }
    }
    /**
     * Crea los atributos para el formato SUMP
     */
    public void crearAtributosSump(FormatoConcreto sump, FormatoConcreto sumq) throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atrRendimientosFaseSumq = sumq.getAtributo("Rendimientos de fase");
            AtributoCompuesto atrTasaInyeccionDefectos = sumq.getAtributo("Tasa de inyección de defectos");
            List atributos = sump.getAtributos();
            List<Double> defectosInyectados = new ArrayList<Double>();
            int t2;
            List atributos2;
            List atributos3;
            double totalPlan;
            double totalReal;
            String etapa;
            double resultadoPorcentaje;
            double totalRealPorcentaje;
            int t = TSP.etapas.size();
            double resultadoPlan;
            double resultadoReal;

            //1. Tamaño del producto (SUMP)
            atributos2 = new ArrayList();

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));

            atributos2.add(new AtributoCompuesto("Páginas de requerimientos (SRS)", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("Otras páginas de texto", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("Páginas de diseño de alto nivel (SDS)", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("Total de líneas de diseño", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            AtributoCompuesto codBase = new AtributoCompuesto("Código base (B)", atributos3);
            atributos2.add(codBase);

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            AtributoCompuesto codEliminado = new AtributoCompuesto("Código eliminado (D)", atributos3);
            atributos2.add(codEliminado);

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            AtributoCompuesto atrModificado = new AtributoCompuesto("Código modificado (M)", atributos3);
            atributos2.add(atrModificado);
            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            AtributoCompuesto atrAniadido = new AtributoCompuesto("Código añadido (A)", atributos3);
            atributos2.add(atrAniadido);

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            AtributoCompuesto atrResumen_Reuso = new AtributoCompuesto("Código reutilizado (R)", atributos3);
            atributos2.add(atrResumen_Reuso);

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrModificado.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atrAniadido.getSubAtributo("Plan").getValor())), null));
            atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrModificado.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atrAniadido.getSubAtributo("Real").getValor())), null));
            AtributoCompuesto atrResumen_CodHora = new AtributoCompuesto("Código total nuevo y reutilizado (N)", atributos3);
            atributos2.add(atrResumen_CodHora);

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrResumen_CodHora.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(codBase.getSubAtributo("Plan").getValor()) - Helper.extraerNumero(codEliminado.getSubAtributo("Plan").getValor()) - Helper.extraerNumero(atrModificado.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Plan").getValor())), null));
            atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrResumen_CodHora.getSubAtributo("Real").getValor()) + Helper.extraerNumero(codBase.getSubAtributo("Real").getValor()) - Helper.extraerNumero(codEliminado.getSubAtributo("Real").getValor()) - Helper.extraerNumero(atrModificado.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Real").getValor())), null));
            AtributoCompuesto atrResumen_total = new AtributoCompuesto("Código total (T)", atributos3);
            atributos2.add(atrResumen_total);

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan", "0", null));
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("Intervalo de predicción (70%)", atributos3));
            
            atributos.add(new AtributoCompuesto("Tamaño del producto", atributos2));



            //2. Tiempo en fase (horas)  (SUMP)
            atributos2 = new ArrayList();
            for (int i = 0; i < t; i++) {
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real"));
                atributos3.add(new AtributoCompuesto("Real porcentaje","0",null));
                atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(i), atributos3));
            }

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Plan"));
            atributos3.add(new AtributoCompuesto("Real"));
            atributos3.add(new AtributoCompuesto("Real porcentaje","0",null));
            AtributoCompuesto atrResumen_totalTiempoFase = new AtributoCompuesto("Total", atributos3);
            atributos2.add(atrResumen_totalTiempoFase);
            ((PlaneacionControl)getControlador("PlaneacionControl")).resumenPlaneacion(atributos2);
            AtributoCompuesto atrTiempoFaseHorasSump = new AtributoCompuesto("Tiempo en fase (horas)", atributos2);
            atributos.add(atrTiempoFaseHorasSump);
            
            //3. Defectos inyectados  (SUMP)
            AtributoCompuesto atrTotalDefectosInyectados;
            if (atrTasaInyeccionDefectos != null) {

                atributos2 = new ArrayList();
                totalPlan = 0;
                totalReal = 0;
                totalRealPorcentaje = 0;
                for (int i = 2; i < t - 2; i++) {
                    etapa = (String) TSP.etapas.get(i);
                    resultadoPlan = Helper.extraerNumero(atrTasaInyeccionDefectos.getSubAtributo(etapa).getSubAtributo("Plan").getValor()) * Helper.extraerNumero(atrTiempoFaseHorasSump.getSubAtributo(etapa).getSubAtributo("Plan").getValor());
                    totalPlan += resultadoPlan;
                    atributos3 = new ArrayList();
                    defectosInyectados.add(resultadoPlan);
                    atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                    atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos3.add(new AtributoCompuesto("Real porcentaje"));
                    atributos2.add(new AtributoCompuesto(etapa, atributos3));
                }
                t2 = atributos2.size();
                AtributoCompuesto defectoInyectadoEtapa;
                for (int i = 0; i < t2; i++) {
                    defectoInyectadoEtapa = ((AtributoCompuesto) atributos2.get(i));
                    resultadoPorcentaje = totalReal == 0 ? 0 : Helper.extraerNumero(defectoInyectadoEtapa.getSubAtributo("Real").getValor()) / totalReal * 100;
                    defectoInyectadoEtapa.getSubAtributo("Real porcentaje").setValor(Double.toString(resultadoPorcentaje));
                    totalRealPorcentaje += resultadoPorcentaje;
                }
                
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(totalPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(totalReal), null));
                atributos3.add(new AtributoCompuesto("Real porcentaje", Double.toString(totalRealPorcentaje), null));
                atrTotalDefectosInyectados = new AtributoCompuesto("Total", atributos3);
                atributos2.add(atrTotalDefectosInyectados);
                atributos.add(new AtributoCompuesto("Defectos inyectados", atributos2));

            } else {
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos3.add(new AtributoCompuesto("Real porcentaje", "0", null));
                    atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(i), atributos3));
                }
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos3.add(new AtributoCompuesto("Real porcentaje", "0", null));
                atrTotalDefectosInyectados = new AtributoCompuesto("Total", atributos3);
                atributos2.add(atrTotalDefectosInyectados);

                atributos.add(new AtributoCompuesto("Defectos inyectados", atributos2));
            }

            //4. Defectos removidos (SUMP)
            if (atrRendimientosFaseSumq != null) {
                totalPlan = 0;
                totalReal = 0;
                totalRealPorcentaje = 0;
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    etapa = (String) TSP.etapas.get(i);
                    atributos3 = new ArrayList();
                    resultadoPlan = defectosInyectados.get(i - 2) * Helper.extraerNumero(atrRendimientosFaseSumq.getSubAtributo(etapa).getSubAtributo("Plan").getValor())/100;
                    totalPlan += resultadoPlan;
                    atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                    resultadoReal = 0;
                    totalReal += resultadoReal;
                    atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                    atributos3.add(new AtributoCompuesto("Real porcentaje"));
                    atributos2.add(new AtributoCompuesto(etapa, atributos3));
                }
                AtributoCompuesto defectoRemovidoEtapa;
                t2 = atributos2.size();
                for (int i = 0; i < t2; i++) {
                    defectoRemovidoEtapa = ((AtributoCompuesto) atributos2.get(i));
                    resultadoPorcentaje = totalReal == 0 ? 0 : Helper.extraerNumero(defectoRemovidoEtapa.getSubAtributo("Real").getValor()) / totalReal * 100;
                    defectoRemovidoEtapa.getSubAtributo("Real porcentaje").setValor(Double.toString(resultadoPorcentaje));
                    totalRealPorcentaje += resultadoPorcentaje;
                }
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(totalPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(totalReal), null));
                atributos3.add(new AtributoCompuesto("Real porcentaje", Double.toString(totalRealPorcentaje), null));
                AtributoCompuesto  atrTotalDefectosRemovidos = new AtributoCompuesto("Total", atributos3);
                atributos2.add(atrTotalDefectosRemovidos);
                
                atributos3 = new ArrayList();
                AtributoCompuesto atrRendimientoFasePruebaAceptacion =  atrRendimientosFaseSumq.getSubAtributo("Prueba de aceptación");
                double defectosRemovidosPruebaAceptacionPlan =(Helper.extraerNumero(atrTotalDefectosInyectados.getSubAtributo("Plan").getValor()) - Helper.extraerNumero(atrTotalDefectosRemovidos.getSubAtributo("Plan").getValor()))*Helper.extraerNumero(atrRendimientoFasePruebaAceptacion.getSubAtributo("Plan").getValor())/100;
                double defectosRemovidosPruebaAceptacionReal = (Helper.extraerNumero(atrTotalDefectosInyectados.getSubAtributo("Real").getValor()) - Helper.extraerNumero(atrTotalDefectosRemovidos.getSubAtributo("Real").getValor()))*Helper.extraerNumero(atrRendimientoFasePruebaAceptacion.getSubAtributo("Real").getValor())/100;
                atributos3.add(new AtributoCompuesto("Plan",Double.toString(defectosRemovidosPruebaAceptacionPlan),null));
                atributos3.add(new AtributoCompuesto("Real",Double.toString(defectosRemovidosPruebaAceptacionReal),null));
                atributos2.add(new AtributoCompuesto("Prueba de aceptación", atributos3));
                
                atributos3 = new ArrayList();                
                atributos3.add(new AtributoCompuesto("Plan",Double.toString(Helper.extraerNumero(atrTotalDefectosInyectados.getSubAtributo("Plan").getValor()) - Helper.extraerNumero(atrTotalDefectosRemovidos.getSubAtributo("Plan").getValor()) + defectosRemovidosPruebaAceptacionPlan),null));
                atributos3.add(new AtributoCompuesto("Real",Double.toString(Helper.extraerNumero(atrTotalDefectosInyectados.getSubAtributo("Real").getValor()) - Helper.extraerNumero(atrTotalDefectosRemovidos.getSubAtributo("Real").getValor()) + defectosRemovidosPruebaAceptacionReal),null));
                atributos2.add(new AtributoCompuesto("Vida de producto", atributos3));                
                
                atributos.add(new AtributoCompuesto("Defectos removidos", atributos2));

            } else {
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos3.add(new AtributoCompuesto("Real porcentaje", "0", null));
                    atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(i), atributos3));
                }

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos3.add(new AtributoCompuesto("Real porcentaje", "0", null));
                atributos2.add(new AtributoCompuesto("Total", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan","0",null));
                atributos3.add(new AtributoCompuesto("Real","0",null));
                atributos2.add(new AtributoCompuesto("Prueba de aceptación", atributos3));
                
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan","0",null));
                atributos3.add(new AtributoCompuesto("Real","0",null));
                atributos2.add(new AtributoCompuesto("Vida de producto", atributos3));  
                
                atributos.add(new AtributoCompuesto("Defectos removidos", atributos2));

            }
            
            int totalActualAtributos = atributos.size();
            //Se consolidan los defectos inyectados y removidos; parámetros de la función.
            ((DefectosControl)getControlador("DefectosControl")).consolidarDefectos((AtributoCompuesto)atributos.get(totalActualAtributos - 2),(AtributoCompuesto)atributos.get(totalActualAtributos - 1));

            //5. Resumen (SUMP)
            atributos2 = new ArrayList();
            atributos3 = new ArrayList();
            resultadoPlan = Helper.extraerNumero(atrResumen_totalTiempoFase.getSubAtributo("Plan").getValor());
            if (resultadoPlan == 0) atributos3.add(new AtributoCompuesto("Plan", "0", null));
            else atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrResumen_CodHora.getSubAtributo("Plan").getValor()) / resultadoPlan), null));
            resultadoReal = Helper.extraerNumero(atrResumen_totalTiempoFase.getSubAtributo("Real").getValor());
            if (resultadoReal == 0) atributos3.add(new AtributoCompuesto("Real", "0", null));
            else atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrResumen_CodHora.getSubAtributo("Real").getValor()) / resultadoReal), null));
            atributos2.add(new AtributoCompuesto("Código/hora", atributos3));
            atributos3 = new ArrayList();
            if (resultadoReal != 0) atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoPlan / resultadoReal), null));
            else atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("CPI (índice de costo-desempeño)", atributos3));

            atributos3 = new ArrayList();
            resultadoPlan = Helper.extraerNumero(atrResumen_total.getSubAtributo("Plan").getValor());
            if (resultadoPlan == 0) atributos3.add(new AtributoCompuesto("Plan", "0", null));
            else atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Plan").getValor()) / resultadoPlan), null));
            resultadoReal = Helper.extraerNumero(atrResumen_total.getSubAtributo("Real").getValor());
            if (resultadoReal == 0) atributos3.add(new AtributoCompuesto("Real", "0", null));
            else atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Real").getValor()) / resultadoReal), null));
            atributos2.add(new AtributoCompuesto("Porcentaje reuso", atributos3));
            atributos.add(new AtributoCompuesto("Resumen", atributos2));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear crear los atributos sump. " + e.getMessage());
        }
    }

    /**
     * Crea los atributos para el formato SUMQ
     * @param sumq
     * @param sump
     * @throws ExceptionFatal 
     */
    public void crearAtributosSumq(FormatoConcreto sumq, FormatoConcreto sump) throws ExceptionFatal {
        try{
            List atributos = sumq.getAtributos();
            List atributos2;
            List atributos3;
            int t = TSP.etapas.size();
            double resultadoPlan;
            double resultadoReal;

            String etapa;
            AtributoCompuesto atrNumerador;
            AtributoCompuesto atrDenominador;
            AtributoCompuesto atrTamanioProductoSump = sump.getAtributo("Tamaño del producto");
            AtributoCompuesto atrTiempoFaseSump = sump.getAtributo("Tiempo en fase (horas)");
            AtributoCompuesto atrDefectosInyectadosSump = sump.getAtributo("Defectos inyectados");
            AtributoCompuesto atrDefectosRemovidosSump = sump.getAtributo("Defectos removidos");

            // 1. Porcentaje libre de defectos (SUMQ)
            atributos2 = new ArrayList();

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("En compilación", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("En prueba de unidad", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("En construcción e integración", atributos3));

            atributos3 = new ArrayList();
            atributos3.add(new AtributoCompuesto("Real", "0", null));
            atributos2.add(new AtributoCompuesto("En prueba de sistema", atributos3));
            ((DefectosControl)getControlador("DefectosControl")).pdf(atributos2);
            atributos.add(new AtributoCompuesto("Porcentaje libre de defectos", atributos2));
            
            //2. Defectos/Página (SUMQ)
            if (atrDefectosRemovidosSump != null && atrTamanioProductoSump != null) {

                AtributoCompuesto atrTamProdPagsRequerim = atrTamanioProductoSump.getSubAtributo("Páginas de requerimientos (SRS)");
                AtributoCompuesto atrDefectosRemovInspeccRequerimientos = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(5));

                resultadoPlan = Helper.extraerNumero(atrTamProdPagsRequerim.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrTamProdPagsRequerim.getSubAtributo("Real").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrDefectosRemovInspeccRequerimientos.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrDefectosRemovInspeccRequerimientos.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos2 = new ArrayList();
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(5), atributos3));

                AtributoCompuesto atrTamProdPagsDisenioAltoNivel = atrTamanioProductoSump.getSubAtributo("Páginas de diseño de alto nivel (SDS)");
                AtributoCompuesto atrDefectosRemovInspDisenioAltoNivel = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(8));

                resultadoPlan = Helper.extraerNumero(atrTamProdPagsDisenioAltoNivel.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrTamProdPagsDisenioAltoNivel.getSubAtributo("Real").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrDefectosRemovInspDisenioAltoNivel.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrDefectosRemovInspDisenioAltoNivel.getSubAtributo("Real").getValor()) / resultadoReal;
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(8), atributos3));

                atributos.add(new AtributoCompuesto("Defectos/página", atributos2));
            } else {
                atributos2 = new ArrayList();
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(5), atributos3));
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(8), atributos3));

                atributos.add(new AtributoCompuesto("Defectos/página", atributos2));
            }
            //3. Defectos/Código (SUMQ)
            if (atrDefectosRemovidosSump != null && atrTamanioProductoSump != null) {
                AtributoCompuesto codigoTotalNuevoReutilizado = atrTamanioProductoSump.getSubAtributo("Código total nuevo y reutilizado (N)");
                atributos2 = new ArrayList();
                resultadoPlan = Helper.extraerNumero(codigoTotalNuevoReutilizado.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(codigoTotalNuevoReutilizado.getSubAtributo("Real").getValor());
                boolean resPlanPositivo = resultadoPlan > 0;
                boolean resRealPositivo = resultadoReal > 0;
                atributos3 = new ArrayList();
                AtributoCompuesto atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(10));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(10), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(12));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(12), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(14));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(14), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(15));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(15), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(16));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(16), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(17));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(17), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(18));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Construcción e integración", atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(19));
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(19), atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo("Total");
                AtributoCompuesto atrDefectosRemovidosSumpTotal = atrEtapaDefecto;
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Desarollo total", atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo("Prueba de aceptación");
                AtributoCompuesto atrDefectosRemovidosSumpPruebaAceptacion = atrDefectosRemovidosSumpTotal;

                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Prueba de aceptación", atributos3));

                atributos3 = new ArrayList();
                atrEtapaDefecto = atrDefectosRemovidosSump.getSubAtributo("Vida de producto");
                AtributoCompuesto atrDefectosRemovidosSumpVidaProducto = atrEtapaDefecto;
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Plan").getValor()) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrEtapaDefecto.getSubAtributo("Real").getValor()) / resultadoReal * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Vida de producto", atributos3));

                atributos3 = new ArrayList();
                if (resPlanPositivo) atributos3.add(new AtributoCompuesto("Plan", Double.toString((Helper.extraerNumero(atrDefectosRemovidosSumpTotal.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atrDefectosRemovidosSumpPruebaAceptacion.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atrDefectosRemovidosSumpVidaProducto.getSubAtributo("Plan").getValor())) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Plan", "0", null));
                if (resRealPositivo) atributos3.add(new AtributoCompuesto("Real", Double.toString((Helper.extraerNumero(atrDefectosRemovidosSumpTotal.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atrDefectosRemovidosSumpPruebaAceptacion.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atrDefectosRemovidosSumpVidaProducto.getSubAtributo("Real").getValor())) / resultadoPlan * 1000), null));
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Total", atributos3));
                
                atributos.add(new AtributoCompuesto("Defectos/Código", atributos2));

            } else {
                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(10), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(12), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(14), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(15), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(16), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(17), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Construcción e integración", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(19), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Desarollo total", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Prueba de aceptación", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Vida de producto", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Total", atributos3));

                atributos.add(new AtributoCompuesto("Defectos/Código", atributos2));
            }

            AtributoCompuesto atrDefectosRemovidosEtapaSump;
            AtributoCompuesto atrDefectosRemovidosEtapaSump2;
            //4. Proporciones de defecto (SUMQ)
            if (atrDefectosRemovidosSump != null) {
                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atrDefectosRemovidosEtapaSump = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(17));
                atrDefectosRemovidosEtapaSump2 = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(10));
                resultadoPlan = Helper.extraerNumero(atrDefectosRemovidosEtapaSump.getSubAtributo("Plan").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrDefectosRemovidosEtapaSump2.getSubAtributo("Plan").getValor()) / resultadoPlan;
                resultadoReal = Helper.extraerNumero(atrDefectosRemovidosEtapaSump.getSubAtributo("Real").getValor());

                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrDefectosRemovidosEtapaSump2.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Revision diseño detallado/Diseño detallado", atributos3));

                atributos3 = new ArrayList();

                atrDefectosRemovidosEtapaSump = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(15));
                atrDefectosRemovidosEtapaSump2 = atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(14));

                resultadoPlan = Helper.extraerNumero(atrDefectosRemovidosEtapaSump.getSubAtributo("Plan").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrDefectosRemovidosEtapaSump2.getSubAtributo("Plan").getValor()) / resultadoPlan;

                resultadoReal = Helper.extraerNumero(atrDefectosRemovidosEtapaSump.getSubAtributo("Real").getValor());

                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrDefectosRemovidosEtapaSump2.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Revisión de código/Compilación", atributos3));

                atributos.add(new AtributoCompuesto("Proporciones de defecto", atributos2));
            } else {
                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Revision diseño detallado/Diseño detallado", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Revisión de código/Compilación", atributos3));

                atributos.add(new AtributoCompuesto("Proporciones de defecto", atributos2));
            }
            //5. Proporciones de tiempo de desarrollo (SUMQ)
            if (atrTiempoFaseSump != null) {

                atrNumerador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(5));
                atrDenominador = atrTiempoFaseSump.getSubAtributo(((String)TSP.etapas.get(3)));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Inspección de requerimientos/requerimientos", atributos3));

                atrNumerador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(8));
                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(6));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Inspección de diseño de alto nivel/Diseño de alto nivel", atributos3));

                atrNumerador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(9));
                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(13));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Diseño detallado/Código", atributos3));

                atrNumerador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(10));
                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(9));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Revisión diseño detallado (DLD)/Diseño detallado", atributos3));

                atrNumerador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(14));
                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(13));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Revisión de código/código", atributos3));

                atributos.add(new AtributoCompuesto("Proporciones de tiempo de desarrollo", atributos2));

            } else {
                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Inspección de requerimientos/requerimientos", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Inspección de diseño de alto nivel/Diseño de alto nivel", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Diseño detallado/Código", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Revisión diseño detallado (DLD)/Diseño detallado", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Revisión de código/código", atributos3));

                atributos.add(new AtributoCompuesto("Proporciones de tiempo de desarrollo", atributos2));
            }
            //6. A/FR (SUMQ)
            if (atrTiempoFaseSump != null) {
                atributos2 = new ArrayList();

                AtributoCompuesto atr2RevisionDisenioDetallado = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(10));
                AtributoCompuesto atr2InspeccionDisenioDetallado = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(12));
                AtributoCompuesto atr2RevisionCodigo = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(14));
                AtributoCompuesto atr2InspeccionCodigo = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(16));

                AtributoCompuesto atr2Compilacion = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(15));
                AtributoCompuesto atr2PruebaUnidad = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(17));

                resultadoPlan = Helper.extraerNumero(atr2Compilacion.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atr2PruebaUnidad.getSubAtributo("Plan").getValor());

                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atr2RevisionDisenioDetallado.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atr2InspeccionDisenioDetallado.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atr2RevisionCodigo.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atr2InspeccionCodigo.getSubAtributo("Plan").getValor()) / resultadoPlan;

                resultadoReal = Helper.extraerNumero(atr2Compilacion.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atr2PruebaUnidad.getSubAtributo("Real").getValor());

                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atr2RevisionDisenioDetallado.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atr2InspeccionDisenioDetallado.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atr2RevisionCodigo.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atr2InspeccionCodigo.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos2.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos2.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));

                atributos.add(new AtributoCompuesto("A/FR", atributos2));
            } else {
                atributos2 = new ArrayList();
                atributos2.add(new AtributoCompuesto("Plan", "0", null));
                atributos2.add(new AtributoCompuesto("Real", "0", null));
                atributos.add(new AtributoCompuesto("A/FR", atributos2));
            }
            //7. Rendimientos de fase (SUMQ)
            if (atrDefectosRemovidosSump != null && atrDefectosInyectadosSump != null) {
                atributos2 = new ArrayList();
                double sumDefectosInyectadosSump = 0;
                double sumDefectosRemovidosSump = 0;
                resultadoReal = Helper.extraerNumero(atrDefectosInyectadosSump.getSubAtributo((String)TSP.etapas.get(2)).getSubAtributo("Real").getValor());
                sumDefectosInyectadosSump += resultadoReal;
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrDefectosRemovidosSump.getSubAtributo((String)TSP.etapas.get(2)).getSubAtributo("Real").getValor()) / resultadoReal;
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(2), atributos3));

                for (int i = 3; i < t - 2; i++) {
                    etapa = (String) TSP.etapas.get(i);
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    sumDefectosInyectadosSump += Helper.extraerNumero(atrDefectosInyectadosSump.getSubAtributo(etapa).getSubAtributo("Real").getValor());
                    sumDefectosRemovidosSump += Helper.extraerNumero(atrDefectosRemovidosSump.getSubAtributo((String) TSP.etapas.get(i - 1)).getSubAtributo("Real").getValor());
                    if (sumDefectosInyectadosSump - sumDefectosRemovidosSump != 0)
                        atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrDefectosRemovidosSump.getSubAtributo((String) TSP.etapas.get(i - 1)).getSubAtributo("Real").getValor()) / (sumDefectosInyectadosSump - sumDefectosRemovidosSump)), null));
                    else atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos2.add(new AtributoCompuesto(etapa, atributos3));
                }
                ((AtributoCompuesto)atributos2.get(3)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(6)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(8)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(10)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(12)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(13)).getSubAtributo("Plan").setValor("50");
                ((AtributoCompuesto)atributos2.get(14)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(15)).getSubAtributo("Plan").setValor("90");
                ((AtributoCompuesto)atributos2.get(16)).getSubAtributo("Plan").setValor("80");
                ((AtributoCompuesto)atributos2.get(17)).getSubAtributo("Plan").setValor("80");
                
                
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "65", null));
                //+=? o =
                sumDefectosInyectadosSump = Helper.extraerNumero(atrDefectosInyectadosSump.getSubAtributo("Total").getSubAtributo("Real").getValor());

                if (sumDefectosInyectadosSump - sumDefectosRemovidosSump != 0) atributos3.add(new AtributoCompuesto("Real", Double.toString(Helper.extraerNumero(atrDefectosRemovidosSump.getSubAtributo("Prueba de aceptación").getSubAtributo("Real").getValor()) / (sumDefectosInyectadosSump - sumDefectosRemovidosSump)), null));                
                else atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Prueba de aceptación", atributos3));

                atributos.add(new AtributoCompuesto("Rendimientos de fase", atributos2));
            } else {
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(i), atributos3));
                }
                
                ((AtributoCompuesto)atributos2.get(3)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(6)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(8)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(10)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(12)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(13)).getSubAtributo("Plan").setValor("50");
                ((AtributoCompuesto)atributos2.get(14)).getSubAtributo("Plan").setValor("70");
                ((AtributoCompuesto)atributos2.get(15)).getSubAtributo("Plan").setValor("90");
                ((AtributoCompuesto)atributos2.get(16)).getSubAtributo("Plan").setValor("80");
                ((AtributoCompuesto)atributos2.get(17)).getSubAtributo("Plan").setValor("80");

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Prueba de aceptación", atributos3));
            }

            AtributoCompuesto atrEtapaDefectosRemovidosSump;
            AtributoCompuesto atrEtapaDefectosInyectadosSump;
            //8. Rendimientos de proceso (SUMQ)
            if (atrDefectosInyectadosSump != null && atrDefectosRemovidosSump != null) {

                atributos2 = new ArrayList();

                resultadoPlan = 0;
                resultadoReal = 0;
                double resultadoPlan2 = 0;
                double resultadoReal2 = 0;
                double resultadoPlanAux;
                double resultadoRealAux;

                atributos3 = new ArrayList();
                for (int i = 2; i < t - 7; i++) {
                    etapa = (String) TSP.etapas.get(i);
                    atrEtapaDefectosInyectadosSump = atrDefectosInyectadosSump.getSubAtributo(etapa);
                    resultadoPlan += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Plan").getValor());
                    resultadoReal += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Real").getValor());
                    atrEtapaDefectosRemovidosSump = atrDefectosRemovidosSump.getSubAtributo(etapa);
                    resultadoPlan2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Plan").getValor());
                    resultadoReal2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Real").getValor());
                }

                resultadoPlanAux = resultadoPlan;
                if (resultadoPlan != 0) resultadoPlan = resultadoPlan2 / resultadoPlan;
                resultadoRealAux = resultadoReal;
                if (resultadoReal != 0) resultadoReal = resultadoReal2 / resultadoReal;

                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                resultadoPlan = resultadoPlanAux;
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                resultadoReal = resultadoRealAux;
                atributos2.add(new AtributoCompuesto("Antes de compilación", atributos3));

                atributos3 = new ArrayList();
                etapa = (String) TSP.etapas.get(t - 5);
                atrEtapaDefectosInyectadosSump = atrDefectosInyectadosSump.getSubAtributo(etapa);
                resultadoPlan += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Plan").getValor());
                resultadoReal += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Real").getValor());
                atrEtapaDefectosRemovidosSump = atrDefectosRemovidosSump.getSubAtributo(etapa);
                resultadoPlan2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Plan").getValor());
                resultadoReal2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Real").getValor());
                resultadoPlanAux = resultadoPlan;
                if (resultadoPlan != 0) resultadoPlan = resultadoPlan2 / resultadoPlan;
                resultadoRealAux = resultadoReal;
                if (resultadoReal != 0) resultadoReal = resultadoReal2 / resultadoReal;
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                resultadoPlan = resultadoPlanAux;
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                resultadoReal = resultadoRealAux;
                atributos2.add(new AtributoCompuesto("Antes de prueba de unidad", atributos3));

                atributos3 = new ArrayList();
                etapa = (String) TSP.etapas.get(t - 4);
                atrEtapaDefectosInyectadosSump = atrDefectosInyectadosSump.getSubAtributo(etapa);
                resultadoPlan += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Plan").getValor());
                resultadoReal += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Real").getValor());
                atrEtapaDefectosRemovidosSump = atrDefectosRemovidosSump.getSubAtributo(etapa);
                resultadoPlan2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Plan").getValor());
                resultadoReal2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Real").getValor());
                resultadoPlanAux = resultadoPlan;
                if (resultadoPlan != 0) resultadoPlan = resultadoPlan2 / resultadoPlan;
                resultadoRealAux = resultadoReal;
                if (resultadoReal != 0) resultadoReal = resultadoReal2 / resultadoReal;

                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                resultadoPlan = resultadoPlanAux;
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                resultadoReal = resultadoRealAux;
                atributos2.add(new AtributoCompuesto("Antes de prueba de construcción e integración", atributos3));

                atributos3 = new ArrayList();
                etapa = (String) TSP.etapas.get(t - 4);
                atrEtapaDefectosInyectadosSump = atrDefectosInyectadosSump.getSubAtributo(etapa);
                resultadoPlan += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Plan").getValor());
                resultadoReal += Helper.extraerNumero(atrEtapaDefectosInyectadosSump.getSubAtributo("Real").getValor());
                atrEtapaDefectosRemovidosSump = atrDefectosRemovidosSump.getSubAtributo(etapa);
                resultadoPlan2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Plan").getValor());
                resultadoReal2 += Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Real").getValor());
                resultadoPlanAux = resultadoPlan;
                if (resultadoPlan != 0) resultadoPlan = resultadoPlan2 / resultadoPlan;
                resultadoRealAux = resultadoReal;
                if (resultadoReal != 0) resultadoReal = resultadoReal2 / resultadoReal;

                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Antes de prueba de sistema", atributos3));

                atributos3 = new ArrayList();
                AtributoCompuesto atrTotalDefectosInyectadosSump = atrDefectosInyectadosSump.getSubAtributo("Total");
                AtributoCompuesto atrTotalDefectosRemovidosSump = atrDefectosRemovidosSump.getSubAtributo("Total");
                resultadoPlan = Helper.extraerNumero(atrTotalDefectosInyectadosSump.getSubAtributo("Plan").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrTotalDefectosRemovidosSump.getSubAtributo("Plan").getValor()) / resultadoPlan;
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                resultadoReal = Helper.extraerNumero(atrTotalDefectosInyectadosSump.getSubAtributo("Real").getValor());
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrTotalDefectosRemovidosSump.getSubAtributo("Real").getValor()) / resultadoReal;
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto("Antes de prueba de aceptación", atributos3));

                atributos.add(new AtributoCompuesto("Rendimientos de proceso", atributos2));
            } else {
                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Antes de compilación", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Antes de prueba de unidad", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Antes de prueba de construcción e integración", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Antes de prueba de sistema", atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto("Antes de prueba de aceptación", atributos3));

                atributos.add(new AtributoCompuesto("Rendimientos de proceso", atributos2));
            }
            //9. Tasa de inyección de defectos (SUMQ)
            if (atrTiempoFaseSump != null && atrDefectosInyectadosSump != null) {
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    etapa = (String) TSP.etapas.get(i);
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));

                    resultadoReal = Helper.extraerNumero(atrTiempoFaseSump.getSubAtributo(etapa).getSubAtributo("Real").getValor());
                    if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrDefectosInyectadosSump.getSubAtributo(etapa).getSubAtributo("Real").getValor()) / resultadoReal;

                    atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                    atributos2.add(new AtributoCompuesto(etapa, atributos3));
                }

                atributos.add(new AtributoCompuesto("Tasa de inyección de defectos", atributos2));
            } else {
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(i), atributos3));
                }
                atributos.add(new AtributoCompuesto("Tasa de inyección de defectos", atributos2));

            }
            //10. Tasa de remoción de defectos (SUMQ)
            if (atrTiempoFaseSump != null && atrDefectosRemovidosSump != null) {
                atributos2 = new ArrayList();

                AtributoCompuesto atrEtapaTiempoFaseSump;
                for (int i = 2; i < t - 2; i++) {
                    etapa = (String) TSP.etapas.get(i);
                    atrEtapaDefectosRemovidosSump = atrDefectosRemovidosSump.getSubAtributo(etapa);
                    atrEtapaTiempoFaseSump = atrTiempoFaseSump.getSubAtributo(etapa);
                    atributos3 = new ArrayList();
                    resultadoPlan = Helper.extraerNumero(atrEtapaTiempoFaseSump.getSubAtributo("Plan").getValor());
                    if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Plan").getValor()) / resultadoPlan;
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    resultadoReal = Helper.extraerNumero(atrEtapaTiempoFaseSump.getSubAtributo("Real").getValor());
                    if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrEtapaDefectosRemovidosSump.getSubAtributo("Real").getValor()) / resultadoReal;

                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                    atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                    atributos2.add(new AtributoCompuesto(etapa, atributos3));
                }
                atributos.add(new AtributoCompuesto("Tasa de remoción de defectos", atributos2));

            } else {
                atributos2 = new ArrayList();
                for (int i = 2; i < t - 2; i++) {
                    atributos3 = new ArrayList();
                    atributos3.add(new AtributoCompuesto("Plan", "0", null));
                    atributos3.add(new AtributoCompuesto("Real", "0", null));
                    atributos2.add(new AtributoCompuesto((String) TSP.etapas.get(i), atributos3));
                }
                atributos.add(new AtributoCompuesto("Tasa de remoción de defectos", atributos2));
            }
            //11. Inspección y proporciones de revisión
            if (atrTamanioProductoSump != null && atrTiempoFaseSump != null) {
                atributos2 = new ArrayList();
                atributos3 = new ArrayList();
                atrNumerador = atrTamanioProductoSump.getSubAtributo("Páginas de requerimientos (SRS)");
                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(5));
                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(5), atributos3));

                atrNumerador = atrTamanioProductoSump.getSubAtributo("Páginas de diseño de alto nivel (SDS)");
                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(8));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor()) / resultadoPlan;

                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(8), atributos3));

                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(10));
                atrNumerador = atrTamanioProductoSump.getSubAtributo("Total de líneas de diseño");
                AtributoCompuesto atrNumerador2 = atrTamanioProductoSump.getSubAtributo("Código total nuevo y reutilizado (N)");
                double numerador;

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());

                if (resultadoPlan != 0) {
                    numerador = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor());
                    if (numerador == 0) resultadoPlan = Helper.extraerNumero(atrNumerador2.getSubAtributo("Plan").getValor());
                    else resultadoPlan = numerador / resultadoPlan;
                }
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoReal != 0) {
                    numerador = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor());
                    if (numerador == 0) resultadoReal = Helper.extraerNumero(atrNumerador2.getSubAtributo("Real").getValor());
                    else resultadoReal = numerador / resultadoReal;
                }

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(10), atributos3));

                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(12));
                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());

                if (resultadoPlan != 0) {
                    numerador = Helper.extraerNumero(atrNumerador.getSubAtributo("Plan").getValor());
                    if (numerador == 0) resultadoPlan = Helper.extraerNumero(atrNumerador2.getSubAtributo("Plan").getValor());
                    else resultadoPlan = numerador / resultadoPlan;
                }

                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());

                if (resultadoReal != 0) {
                    numerador = Helper.extraerNumero(atrNumerador.getSubAtributo("Real").getValor());
                    if (numerador == 0) resultadoReal = Helper.extraerNumero(atrNumerador2.getSubAtributo("Real").getValor());
                    else resultadoReal = numerador / resultadoReal;
                }

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(12), atributos3));

                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(14));
                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador2.getSubAtributo("Plan").getValor()) / resultadoPlan;                
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador2.getSubAtributo("Real").getValor()) / resultadoReal;

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(14), atributos3));

                atrDenominador = atrTiempoFaseSump.getSubAtributo((String)TSP.etapas.get(16));

                resultadoPlan = Helper.extraerNumero(atrDenominador.getSubAtributo("Plan").getValor());
                if (resultadoPlan != 0) resultadoPlan = Helper.extraerNumero(atrNumerador2.getSubAtributo("Plan").getValor()) / resultadoPlan;
                resultadoReal = Helper.extraerNumero(atrDenominador.getSubAtributo("Real").getValor());
                if (resultadoReal != 0) resultadoReal = Helper.extraerNumero(atrNumerador2.getSubAtributo("Real").getValor()) / resultadoReal;
                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", Double.toString(resultadoPlan), null));
                atributos3.add(new AtributoCompuesto("Real", Double.toString(resultadoReal), null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(16), atributos3));

                atributos.add(new AtributoCompuesto("Inspección/Proporciones de revisión", atributos2));
            } else {
                atributos2 = new ArrayList();

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(5), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(8), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(10), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(12), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(14), atributos3));

                atributos3 = new ArrayList();
                atributos3.add(new AtributoCompuesto("Plan", "0", null));
                atributos3.add(new AtributoCompuesto("Real", "0", null));
                atributos2.add(new AtributoCompuesto((String)TSP.etapas.get(16), atributos3));

                atributos.add(new AtributoCompuesto("Inspección/Proporciones de revisión", atributos2));
            }
        }catch(Exception e){
            throw new ExceptionFatal("Error al crear crear los atributos sumq " + e.getMessage());
        }
    }
    
    private Object getControlador(String clase) throws ExceptionFatal {
        try {
            clase = "b_controlador.a_gestion." + clase;
            FacesContext c = FacesContext.getCurrentInstance();
            String[] items = Helper.dividir(clase, ".");
            return c.getApplication().evaluateExpressionGet(c, "#{" + Helper.primeraLetraMinus(items[items.length - 1]) + "}", Class.forName(clase));
        } catch (Exception e) {
            throw new ExceptionFatal("Error fatal al tratar de localizar " + clase + ". " + e.getMessage());
        }
    }
}