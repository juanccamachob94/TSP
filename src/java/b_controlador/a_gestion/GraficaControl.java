package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import b_controlador.c_fabricas.d_fabrica_graficas.FabricaGraficas;
import c_negocio.a_relacional.Meta;
import c_negocio.a_relacional.Proyecto;
import c_negocio.a_relacional.RlCl;
import c_negocio.a_relacional.RlClId;
import c_negocio.a_relacional.Rol;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import d_datos.a_dao.ProyectoDAO;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.ChartModel;

@ManagedBean
@SessionScoped
public class GraficaControl extends Control {
    
    private ChartModel grafica;
    private String tipoGrafica;
    private String widthGrafica;
    
    private ChartModel graficaTotal;
    private String tipoGraficaTotal;
    
    private ChartModel graficaPersonal;
    private String tipoGraficaPersonal;
    private String widthGraficaPersonal;
    private List<AtributoCompuesto> informacionGraficaPersonal;
    
    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    
    public GraficaControl() {
    }
    
    public void reiniciarValores() throws ExceptionFatal, ExceptionWarn {
        try{
            this.grafica = null;
            this.graficaTotal = null;
            this.tipoGrafica = null;
            this.tipoGraficaTotal = null;
            this.widthGrafica = null;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void reiniciarValoresPersonales() throws ExceptionFatal, ExceptionWarn {
        try{
            this.graficaPersonal = null;
            this.tipoGraficaPersonal = null;
            this.widthGraficaPersonal = null;
            this.informacionGraficaPersonal = null;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    
    public void crearGraficaTiempoEtapa(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaSump = formatoFachada.consultarFormatos("sump");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMP del cual extraer los datos");
            List<AtributoCompuesto> atrsTiempoFase = ((FormatoConcreto)listaSump.get(0)).getAtributo("Tiempo en fase (horas)").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsTiempoFase.size();
            for(int i = 0; i < t - 1; i++){
                atrTiempoFase = atrsTiempoFase.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Tiempo en etapas");
            extras.add(pos);
            this.widthGrafica = "4500px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            datos = new ArrayList();
            planes = new ArrayList();
            reales = new ArrayList();
            extras = new ArrayList();
            atrTiempoFase = atrsTiempoFase.get(t - 1);
            planes.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan total",null,planes));
            datos.add(new AtributoCompuesto("Real total",null,reales));
            extras.add("Total - Tiempo en etapas");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no puede construir la gráfica de tiempo por etapas. " + e.getMessage());
        }
    }

    public void graficaTamanioProducto(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaSump = formatoFachada.consultarFormatos("sump");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMP del cual extraer los datos");
            List<AtributoCompuesto> atrsTamanioProducto = ((FormatoConcreto)listaSump.get(0)).getAtributo("Tamaño del producto").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsTamanioProducto.size();
            for(int i = 0; i < t - 2; i++){
                atrTiempoFase = atrsTamanioProducto.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            atrTiempoFase = atrsTamanioProducto.get(t - 1);
            etapa = atrTiempoFase.getAtributo();
            planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Tamaño del producto");
            extras.add(pos);
            this.widthGrafica = "2700px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            datos = new ArrayList();
            planes = new ArrayList();
            reales = new ArrayList();
            extras = new ArrayList();
            atrTiempoFase = atrsTamanioProducto.get(t - 2);
            planes.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan total",null,planes));
            datos.add(new AtributoCompuesto("Real total",null,reales));
            extras.add("Total - Tamaño producto");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarDefectosInyectados(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaSump = formatoFachada.consultarFormatos("sump");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMP del cual extraer los datos");
            List<AtributoCompuesto> atrsDefectosInyectados = ((FormatoConcreto)listaSump.get(0)).getAtributo("Defectos inyectados").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsDefectosInyectados.size();
            for(int i = 0; i < t - 1; i++){
                atrTiempoFase = atrsDefectosInyectados.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Defectos inyectados");
            extras.add(pos);
            this.widthGrafica = "4500px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            datos = new ArrayList();
            planes = new ArrayList();
            reales = new ArrayList();
            extras = new ArrayList();
            atrTiempoFase = atrsDefectosInyectados.get(t - 1);
            planes.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan total",null,planes));
            datos.add(new AtributoCompuesto("Real total",null,reales));
            extras.add("Total - Defectos inyectados");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarDefectosRemovidos(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaSump = formatoFachada.consultarFormatos("sump");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMP del cual extraer los datos");
            List<AtributoCompuesto> atrsDefectosRemovidos = ((FormatoConcreto)listaSump.get(0)).getAtributo("Defectos removidos").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsDefectosRemovidos.size();
            for(int i = 0; i < t - 3; i++){
                atrTiempoFase = atrsDefectosRemovidos.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            atrTiempoFase = atrsDefectosRemovidos.get(t - 2);
            etapa = atrTiempoFase.getAtributo();
            planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            atrTiempoFase = atrsDefectosRemovidos.get(t - 1);
            etapa = atrTiempoFase.getAtributo();
            planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Defectos removidos");
            extras.add(pos);
            this.widthGrafica = "4500px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            datos = new ArrayList();
            planes = new ArrayList();
            reales = new ArrayList();
            extras = new ArrayList();
            atrTiempoFase = atrsDefectosRemovidos.get(t - 3);
            planes.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan total",null,planes));
            datos.add(new AtributoCompuesto("Real total",null,reales));
            extras.add("Total - Defectos removidos");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarPDFCompilacion(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "pie";
            List listaSumq = formatoFachada.consultarFormatos("sumq");
            if(listaSumq.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            AtributoCompuesto atrCompilacion = ((FormatoConcreto)listaSumq.get(0)).getAtributo("Porcentaje libre de defectos").getSubAtributo("En compilación").getSubAtributo("Real");
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("% Libre de defectos",atrCompilacion.getValor(),null));
            datos.add(new AtributoCompuesto("% Con defectos",Double.toString(100 - Helper.extraerNumero(atrCompilacion.getValor())),null));
            List extras = new ArrayList();
            extras.add("% Defectos en compilación");
            extras.add(pos);
            this.widthGrafica = "1000px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
            this.tipoGraficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarPDFPruebaUnidad(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "pie";
            List listaSumq = formatoFachada.consultarFormatos("sumq");
            if(listaSumq.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            AtributoCompuesto atrCompilacion = ((FormatoConcreto)listaSumq.get(0)).getAtributo("Porcentaje libre de defectos").getSubAtributo("En prueba de unidad").getSubAtributo("Real");
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("% Libre de defectos",atrCompilacion.getValor(),null));
            datos.add(new AtributoCompuesto("% Con defectos",Double.toString(100 - Helper.extraerNumero(atrCompilacion.getValor())),null));
            List extras = new ArrayList();
            extras.add("% Defectos en compilación");
            extras.add(pos);
            this.widthGrafica = "1000px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
            this.tipoGraficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarPDFConstIntegracion(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "pie";
            List listaSumq = formatoFachada.consultarFormatos("sumq");
            if(listaSumq.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            AtributoCompuesto atrCompilacion = ((FormatoConcreto)listaSumq.get(0)).getAtributo("Porcentaje libre de defectos").getSubAtributo("En construcción e integración").getSubAtributo("Real");
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("% Libre de defectos",atrCompilacion.getValor(),null));
            datos.add(new AtributoCompuesto("% Con defectos",Double.toString(100 - Helper.extraerNumero(atrCompilacion.getValor())),null));
            List extras = new ArrayList();
            extras.add("% Defectos en compilación");
            extras.add(pos);
            this.widthGrafica = "1000px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
            this.tipoGraficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarPDFPruebaSistema(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "pie";
            List listaSumq = formatoFachada.consultarFormatos("sumq");
            if(listaSumq.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            AtributoCompuesto atrCompilacion = ((FormatoConcreto)listaSumq.get(0)).getAtributo("Porcentaje libre de defectos").getSubAtributo("En prueba de sistema").getSubAtributo("Real");
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("% Libre de defectos",atrCompilacion.getValor(),null));
            datos.add(new AtributoCompuesto("% Con defectos",Double.toString(100 - Helper.extraerNumero(atrCompilacion.getValor())),null));
            List extras = new ArrayList();
            extras.add("% Defectos en compilación");
            extras.add(pos);
            this.widthGrafica = "1000px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
            this.tipoGraficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarTiempoArregloDefectos(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            long widthPorDefecto = 250;
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaLogd = formatoFachada.consultarFormatos("logd");
            int tLogd = listaLogd.size();
            List defectos = ProyectoDAO.consultarOpcionesTSP("defecto");
            int tDefectos = defectos.size();
            double tiempos[] = new double[tDefectos];
            double tiempoTotalArreglo = 0;
            String tipo;
            AtributoCompuesto atr;
            List<AtributoCompuesto> atrsFormato;
            int tFormato;
            for(int i = 0;i < tLogd; i++){
                atrsFormato = ((FormatoConcreto)listaLogd.get(i)).getAtributo("contenido iterable").getAtributos();
                tFormato = atrsFormato.size();
                for(int k = 0; k < tFormato; k++){
                    atr = (AtributoCompuesto) atrsFormato.get(k);
                    tipo = atr.getSubAtributo("tipo").getValor();
                    for(int j = 0; j < tDefectos; j++){
                        if(tipo.equals((String)defectos.get(j))){
                            tiempos[j] += Helper.extraerNumero(atr.getSubAtributo("tiempo arreglo").getValor());
                            break;
                        }
                    }
                }
            }
            List<AtributoCompuesto> atrsDefectos = new ArrayList();
            for(int i = 0; i < tDefectos; i++){
                widthPorDefecto += 250;
                tiempoTotalArreglo += tiempos[i];
                atrsDefectos.add(new AtributoCompuesto((String)defectos.get(i),Double.toString(tiempos[i]),null));
            }
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("Tiempo por defecto",null,atrsDefectos));
            List extras = new ArrayList();
            extras.add("Defectos inyectados");
            extras.add(pos);
            this.widthGrafica = Long.toString(widthPorDefecto) + "px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            atrsDefectos = new ArrayList();
            datos = new ArrayList();
            atrsDefectos.add(new AtributoCompuesto("Tiempo de arreglo",Double.toString(tiempoTotalArreglo),null));
            datos.add(new AtributoCompuesto("Total tiempo de arreglo",null,atrsDefectos));
            extras = new ArrayList();
            extras.add("Total tiempo de arreglo");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarVPVG(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            long widthPorSemana = 250;
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaSchedule = formatoFachada.consultarFormatos("schedule");
            if(listaSchedule.isEmpty()) throw new ExceptionWarn("No existe el formato SCHEDULE del cual extraer los datos");
            List<AtributoCompuesto> atrsSchedule = ((FormatoConcreto)listaSchedule.get(0)).getAtributos();
            int tAtrsSchedule  = atrsSchedule.size();
            List<AtributoCompuesto> valoresPlaneados = new ArrayList();
            List<AtributoCompuesto> valoresGanados = new ArrayList();
            AtributoCompuesto atr;
            String strNSemana;
            String strVPAcumAnterior;
            String valorGanado;
            
            atr = atrsSchedule.get(0);
            strNSemana = atr.getSubAtributo("N. Semana").getValor();
            strVPAcumAnterior = atr.getSubAtributo("Plan").getSubAtributo("VP Acumulado").getValor();
            valoresPlaneados.add(new AtributoCompuesto("Semana " + strNSemana,strVPAcumAnterior,null));
            valorGanado = atr.getSubAtributo("Real").getSubAtributo("VG Semanal").getValor();
            valoresGanados.add(new AtributoCompuesto("Semana " + strNSemana,valorGanado,null));
            double totalVP = Helper.extraerNumero(strVPAcumAnterior);
            double totalVG = Helper.extraerNumero(valorGanado);
            
            double vp;
            String strVPAcumActual;
            for(int i = 1; i < tAtrsSchedule; i++){
                atr = atrsSchedule.get(i);
                strNSemana = atr.getSubAtributo("N. Semana").getValor();
                strVPAcumActual = atr.getSubAtributo("Plan").getSubAtributo("VP Acumulado").getValor();
                vp = Helper.extraerNumero(strVPAcumActual) - Helper.extraerNumero(strVPAcumAnterior);
                valoresPlaneados.add(new AtributoCompuesto("Semana " + strNSemana,Double.toString(vp),null));
                valorGanado = atr.getSubAtributo("Real").getSubAtributo("VG Semanal").getValor();
                valoresGanados.add(new AtributoCompuesto("Semana " + strNSemana,valorGanado,null));
                widthPorSemana += 250;
                totalVP += vp;
                totalVG += Helper.extraerNumero(valorGanado);
                strVPAcumAnterior = strVPAcumActual;
            }
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("VG",null,valoresPlaneados));
            datos.add(new AtributoCompuesto("VP",null,valoresGanados));
            List extras = new ArrayList();
            extras.add("VG vs VP");
            extras.add(pos);
            this.widthGrafica = Long.toString(widthPorSemana) + "px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            valoresPlaneados = new ArrayList();
            valoresPlaneados.add(new AtributoCompuesto("Total VP",Double.toString(totalVP),null));
            valoresGanados = new ArrayList();
            valoresGanados.add(new AtributoCompuesto("Total VG",Double.toString(totalVG),null));
            datos = new ArrayList();
            datos.add(new AtributoCompuesto("Total",null,valoresPlaneados));
            datos.add(new AtributoCompuesto("Total",null,valoresGanados));
            extras = new ArrayList();
            extras.add("Total VG vs VP");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    
    public void graficarCumplimientoMetasCiclo(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            Proyecto proyectoSesion = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion();
            int tRolesTSP = TSP.rolesTSP.size();
            String nombreRol;
            List<Meta> metasRol;
            int numMetasLogradas;
            int numMetasNoLogradas;
            int tMetasRol;
            Boolean lograda;
            List<AtributoCompuesto> datos = new ArrayList();
            List<AtributoCompuesto> metasLogradas = new ArrayList();
            List<AtributoCompuesto> metasNoLogradas = new ArrayList();
            for(int i = 0; i < tRolesTSP; i++){
                nombreRol = ((Rol)TSP.rolesTSP.get(i)).getNombre();
                numMetasLogradas = 0;
                numMetasNoLogradas = 0;
                metasRol = ProyectoDAO.consultarMetasRolCicloProyecto(proyectoSesion,nombreRol);
                tMetasRol = metasRol.size();
                for(int j = 0; j < tMetasRol; j++){
                    lograda = metasRol.get(j).getLograda();
                    if(lograda == null) numMetasNoLogradas += 1;
                    else{
                        if(lograda) numMetasLogradas += 1;
                        else numMetasNoLogradas += 1;
                    }
                }
                metasLogradas.add(new AtributoCompuesto(nombreRol,Integer.toString(numMetasLogradas),null));
                metasNoLogradas.add(new AtributoCompuesto(nombreRol,Integer.toString(numMetasNoLogradas),null));
            }
            
            datos.add(new AtributoCompuesto("Metas logradas",null,metasLogradas));
            datos.add(new AtributoCompuesto("Metas no logradas",null,metasNoLogradas));
            List extras = new ArrayList();
            extras.add("Metas de rol");
            extras.add(pos);
            this.widthGrafica = "1250px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            datos = new ArrayList();
            metasLogradas = new ArrayList();
            metasNoLogradas = new ArrayList();
            extras = new ArrayList();
            
            List<Meta> metasProyecto = ProyectoDAO.consultarMetasCicloProyecto(proyectoSesion);
            int tMetasProyecto = metasProyecto.size();
            numMetasLogradas = 0;
            numMetasNoLogradas = 0;
            for(int i = 0; i < tMetasProyecto; i++){
                lograda = metasProyecto.get(i).getLograda();
                if(lograda == null) numMetasNoLogradas += 1;
                else{
                    if(lograda) numMetasLogradas += 1;
                    else numMetasNoLogradas += 1;
                }
            }
            metasLogradas.add(new AtributoCompuesto("Logro metas ciclo",Integer.toString(numMetasLogradas),null));
            metasNoLogradas.add(new AtributoCompuesto("Logro metas ciclo", Integer.toString(numMetasNoLogradas),null));
            datos.add(new AtributoCompuesto("Metas logradas",null,metasLogradas));
            datos.add(new AtributoCompuesto("Metas no logradas",null,metasNoLogradas));
            extras.add("Total - Logro de metas");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarDefectosCodigo(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = this.tipoGraficaTotal = "bar";
            List listaSump = formatoFachada.consultarFormatos("sumq");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            List<AtributoCompuesto> atrsTiempoFase = ((FormatoConcreto)listaSump.get(0)).getAtributo("Defectos/Código").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsTiempoFase.size();
            for(int i = 0; i < t - 1; i++){
                atrTiempoFase = atrsTiempoFase.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Defectos/Código");
            extras.add(pos);
            this.widthGrafica = "2750px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            datos = new ArrayList();
            planes = new ArrayList();
            reales = new ArrayList();
            extras = new ArrayList();
            atrTiempoFase = atrsTiempoFase.get(t - 1);
            planes.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Plan").getValor(),null));
            reales.add(new AtributoCompuesto("Total", atrTiempoFase.getSubAtributo("Real").getValor(),null));
            datos.add(new AtributoCompuesto("Plan total",null,planes));
            datos.add(new AtributoCompuesto("Real total",null,reales));
            extras.add("Total - Defectos/Código");
            extras.add(pos);
            this.graficaTotal = FabricaGraficas.construirGrafica(this.tipoGraficaTotal, datos, extras, null);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarRendimientosFase(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "bar";
            this.tipoGraficaTotal = null;
            List listaSump = formatoFachada.consultarFormatos("sumq");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            List<AtributoCompuesto> atrsTiempoFase = ((FormatoConcreto)listaSump.get(0)).getAtributo("Rendimientos de fase").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsTiempoFase.size();
            for(int i = 0; i < t - 1; i++){
                atrTiempoFase = atrsTiempoFase.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Rendimientos de fase");
            extras.add(pos);
            this.widthGrafica = "4750px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    
    public void graficarTasaInyeccionDefectos(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "bar";
            this.tipoGraficaTotal = null;
            List listaSump = formatoFachada.consultarFormatos("sumq");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            List<AtributoCompuesto> atrsTiempoFase = ((FormatoConcreto)listaSump.get(0)).getAtributo("Tasa de inyección de defectos").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsTiempoFase.size();
            for(int i = 0; i < t - 1; i++){
                atrTiempoFase = atrsTiempoFase.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Tasa de inyección de defectos");
            extras.add(pos);
            this.widthGrafica = "4500px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    
    public void graficarTasaRemocionDefectos(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGrafica = "bar";
            this.tipoGraficaTotal = null;
            List listaSump = formatoFachada.consultarFormatos("sumq");
            if(listaSump.isEmpty()) throw new ExceptionWarn("No existe el formato SUMQ del cual extraer los datos");
            List<AtributoCompuesto> atrsTiempoFase = ((FormatoConcreto)listaSump.get(0)).getAtributo("Tasa de remoción de defectos").getAtributos();

            List<AtributoCompuesto> datos = new ArrayList();
            AtributoCompuesto atrTiempoFase;
            List<AtributoCompuesto> planes = new ArrayList();
            List<AtributoCompuesto> reales = new ArrayList();
            String etapa;
            int t = atrsTiempoFase.size();
            for(int i = 0; i < t - 1; i++){
                atrTiempoFase = atrsTiempoFase.get(i);
                etapa = atrTiempoFase.getAtributo();
                planes.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Plan").getValor(),null));
                reales.add(new AtributoCompuesto(etapa, atrTiempoFase.getSubAtributo("Real").getValor(),null));
            }
            datos.add(new AtributoCompuesto("Plan",null,planes));
            datos.add(new AtributoCompuesto("Real",null,reales));
            List extras = new ArrayList();
            extras.add("Tasa de remoción de defectos");
            extras.add(pos);
            this.widthGrafica = "4500px";
            this.grafica = FabricaGraficas.construirGrafica(this.tipoGrafica, datos, extras, null);
            this.graficaTotal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarTiempo(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGraficaPersonal = "line";
            long widthPorDefecto = 250;
            List<Proyecto> proyectosSeleccionados = ((ProyectoControl)getControlador("ProyectoControl")).proyectosUsuarioSesionSeleccionados();
            String identificacionUsuarioSesion = ((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion();
            Proyecto proyecto;
            int cicloActual;
            List<FormatoConcreto> formatosLogt;
            int numFormatosLogt;
            List<AtributoCompuesto> atributosLogt;
            int numAtributosLogt;
            String nombreProyecto;
            int i, j, k, l;
            List<AtributoCompuesto> tiempoDedicado = new ArrayList();
            List<AtributoCompuesto> tiempoInterrumpido = new ArrayList();
            double totalDedicadoProyecto;
            double totalInterrumpidoProyecto;
            AtributoCompuesto registroLogt;
            int numProyectosSeleccionados = proyectosSeleccionados.size();
            for(i = 0; i < numProyectosSeleccionados; i++){
                proyecto = proyectosSeleccionados.get(i);
                nombreProyecto = proyecto.getNombre();
                cicloActual = proyecto.getCicloActual();
                totalDedicadoProyecto = 0;
                totalInterrumpidoProyecto = 0;
                for(j = 1; j <= cicloActual; j++){
                    formatosLogt = formatoFachada.consultarFormatos("logt",nombreProyecto,j,null,identificacionUsuarioSesion);
                    numFormatosLogt = formatosLogt.size();
                    for(k = 0; k < numFormatosLogt; k++){
                        atributosLogt = formatosLogt.get(k).getAtributos();
                        numAtributosLogt = atributosLogt.size();
                        for(l = 0; l < numAtributosLogt; l++){
                            registroLogt = atributosLogt.get(l);
                            totalDedicadoProyecto += Helper.extraerNumero(registroLogt.getSubAtributo("Tiempo dedicado").getValor());
                            totalInterrumpidoProyecto += Helper.extraerNumero(registroLogt.getSubAtributo("Tiempo interrumpido").getValor());
                        }
                    }
                }
                tiempoDedicado.add(new AtributoCompuesto(nombreProyecto,Double.toString(totalDedicadoProyecto),null));
                tiempoInterrumpido.add(new AtributoCompuesto(nombreProyecto,Double.toString(totalInterrumpidoProyecto),null));
                widthPorDefecto += 250;
            }
            
            List<AtributoCompuesto> datos = new ArrayList();
            datos.add(new AtributoCompuesto("Tiempo dedicado",null,tiempoDedicado));
            datos.add(new AtributoCompuesto("Tiempo interrumpido",null,tiempoInterrumpido));
            List extras = new ArrayList();
            extras.add("Tiempo personal en proyectos");
            extras.add(pos);
            extras.add(true);
            extras.add(null);
            extras.add(null);
            extras.add("Proyectos");
            extras.add("Tiempo (minutos)");
            this.widthGrafica = Long.toString(widthPorDefecto) + "px";
            this.graficaPersonal = FabricaGraficas.construirGrafica(this.tipoGraficaPersonal, datos, extras, 1);
            this.informacionGraficaPersonal = null;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarTrabajoDificultad(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGraficaPersonal = "line";
            long widthPorDefecto = 250;
            List<Proyecto> proyectosSeleccionados = ((ProyectoControl)getControlador("ProyectoControl")).proyectosUsuarioSesionSeleccionados();
            Proyecto proyecto;
            int cicloActual;
            String nombreProyecto;
            int i, j, k, l, m;
            List<FormatoConcreto> listaPeer;
            AtributoCompuesto trabajoDificultad;
            int numFormatosPeer;
            int numProyectosSeleccionados = proyectosSeleccionados.size();
            double promedioTrabajo;
            double promedioDificultad;
            List<String> nombresRolesUsuarioCicloProyecto;
            int numRolesUsuarioCicloProyecto;
            RlCl rc = new RlCl();
            RlClId id = new RlClId();
            AtributoCompuesto atrPeerTrabajoDificultadRol;
            int numIntegrantesConRol;
            Proyecto pyAux;
            String nombreRol;
            List<RlCl> rolesCicloProyecto;
            int numRolesCicloProyecto;
            double trabajo;
            double dificultad;
            List<AtributoCompuesto> trabajos = new ArrayList();
            List<AtributoCompuesto> dificultades = new ArrayList();
            List<AtributoCompuesto> datos = new ArrayList();
            id.setUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            this.informacionGraficaPersonal = new ArrayList();
            List<AtributoCompuesto> datosRegistro;
            List<AtributoCompuesto> atrsCiclo;
            List<AtributoCompuesto> registrosCiclo;
            double trabajoRol;
            double dificultadRol;
            for(i = 0; i < numProyectosSeleccionados; i++){
                proyecto = proyectosSeleccionados.get(i);
                nombreProyecto = proyecto.getNombre();
                cicloActual = proyecto.getCicloActual();
                id.setProyecto(nombreProyecto);
                promedioTrabajo = 0;
                promedioDificultad = 0;
                atrsCiclo = new ArrayList();
                for(j = 1; j <= cicloActual; j++){
                    registrosCiclo = new ArrayList();
                    pyAux = new Proyecto();
                    pyAux.setNombre(nombreProyecto);
                    pyAux.setCicloActual(b(j));
                    rolesCicloProyecto = ProyectoDAO.consultarRolesCiclo(pyAux);
                    numRolesCicloProyecto = rolesCicloProyecto.size();
                    listaPeer = formatoFachada.consultarFormatos("peer", nombreProyecto, j,null,null);
                    numFormatosPeer = listaPeer.size();
                    id.setNCiclo(b(j));
                    rc.setId(id);
                    nombresRolesUsuarioCicloProyecto = ProyectoDAO.consultarRolesDeUsuario(rc);
                    numRolesUsuarioCicloProyecto = nombresRolesUsuarioCicloProyecto.size();
                    trabajo = 0;
                    dificultad = 0;

                    for(l = 0; l < numRolesUsuarioCicloProyecto; l++){
                        nombreRol = nombresRolesUsuarioCicloProyecto.get(l);
                        numIntegrantesConRol = 0;
                        for(m = 0; m < numRolesCicloProyecto; m++)
                            if(rolesCicloProyecto.get(m).getId().getRol().equals(nombreRol))
                                numIntegrantesConRol += 1;
                        
                        datosRegistro = new ArrayList();
                        datosRegistro.add(new AtributoCompuesto("Rol",nombreRol,null));
                        datosRegistro.add(new AtributoCompuesto("N. Integrantes",Integer.toString(numIntegrantesConRol),null));
                        
                        trabajoRol = 0;
                        dificultadRol = 0;
                        if(!nombreRol.equals(TSP.instructor.getNombre()))
                            for(k = 0; k < numFormatosPeer; k++){
                                trabajoDificultad = listaPeer.get(k).getAtributo("Trabajo y dificultad");
                                atrPeerTrabajoDificultadRol = trabajoDificultad.getSubAtributo(nombreRol);
                                trabajoRol += Helper.extraerNumero(atrPeerTrabajoDificultadRol.getSubAtributo("Trabajo requerido").getValor());
                                dificultadRol += Helper.extraerNumero(atrPeerTrabajoDificultadRol.getSubAtributo("Dificultad del rol").getValor());
                            }
                        datosRegistro.add(new AtributoCompuesto("Total rol","T: " + Double.toString(trabajoRol) + " D: " + Double.toString(dificultadRol),null));
                        datosRegistro.add(new AtributoCompuesto("Total persona","T: " + Double.toString(trabajoRol/numIntegrantesConRol)+ " D: " + Double.toString(dificultadRol/numIntegrantesConRol),null));
                        registrosCiclo.add(new AtributoCompuesto(datosRegistro));
                        trabajo += trabajoRol / numIntegrantesConRol;
                        dificultad += dificultadRol / numIntegrantesConRol;
                    }
                    if(numFormatosPeer > 0){
                        promedioTrabajo += trabajo/numFormatosPeer;
                        promedioDificultad += dificultad/numFormatosPeer;
                    }
                    atrsCiclo.add(new AtributoCompuesto(Integer.toString(j),null,registrosCiclo));
                }
                this.informacionGraficaPersonal.add(new AtributoCompuesto(nombreProyecto,null,atrsCiclo));
                promedioTrabajo /= cicloActual;
                promedioDificultad /= cicloActual;
                trabajos.add(new AtributoCompuesto(nombreProyecto,Double.toString(promedioTrabajo),null));
                dificultades.add(new AtributoCompuesto(nombreProyecto,Double.toString(promedioDificultad),null));
                widthPorDefecto += 250;
            }
            datos.add(new AtributoCompuesto("Trabajo",null,trabajos));
            datos.add(new AtributoCompuesto("Dificultad",null,dificultades));
            List extras = new ArrayList();
            extras.add("Trabajo y dificultad");
            extras.add(pos);
            extras.add(true);
            extras.add(null);
            extras.add(null);
            extras.add("Proyectos");
            extras.add("Porcentaje promedio");
            this.widthGrafica = Long.toString(widthPorDefecto) + "px";
            this.graficaPersonal = FabricaGraficas.construirGrafica(this.tipoGraficaPersonal, datos, extras, 1);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarContribucionGeneral(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGraficaPersonal = "line";
            long widthPorDefecto = 250;
            List<Proyecto> proyectosSeleccionados = ((ProyectoControl)getControlador("ProyectoControl")).proyectosUsuarioSesionSeleccionados();
            Proyecto proyecto;
            int cicloActual;
            String nombreProyecto;
            int i, j, k, l, m;
            List<FormatoConcreto> listaPeer;
            AtributoCompuesto trabajoDificultad;
            int numFormatosPeer;
            int numProyectosSeleccionados = proyectosSeleccionados.size();
            double promedioContribucion;
            List<String> nombresRolesUsuarioCicloProyecto;
            int numRolesUsuarioCicloProyecto;
            RlCl rc = new RlCl();
            RlClId id = new RlClId();
            AtributoCompuesto atrPeerTrabajoDificultadRol;
            int numIntegrantesConRol;
            Proyecto pyAux;
            String nombreRol;
            List<RlCl> rolesCicloProyecto;
            int numRolesCicloProyecto;
            double contribucion;
            List<AtributoCompuesto> contribuciones = new ArrayList();
            List<AtributoCompuesto> datos = new ArrayList();
            id.setUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            this.informacionGraficaPersonal = new ArrayList();
            List<AtributoCompuesto> datosRegistro;
            List<AtributoCompuesto> atrsCiclo;
            List<AtributoCompuesto> registrosCiclo;
            double contribucionRol;
            for(i = 0; i < numProyectosSeleccionados; i++){
                proyecto = proyectosSeleccionados.get(i);
                nombreProyecto = proyecto.getNombre();
                cicloActual = proyecto.getCicloActual();
                id.setProyecto(nombreProyecto);
                promedioContribucion = 0;
                atrsCiclo = new ArrayList();
                for(j = 1; j <= cicloActual; j++){
                    registrosCiclo = new ArrayList();
                    pyAux = new Proyecto();
                    pyAux.setNombre(nombreProyecto);
                    pyAux.setCicloActual(b(j));
                    rolesCicloProyecto = ProyectoDAO.consultarRolesCiclo(pyAux);
                    numRolesCicloProyecto = rolesCicloProyecto.size();
                    listaPeer = formatoFachada.consultarFormatos("peer", nombreProyecto, j,null,null);
                    numFormatosPeer = listaPeer.size();
                    id.setNCiclo(b(j));
                    rc.setId(id);
                    nombresRolesUsuarioCicloProyecto = ProyectoDAO.consultarRolesDeUsuario(rc);
                    numRolesUsuarioCicloProyecto = nombresRolesUsuarioCicloProyecto.size();
                    contribucion = 0;

                    for(l = 0; l < numRolesUsuarioCicloProyecto; l++){
                        nombreRol = nombresRolesUsuarioCicloProyecto.get(l);
                        numIntegrantesConRol = 0;
                        for(m = 0; m < numRolesCicloProyecto; m++)
                            if(rolesCicloProyecto.get(m).getId().getRol().equals(nombreRol))
                                numIntegrantesConRol += 1;
                        
                        datosRegistro = new ArrayList();
                        datosRegistro.add(new AtributoCompuesto("Rol",nombreRol,null));
                        datosRegistro.add(new AtributoCompuesto("N. Integrantes",Integer.toString(numIntegrantesConRol),null));
                        contribucionRol = 0;
                        if(!nombreRol.equals(TSP.instructor.getNombre()))
                            for(k = 0; k < numFormatosPeer; k++){
                                trabajoDificultad = listaPeer.get(k).getAtributo("Contribución general");
                                atrPeerTrabajoDificultadRol = trabajoDificultad.getSubAtributo(nombreRol);
                                contribucionRol += Helper.extraerNumero(atrPeerTrabajoDificultadRol.getValor());
                            }
                        datosRegistro.add(new AtributoCompuesto("Total rol", Double.toString(contribucionRol),null));
                        datosRegistro.add(new AtributoCompuesto("Total persona",Double.toString(contribucionRol/numIntegrantesConRol),null));
                        registrosCiclo.add(new AtributoCompuesto(datosRegistro));
                        contribucion += contribucionRol / numIntegrantesConRol;
                    }
                    if(numFormatosPeer > 0) promedioContribucion += contribucion/numFormatosPeer;
                    atrsCiclo.add(new AtributoCompuesto(Integer.toString(j),null,registrosCiclo));
                }
                this.informacionGraficaPersonal.add(new AtributoCompuesto(nombreProyecto,null,atrsCiclo));
                promedioContribucion /= cicloActual;
                contribuciones.add(new AtributoCompuesto(nombreProyecto,Double.toString(promedioContribucion),null));
                widthPorDefecto += 250;
            }
            datos.add(new AtributoCompuesto("Contribución en proyecto",null,contribuciones));
            List extras = new ArrayList();
            extras.add("Contribución general");
            extras.add(pos);
            extras.add(true);
            extras.add(null);
            extras.add(null);
            extras.add("Proyectos");
            extras.add("Puntaje promedio");
            this.widthGrafica = Long.toString(widthPorDefecto) + "px";
            this.graficaPersonal = FabricaGraficas.construirGrafica(this.tipoGraficaPersonal, datos, extras, 1);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarAyudaSoporte(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGraficaPersonal = "line";
            long widthPorDefecto = 250;
            List<Proyecto> proyectosSeleccionados = ((ProyectoControl)getControlador("ProyectoControl")).proyectosUsuarioSesionSeleccionados();
            Proyecto proyecto;
            int cicloActual;
            String nombreProyecto;
            int i, j, k, l, m;
            List<FormatoConcreto> listaPeer;
            AtributoCompuesto trabajoDificultad;
            int numFormatosPeer;
            int numProyectosSeleccionados = proyectosSeleccionados.size();
            double promedioContribucion;
            List<String> nombresRolesUsuarioCicloProyecto;
            int numRolesUsuarioCicloProyecto;
            RlCl rc = new RlCl();
            RlClId id = new RlClId();
            AtributoCompuesto atrPeerTrabajoDificultadRol;
            int numIntegrantesConRol;
            Proyecto pyAux;
            String nombreRol;
            List<RlCl> rolesCicloProyecto;
            int numRolesCicloProyecto;
            double contribucion;
            List<AtributoCompuesto> contribuciones = new ArrayList();
            List<AtributoCompuesto> datos = new ArrayList();
            id.setUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            this.informacionGraficaPersonal = new ArrayList();
            List<AtributoCompuesto> datosRegistro;
            List<AtributoCompuesto> atrsCiclo;
            List<AtributoCompuesto> registrosCiclo;
            double contribucionRol;
            for(i = 0; i < numProyectosSeleccionados; i++){
                proyecto = proyectosSeleccionados.get(i);
                nombreProyecto = proyecto.getNombre();
                cicloActual = proyecto.getCicloActual();
                id.setProyecto(nombreProyecto);
                promedioContribucion = 0;
                atrsCiclo = new ArrayList();
                for(j = 1; j <= cicloActual; j++){
                    registrosCiclo = new ArrayList();
                    pyAux = new Proyecto();
                    pyAux.setNombre(nombreProyecto);
                    pyAux.setCicloActual(b(j));
                    rolesCicloProyecto = ProyectoDAO.consultarRolesCiclo(pyAux);
                    numRolesCicloProyecto = rolesCicloProyecto.size();
                    listaPeer = formatoFachada.consultarFormatos("peer", nombreProyecto, j,null,null);
                    numFormatosPeer = listaPeer.size();
                    id.setNCiclo(b(j));
                    rc.setId(id);
                    nombresRolesUsuarioCicloProyecto = ProyectoDAO.consultarRolesDeUsuario(rc);
                    numRolesUsuarioCicloProyecto = nombresRolesUsuarioCicloProyecto.size();
                    contribucion = 0;

                    for(l = 0; l < numRolesUsuarioCicloProyecto; l++){
                        nombreRol = nombresRolesUsuarioCicloProyecto.get(l);
                        numIntegrantesConRol = 0;
                        for(m = 0; m < numRolesCicloProyecto; m++)
                            if(rolesCicloProyecto.get(m).getId().getRol().equals(nombreRol))
                                numIntegrantesConRol += 1;
                        
                        datosRegistro = new ArrayList();
                        datosRegistro.add(new AtributoCompuesto("Rol",nombreRol,null));
                        datosRegistro.add(new AtributoCompuesto("N. Integrantes",Integer.toString(numIntegrantesConRol),null));
                        contribucionRol = 0;
                        if(!nombreRol.equals(TSP.instructor.getNombre()))
                            for(k = 0; k < numFormatosPeer; k++){
                                trabajoDificultad = listaPeer.get(k).getAtributo("Ayuda y soporte");
                                atrPeerTrabajoDificultadRol = trabajoDificultad.getSubAtributo(nombreRol);
                                contribucionRol += Helper.extraerNumero(atrPeerTrabajoDificultadRol.getValor());
                            }
                        datosRegistro.add(new AtributoCompuesto("Total rol", Double.toString(contribucionRol),null));
                        datosRegistro.add(new AtributoCompuesto("Total persona",Double.toString(contribucionRol/numIntegrantesConRol),null));
                        registrosCiclo.add(new AtributoCompuesto(datosRegistro));
                        contribucion += contribucionRol / numIntegrantesConRol;
                    }
                    if(numFormatosPeer > 0) promedioContribucion += contribucion/numFormatosPeer;
                    atrsCiclo.add(new AtributoCompuesto(Integer.toString(j),null,registrosCiclo));
                }
                this.informacionGraficaPersonal.add(new AtributoCompuesto(nombreProyecto,null,atrsCiclo));
                promedioContribucion /= cicloActual;
                contribuciones.add(new AtributoCompuesto(nombreProyecto,Double.toString(promedioContribucion),null));
                widthPorDefecto += 250;
            }
            datos.add(new AtributoCompuesto("Ayuda y soporte en proyecto",null,contribuciones));
            List extras = new ArrayList();
            extras.add("Ayuda y soporte");
            extras.add(pos);
            extras.add(true);
            extras.add(null);
            extras.add(null);
            extras.add("Proyectos");
            extras.add("Puntaje promedio");
            this.widthGrafica = Long.toString(widthPorDefecto) + "px";
            this.graficaPersonal = FabricaGraficas.construirGrafica(this.tipoGraficaPersonal, datos, extras, 1);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public void graficarDesempenio(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGraficaPersonal = "line";
            long widthPorDefecto = 250;
            List<Proyecto> proyectosSeleccionados = ((ProyectoControl)getControlador("ProyectoControl")).proyectosUsuarioSesionSeleccionados();
            Proyecto proyecto;
            int cicloActual;
            String nombreProyecto;
            int i, j, k, l, m;
            List<FormatoConcreto> listaPeer;
            AtributoCompuesto trabajoDificultad;
            int numFormatosPeer;
            int numProyectosSeleccionados = proyectosSeleccionados.size();
            double promedioContribucion;
            List<String> nombresRolesUsuarioCicloProyecto;
            int numRolesUsuarioCicloProyecto;
            RlCl rc = new RlCl();
            RlClId id = new RlClId();
            AtributoCompuesto atrPeerTrabajoDificultadRol;
            int numIntegrantesConRol;
            Proyecto pyAux;
            String nombreRol;
            List<RlCl> rolesCicloProyecto;
            int numRolesCicloProyecto;
            double contribucion;
            List<AtributoCompuesto> contribuciones = new ArrayList();
            List<AtributoCompuesto> datos = new ArrayList();
            id.setUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            this.informacionGraficaPersonal = new ArrayList();
            List<AtributoCompuesto> datosRegistro;
            List<AtributoCompuesto> atrsCiclo;
            List<AtributoCompuesto> registrosCiclo;
            double contribucionRol;
            for(i = 0; i < numProyectosSeleccionados; i++){
                proyecto = proyectosSeleccionados.get(i);
                nombreProyecto = proyecto.getNombre();
                cicloActual = proyecto.getCicloActual();
                id.setProyecto(nombreProyecto);
                promedioContribucion = 0;
                atrsCiclo = new ArrayList();
                for(j = 1; j <= cicloActual; j++){
                    registrosCiclo = new ArrayList();
                    pyAux = new Proyecto();
                    pyAux.setNombre(nombreProyecto);
                    pyAux.setCicloActual(b(j));
                    rolesCicloProyecto = ProyectoDAO.consultarRolesCiclo(pyAux);
                    numRolesCicloProyecto = rolesCicloProyecto.size();
                    listaPeer = formatoFachada.consultarFormatos("peer", nombreProyecto, j,null,null);
                    numFormatosPeer = listaPeer.size();
                    id.setNCiclo(b(j));
                    rc.setId(id);
                    nombresRolesUsuarioCicloProyecto = ProyectoDAO.consultarRolesDeUsuario(rc);
                    numRolesUsuarioCicloProyecto = nombresRolesUsuarioCicloProyecto.size();
                    contribucion = 0;

                    for(l = 0; l < numRolesUsuarioCicloProyecto; l++){
                        nombreRol = nombresRolesUsuarioCicloProyecto.get(l);
                        numIntegrantesConRol = 0;
                        for(m = 0; m < numRolesCicloProyecto; m++)
                            if(rolesCicloProyecto.get(m).getId().getRol().equals(nombreRol))
                                numIntegrantesConRol += 1;
                        
                        datosRegistro = new ArrayList();
                        datosRegistro.add(new AtributoCompuesto("Rol",nombreRol,null));
                        datosRegistro.add(new AtributoCompuesto("N. Integrantes",Integer.toString(numIntegrantesConRol),null));
                        contribucionRol = 0;
                        if(!nombreRol.equals(TSP.instructor.getNombre()))
                            for(k = 0; k < numFormatosPeer; k++){
                                trabajoDificultad = listaPeer.get(k).getAtributo("Desempeño");
                                atrPeerTrabajoDificultadRol = trabajoDificultad.getSubAtributo(nombreRol);
                                contribucionRol += Helper.extraerNumero(atrPeerTrabajoDificultadRol.getValor());
                            }
                        datosRegistro.add(new AtributoCompuesto("Total rol", Double.toString(contribucionRol),null));
                        datosRegistro.add(new AtributoCompuesto("Total persona",Double.toString(contribucionRol/numIntegrantesConRol),null));
                        registrosCiclo.add(new AtributoCompuesto(datosRegistro));
                        contribucion += contribucionRol / numIntegrantesConRol;
                    }
                    if(numFormatosPeer > 0) promedioContribucion += contribucion/numFormatosPeer;
                    atrsCiclo.add(new AtributoCompuesto(Integer.toString(j),null,registrosCiclo));
                }
                this.informacionGraficaPersonal.add(new AtributoCompuesto(nombreProyecto,null,atrsCiclo));
                promedioContribucion /= cicloActual;
                contribuciones.add(new AtributoCompuesto(nombreProyecto,Double.toString(promedioContribucion),null));
                widthPorDefecto += 250;
            }
            datos.add(new AtributoCompuesto("Desempeño en proyecto",null,contribuciones));
            List extras = new ArrayList();
            extras.add("Desempeño");
            extras.add(pos);
            extras.add(true);
            extras.add(null);
            extras.add(null);
            extras.add("Proyectos");
            extras.add("Puntaje promedio");
            this.widthGrafica = Long.toString(widthPorDefecto) + "px";
            this.graficaPersonal = FabricaGraficas.construirGrafica(this.tipoGraficaPersonal, datos, extras, 1);
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    
    public void graficarPlanPersonal(String pos) throws ExceptionFatal, ExceptionWarn {
        try{
            this.tipoGraficaPersonal = "line";
            long widthPorDefecto = 250;
            ProyectoControl pc = (ProyectoControl)getControlador("ProyectoControl");
            List<Proyecto> proyectosSeleccionados = pc.proyectosUsuarioSesionSeleccionados();
            int numProyectosSeleccionados = proyectosSeleccionados.size();
            Proyecto proyecto;
            int numCiclosProyecto;
            int i, j, k, l;
            List registrosTask;
            int numRegistrosTask;
            AtributoCompuesto atributoTask;
            FormatoConcreto formatoTask = new FormatoConcreto();
            RlCl rlcl = new RlCl();
            RlClId rlclid = new RlClId();
            rlclid.setUsuario(((UsuarioControl)getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            formatoTask.setNombre("task");
            List<String> rolesUsuarioCiclo;
            int numRolesUsuarioCiclo;
            AtributoCompuesto horasDelPlan;
            RlCl rlcl2 = new RlCl();
            RlClId rlclId2 = new RlClId();
            for(i = 0; i < numProyectosSeleccionados; i++){
                proyecto = proyectosSeleccionados.get(i);
                formatoTask.setProyecto(proyecto.getNombre());
                numCiclosProyecto = proyecto.getNCiclos();
                rlclid.setProyecto(proyecto.getNombre());
                rlclId2.setProyecto(proyecto.getNombre());
                for(j = 0; j < numCiclosProyecto; j++){
                    rlclId2.setNCiclo(b(j));
                    rlcl2.setId(rlclId2);
                    rlclid.setNCiclo(b(j));
                    rlcl.setId(rlclid);
                    rolesUsuarioCiclo = ProyectoDAO.consultarRolesDeUsuario(rlcl);
                    numRolesUsuarioCiclo = rolesUsuarioCiclo.size();
                    formatoTask.setCiclo(j);
                    registrosTask = formatoFachada.consultarFormato(formatoTask).getAtributos();
                    numRegistrosTask = registrosTask.size();
                    for(k = 0; k < numRegistrosTask; k++){
                        atributoTask = (AtributoCompuesto)registrosTask.get(k);
                        horasDelPlan = atributoTask.getSubAtributo("Horas del plan");
                        for(l = 0; l < numRolesUsuarioCiclo; l++){
                            horasDelPlan.getSubAtributo(rolesUsuarioCiclo.get(l)).getValor();
                        }
                    }
                }
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("GraficaControl no. " + e.getMessage());
        }
    }
    
    public ChartModel getGrafica() {
        return grafica;
    }

    public void setGrafica(ChartModel grafica) {
        this.grafica = grafica;
    }

    public String getTipoGrafica() {
        return tipoGrafica;
    }

    public void setTipoGrafica(String tipoGrafica) {
        this.tipoGrafica = tipoGrafica;
    }

    public ChartModel getGraficaTotal() {
        return graficaTotal;
    }

    public void setGraficaTotal(ChartModel graficaTotal) {
        this.graficaTotal = graficaTotal;
    }

    public String getTipoGraficaTotal() {
        return tipoGraficaTotal;
    }

    public void setTipoGraficaTotal(String tipoGraficaTotal) {
        this.tipoGraficaTotal = tipoGraficaTotal;
    }

    public String getWidthGrafica() {
        return widthGrafica;
    }

    public void setWidthGrafica(String widthGrafica) {
        this.widthGrafica = "4500px";
    }

    public FormatoFachada getFormatoFachada() {
        return formatoFachada;
    }

    public void setFormatoFachada(FormatoFachada formatoFachada) {
        this.formatoFachada = formatoFachada;
    }

    public List<AtributoCompuesto> getAtrsDetallesGraficasCiclo(){
        return (List<AtributoCompuesto>)TSP.atrsDetallesGraficasCiclo;
    }
    
    public List<AtributoCompuesto> getAtrsDetallesGraficasPersonales(){
        return (List<AtributoCompuesto>)TSP.atrsDetallesGraficasPersonales;
    }

    public ChartModel getGraficaPersonal() {
        return graficaPersonal;
    }

    public void setGraficaPersonal(ChartModel graficaPersonal) {
        this.graficaPersonal = graficaPersonal;
    }

    public String getTipoGraficaPersonal() {
        return tipoGraficaPersonal;
    }

    public void setTipoGraficaPersonal(String tipoGraficaPersonal) {
        this.tipoGraficaPersonal = tipoGraficaPersonal;
    }

    public List<AtributoCompuesto> getInformacionGraficaPersonal() {
        return informacionGraficaPersonal;
    }

    public void setInformacionGraficaPersonal(List<AtributoCompuesto> informacionGraficaPersonal) {
        this.informacionGraficaPersonal = informacionGraficaPersonal;
    }

    public String getWidthGraficaPersonal() {
        return widthGraficaPersonal;
    }

    public void setWidthGraficaPersonal(String widthGraficaPersonal) {
        this.widthGraficaPersonal = widthGraficaPersonal;
    }
    
    
    
}
