package b_controlador.c_fabricas.d_fabrica_graficas;

import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import e_utilitaria.Helper;
import java.util.List;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

public class FabricaGraficas extends FabricaAbstractaGraficas{
    
    public static ChartModel construirGrafica(String tipo, List<AtributoCompuesto> datos, List extras, Integer dimensiones){
        switch(tipo){
            case "pie":
                return construirTorta(datos,extras);
            case "line":
                return construirLineas(datos,dimensiones,extras);
            case "bar":
                return construirBarras(datos,extras);
        }
        return null;
    }
    
    private static ChartModel construirTorta(List<AtributoCompuesto> datos, List extras){
        PieChartModel modelo = new PieChartModel();
        int t = datos.size();
        AtributoCompuesto atr;
        for(int i = 0; i < t; i++){
            atr = datos.get(i);
            modelo.set(atr.getAtributo(), Helper.extraerNumero(atr.getValor()));
        }
        modelo.setTitle((String)extras.get(0));
        modelo.setLegendPosition((String)extras.get(1));
        t = extras.size();
        if(t > 2) if(extras.get(2) != null)modelo.setFill((boolean) extras.get(2));
        if(t > 3) if(extras.get(3) != null) modelo.setShowDataLabels((boolean) extras.get(3));
        if(t > 4) if(extras.get(4) != null) modelo.setDiameter((int) extras.get(4));
        return modelo;
    }

    private static ChartModel construirLineas(List<AtributoCompuesto> datos, int dimensiones, List extras) {
        LineChartModel modelo = new LineChartModel();
        int totalSeriales = datos.size();
        ChartSeries serie;
        AtributoCompuesto atr;
        int totalSubAtributos;
        List<AtributoCompuesto> subAtributos;
        AtributoCompuesto subAtributo;
        if(dimensiones == 1)
            for(int i = 0; i < totalSeriales; i++){
                atr = datos.get(i);
                serie = new ChartSeries();
                serie.setLabel(atr.getAtributo());
                subAtributos = atr.getAtributos();
                totalSubAtributos = subAtributos.size();
                for(int j = 0; j < totalSubAtributos; j++){
                    subAtributo = subAtributos.get(j);
                    serie.set(subAtributo.getAtributo(), Helper.extraerNumero(subAtributo.getValor()));
                }
                modelo.addSeries(serie);
            }
        else
            for(int i = 0; i < totalSeriales; i++){
                atr = datos.get(i);
                serie = new ChartSeries();
                serie.setLabel(atr.getAtributo());
                subAtributos = atr.getAtributos();
                totalSubAtributos = subAtributos.size();
                for(int j = 0; j < totalSubAtributos; j++){
                    subAtributo = subAtributos.get(j);
                    serie.set(Helper.extraerNumero(subAtributo.getAtributo()), Helper.extraerNumero(subAtributo.getValor()));
                }
                modelo.addSeries(serie);
            }
        modelo.setTitle((String)extras.get(0));
        modelo.setLegendPosition((String)extras.get(1));
        totalSeriales = extras.size();
        if(totalSeriales > 2) if(extras.get(2) != null) modelo.setShowPointLabels((boolean)extras.get(2));
        Axis yAxis = modelo.getAxis(AxisType.Y);
        if(totalSeriales > 3) if(extras.get(3) != null) yAxis.setMin(extras.get(3));
        if(totalSeriales > 4) if(extras.get(4) != null) yAxis.setMax(extras.get(4));
        if(totalSeriales > 5) if(extras.get(5) != null) modelo.getAxes().put(AxisType.X, new CategoryAxis((String)extras.get(5)));//Título horizontal
        if(totalSeriales > 6) if(extras.get(6) != null) yAxis.setLabel((String)extras.get(6));//Título vertical
        if(totalSeriales > 7) if(extras.get(7) != null) modelo.setStacked((boolean)extras.get(7));//Para rellenar áreas
        return modelo;
    }
    
    private static ChartModel construirBarras(List<AtributoCompuesto> datos, List extras) {
        BarChartModel modelo = new BarChartModel();
        int totalSeriales = datos.size();
        ChartSeries serie;
        AtributoCompuesto atr;
        int totalSubAtributos;
        List<AtributoCompuesto> subAtributos;
        AtributoCompuesto subAtributo;
        for(int i = 0; i < totalSeriales; i++){
            atr = datos.get(i);
            serie = new ChartSeries();
            serie.setLabel(atr.getAtributo());
            subAtributos = atr.getAtributos();
            totalSubAtributos = subAtributos.size();
            for(int j = 0; j < totalSubAtributos; j++){
                subAtributo = subAtributos.get(j);
                serie.set(subAtributo.getAtributo(), Helper.extraerNumero(subAtributo.getValor()));
            }
            modelo.addSeries(serie);
        }
        modelo.setTitle((String)extras.get(0));
        modelo.setLegendPosition((String)extras.get(1));
        totalSeriales = extras.size();
        Axis yAxis = modelo.getAxis(AxisType.Y);
        if(totalSeriales > 2) if(extras.get(2) != null) yAxis.setMin(extras.get(2));
        if(totalSeriales > 3) if(extras.get(3) != null) yAxis.setMax(extras.get(3));
        if(totalSeriales > 4) if(extras.get(4) != null) yAxis.setLabel((String)extras.get(4));
        if(totalSeriales > 5) if(extras.get(5) != null) modelo.getAxis(AxisType.X).setLabel((String)extras.get(5));
        return modelo;
    }
}