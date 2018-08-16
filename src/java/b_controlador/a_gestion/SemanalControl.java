/**
 * Controlador para los dos formatos a registrar semanalmente: SCHEDULE y WEEK
 */
package b_controlador.a_gestion;

import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.Helper;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import b_controlador.b_fachada.FormatoFachada;
import c_negocio.a_relacional.Criterio;
import e_utilitaria.TSP;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class SemanalControl extends Control {

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private FormatoConcreto schedule;
    private FormatoConcreto week;
    private List registrosSchedule;
    private int diaReferencia;
    private final int MIN_DIAS_SEMANA = 4;
    private int numSemanaUltimoFormatoWeek;

    public SemanalControl() {
    }

    /**
     * carga a memoria el día de referencia para el control semanal
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void cargarInformacionSemanal() throws ExceptionFatal, ExceptionWarn {
        try{
            this.diaReferencia = Helper.diaSemana(((ProyectoControl) getControlador("ProyectoControl")).valorDeCriterio(((Criterio)TSP.criterios.get(0)).getNombre()));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede cargar la información semanal. " + e.getMessage());
        }
    }

    /**
     * Carga el formato Schedule
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarSchedule() throws ExceptionFatal, ExceptionWarn {
        try{
            this.cargarInformacionSemanal();
            this.schedule = formatoFachada.cargarFormato("schedule");
            this.registrosSchedule = this.schedule.getAtributos();
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede cargar el formato schedule. " + e.getMessage());
        }
    }

    /**
     * indica si la semana enviada ya está registrada en el schedule
     * @param numSemana
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean semanaScheduleRegistrada(int numSemana) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = this.schedule.getAtributos();
            int t = atributos.size();
            for (int i = t - 1; i >= 0; i--)
                if (Integer.parseInt(((AtributoCompuesto) atributos.get(i)).getSubAtributo("N. Semana").getValor()) == numSemana)
                    return true;
            return false;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede validar el registro schedule de la semana " + Integer.toString(numSemana) + ". " + e.getMessage());
        }
    }

    /**
     * actualiza el formato schedule
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String actualizarSchedule() throws ExceptionFatal, ExceptionWarn {
        try{
            Date fechaHoy = new Date();
            int numSemanaTSP;
            int numUltSemRegistrada;
            if (esDiaReferencia(fechaHoy)) {
                numSemanaTSP = numSemanaTSP(fechaHoy);
                if (!semanaScheduleRegistrada(numSemanaTSP)) {
                    if (numSemanaTSP == 0) throw new ExceptionWarn("No han transcurrido el mínimo (" + Integer.toString(this.MIN_DIAS_SEMANA) + ") número de días suficientes para registrar la semana");
                    else {
                        numUltSemRegistrada = numUltimaSemanaRegistradaSchedule(this.registrosSchedule);
                        for (int i = numUltSemRegistrada + 1; i <= numSemanaTSP; i++) registrarSemanaActual(i);
                        if (numUltSemRegistrada + 1 == numSemanaTSP) return "Registro Schedule de esta semana (semana " + Integer.toString(numSemanaTSP) + " creado satisfactoriamente)";
                        else return "Registros Schedule de las semanas comprendidas entre " + Integer.toString(numUltSemRegistrada + 1) + " y " + Integer.toString(numSemanaTSP) + " creados satisfactoriamente)";
                    }
                } else throw new ExceptionWarn("La semana " + Integer.toString(numSemanaTSP) + " ya se encuentra registrada");

            } else throw new ExceptionWarn("Sólo puedes actualizar el formato schedule el día " + Helper.diaSemana(this.diaReferencia));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede actualizar el formato schedule. " + e.getMessage());
        }
    }

    /**
     * Calcula el número de semana TSP
     * @param fecha
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int numSemanaTSP(Date fecha) throws ExceptionFatal, ExceptionWarn {
        try{
            int numSemana;
            Date fechaCreacionCiclo = ((ProyectoControl) getControlador("ProyectoControl")).getCicloSesion().getFInicio();
            numSemana = Helper.semanasEntreFechas(fechaCreacionCiclo, fecha);
            int diferencia = fechaCreacionCiclo.getDay() - this.diaReferencia;
            if (Math.abs(diferencia) >= this.MIN_DIAS_SEMANA || (diferencia > 0 && diferencia <= 7 - this.MIN_DIAS_SEMANA))
                return numSemana + 1;
            return numSemana;
        }catch(Exception e){
            throw new ExceptionFatal("Error al calcular el número de semana TSP. " + e.getMessage());
        }
    }

    /**
     * Indica si el día de referencia es el mismo día de "hoy"
     * @param fecha
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean esDiaReferencia(Date fecha) throws ExceptionFatal, ExceptionWarn {
        try{
            return this.diaReferencia == fecha.getDay();
        }catch(Exception e){
            throw new ExceptionFatal("Error al validar si el día de hoy está permitido registrar el formato. " + e.getMessage());
        }
    }

    /**
     * Registra la semana actual para el formato schedule
     * @param numSemana
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void registrarSemanaActual(int numSemana) throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atributo = fabricaAtributos.crearAtributosSchedule(numSemana);
            AtributoCompuesto atr;
            List atributos;

            AtributoCompuesto atributo2Plan = atributo.getSubAtributo("Plan");
            AtributoCompuesto atributo2Real = atributo.getSubAtributo("Real");
            AtributoCompuesto atributo3HorasDirectas = atributo2Plan.getSubAtributo("Horas directas");
            AtributoCompuesto atributo3HorasDeEquipo = atributo2Real.getSubAtributo("Horas de equipo");
            AtributoCompuesto atributo3VPAcumulado = atributo2Plan.getSubAtributo("VP Acumulado");
            AtributoCompuesto atributo3VGSemanal = atributo2Real.getSubAtributo("VG Semanal");

            ((PlaneacionControl) getControlador("PlaneacionControl")).horasPorSemana(numSemana, atributo3HorasDirectas, atributo3HorasDeEquipo, atributo3VPAcumulado, atributo3VGSemanal);
            if (numSemana == 1) {
                atributo2Plan.getSubAtributo("Horas acumuladas").setValor(atributo3HorasDirectas.getValor());
                atributo2Real.getSubAtributo("Horas acumuladas").setValor(atributo3HorasDeEquipo.getValor());
                atributo2Real.getSubAtributo("VG Acumulado").setValor(atributo3VGSemanal.getValor());
            } else {
                atributos = this.schedule.getAtributos();
                atr = (AtributoCompuesto) atributos.get(atributos.size() - 1);
                atributo2Plan.getSubAtributo("Horas acumuladas").setValor(Double.toString(Helper.extraerNumero(atr.getSubAtributo("Plan").getSubAtributo("Horas acumuladas").getValor()) + Helper.extraerNumero(atributo3HorasDirectas.getValor())));
                atributo2Real.getSubAtributo("Horas acumuladas").setValor(Double.toString(Helper.extraerNumero(atr.getSubAtributo("Real").getSubAtributo("Horas acumuladas").getValor()) + Helper.extraerNumero(atributo3HorasDeEquipo.getValor())));
                atributo2Real.getSubAtributo("VG Acumulado").setValor(Double.toString(Helper.extraerNumero(atr.getSubAtributo("Real").getSubAtributo("VG Acumulado").getValor()) + Helper.extraerNumero(atributo3VGSemanal.getValor())));
            }
            this.registrosSchedule.add(atributo);
            formatoFachada.actualizarFormato(this.schedule);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede registrar la semana actual. " + e.getMessage());
        }
    }

    /**
     * Carga el formato week actual
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarWeek() throws ExceptionFatal, ExceptionWarn {
        try{
            this.cargarInformacionSemanal();
            Date fechaHoy = new Date();
            int numSemanaTSP = numSemanaTSP(fechaHoy);
            ProyectoControl pb = ((ProyectoControl) getControlador("ProyectoControl"));
            Proyecto proyecto = pb.getProyectoSesion();
            this.numSemanaUltimoFormatoWeek = consultarUltimoFormatoWeek(proyecto, numSemanaTSP);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede cargar el formato Week. " + e.getMessage());
        }
    }

    /**
     * Consulta el último formato Week registrado en el sistema
     * @param proyecto
     * @param numSemanaTSP
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int consultarUltimoFormatoWeek(Proyecto proyecto, int numSemanaTSP) throws ExceptionFatal, ExceptionWarn {
        try{
            int numSemanaFormato = numSemanaTSP + 1;
            do {
                this.week = formatoFachada.cargarFormato("week","semana",Integer.toString(--numSemanaFormato));
            } while (this.week.getAtributos().isEmpty() && numSemanaFormato > 1);
            if (this.week.getAtributos().isEmpty() && numSemanaFormato == 1) return 0;
            return numSemanaFormato;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede consultar el último formato week generado. " + e.getMessage());
        }
    }

    /**
     * Indica si existe el formato week
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean isExisteWeek() throws ExceptionFatal, ExceptionWarn {
        try{
            return !this.week.getAtributos().isEmpty();
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl ni puede validar la existencia del formato week. " + e.getMessage());
        }
    }

    /**
     * Genere el week de la semana actual
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public String generarWeekSemanaActual() throws ExceptionFatal, ExceptionWarn {
        try{
            Date fechaHoy = new Date();
            int numSemanaTSP;
            if (esDiaReferencia(fechaHoy)) {
                numSemanaTSP = numSemanaTSP(fechaHoy);
                if (numSemanaTSP == 0)
                    throw new ExceptionWarn("No han transcurrido el mínimo (" + Integer.toString(this.MIN_DIAS_SEMANA) + ") número de días suficientes para registrar la semana");
                else {
                    if (this.numSemanaUltimoFormatoWeek < numSemanaTSP) {
                        cargarSchedule();
                        if (semanaScheduleRegistrada(numSemanaTSP)) {
                            this.week = formatoFachada.cargarFormato("week","semana",Integer.toString(numSemanaTSP));
                            fabricaAtributos.crearAtributosWeek(this,this.week,fechaHoy, numSemanaTSP);
                            formatoFachada.actualizarFormato(this.week);
                            this.numSemanaUltimoFormatoWeek = numSemanaTSP;
                            return Integer.toString(numSemanaTSP);
                        } else throw new ExceptionWarn("Necesita actualizar el formato SCHEDULE para generar el formato WEEK");
                    } else throw new ExceptionWarn("No es posible generar el formato WEEK para esta semana (semana " + Integer.toString(numSemanaTSP) + "). El último formato generado corresponde a la semana " + Integer.toString(this.numSemanaUltimoFormatoWeek));
                }
            } else throw new ExceptionWarn("Sólo puedes generar el formato WEEK el día " + Helper.diaSemana(this.diaReferencia));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede generar el week de la semana actual. " + e.getMessage());
        }
    }

    /**
     * Entrega los datos de resumen de la semana al formato schedule
     * @param numSemana
     * @param atributo2HorasProyectoSemana
     * @param atributo2HorasProyectoCiclo
     * @param atributo2VGSemana
     * @param atributo2VGCiclo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void consolidarScheduleSemana(int numSemana, AtributoCompuesto atributo2HorasProyectoSemana, AtributoCompuesto atributo2HorasProyectoCiclo, AtributoCompuesto atributo2VGSemana, AtributoCompuesto atributo2VGCiclo) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = this.schedule.getAtributos();
            int t = atributos.size();
            AtributoCompuesto atributo;
            AtributoCompuesto atributo2;
            for (int i = t - 1; i >= 0; i--) {
                atributo = (AtributoCompuesto) atributos.get(i);
                if (Integer.parseInt(atributo.getSubAtributo("N. Semana").getValor()) == numSemana) {
                    atributo2 = atributo.getSubAtributo("Plan");
                    atributo2HorasProyectoSemana.getSubAtributo("Plan").setValor(atributo2.getSubAtributo("Horas directas").getValor());
                    atributo2HorasProyectoCiclo.getSubAtributo("Plan").setValor(atributo2.getSubAtributo("Horas acumuladas").getValor());
                    if (i - 1 >= 0)
                        atributo2VGSemana.getSubAtributo("Plan").setValor(Double.toString(Double.parseDouble(atributo2.getSubAtributo("VP Acumulado").getValor()) - Double.parseDouble(((AtributoCompuesto) atributos.get(i - 1)).getSubAtributo("Plan").getSubAtributo("VP Acumulado").getValor())));
                    else
                        atributo2VGSemana.getSubAtributo("Plan").setValor(atributo2.getSubAtributo("VP Acumulado").getValor());
                    atributo2VGCiclo.getSubAtributo("Plan").setValor(atributo2.getSubAtributo("VP Acumulado").getValor());
                    atributo2 = atributo.getSubAtributo("Real");
                    atributo2HorasProyectoSemana.getSubAtributo("Real").setValor(atributo2.getSubAtributo("Horas de equipo").getValor());
                    atributo2HorasProyectoCiclo.getSubAtributo("Real").setValor(atributo2.getSubAtributo("Horas acumuladas").getValor());
                    atributo2VGSemana.getSubAtributo("Real").setValor(atributo2.getSubAtributo("VG Semanal").getValor());
                    atributo2VGCiclo.getSubAtributo("Real").setValor(atributo2.getSubAtributo("VG Acumulado").getValor());
                    break;
                }
            }
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede consolidar la información para el formato schedule de esta semana. " + e.getMessage());
        }
    }

    /**
     * Indica el número de la última semana registrada en el formato schedule
     * @param registrosSchedule
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int numUltimaSemanaRegistradaSchedule(List registrosSchedule) throws ExceptionFatal, ExceptionWarn {
        try{
            int max = 0;
            int t = registrosSchedule.size();
            int semana;
            for (int i = 0; i < t; i++) {
                semana = (int) Helper.extraerNumero(((AtributoCompuesto) registrosSchedule.get(i)).getSubAtributo("N. Semana").getValor());
                if (max < semana) max = semana;
            }
            return max;
        }catch(Exception e){
            throw new ExceptionFatal("SemanalControl no puede determinarel número de semana del último registro schedule. " + e.getMessage());
        }
    }

    public FormatoConcreto getWeek() {
        return week;
    }

    public void setWeek(FormatoConcreto week) {
        this.week = week;
    }


    public int getNumSemanaUltimoFormatoWeek() {
        return numSemanaUltimoFormatoWeek;
    }

    public void setNumSemanaUltimoFormatoWeek(int numSemanaUltimoFormatoWeek) {
        this.numSemanaUltimoFormatoWeek = numSemanaUltimoFormatoWeek;
    }
    
    public FormatoConcreto getSchedule() {
        return schedule;
    }

    public void setSchedule(FormatoConcreto schedule) {
        this.schedule = schedule;
    }

    public List getRegistrosSchedule() {
        return registrosSchedule;
    }

    public void setRegistrosSchedule(List registrosSchedule) {
        this.registrosSchedule = registrosSchedule;
    }

    public int getDiaReferencia() {
        return diaReferencia;
    }

    public void setDiaReferencia(int diaReferencia) {
        this.diaReferencia = diaReferencia;
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