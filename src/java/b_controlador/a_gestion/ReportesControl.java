/**
 * Permite la generación de reportes; actualmente sólo en formato excel.
 */
package b_controlador.a_gestion;

import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.Serial;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.primefaces.model.DefaultStreamedContent;

@ManagedBean
@SessionScoped
public class ReportesControl extends Control{

    private DefaultStreamedContent archivoExcel;
    private int ultimaFila;

    public ReportesControl() {
    }

    /**
     * Permite exportar el formato en excel.
     * @param formatoReal
     * @param iterable
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void exportarFormatoExcel(FormatoConcreto formatoReal, boolean iterable) throws ExceptionFatal, ExceptionWarn {
        try{
            FormatoConcreto formato = ajustarFormato(formatoReal);
            int numFila;
            int numColumna;
            Row fila;
            HSSFWorkbook excel = new HSSFWorkbook();
            Cell celda;
            Sheet hojaExcel = excel.createSheet(formato.getNombre());
            //Nombre del formato
            numFila = 0;
            this.ultimaFila = 0;
            fila = hojaExcel.createRow(numFila);

            numColumna = 0;
            celda = fila.createCell(numColumna);
            celda.setCellValue("Formato");

            numColumna = 1;
            celda = fila.createCell(numColumna);
            celda.setCellValue(formato.getNombre());
            //Proyecto
            numFila = 1;
            this.ultimaFila = 1;
            fila = hojaExcel.createRow(numFila);
            
            numColumna = 0;
            celda = fila.createCell(numColumna);
            celda.setCellValue("Proyecto");

            numColumna = 1;
            celda = fila.createCell(numColumna);
            celda.setCellValue(formato.getProyecto());

            //Ciclo
            numFila += 1;
            this.ultimaFila += 1;
            fila = hojaExcel.createRow(numFila);

            numColumna = 0;
            celda = fila.createCell(numColumna);
            celda.setCellValue("Ciclo");

            numColumna = 1;
            celda = fila.createCell(numColumna);
            celda.setCellValue(Integer.toString(formato.getCiclo()));
            
            //Fecha
            numFila += 1;
            this.ultimaFila += 1;
            fila = hojaExcel.createRow(numFila);

            numColumna = 0;
            celda = fila.createCell(numColumna);
            celda.setCellValue("Fecha");

            numColumna = 1;
            celda = fila.createCell(numColumna);
            if(formato.getFecha() != null)
                celda.setCellValue(formato.getFecha());
            //Rol
            numFila += 1;
            this.ultimaFila += 1;
            fila = hojaExcel.createRow(numFila);

            numColumna = 0;
            celda = fila.createCell(numColumna);
            celda.setCellValue("Rol");

            numColumna = 1;
            celda = fila.createCell(numColumna);
            if(formato.getRol() != null)
            celda.setCellValue(formato.getRol());
            
            //Autor
            numFila += 1;
            this.ultimaFila += 1;
            fila = hojaExcel.createRow(numFila);

            numColumna = 0;
            celda = fila.createCell(numColumna);
            celda.setCellValue("Autor");

            numColumna = 1;
            celda = fila.createCell(numColumna);
            if(formato.getAutor() != null)
            celda.setCellValue(formato.getAutor());
            
            //Se asigna la fila sobre la que se empiezan a generar los atributos
            numFila += 1;
            this.ultimaFila += 1;
            fila = hojaExcel.createRow(numFila);
            List atributos = formato.getAtributos();
            int t = atributos.size();
            int t2;
            AtributoCompuesto subAtributo;
            List subAtributos;
            AtributoCompuesto atributo;
            if (iterable && t > 0) {
                crearTituloAtributo(((AtributoCompuesto) atributos.get(0)).getAtributos(), fila, numFila, hojaExcel, 0);
                this.ultimaFila--;
                numFila = this.ultimaFila;
                for (int i = 0; i < t; i++) {
                    numFila += 1;
                    fila = hojaExcel.createRow(numFila);
                    crearColumnasAtributo((AtributoCompuesto) atributos.get(i), fila, 0);
                }
            } else for (int i = 0; i < t; i++) {
                    atributo = (AtributoCompuesto) atributos.get(i);
                    if (esAtributoIterable(atributo, 2)) {
                        celda = fila.createCell(0);
                        celda.setCellValue(atributo.getAtributo());
                        crearTituloAtributo(((AtributoCompuesto) atributo.getAtributos().get(0)).getAtributos(), fila, numFila, hojaExcel, 1);
                        this.ultimaFila--;
                        numFila = this.ultimaFila;
                        subAtributos = atributo.getAtributos();
                        t2 = subAtributos.size();
                        for (int j = 0; j < t2; j++) {
                            subAtributo = (AtributoCompuesto) subAtributos.get(j);
                            numFila += 1;
                            this.ultimaFila = numFila;
                            fila = hojaExcel.createRow(numFila);
                            celda = fila.createCell(0);
                            celda.setCellValue(subAtributo.getAtributo());
                            crearColumnasAtributo(subAtributo, fila, 1);
                        }
                        numFila += 1;
                        this.ultimaFila = numFila;
                        fila = hojaExcel.createRow(numFila);

                    } else if (atributo.getAtributos() != null) {
                            //Atributo compuesto
                            celda = fila.createCell(0);
                            celda.setCellValue(atributo.getAtributo());
                            crearTituloAtributo(atributo.getAtributos(), fila, numFila, hojaExcel, 1);
                            numFila = this.ultimaFila;
                            fila = hojaExcel.createRow(numFila);
                            crearColumnasAtributo(atributo, fila, 1);
                            numFila += 1;
                            this.ultimaFila = numFila;
                            fila = hojaExcel.createRow(numFila);
                        } else {
                            //Atributo simple
                            celda = fila.createCell(0);
                            celda.setCellValue(atributo.getAtributo());
                            celda = fila.createCell(1);
                            celda.setCellValue(atributo.getValor());
                            numFila += 1;
                            this.ultimaFila = numFila;
                            fila = hojaExcel.createRow(numFila);
                        }
                    }
            File archivo = new File(formato.getProyecto() + "_ciclo" + Integer.toString(formato.getCiclo()) + "_" + formato.getNombre() + ".xls");
            excel.write(new FileOutputStream(archivo));
            InputStream input = new FileInputStream(archivo);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            this.archivoExcel = new DefaultStreamedContent(input, externalContext.getMimeType(archivo.getName()), archivo.getName());
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("ReportesControl no puede exportar el excel. " + e.getMessage());
        }
    }
    
    /**
     * Crea las celdas excel para el título de los atributos
     * @param atributos
     * @param fila
     * @param numFila
     * @param hojaExcel
     * @param numColumna
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void crearTituloAtributo(List<AtributoCompuesto> atributos, Row fila, int numFila, Sheet hojaExcel, int numColumna) throws ExceptionFatal, ExceptionWarn {
        try{
            List subAtributos;
            int t = atributos.size();
            AtributoCompuesto atributo;
            Cell celda;
            Row subFila = hojaExcel.createRow(numFila + 1);
            if (numFila + 1 > this.ultimaFila) this.ultimaFila += 1;
            for (int i = 0; i < t; i++) {
                atributo = atributos.get(i);
                celda = fila.createCell(numColumna);
                celda.setCellValue(atributo.getAtributo());
                subAtributos = atributo.getAtributos();
                if (subAtributos == null) numColumna += 1;
                else {
                    crearTituloAtributo(subAtributos, subFila, numFila + 1, hojaExcel, numColumna);
                    numColumna += subAtributos.size();
                }
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede crear el título. " + e.getMessage());
        }
    }

    /**
     * Crea las columnas del contenido del formato en excel
     * @param atributo
     * @param fila
     * @param numColumna
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int crearColumnasAtributo(AtributoCompuesto atributo, Row fila, int numColumna) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = atributo.getAtributos();
            int t;
            Cell celda;
            if (atributos == null) {
                celda = fila.createCell(numColumna);
                celda.setCellValue(atributo.getValor());
                return numColumna + 1;
            } else {
                t = atributos.size();
                for (int i = 0; i < t; i++)
                    numColumna = crearColumnasAtributo((AtributoCompuesto) atributos.get(i), fila, numColumna);
            }
            return numColumna;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se pueden crear las columnas. " + e.getMessage());
        }
    }

    /**
     * Identifica si es un atributo con elementos en forma iterable; se comporta como tabla
     * @param atributo
     * @param nivelRevision
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean esAtributoIterable(AtributoCompuesto atributo, int nivelRevision) throws ExceptionFatal, ExceptionWarn {
        try{
            List subAtributos = atributo.getAtributos();
            if (subAtributos == null) return false;
            int t = subAtributos.size();
            if (t == 1 || t == 0) return false;
            String formatoBase = formatoFinalAtributo((AtributoCompuesto) subAtributos.get(0));
            t = t / nivelRevision;
            t = t < 2 ? 2 : t;
            for (int i = 0; i < t; i++)
                if (!formatoFinalAtributo((AtributoCompuesto) subAtributos.get(i)).equals(formatoBase))
                    return false;
            return true;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("Error al identificar la iterabilidad del formato. " + e.getMessage());
        }

    }

    /**
     * Identifica el patrón de las últimos hijos (hojas)
     * @param atributo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private String formatoFinalAtributo(AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos;
            String formato = "";
            AtributoCompuesto atr = atributo;
            AtributoCompuesto atr2;
            int it = 0;
            while (true) {
                atributos = atr.getAtributos();
                if (atributos == null) return atr.getAtributo();
                if (atributos.isEmpty()) return atr.getAtributo();
                atr2 = (AtributoCompuesto) atributos.get(0);
                if (atr2.getAtributos() == null) break;
                if (atr2.getAtributos().isEmpty()) break;
                atr = atr2;
            }
            int t = atributos.size();
            for (int i = 0; i < t; i++)
                formato += ((AtributoCompuesto) atributos.get(i)).getAtributo();
            return formato;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede determinar el patrón de datos de los atributos. " + e.getMessage());
        }
    }

    /**
     * Ajusta un formato antes de exportarlo a excel para garantizar el formato de matriz
     * @param formatoReal
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private FormatoConcreto ajustarFormato(FormatoConcreto formatoReal) throws ExceptionFatal, ExceptionWarn {
        try{
           List atributos;
            List subAtributos;
            int t;
            FormatoConcreto formato = (FormatoConcreto) Serial.clonar(formatoReal);
            AtributoCompuesto atributo;
            switch (formato.getNombre()) {
                case "task":
                    atributos = formato.getAtributos();
                    t = atributos.size();
                    for (int i = 0; i < t; i++) {
                        subAtributos = ((AtributoCompuesto) atributos.get(i)).getAtributos();
                        subAtributos.remove(subAtributos.size() - 1);
                    }
                    break;
                case "sump":
                    if(formato.getAtributo("Resumen") != null)
                        formato.getAtributo("Resumen").getSubAtributo("CPI (índice de costo-desempeño)").getAtributos().add(0, new AtributoCompuesto("Plan", "", null));
                    break;
                case "Comparación de código":
                    atributos = formato.getAtributos();
                    t = atributos.size();
                    for (int i = 0; i < t; i++) {
                        atributo = (AtributoCompuesto) atributos.get(i);
                        if (atributo.getAtributo().equals("archivos")) {
                            atributos.remove(i);
                            subAtributos = atributo.getAtributos();
                            t = subAtributos.size();
                            for (int j = 0; j < t; j++)
                                atributos.add(subAtributos.get(j));
                            break;
                        }
                    }
                    break;
            }
            return formato;
        }catch(Exception e){
            throw new ExceptionFatal("Error al ajustar el formato para la generación del documento. " + e.getMessage());
        }
    }

    public DefaultStreamedContent getArchivoExcel() {
        return archivoExcel;
    }

    public void setArchivoExcel(DefaultStreamedContent archivoExcel) {
        this.archivoExcel = archivoExcel;
    }
}