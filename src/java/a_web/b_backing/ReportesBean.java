package a_web.b_backing;

import b_controlador.a_gestion.ReportesControl;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultStreamedContent;

@ManagedBean
@SessionScoped
public class ReportesBean extends GeneralBean {

    @ManagedProperty("#{reportesControl}")
    private ReportesControl reportesControl;

    public ReportesBean() {
    }

    public void exportarFormatoExcel(FormatoConcreto formatoReal, boolean iterable) {
        try {
            reportesControl.exportarFormatoExcel(formatoReal,iterable);
            this.enviarMensaje(null,"Formato " + formatoReal.getNombre() + " exportado a excel satisfactoriamente","info");
        }catch(ExceptionFatal fatal){
            this.enviarMensaje(null,"Error al exportar el formato excel. " + fatal.getMessage(),"fatal");
        }catch(ExceptionWarn warn){
            this.enviarMensaje(null,"Error al exportar el formato excel. " + warn.getMessage(),"warn");
        }
    }

    public DefaultStreamedContent getArchivoExcel() {
        return reportesControl.getArchivoExcel();
    }

    public void setArchivoExcel(DefaultStreamedContent archivoExcel) {
        reportesControl.setArchivoExcel(archivoExcel);
    }

    public ReportesControl getReportesControl() {
        return reportesControl;
    }

    public void setReportesControl(ReportesControl reportesControl) {
        this.reportesControl = reportesControl;
    }
    
    
    
}
