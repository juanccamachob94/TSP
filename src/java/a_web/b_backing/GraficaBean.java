package a_web.b_backing;

import b_controlador.a_gestion.GraficaControl;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.ChartModel;

@ManagedBean
@SessionScoped
public class GraficaBean extends GeneralBean {
    
    @ManagedProperty("#{graficaControl}")
    GraficaControl graficaControl;

    public GraficaBean() {
    }

    public void crearGraficaCiclo(int nGrafica, String pos) {
        try {
            graficaControl.reiniciarValores();
            switch(nGrafica){
                case 1:
                    graficaControl.crearGraficaTiempoEtapa(pos);
                    break;
                case 2:
                    graficaControl.graficaTamanioProducto(pos);
                    break;
                case 3:
                    graficaControl.graficarDefectosInyectados(pos);
                    break;
                case 4:
                    graficaControl.graficarDefectosRemovidos(pos);
                    break;
                case 5:
                    graficaControl.graficarPDFCompilacion(pos);
                    break;
                case 6:
                    graficaControl.graficarPDFPruebaUnidad(pos);
                    break;
                case 7:
                    graficaControl.graficarPDFConstIntegracion(pos);
                    break;
                case 8:
                    graficaControl.graficarPDFPruebaSistema(pos);
                    break;
                case 9:
                    graficaControl.graficarTiempoArregloDefectos(pos);
                    break;
                case 10:
                    graficaControl.graficarVPVG(pos);
                    break;
                case 11:
                    graficaControl.graficarCumplimientoMetasCiclo(pos);
                    break;
                case 12:
                    graficaControl.graficarDefectosCodigo(pos);
                    break;
                case 13:
                    graficaControl.graficarRendimientosFase(pos);
                    break;
                case 14:
                    graficaControl.graficarTasaInyeccionDefectos(pos);
                    break;
                case 15:
                    graficaControl.graficarTasaRemocionDefectos(pos);
                    break;
                default:
                    throw new ExceptionWarn("No existe gráfica para la opción seleccionada");
            }
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al crear la gráfica. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al crear la gráfica. " + warn.getMessage(), "warn");
        }
    }
    
    public void crearGraficaPersonal(int nGrafica, String pos) {
        try {
            graficaControl.reiniciarValoresPersonales();
            switch(nGrafica){
                case 1:
                    graficaControl.graficarTiempo(pos);
                    break;
                case 2:
                    graficaControl.graficarTrabajoDificultad(pos);
                    break;
                case 3:
                    graficaControl.graficarContribucionGeneral(pos);
                    break;
                case 4:
                    graficaControl.graficarAyudaSoporte(pos);
                    break;
                case 5:
                    graficaControl.graficarDesempenio(pos);
                    break;
                case 6:
                    graficaControl.graficarPlanPersonal(pos);
                default:
                    throw new ExceptionWarn("No existe gráfica para la opción seleccionada");
            }
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, "Error al crear la gráfica. " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, "Error al crear la gráfica. " + warn.getMessage(), "warn");
        }
    }
    
    public void reiniciarValoresPersonales() {
        try {
            graficaControl.reiniciarValoresPersonales();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, ". " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, ". " + warn.getMessage(), "warn");
        }
    }
    

    public void reiniciarValores() {
        try {
            graficaControl.reiniciarValores();
        } catch (ExceptionFatal fatal) {
            this.enviarMensaje(null, ". " + fatal.getMessage(), "fatal");
        } catch (ExceptionWarn warn) {
            this.enviarMensaje(null, ". " + warn.getMessage(), "warn");
        }
    }
    
    public GraficaControl getGraficaControl() {
        return graficaControl;
    }

    public void setGraficaControl(GraficaControl graficaControl) {
        this.graficaControl = graficaControl;
    }

    public ChartModel getGrafica() {
        return graficaControl.getGrafica();
    }

    public void setGrafica(ChartModel grafica) {
        graficaControl.setGrafica(grafica);
    }

    public String getTipoGrafica() {
        return graficaControl.getTipoGrafica();
    }

    public void setTipoGrafica(String tipoGrafica) {
        graficaControl.setTipoGrafica(tipoGrafica);
    }

    public ChartModel getGraficaTotal() {
        return graficaControl.getGraficaTotal();
    }

    public void setGraficaTotal(ChartModel graficaTotal) {
        graficaControl.setGraficaTotal(graficaTotal);
    }

    public String getTipoGraficaTotal() {
        return graficaControl.getTipoGraficaTotal();
    }

    public void setTipoGraficaTotal(String tipoGraficaTotal) {
        graficaControl.setTipoGraficaTotal(tipoGraficaTotal);
    }
    
    public String getWidthGrafica() {
        return graficaControl.getWidthGrafica();
    }

    public void setWidthGrafica(String widthGrafica) {
        graficaControl.setWidthGrafica(widthGrafica);
    }
    
    public List<AtributoCompuesto> getAtrsDetallesGraficasCiclo(){
        return graficaControl.getAtrsDetallesGraficasCiclo();
    }

    public List<AtributoCompuesto> getAtrsDetallesGraficasPersonales(){
        return graficaControl.getAtrsDetallesGraficasPersonales();
    }
        
    public List<AtributoCompuesto> getInformacionGraficaPersonal() {
        return graficaControl.getInformacionGraficaPersonal();
    }

    public void setInformacionGraficaPersonal(List<AtributoCompuesto> informacionGraficaPersonal) {
        graficaControl.setInformacionGraficaPersonal(informacionGraficaPersonal);
    }
    
    public ChartModel getGraficaPersonal() {
        return graficaControl.getGraficaPersonal();
    }

    public void setGraficaPersonal(ChartModel graficaPersonal) {
        graficaControl.setGraficaPersonal(graficaPersonal);
    }

    public String getTipoGraficaPersonal() {
        return graficaControl.getTipoGraficaPersonal();
    }

    public void setTipoGraficaPersonal(String tipoGraficaPersonal) {
        graficaControl.setTipoGraficaPersonal(tipoGraficaPersonal);
    }

    public String getWidthGraficaPersonal() {
        return graficaControl.getWidthGraficaPersonal();
    }

    public void setWidthGraficaPersonal(String widthGraficaPersonal) {
        graficaControl.setWidthGraficaPersonal(widthGraficaPersonal);
    }   
    
}