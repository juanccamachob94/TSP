/**
 * Permite comparar dos códigos: el de antes y el de después. Se necesitan los
 * archivos a subir, los archivos a crear, las dos listas con la información
 * capturada de las funciones y la lista de funciones que almacena la
 * información de si la función fue [eliminada,modificada,agregada, igual] y los
 * comentarios al respecto
 */
package b_controlador.a_gestion;

import e_utilitaria.GestorArchivos;
import b_controlador.b_fachada.FormatoFachada;
import c_negocio.c_otros.RevisorCodigo;
import b_controlador.c_fabricas.c_fabrica_revisorescodigo.FabricaRevisoresCodigo;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class CodigoControl extends Control {

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;

    private File archivoAntes;
    private File archivoDespues;

    private UploadedFile archivoSubidoAntes;
    private UploadedFile archivoSubidoDespues;

    private List archivosAntes;
    private List archivosDespues;

    private boolean codigoAntesValido;
    private boolean codigoDespuesValido;

    private FormatoConcreto formatoComparacionCodigo;

    private int numAgregados;
    private int numIguales;
    private int numEliminados;
    private int numModificados;

    public CodigoControl() {
    }

    /**
     * Reinicia las variables de archivo para brindar otro análisis
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    private void reestablecerRevisionCodigo() throws ExceptionFatal, ExceptionWarn {
        try {
            this.archivoAntes = null;
            this.archivoDespues = null;
            this.archivoSubidoAntes = null;
            this.archivoSubidoDespues = null;
            this.archivosAntes = null;
            this.archivosDespues = null;
            this.codigoAntesValido = false;
            this.codigoDespuesValido = false;
            this.numAgregados = 0;
            this.numIguales = 0;
            this.numEliminados = 0;
            this.numModificados = 0;
        } catch (Exception e) {
            throw new ExceptionFatal("CodigoControl no puede reestablecer las variables para el análisis de código. " + e.getMessage());
        }
    }

    /**
     * Reinicia las variables para brindar el primer análisis
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void reiniciarVariables() throws ExceptionFatal, ExceptionWarn {
        try {
            reestablecerRevisionCodigo();
            this.formatoComparacionCodigo = null;
        } catch (Exception e) {
            throw new ExceptionFatal("CodigoControl no puede reiniciar las variables para el análisis de código. " + e.getMessage());
        }
    }

    /**
     * Construye un archivo en memoria con base en el archivo zip subido
     * (anterior)
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void subirCodigoAntes() throws ExceptionFatal, ExceptionWarn {
        try {
            if (GestorArchivos.extension(this.archivoSubidoAntes).equals("zip"))
                this.archivoAntes = GestorArchivos.construirArchivo(this.archivoSubidoAntes);
            else throw new ExceptionWarn("Sólo está permitido subir archivos de extensión zip");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CodigoControl no puede subir el código anterior. " + e.getMessage());
        }
    }

    /**
     * Construye un archivo en memoria con base en el archivo zip subido
     * (posterior)
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void subirCodigoDespues() throws ExceptionFatal, ExceptionWarn {
        try {
            if (GestorArchivos.extension(this.archivoSubidoDespues).equals("zip"))
                this.archivoDespues = GestorArchivos.construirArchivo(this.archivoSubidoDespues);
            else
                throw new ExceptionWarn("Sólo está permitido subir archivos de extensión zip");
        } catch (ExceptionWarn e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("CodigoControl no puede subir el código posterior. " + e.getMessage());
        }
    }

    /**
     * Llena la lista de archivosAntes con atributos de archivo con listas de
     * atributos de funciones
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void validarCodigoAntes() throws ExceptionFatal, ExceptionWarn {
        try {
            this.archivosAntes = new ArrayList();
            ZipFile archivoZip = new ZipFile(this.archivoAntes);
            Enumeration<? extends ZipEntry> entradasZip = archivoZip.entries();
            ZipEntry entradaZip;
            while (entradasZip.hasMoreElements()) {
                entradaZip = entradasZip.nextElement();
                filtrarArchivo(this.archivosAntes, GestorArchivos.lineasArchivo(new BufferedReader(new InputStreamReader(archivoZip.getInputStream(entradaZip)))), entradaZip.getName(), FabricaRevisoresCodigo.crearRevisorCodigo("java"));
            }
            this.codigoAntesValido = true;
        } catch (Exception e) {
            this.codigoAntesValido = false;
            this.archivoAntes = new File("");
            throw new ExceptionWarn("No se pudo validar el código del archivo anterior " + this.archivoAntes.getName() + ". " + e.getMessage());
        }
    }

    /**
     * Llena la lista de archivosDespues con atributos de archivo con listas de
     * atributos de funciones
     * @throws ExceptionFatal
     * @throws ExceptionWarn
     */
    public void validarCodigoDespues() throws ExceptionFatal, ExceptionWarn {
        try {
            this.archivosDespues = new ArrayList();
            ZipFile archivoZip = new ZipFile(this.archivoDespues);
            Enumeration<? extends ZipEntry> entradasZip = archivoZip.entries();
            ZipEntry entradaZip;
            while (entradasZip.hasMoreElements()) {
                entradaZip = entradasZip.nextElement();
                filtrarArchivo(this.archivosDespues, GestorArchivos.lineasArchivo(new BufferedReader(new InputStreamReader(archivoZip.getInputStream(entradaZip)))), entradaZip.getName(), FabricaRevisoresCodigo.crearRevisorCodigo("java"));
            }
            this.codigoDespuesValido = true;
        } catch (Exception e) {
            this.codigoDespuesValido = false;
            this.archivoDespues = new File("");
            throw new ExceptionWarn("No se pudo validar el código del archivo posterior " + this.archivoDespues.getName() + ". " + e.getMessage());
        }
    }

    /**
     * Valida que cada elemento, dentro del zip, a revisar, no sea una carpeta,
     * no contenga elementos abstractos y posea las extensiones permitidas
     * @param archivos
     * @param lineasArchivo
     * @param nombreEntradaZip
     * @param rc
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void filtrarArchivo(List archivos, List lineasArchivo, String nombreEntradaZip, RevisorCodigo rc) throws ExceptionFatal, ExceptionWarn {
        try {
            if (!nombreEntradaZip.endsWith("/") && !rc.esAbstracta(lineasArchivo) && rc.extensionCorrecta(nombreEntradaZip))
                agregarContenidoArchivo(archivos, nombreEntradaZip, lineasArchivo, rc);
        } catch (Exception e) {
            throw new ExceptionFatal("Imposible filtrar el archivo " + nombreEntradaZip + ". " + e.getMessage());
        }
    }

    /**
     * Agrega los atributos de función a la lista de atributos del archivo
     * @param archivos
     * @param nombreArchivo
     * @param lineasArchivo
     * @param rc
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void agregarContenidoArchivo(List archivos, String nombreArchivo, List<String> lineasArchivo, RevisorCodigo rc) throws ExceptionFatal, ExceptionWarn {
        try {
            List funcionesDeArchivo = new ArrayList();
            int t = lineasArchivo.size();
            AtributoCompuesto atrNumLinea;
            for (int i = 0; i < t; i++)
                if (rc.comienzaFuncion(lineasArchivo.get(i))) {
                    atrNumLinea = new AtributoCompuesto(Integer.toString(i));
                    funcionesDeArchivo.add(informacionFuncion(rc, i, lineasArchivo, atrNumLinea));
                    i = Integer.parseInt(atrNumLinea.getAtributo()) + 1;
                }
            if (!funcionesDeArchivo.isEmpty())
                archivos.add(new AtributoCompuesto(nombreArchivo, funcionesDeArchivo));
        } catch (Exception e) {
            throw new ExceptionFatal("No fue posible agregar el contenido del archivo. " + e.getMessage());
        }
    }

    /**
     * Devuelve un atributo con toda la información pertinente a la función que
     * da inicio en la línea i
     * @param rc
     * @param i
     * @param lineasArchivo
     * @param atrNumLinea
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private AtributoCompuesto informacionFuncion(RevisorCodigo rc, int i, List<String> lineasArchivo, AtributoCompuesto atrNumLinea) throws ExceptionFatal, ExceptionWarn {
        try {
            String contenido = rc.informacionFuncion(i, lineasArchivo);
            int numLineasFuncion = rc.getNumLineasFuncion();
            int tamFuncion = rc.getNumLineasRealCodigoFuncion();
            atrNumLinea.setAtributo(Integer.toString(i + numLineasFuncion));
            List atributos = new ArrayList();
            atributos.add(new AtributoCompuesto("nombre", rc.nombreFuncion(lineasArchivo.get(i)), null));
            atributos.add(new AtributoCompuesto("tamaño", Integer.toString(tamFuncion), null));
            atributos.add(new AtributoCompuesto("parámetros", rc.cadenaParametros(lineasArchivo.get(i)), null));
            atributos.add(new AtributoCompuesto("contenido", contenido, null));
            atributos.add(new AtributoCompuesto("estado", "p", null));//p = pendiente
            return new AtributoCompuesto(atributos);
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede obtener la información de la función. " + e.getMessage());
        }
    }

    /**
     * Realiza la comparación de las dos listas de archivos con base en los
     * atributos capturados de cada función
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void analizar() throws ExceptionFatal, ExceptionWarn {
        try {
            this.formatoComparacionCodigo = formatoFachada.crearFormato("Comparación de código");
            List atributos = this.formatoComparacionCodigo.getAtributos();
            List cantidades = new ArrayList();
            List archivos = new ArrayList();
            atributos.add(new AtributoCompuesto("cantidades", cantidades));
            atributos.add(new AtributoCompuesto("archivos", archivos));
            AtributoCompuesto atrArchivoAntes;
            AtributoCompuesto atrArchivoDespues;
            String nombreArchivoAntes;
            List resultadosArchivo;
            int tAntes = this.archivosAntes.size();
            int tDespues = this.archivosDespues.size();
            //Alude al hecho de que el mismo archivo, aunque modificado, aún existe en la versión "después"
            boolean archivoConservado;
            for (int i = 0; i < tAntes; i++) {
                resultadosArchivo = new ArrayList();
                atrArchivoAntes = (AtributoCompuesto) this.archivosAntes.get(i);
                nombreArchivoAntes = atrArchivoAntes.getAtributo();
                archivoConservado = false;
                for (int j = 0; j < tDespues; j++) {
                    atrArchivoDespues = (AtributoCompuesto) this.archivosDespues.get(j);
                    if (atrArchivoDespues.getAtributo().equals(nombreArchivoAntes)) {
                        analizarArchivoAntesDespues(atrArchivoAntes, atrArchivoDespues, resultadosArchivo);
                        archivoConservado = true;
                        break;
                    }
                }
                //Si el archivo no se encuentra, quiere decir que todas las funciones de éste fueron eliminadas
                if (!archivoConservado) {
                    resultadosArchivo = new ArrayList();
                    this.numEliminados += this.registrarFunciones(atrArchivoAntes.getAtributos(), resultadosArchivo, "eliminado");
                }
                archivos.add(new AtributoCompuesto(nombreArchivoAntes, resultadosArchivo));
            }
            //Una vez revisado lo de antes contra lo de ahora, hay que tomar los de ahora (después) no seleccionados y agregarlos como "nuevos"
            boolean archivoRegistrado;
            for (int i = 0; i < tDespues; i++) {
                archivoRegistrado = false;
                atrArchivoDespues = (AtributoCompuesto) this.archivosDespues.get(i);
                resultadosArchivo = listaDeArchivo(atrArchivoDespues.getAtributo());
                if (resultadosArchivo == null) resultadosArchivo = new ArrayList();
                else archivoRegistrado = true;
                this.numAgregados += this.registrarFunciones(atrArchivoDespues.getAtributos(), resultadosArchivo, "agregado");
                if (!archivoRegistrado && !resultadosArchivo.isEmpty()) archivos.add(new AtributoCompuesto(atrArchivoDespues.getAtributo(), resultadosArchivo));
            }
            cantidades.add(new AtributoCompuesto("Código igual", Integer.toString(this.numIguales), null));
            cantidades.add(new AtributoCompuesto("Código modificado", Integer.toString(this.numModificados), null));
            cantidades.add(new AtributoCompuesto("Código eliminado", Integer.toString(this.numEliminados), null));
            cantidades.add(new AtributoCompuesto("Código agregado", Integer.toString(this.numAgregados), null));

            this.reestablecerRevisionCodigo();
        } catch (Exception e) {
            throw new ExceptionFatal("CodigoControl no puede analizar los códigos. " + e.getMessage());
        }
    }

    /**
     * Devuelve la lista de atributos de funciones de un archivo
     * @param nombreArchivo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List listaDeArchivo(String nombreArchivo) throws ExceptionFatal, ExceptionWarn {
        try {
            List archivos = this.formatoComparacionCodigo.getAtributo("archivos").getAtributos();
            int t = archivos.size();
            AtributoCompuesto atResultado;
            for (int i = 0; i < t; i++) {
                atResultado = (AtributoCompuesto) archivos.get(i);
                if (atResultado.getAtributo().equals(nombreArchivo)) {
                    return atResultado.getAtributos();
                }
            }
            return null;
        } catch (Exception e) {
            throw new ExceptionFatal("CodigoControl no puede obtener la lista del archivo " + nombreArchivo + ". " + e.getMessage());
        }
    }

    /**
     * Compara los valores de dos atributos de función, antes y después, y el
     * resultado lo agrega a la lista de resultados
     * @param atrArchivoAntes
     * @param atrArchivoDespues
     * @param resultadosArchivo
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void analizarArchivoAntesDespues(AtributoCompuesto atrArchivoAntes, AtributoCompuesto atrArchivoDespues, List resultadosArchivo) throws ExceptionFatal, ExceptionWarn {
        try {
            AtributoCompuesto atrFuncionAntes;
            AtributoCompuesto atrFuncionDespues;
            String nombreFuncionAntes;
            String parametrosFuncionAntes;
            String contenidoFuncionAntes;
            String tamanioFuncionAntes;
            int numParametrosAntes;
            int numParametrosDespues;
            int numParametrosCoincidente;
            int idCoincidenteSeleccionado;
            List atributosFuncionesCoincidentes;
            AtributoCompuesto atrFuncionCoincidente;
            List atrsAntes = atrArchivoAntes.getAtributos();
            int tAntes = atrsAntes.size();
            List atrsDespues = atrArchivoDespues.getAtributos();
            int tDespues = atrsDespues.size();
            List atributosResultadoFuncion;
            int tCoincidentes;

            for (int i = 0; i < tAntes; i++) {
                atrFuncionAntes = (AtributoCompuesto) atrsAntes.get(i);
                nombreFuncionAntes = atrFuncionAntes.getSubAtributo("nombre").getValor();
                parametrosFuncionAntes = atrFuncionAntes.getSubAtributo("parámetros").getValor();
                contenidoFuncionAntes = atrFuncionAntes.getSubAtributo("contenido").getValor();
                tamanioFuncionAntes = atrFuncionAntes.getSubAtributo("tamaño").getValor();
                atributosFuncionesCoincidentes = new ArrayList();
                for (int j = 0; j < tDespues; j++) {
                    atrFuncionDespues = (AtributoCompuesto) atrsDespues.get(j);
                    if (atrFuncionDespues.getSubAtributo("nombre").getValor().equals(nombreFuncionAntes)) {
                        if (!atrFuncionDespues.getSubAtributo("estado").getValor().equals("s"))//sólo aquellos que no han sido seleccionados
                        {
                            atributosFuncionesCoincidentes.add(atrFuncionDespues);
                        }
                    }
                }
                //Si la lista está vacía quiere decir que fue eliminado
                tCoincidentes = atributosFuncionesCoincidentes.size();
                if (tCoincidentes == 0) {
                    atributosResultadoFuncion = new ArrayList();
                    atributosResultadoFuncion.add(new AtributoCompuesto("estado", "eliminado", null));
                    atributosResultadoFuncion.add(new AtributoCompuesto("comentarios", null, null));
                    this.numEliminados += 1;
                } else {
                    //Si sólo hay 1 elemento, entonces se considera que es el mismo, aún si los parámetros son diferentes
                    if (tCoincidentes == 1) {
                        //Hay que rectificar si es lo mismo o está cambiada la función 
                        atrFuncionCoincidente = (AtributoCompuesto) atributosFuncionesCoincidentes.get(0);
                        //¿Le cambiaron los parámetros?
                        atributosResultadoFuncion = new ArrayList();
                        llenarAtributosResultadoFuncion(parametrosFuncionAntes, contenidoFuncionAntes, tamanioFuncionAntes, atrFuncionCoincidente, atributosResultadoFuncion);
                        //Se cambia el estado del atributo posterior coincidente a seleccionado
                        atrFuncionCoincidente.getSubAtributo("estado").setValor("s");
                    } else {
                        //Se elige al que está más cerca de ser la misma función
                        idCoincidenteSeleccionado = 0;
                        numParametrosCoincidente = 1000;//El número más grande "posible"
                        numParametrosAntes = Helper.dividir(parametrosFuncionAntes, " ").length;
                        for (int j = 0; j < tCoincidentes; j++) {
                            atrFuncionCoincidente = (AtributoCompuesto) atributosFuncionesCoincidentes.get(j);
                            numParametrosDespues = Helper.dividir(atrFuncionCoincidente.getSubAtributo("parámetros").getValor(), " ").length;
                            if (Math.abs(numParametrosDespues - numParametrosAntes) < numParametrosCoincidente) {
                                numParametrosCoincidente = numParametrosDespues;
                                idCoincidenteSeleccionado = j;
                            }
                        }
                        atrFuncionCoincidente = (AtributoCompuesto) atributosFuncionesCoincidentes.get(idCoincidenteSeleccionado);
                        atributosResultadoFuncion = new ArrayList();
                        llenarAtributosResultadoFuncion(parametrosFuncionAntes, contenidoFuncionAntes, tamanioFuncionAntes, atrFuncionCoincidente, atributosResultadoFuncion);
                        //Se cambia el estado del atributo posterior coincidente a seleccionado
                        atrFuncionCoincidente.getSubAtributo("estado").setValor("s");
                    }
                }
                resultadosArchivo.add(new AtributoCompuesto(nombreFuncionAntes, atributosResultadoFuncion));
                //Se cambia el estado del atributo anterior a seleccionado
                atrFuncionAntes.getSubAtributo("estado").setValor("s");
            }
        } catch (Exception e) {
            if (atrArchivoAntes != null && atrArchivoDespues != null) {
                throw new ExceptionFatal("CodigoControl no puede comparar los archivos: " + atrArchivoAntes.getAtributo() + " y " + atrArchivoDespues.getAtributo() + ". " + e.getMessage());
            }
            throw new ExceptionFatal("CodigoControl no puede comparar los archivos. " + e.getMessage());
        }
    }

    /**
     * Registra todos los atributos de un archivo al resultado con base en el
     * estado, si es modificado o eliminado
     * @param atributosArchivo
     * @param resultadosArchivo
     * @param estado
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int registrarFunciones(List atributosArchivo, List resultadosArchivo, String estado) throws ExceptionFatal, ExceptionWarn {
        try {
            List resultadosFuncion;
            List atributosResultadoFuncion;
            int tFunciones = atributosArchivo.size();
            AtributoCompuesto atrEstadoFuncion;
            AtributoCompuesto atrFuncion;
            int numEstado = 0;
            if (estado.equals("agregado")) {
                for (int i = 0; i < tFunciones; i++) {
                    atrFuncion = (AtributoCompuesto) atributosArchivo.get(i);
                    atrEstadoFuncion = atrFuncion.getSubAtributo("estado");
                    if (atrEstadoFuncion.getValor().equals("p")) {
                        atributosResultadoFuncion = new ArrayList();
                        atributosResultadoFuncion.add(new AtributoCompuesto("estado", estado, null));
                        atributosResultadoFuncion.add(new AtributoCompuesto("comentarios", null, null));
                        numEstado += 1;
                        resultadosArchivo.add(new AtributoCompuesto(atrFuncion.getSubAtributo("nombre").getValor(), atributosResultadoFuncion));
                        atrEstadoFuncion.setValor("s");
                    }
                }
            } else
                for (int i = 0; i < tFunciones; i++) {
                    atrFuncion = (AtributoCompuesto) atributosArchivo.get(i);
                    atributosResultadoFuncion = new ArrayList();
                    atributosResultadoFuncion.add(new AtributoCompuesto("estado", estado, null));
                    atributosResultadoFuncion.add(new AtributoCompuesto("comentarios", null, null));
                    numEstado += 1;
                    resultadosArchivo.add(new AtributoCompuesto(atrFuncion.getSubAtributo("nombre").getValor(), atributosResultadoFuncion));
                    atrFuncion.getSubAtributo("estado").setValor("s");
                }
            return numEstado;
        } catch (Exception e) {
            throw new ExceptionFatal("No se pueden registrar las funciones. " + e.getMessage());
        }
    }

    /**
     * Agrega un nuevo atributo de resultado de análisis para los casos en que
     * una función haya sido modificada o permanezca igual
     * @param parametrosFuncionAntes
     * @param contenidoFuncionAntes
     * @param tamanioFuncionAntes
     * @param atrFuncionCoincidente
     * @param atributosResultadoFuncion
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private void llenarAtributosResultadoFuncion(String parametrosFuncionAntes, String contenidoFuncionAntes, String tamanioFuncionAntes, AtributoCompuesto atrFuncionCoincidente, List atributosResultadoFuncion) throws ExceptionFatal, ExceptionWarn {
        try {
            String comentarios = "";
            String tamanioFuncionDespues;
            if (!parametrosFuncionAntes.equals(atrFuncionCoincidente.getSubAtributo("parámetros").getValor()))
                comentarios = "Parámetros modificados.";

            if (!contenidoFuncionAntes.equals(atrFuncionCoincidente.getSubAtributo("contenido").getValor())){
                if (!"".equals(comentarios))
                    comentarios += " ";
            comentarios += "Contenido modificado.";
            }
            tamanioFuncionDespues = atrFuncionCoincidente.getSubAtributo("tamaño").getValor();

            if (!tamanioFuncionAntes.equals(tamanioFuncionDespues)){
                if (!"".equals(comentarios))
                    comentarios += " ";
                comentarios += "Tamaño de " + tamanioFuncionAntes + " a " + tamanioFuncionDespues;
            }

            if (!"".equals(comentarios)) {
                atributosResultadoFuncion.add(new AtributoCompuesto("estado", "modificado", null));
                atributosResultadoFuncion.add(new AtributoCompuesto("comentarios", comentarios, null));
                this.numModificados += 1;
            } else {
                atributosResultadoFuncion.add(new AtributoCompuesto("estado", "igual", null));
                atributosResultadoFuncion.add(new AtributoCompuesto("comentarios", "", null));
                this.numIguales += 1;
            }
        } catch (Exception e) {
            throw new ExceptionFatal("No se pueden llenar los resultado de la función. " + e.getMessage());
        }
    }

    public UploadedFile getArchivoSubidoAntes() {
        return archivoSubidoAntes;
    }

    public void setArchivoSubidoAntes(UploadedFile archivoSubidoAntes) {
        this.archivoSubidoAntes = archivoSubidoAntes;
    }

    public UploadedFile getArchivoSubidoDespues() {
        return archivoSubidoDespues;
    }

    public void setArchivoSubidoDespues(UploadedFile archivoSubidoDespues) {
        this.archivoSubidoDespues = archivoSubidoDespues;
    }

    public File getArchivoAntes() {
        return archivoAntes;
    }

    public void setArchivoAntes(File archivoAntes) {
        this.archivoAntes = archivoAntes;
    }

    public List getArchivosAntes() {
        return archivosAntes;
    }

    public void setArchivosAntes(List archivosAntes) {
        this.archivosAntes = archivosAntes;
    }

    public List getArchivosDespues() {
        return archivosDespues;
    }

    public void setArchivosDespues(List archivosDespues) {
        this.archivosDespues = archivosDespues;
    }

    public File getArchivoDespues() {
        return archivoDespues;
    }

    public void setArchivoDespues(File archivoDespues) {
        this.archivoDespues = archivoDespues;
    }

    public boolean isCodigoAntesValido() {
        return codigoAntesValido;
    }

    public void setCodigoAntesValido(boolean codigoAntesValido) {
        this.codigoAntesValido = codigoAntesValido;
    }

    public boolean isCodigoDespuesValido() {
        return codigoDespuesValido;
    }

    public void setCodigoDespuesValido(boolean codigoDespuesValido) {
        this.codigoDespuesValido = codigoDespuesValido;
    }

    public FormatoConcreto getFormatoComparacionCodigo() {
        return formatoComparacionCodigo;
    }

    public void setFormatoComparacionCodigo(FormatoConcreto formatoComparacionCodigo) {
        this.formatoComparacionCodigo = formatoComparacionCodigo;
    }

    public int getNumAgregados() {
        return numAgregados;
    }

    public void setNumAgregados(int numAgregados) {
        this.numAgregados = numAgregados;
    }

    public int getNumIguales() {
        return numIguales;
    }

    public void setNumIguales(int numIguales) {
        this.numIguales = numIguales;
    }

    public int getNumEliminados() {
        return numEliminados;
    }

    public void setNumEliminados(int numEliminados) {
        this.numEliminados = numEliminados;
    }

    public int getNumModificados() {
        return numModificados;
    }

    public void setNumModificados(int numModificados) {
        this.numModificados = numModificados;
    }

    public FormatoFachada getFormatoFachada() {
        return formatoFachada;
    }

    public void setFormatoFachada(FormatoFachada formatoFachada) {
        this.formatoFachada = formatoFachada;
    }
}