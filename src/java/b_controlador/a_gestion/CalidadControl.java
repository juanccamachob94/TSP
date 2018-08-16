/*
* Gestión de los formatos SUMP y SUMQ
*/
package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
@ManagedBean
@SessionScoped
public class CalidadControl extends Control{

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private FormatoConcreto sump;
    private FormatoConcreto sumq;

    public CalidadControl() {
    }

    /**
     * Se cargan los datos de sump para ser presentados al usuario.
     * El parámetro indica si la solicitud se hace sobre la página de sump a fin de
     * validar la carga de Sumq
     * @param pagSump
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public void cargarSump(boolean pagSump) throws ExceptionFatal, ExceptionWarn {
        try{
            this.sump = formatoFachada.cargarFormato("sump");
            if (!this.sump.estaGuardado()) {
                if (pagSump) this.cargarSumq(false);
                fabricaAtributos.crearAtributosSump(this.sump, this.sumq);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("CalidadControl no puede cargar SUMP. " + e.getMessage());
        }
    }

    /**
     * Se cargan los datos de sumq para ser presentados al usuario.
     * El parámetro indica si la solicitud se hace sobre la página de sumq a fin de
     * validar la carga de Sump
     * @param pagSumq
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public void cargarSumq(boolean pagSumq) throws ExceptionFatal, ExceptionWarn {
        try{
            this.sumq = formatoFachada.cargarFormato("sumq");
            if (!this.sumq.estaGuardado()) {
                if (pagSumq) this.cargarSump(false);
                fabricaAtributos.crearAtributosSumq(this.sumq,this.sump);
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("CalidadControl no puede cargar SUMQ. " + e.getMessage());
        }
    }
    
    /**
     * Permite guardar elformato SUMP en forma persistente
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public void guardarFormatoSump() throws ExceptionFatal, ExceptionWarn {
        try{
            if(this.sump.estaGuardado()){
                FormatoConcreto sumpAux = formatoFachada.crearFormato("sump");
                FormatoConcreto sumqAux = formatoFachada.cargarFormato("sumq");
                fabricaAtributos.crearAtributosSumq(sumqAux,this.sump);
                fabricaAtributos.crearAtributosSump(sumpAux,sumqAux);
                consolidarDatosSump(this.sump,sumpAux);
            }
            formatoFachada.actualizarFormato(this.sump);
        }catch(Exception e){
            throw new ExceptionFatal("CalidadControl no puede guardar el formato SUMP. " + e.getMessage());
        }
    }
    
    /**
     * Permite guardar elformato SUMQ en forma persistente
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public void guardarFormatoSumq() throws ExceptionFatal, ExceptionWarn {
        try{
            if(this.sumq.estaGuardado()){
                FormatoConcreto sumqAux = formatoFachada.crearFormato("sumq");
                FormatoConcreto sumpAux = formatoFachada.cargarFormato("sump");
                fabricaAtributos.crearAtributosSump(sumpAux, this.sumq);
                fabricaAtributos.crearAtributosSumq(sumqAux,sumpAux);
                consolidarDatosSumq(this.sumq,sumqAux);
            }
            formatoFachada.actualizarFormato(this.sumq);
        }catch(Exception e){
            throw new ExceptionFatal("CalidadControl no puede guardar el formato SUMQ. " + e.getMessage());
        }
    }

    /**
     * Permite unificar los datos ingresados por el usuario y los calculados por el sistema para el formato SUMP
     * @param sumpActual
     * @param sumpAux
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void consolidarDatosSump(FormatoConcreto sumpActual, FormatoConcreto sumpAux) throws ExceptionFatal, ExceptionWarn {
        try{
            sumpAux.getAtributo("Tamaño del producto").setAtributos(sumpActual.getAtributo("Tamaño del producto").getAtributos());
            sumpAux.getAtributo("Resumen").getSubAtributo("Código/hora").setAtributos(sumpActual.getAtributo("Resumen").getSubAtributo("Código/hora").getAtributos());
            sumpAux.getAtributo("Resumen").getSubAtributo("Porcentaje reuso").setAtributos(sumpActual.getAtributo("Resumen").getSubAtributo("Porcentaje reuso").getAtributos());
            sumpActual.setAtributos(sumpAux.getAtributos());
        }catch(Exception e){
            throw new ExceptionFatal("No se pueden consolidar los atributos SUMP. " + e.getMessage());
        }
    }

    /**
     * Permite unificar los datos ingresados por el usuario y los calculados por el sistema para el formato SUMQ
     * @param sumqActual
     * @param sumqAux
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void consolidarDatosSumq(FormatoConcreto sumqActual, FormatoConcreto sumqAux) throws ExceptionFatal, ExceptionWarn {
        try{
            List rendimientosFaseActual = sumqActual.getAtributo("Rendimientos de fase").getAtributos();
            List rendimientosFaseAux = sumqAux.getAtributo("Rendimientos de fase").getAtributos();
            int numAtrsRendimientoFase = rendimientosFaseActual.size();
            for(int i = 0; i < numAtrsRendimientoFase; i++)
                ((AtributoCompuesto)rendimientosFaseAux.get(i)).getSubAtributo("Plan").setValor(((AtributoCompuesto)rendimientosFaseActual.get(i)).getSubAtributo("Plan").getValor());
            List tasaInyeccionDefectosActual = sumqActual.getAtributo("Tasa de inyección de defectos").getAtributos();
            List tasaInyeccionDefectosAux = sumqAux.getAtributo("Tasa de inyección de defectos").getAtributos();
            int numAtrsTasaInyecDefectos = tasaInyeccionDefectosActual.size();
            for(int i = 0; i < numAtrsTasaInyecDefectos; i++)
                ((AtributoCompuesto)tasaInyeccionDefectosAux.get(i)).getSubAtributo("Plan").setValor(((AtributoCompuesto)tasaInyeccionDefectosActual.get(i)).getSubAtributo("Plan").getValor());
            sumqActual.setAtributos(sumqAux.getAtributos());
        }catch(Exception e){
            throw new ExceptionFatal("No se pueden consolidar los atributos SUMQ. " + e.getMessage());
        }
    }

    /**
     * Los cambios realizados sobre algunos datos SUMP requieren la actualización de otros, dependiente de éste.
     * @param atrCodigo
     * @throws e_utilitaria.ExceptionFatal
     * @throws e_utilitaria.ExceptionWarn
     */
    public void totalizarCodigoSump(AtributoCompuesto atrCodigo) throws ExceptionFatal,ExceptionWarn {
        try{
            AtributoCompuesto atrTamanioProducto = this.sump.getAtributo("Tamaño del producto");
            AtributoCompuesto atrCodigoTotalNuevReut = atrTamanioProducto.getSubAtributo("Código total nuevo y reutilizado (N)");
            AtributoCompuesto atrCodigoTotal = atrTamanioProducto.getSubAtributo("Código total (T)");
            AtributoCompuesto codBase = atrTamanioProducto.getSubAtributo("Código base (B)");
            AtributoCompuesto codEliminado = atrTamanioProducto.getSubAtributo("Código eliminado (D)");
            AtributoCompuesto atrModificado = atrTamanioProducto.getSubAtributo("Código modificado (M)");
            AtributoCompuesto atrAniadido = atrTamanioProducto.getSubAtributo("Código añadido (A)");
            AtributoCompuesto atrResumen_Reuso = atrTamanioProducto.getSubAtributo("Código reutilizado (R)");
            atrCodigoTotalNuevReut.getSubAtributo("Plan").setValor(Double.toString(Helper.extraerNumero(atrModificado.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atrAniadido.getSubAtributo("Plan").getValor())));
            atrCodigoTotalNuevReut.getSubAtributo("Real").setValor(Double.toString(Helper.extraerNumero(atrModificado.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atrAniadido.getSubAtributo("Real").getValor())));
            double totalCodigoPlan = Helper.extraerNumero(atrCodigoTotalNuevReut.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(codBase.getSubAtributo("Plan").getValor()) - Helper.extraerNumero(codEliminado.getSubAtributo("Plan").getValor()) - Helper.extraerNumero(atrModificado.getSubAtributo("Plan").getValor()) + Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Plan").getValor());
            double totalCodigoReal = Helper.extraerNumero(atrCodigoTotalNuevReut.getSubAtributo("Real").getValor()) + Helper.extraerNumero(codBase.getSubAtributo("Real").getValor()) - Helper.extraerNumero(codEliminado.getSubAtributo("Real").getValor()) - Helper.extraerNumero(atrModificado.getSubAtributo("Real").getValor()) + Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Real").getValor());
            atrCodigoTotal.getSubAtributo("Plan").setValor(Double.toString(totalCodigoPlan));
            atrCodigoTotal.getSubAtributo("Real").setValor(Double.toString(totalCodigoReal));
            
            AtributoCompuesto totalTiempoFase = this.sump.getAtributo("Tiempo en fase (horas)").getSubAtributo("Total");
            AtributoCompuesto atrResumen = this.sump.getAtributo("Resumen");
            AtributoCompuesto atrCodHora = atrResumen.getSubAtributo("Código/hora");
            double totalTiempoFasePlan = Helper.extraerNumero(totalTiempoFase.getSubAtributo("Plan").getValor());
            double totalTiempoFaseReal = Helper.extraerNumero(totalTiempoFase.getSubAtributo("Real").getValor());
            
            if(totalTiempoFasePlan == 0) atrCodHora.getSubAtributo("Plan").setValor("0");
            else atrCodHora.getSubAtributo("Plan").setValor(Double.toString(Helper.extraerNumero(atrCodigoTotalNuevReut.getSubAtributo("Plan").getValor())/totalTiempoFasePlan));
            if(totalTiempoFaseReal == 0) atrCodHora.getSubAtributo("Real").setValor("0");
            else atrCodHora.getSubAtributo("Real").setValor(Double.toString(Helper.extraerNumero(atrCodigoTotalNuevReut.getSubAtributo("Real").getValor())/totalTiempoFaseReal));            
            AtributoCompuesto atrPorcentajeReuso = atrResumen.getSubAtributo("Porcentaje reuso");
            if(totalCodigoPlan == 0) atrPorcentajeReuso.getSubAtributo("Plan").setValor("0");
            else atrPorcentajeReuso.getSubAtributo("Plan").setValor(Double.toString(Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Plan").getValor())/totalCodigoPlan));
            if(totalCodigoReal == 0) atrPorcentajeReuso.getSubAtributo("Real").setValor("0");
            else atrPorcentajeReuso.getSubAtributo("Real").setValor(Double.toString(Helper.extraerNumero(atrResumen_Reuso.getSubAtributo("Real").getValor())/totalCodigoReal));
        }catch(Exception e){
            throw new ExceptionFatal("CalidadControl no puede totalizar el código. " + e.getMessage());
        }
    }
    
    public FormatoConcreto getSump() {
        return sump;
    }

    public void setSump(FormatoConcreto sump) {
        this.sump = sump;
    }

    public FormatoConcreto getSumq() {
        return sumq;
    }

    public void setSumq(FormatoConcreto sumq) {
        this.sumq = sumq;
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