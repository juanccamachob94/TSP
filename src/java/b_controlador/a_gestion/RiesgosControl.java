/**
 * Controlador para la gestión del formato itl
 */
package b_controlador.a_gestion;

import b_controlador.c_fabricas.b_fabrica_atributos.FabricaAtributos;
import c_negocio.a_relacional.Proyecto;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import e_utilitaria.Helper;
import e_utilitaria.TSP;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import b_controlador.b_fachada.FormatoFachada;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class RiesgosControl extends Control{

    @ManagedProperty("#{formatoFachada}")
    private FormatoFachada formatoFachada;
    @ManagedProperty("#{fabricaAtributos}")
    private FabricaAtributos fabricaAtributos;

    private FormatoConcreto itl;
    private String parteFiltro;
    private AtributoCompuesto registroItl;

    public RiesgosControl() {
    }

    /**
     * Permite cargar el formato itl a partir de la parte sobre la cuál se va a realizar
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarItlFiltroParte() throws ExceptionFatal, ExceptionWarn {
        try{
            this.itl = formatoFachada.cargarFormato("itl","parte", this.parteFiltro);
            List atributos = this.itl.getAtributos();
            if (atributos.isEmpty()) {
                atributos.add(new AtributoCompuesto("parte", this.parteFiltro, null));
                atributos.add(new AtributoCompuesto("contenido iterable", new ArrayList()));
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("RiesgosBean no puede cargar el formato itl con parte " + parteFiltro + ". " + e.getMessage());
        }
    }

    /**
     * Indica si el registro itl tiene por autor al usuario en sesión
     * @param atributoAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public boolean esAutor(AtributoCompuesto atributoAutor) throws ExceptionFatal, ExceptionWarn {
        try{
            return ((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion().equals(atributoAutor.getValor());
        }catch(Exception e){
            throw new ExceptionFatal("RiesgosBean no puede identificar el autor de los registros del formato. " + e.getMessage());
        }
    }

    /**
     * Agrega el registro itl al formato
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void agregarRegistroItl() throws ExceptionFatal, ExceptionWarn {
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaSeguimiento = df.parse(this.registroItl.getSubAtributo("fecha seguimiento").getValor());
            Date fechaResolucion = df.parse(this.registroItl.getSubAtributo("fecha resolución").getValor());
            Date fechaInicioProyecto = ((ProyectoControl)getControlador("ProyectoControl")).getProyectoSesion().getFInicio();
            if (registroItlValido(this.registroItl)) {
                if(fechaResolucion.before(fechaSeguimiento))
                    throw new ExceptionWarn("La fecha de seguimiento debe anteceder a la fecha de resolución");
                if(fechaSeguimiento.before(fechaInicioProyecto))
                    throw new ExceptionWarn("La fecha de seguimiento no puede anteceder a la creación del proyecto");
                if(fechaResolucion.before(fechaInicioProyecto))
                    throw new ExceptionWarn("La fecha de resolución no puede anteceder a la creación del proyecto");
                cargarItlFiltroParte();
                this.registroItl.getSubAtributo("fecha").setValor(Helper.formatearDate(new Date(), "dd/M/yyyy"));
                this.registroItl.getSubAtributo("numero").setValor(Integer.toString(numeroUltimoRiesgoProblema() + 1));
                this.itl.getAtributo("contenido iterable").getAtributos().add(this.registroItl);
                formatoFachada.actualizarFormato(this.itl);
            } else throw new ExceptionWarn("Todos los campos deben ser dilifenciados");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("RiesgosBean no puede agregar el registro itl. " + e.getMessage());
        }
    }

    /**
     * Actualiza los cambios realizados al formato itl
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarItl() throws ExceptionFatal, ExceptionWarn {
        try{
            AtributoCompuesto atributo;
            List atributos = this.itl.getAtributo("contenido iterable").getAtributos();
            int t = atributos.size();
            String numero = null;
            boolean registroIncompleto;
            if (t == 0)
                throw new ExceptionWarn("El formato ITL no tiene ningún registro; no puede ser actualizado");
            else {
                if (this.parteFiltro != null) {
                    registroIncompleto = false;
                    for (int i = 0; i < t; i++) {
                        atributo = (AtributoCompuesto) atributos.get(i);
                        numero = atributo.getSubAtributo("numero").getValor();
                        if (!registroItlValido(atributo)) {
                            registroIncompleto = true;
                            break;
                        }
                    }
                    if (!registroIncompleto) formatoFachada.actualizarFormato(this.itl);
                    else throw new ExceptionWarn("El riesgo/problema " + numero + " debe ser llenado por completo");
                } else throw new ExceptionWarn("Selecciona una parte válida");
            }
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("RiesgosBean no puede actualizar el formato itl. " + e.getMessage());
        }
    }

    /**
     * Brinda el último número registrado de riesgo/problema
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private int numeroUltimoRiesgoProblema() throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = formatoFachada.cargarFormato("itl","parte", this.parteFiltro).getAtributos();
            if (atributos.isEmpty()) return 0;
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
            int max = 0;
            int num;
            for (int i = 0; i < t; i++) {
                num = (int) Helper.extraerNumero(((AtributoCompuesto) atributos.get(i)).getSubAtributo("numero").getValor());
                if (num > max) max = num;
            }
            return max;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede obtener el número del último riesgo. " + e.getMessage());
        }
    }

    /**
     * carga los datos para desplegar el modal de nuevo registro itl
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void cargarModalNuevoRegistroItl() throws ExceptionFatal, ExceptionWarn {
        try{
            if (this.parteFiltro != null) {
                this.registroItl = fabricaAtributos.crearAtributosItl();
                this.registroItl.getSubAtributo("autor").setValor(((UsuarioControl) getControlador("UsuarioControl")).getUsuarioSesion().getIdentificacion());
            } else
                throw new ExceptionWarn("Seleccione una parte válida");
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("RiesgosBean no puede cargar los datos para el nuevo registro itl. " + e.getMessage());
        }
    }

    /**
     * Obtiene el número de riesto o problema del actual registro a añadir
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List getNumerosRiesgoProblema() throws ExceptionFatal, ExceptionWarn {
        try{
            List atributos = formatoFachada.cargarFormato("itl","parte", this.parteFiltro).getAtributos();
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
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("RiesgosBean no puede generar la lista de selección de riesgos. " + e.getMessage());
        }
    }

    /**
     * Indica si el registro itl a añadir es válido o no.
     * @param atributo
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    private boolean registroItlValido(AtributoCompuesto atributo) throws ExceptionFatal, ExceptionWarn {
        try{
            String riesgoProblema = atributo.getSubAtributo("riesgo-problema").getValor();
            String prioridad = atributo.getSubAtributo("prioridad").getValor();
            String fechaSeguimiento = atributo.getSubAtributo("fecha seguimiento").getValor();
            String fechaResolucion = atributo.getSubAtributo("fecha resolución").getValor();
            String descripcion = atributo.getSubAtributo("descripción").getValor();
            if (riesgoProblema == null) return false;
            else if (riesgoProblema.trim().equals("")) return false;
            if (prioridad == null) return false;
            else if (prioridad.trim().equals("")) return false;
            if (fechaSeguimiento == null) return false;
            else if (fechaSeguimiento.trim().equals("")) return false;
            if (fechaResolucion == null) return false;
            else if (fechaResolucion.trim().equals("")) return false;
            if (descripcion == null) return false;
            else if (descripcion.trim().equals("")) return false;
            return true;
        }catch(Exception e){
            throw new ExceptionFatal("No se puede validar el registro itl. " + e.getMessage());
        }
    }

    public FormatoConcreto getItl() {
        return itl;
    }

    public void setItl(FormatoConcreto itl) {
        this.itl = itl;
    }

    public String getParteFiltro() {
        return parteFiltro;
    }

    public void setParteFiltro(String parteFiltro) {
        this.parteFiltro = parteFiltro;
    }

    public AtributoCompuesto getRegistroItl() {
        return registroItl;
    }

    public void setRegistroItl(AtributoCompuesto registroItl) {
        this.registroItl = registroItl;
    }

    public List getRiesgosProblemas() {
        return TSP.riesgoProblema;
    }

    public List getPrioridades() {
        return TSP.prioridades;
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