/***
 * Controlador para el formato LOGD
 */
package b_controlador.a_gestion;

import b_controlador.b_fachada.FormatoFachada;
import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Opciontsp;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import d_datos.a_dao.ProyectoDAO;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class DefectosControl extends Control{

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private FormatoConcreto logd;
    private AtributoCompuesto registroLogd;

    //Cadena auxiliar para consultar el logd correspondiente a esa parte
    private String parteFiltro;

    public DefectosControl() {
    }

    /**
     * A partir de parteFiltro consulta o crea una instancia del formato
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarLogdFiltroParte() throws ExceptionFatal, ExceptionWarn {
        try {
            this.logd = formatoFachada.cargarFormato("logd","parte",this.parteFiltro);
            List atributos = this.logd.getAtributos();
            if (atributos.isEmpty()) {
                atributos.add(new AtributoCompuesto("parte", this.parteFiltro, null));
                atributos.add(new AtributoCompuesto("contenido iterable", new ArrayList()));
            }
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("DefectosControl no pudo cargar el formato logd. " + e.getMessage());
        }
    }

    /**
     * Indica si el autor de un registro logd tiene por autor al usuario que está en sesión
     * @param atributoAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean esAutor(AtributoCompuesto atributoAutor) throws ExceptionFatal, ExceptionWarn {
        try {
            return ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion().equals(atributoAutor.getValor());
        } catch (Exception e) {
            throw new ExceptionFatal("DefectosControl no pudo determinar el autor de los registros logd. " + e.getMessage());
        }
    }

    /**
     * Agrega el registro logd con los datos que ha ingresado el usuario y valores automáticos
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void agregarRegistroLogd() throws ExceptionFatal, ExceptionWarn {
        try {
            if (registroLogdValido(this.registroLogd)) {
                cargarLogdFiltroParte();
                this.registroLogd.getSubAtributo("fecha").setValor(Helper.formatearDate(new Date(), "dd/M/yyyy"));
                this.registroLogd.getSubAtributo("numero").setValor(Integer.toString(numeroUltimoDefecto() + 1));
                this.logd.getAtributo("contenido iterable").getAtributos().add(this.registroLogd);
                formatoFachada.actualizarFormato(this.logd);
            }else
                throw new ExceptionWarn("Todos los campos deben ser dilifenciados a excepción de 'defecto arreglado'");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("DefectosControl no pudo agregar el registro logd. " + e.getMessage());
        }
    }

    /**
     * Permite la actualización del formato logd de acuerdo a los cambios que reciba
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarLogd() throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atributo;
            List atributos = this.logd.getAtributo("contenido iterable").getAtributos();
            int t = atributos.size();
            String numero = null;
            boolean referenciaAutonoma;
            AtributoCompuesto defectoArreglado;
            boolean registroIncompleto;
            if (t == 0)
                throw new ExceptionWarn("El formato LOGD no tiene ningún registro; no puede ser actualizado");
            if (this.parteFiltro != null) {
                referenciaAutonoma = false;
                registroIncompleto = false;
                for (int i = 0; i < t; i++) {
                    atributo = (AtributoCompuesto) atributos.get(i);
                    numero = atributo.getSubAtributo("numero").getValor();
                    defectoArreglado = atributo.getSubAtributo("defecto arreglado");
                    if (defectoArreglado.getValor() != null)
                        if (defectoArreglado.getValor().equals(numero)) {
                            referenciaAutonoma = true;
                            break;
                        }
                    if (!registroLogdValido(atributo)) {
                        registroIncompleto = true;
                        break;
                    }
                }
                if (!referenciaAutonoma) {
                    if (!registroIncompleto) {
                        formatoFachada.actualizarFormato(this.logd);
                    } else throw new ExceptionWarn("El defecto " + numero + " debe ser llenado por completo");
                } else throw new ExceptionWarn("El defecto " + numero + " no puede referenciarse a sí mismo como defecto arreglado");
            } else throw new ExceptionWarn("Seleccione una parte válida");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("DefectosControl no puede actualizar el formato logd. " + e.getMessage());
        }
    }

    /**
     * Indica el número de defecto de mayor denominiación registrada en el formato
     * @return 
     */
    private int numeroUltimoDefecto() {
        try {
            List atributos = formatoFachada.cargarFormato("logd","parte", this.parteFiltro).getAtributos();
            if (atributos.isEmpty()) return 0;
            int t = atributos.size();
            AtributoCompuesto atributo;
            for (int i = 0; i < t; i++){
                atributo = (AtributoCompuesto) atributos.get(i);
                if (atributo.getAtributo().equals("contenido iterable")) {
                    atributos = atributo.getAtributos();
                    break;
                }
            }
            t = atributos.size();
            int max = 0;
            int num;
            for (int i = 0; i < t; i++) {
                num = (int) Helper.extraerNumero(((AtributoCompuesto) atributos.get(i)).getSubAtributo("numero").getValor());
                if (num > max) max = num;
            }
            return max;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Carga la información necesaria para el nuevo registro logd
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalNuevoRegistroLogd() throws ExceptionFatal, ExceptionWarn {
        try {
            if (this.parteFiltro != null) {
                this.registroLogd = fabricaAtributos.crearAtributosLogd();
                this.registroLogd.getSubAtributo("autor").setValor(((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            } else
                throw new ExceptionWarn("Seleccione una parte válida");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("DefectosControl no puede cargar el modal para el nuevo registro logd. " + e.getMessage());
        }
    }

    /**
     * Brinda una lista con todos los números de defecto existentes en el formato logd
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List getNumerosDefecto() throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = formatoFachada.cargarFormato("logd","parte", this.parteFiltro).getAtributos();
            if (atributos.isEmpty()) return null;
            List numeros = new ArrayList();
            int t = atributos.size();
            AtributoCompuesto atributo;
            for (int i = 0; i < t; i++) {
                atributo = (AtributoCompuesto) atributos.get(i);
                if (atributo.getAtributo().equals("contenido iterable")) {
                    atributos = atributo.getAtributos();
                    break;
                }
            }
            t = atributos.size();
            for (int i = 0; i < t; i++)
                numeros.add(((AtributoCompuesto) atributos.get(i)).getSubAtributo("numero").getValor());
            return numeros;
        } catch (Exception e) {
            throw new ExceptionFatal("DefectosControl falló al localizar las opciones de número de defecto. " + e.getMessage());
        }
    }

    /**
     * Indica si el registro a crear por el usuario contiene todos los valores mínimos requeridos
     * @param atributo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean registroLogdValido(AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            String tipo = atributo.getSubAtributo("tipo").getValor();
            String etapaInyeccion = atributo.getSubAtributo("etapa inyección").getValor();
            String etapaRemocion = atributo.getSubAtributo("etapa remoción").getValor();
            String tiempoArreglo = atributo.getSubAtributo("tiempo arreglo").getValor();
            String descripcion = atributo.getSubAtributo("descripción").getValor();
            if (tipo == null) return false;
            else if (tipo.trim().equals("")) return false;
            if (etapaInyeccion == null) return false;
            else if (etapaInyeccion.trim().equals("")) return false;
            if (etapaRemocion == null) return false;
            else if (etapaRemocion.trim().equals("")) return false;
            if (tiempoArreglo == null)return false;
            else if (tiempoArreglo.trim().equals("")) return false;
            if (descripcion == null) return false;
            else if (descripcion.trim().equals("")) return false;
            return true;
        }catch(Exception e){
            throw new ExceptionFatal("Error al validar el registro LOGD. " + e.getMessage());
        }
    }

    /**
     * Identifica cuál es la posición que ocupa la etapa de inyección y remoción respectivamente en la lista de etapas TSP                        
     * @param etapaInyeccion
     * @param etapaRemocion
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int[] posEtapaDefectos(String etapaInyeccion, String etapaRemocion) throws ExceptionFatal, ExceptionWarn {
        try{
            int[] r = new int[2];
            r[0] = -1;
            r[1] = -1;
            int t = TSP.etapas.size();
            String etapa;
            for(int i = 2; i < t - 2; i++){
                etapa =(String) TSP.etapas.get(i);
                if(etapa.equals(etapaInyeccion))
                    r[0] = i - 2;
                if(etapa.equals(etapaRemocion))
                    r[1] = i - 2;
            }
            return r;
        }catch(Exception e){
            throw new ExceptionFatal("Error al obtener la posición la etapa del defecto. " + e.getMessage());
        }
    }
    
    /**
     * Consolida la información sobre la cantidad de defectos inyectados y removidos, por cada etapa, respectivamente.
     * @param atrDefectosInyectados
     * @param atrDefectosRemovidos
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void consolidarDefectos(AtributoCompuesto atrDefectosInyectados, AtributoCompuesto atrDefectosRemovidos) throws ExceptionFatal, ExceptionWarn{
        try{
            List formatosLogdCiclo = formatoFachada.consultarFormatos("logd");
            int numFormatosLogdCiclo = formatosLogdCiclo.size();
            int numItemsDefecto = TSP.etapas.size() - 4;
            AtributoCompuesto atr;
            List<AtributoCompuesto> atributosIterables;
            int numAtributosFormato;
            int[] defectosInyectados = new int[numItemsDefecto];
            int[] defectosRemovidos = new int[numItemsDefecto];
            int totalDeftsInyectados = 0;
            int totalDeftsRemovidos = 0;
            double totalPorcentajeDefectsInyectds = 0;
            double totalPorcentajeDefectsRemovidos = 0;
            
            int[] posiciones;
            for(int i = 0; i < numFormatosLogdCiclo; i++){
                 atributosIterables = ((FormatoConcreto) formatosLogdCiclo.get(i)).getAtributo("contenido iterable").getAtributos();
                 numAtributosFormato = atributosIterables.size();
                 for(int j = 0; j < numAtributosFormato; j++){
                     atr = (AtributoCompuesto) atributosIterables.get(j);
                     posiciones = posEtapaDefectos(atr.getSubAtributo("etapa inyección").getValor(),atr.getSubAtributo("etapa remoción").getValor());
                     if(posiciones[0] >= 0) defectosInyectados[posiciones[0]]+=1;
                     if(posiciones[1] >= 0) defectosRemovidos[posiciones[1]]+=1;
                 }
            }
            List atrsDefectosInyectados = atrDefectosInyectados.getAtributos();
            List atrsDefectosRemovidos = atrDefectosRemovidos.getAtributos();
            for(int i = 0; i < numItemsDefecto; i++){
                ((AtributoCompuesto)atrsDefectosInyectados.get(i)).getSubAtributo("Real").setValor(Integer.toString(defectosInyectados[i]));
                ((AtributoCompuesto)atrsDefectosRemovidos.get(i)).getSubAtributo("Real").setValor(Integer.toString(defectosRemovidos[i]));
                totalDeftsInyectados += defectosInyectados[i];
                totalDeftsRemovidos += defectosRemovidos[i];
            }
            ((AtributoCompuesto)atrsDefectosInyectados.get(numItemsDefecto)).getSubAtributo("Real").setValor(Integer.toString(totalDeftsInyectados));
            ((AtributoCompuesto)atrsDefectosRemovidos.get(numItemsDefecto)).getSubAtributo("Real").setValor(Integer.toString(totalDeftsRemovidos));

            if(totalDeftsInyectados != 0)
                for(int i = 0; i < numItemsDefecto; i++){
                    ((AtributoCompuesto)atrsDefectosInyectados.get(i)).getSubAtributo("Real porcentaje").setValor(Double.toString(((double)defectosInyectados[i]/totalDeftsInyectados)*100));
                    totalPorcentajeDefectsInyectds += ((double)defectosInyectados[i]/totalDeftsInyectados)*100;
                }
            if(totalDeftsRemovidos != 0)
                for(int i = 0; i < numItemsDefecto; i++){
                    ((AtributoCompuesto)atrsDefectosRemovidos.get(i)).getSubAtributo("Real porcentaje").setValor(Double.toString(((double)defectosRemovidos[i]/totalDeftsRemovidos)*100));
                    totalPorcentajeDefectsRemovidos += ((double)defectosRemovidos[i]/totalDeftsRemovidos)*100;
                }
            ((AtributoCompuesto)atrsDefectosInyectados.get(numItemsDefecto)).getSubAtributo("Real porcentaje").setValor(Double.toString(totalPorcentajeDefectsInyectds));
            ((AtributoCompuesto)atrsDefectosRemovidos.get(numItemsDefecto)).getSubAtributo("Real porcentaje").setValor(Double.toString(totalPorcentajeDefectsRemovidos));
        }catch(Exception e){
            throw new ExceptionFatal("DefectosControl no puede consolidar los defectos. " + e.getMessage());
        }
    }

    /**
     * Calcula el porcentaje libre de defectos para el formato SUMP
     * @param atributosPDF
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void pdf(List atributosPDF) throws ExceptionFatal, ExceptionWarn {
        try{
            List formatosLogdCiclo = formatoFachada.consultarFormatos("logd");
            int numFormatosLogdCiclo = formatosLogdCiclo.size();
            List<AtributoCompuesto> atributosIterables;
            int numAtributosFormato;
            boolean compilacionEncontrada;
            boolean pruebaUnidadEncontrada;
            boolean constIntegraEncontrada;
            boolean pruebaSistemaEncontrada;
            double []numsPartesDefectos = new double[4];
            numsPartesDefectos[0] = 0;
            numsPartesDefectos[1] = 0;
            numsPartesDefectos[2] = 0;
            numsPartesDefectos[3] = 0;
            String etapaObtenida;
            for(int i = 0; i < numFormatosLogdCiclo; i++){
                 atributosIterables = ((FormatoConcreto) formatosLogdCiclo.get(i)).getAtributo("contenido iterable").getAtributos();
                 numAtributosFormato = atributosIterables.size();
                 compilacionEncontrada = pruebaUnidadEncontrada = constIntegraEncontrada = pruebaSistemaEncontrada = false;
                 for(int j = 0; j < numAtributosFormato; j++){
                    etapaObtenida = ((AtributoCompuesto) atributosIterables.get(j)).getSubAtributo("etapa inyección").getValor();
                    if(etapaObtenida.equals((String)TSP.etapas.get(15))){
                        compilacionEncontrada = true;
                    }else if(etapaObtenida.equals((String)TSP.etapas.get(17))){
                        pruebaUnidadEncontrada = true;
                    }else if(etapaObtenida.equals((String)TSP.etapas.get(18))){
                        constIntegraEncontrada = true;
                    }else if(etapaObtenida.equals((String)TSP.etapas.get(19))){
                        pruebaSistemaEncontrada = true;
                    }
                 }
                if(compilacionEncontrada) numsPartesDefectos[0] += 1;
                if(pruebaUnidadEncontrada) numsPartesDefectos[1] += 1;
                if(constIntegraEncontrada) numsPartesDefectos[2] += 1;
                if(pruebaSistemaEncontrada) numsPartesDefectos[3] += 1;
            }
            if(numFormatosLogdCiclo != 0){
                numsPartesDefectos[0] = ((double)numsPartesDefectos[0]/numFormatosLogdCiclo)*100;
                numsPartesDefectos[1] = ((double)numsPartesDefectos[1]/numFormatosLogdCiclo)*100;
                numsPartesDefectos[2] = ((double)numsPartesDefectos[2]/numFormatosLogdCiclo)*100;
                numsPartesDefectos[3] = ((double)numsPartesDefectos[3]/numFormatosLogdCiclo)*100;
            }
            for(int i = 0; i < 4; i++)
                ((AtributoCompuesto)atributosPDF.get(i)).getSubAtributo("Real").setValor(Double.toString(100 - numsPartesDefectos[i]));
        }catch(Exception e){
            throw new ExceptionFatal("DefectosControl no puede calcular PDF. " + e.getMessage());
        }
    }

    public FormatoConcreto getLogd() {
        return logd;
    }

    public void setLogd(FormatoConcreto logd) {
        this.logd = logd;
    }

    public String getParteFiltro() {
        return parteFiltro;
    }

    public void setParteFiltro(String parteFiltro) {
        this.parteFiltro = parteFiltro;
    }

    public List getEtapas() {
        return new ArrayList(TSP.etapas.subList(2, TSP.etapas.size() - 2));
    }

    public List getDefectos() throws ExceptionFatal{
        try{
        List defectos = ProyectoDAO.consultarOpcionesTSP("defecto");
        if(defectos.isEmpty()){
            int t = TSP.defectos.size();
            Opciontsp o;
            for(int i = 0; i < t; i++){
                o = new Opciontsp();
                o.setNombre((String)TSP.defectos.get(i));
                o.setTipo("defecto");
                defectos.add(o);
            }
            ProyectoDAO.insertarOpcionesTSP(defectos);
            return TSP.defectos;   
        }
        return defectos;
        }catch(Exception e){
            throw new ExceptionFatal("DefectosControl no puede obtener los defectos TSP");
        }
    }

    public AtributoCompuesto getRegistroLogd() {
        return registroLogd;
    }

    public void setRegistroLogd(AtributoCompuesto registroLOGD) {
        this.registroLogd = registroLOGD;
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