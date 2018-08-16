/**
 * Permite obtener formatos sin conocer a detalle su origen o construcción
 */
package b_controlador.b_fachada;

import b_controlador.c_fabricas.a_fabrica_formatos.FabricaFormatos;
import c_negocio.b_no_relacional.formato.FormatoConcreto;
import d_datos.c_datasource.DataSource;
import d_datos.b_fabrica_conexiones.FabricaConexiones;
import e_utilitaria.ExceptionFatal;
import e_utilitaria.ExceptionWarn;
import e_utilitaria.Helper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FormatoFachada implements Serializable {

    @ManagedProperty("#{fabricaFormatos}")
    private FabricaFormatos fabricaFormatos;

    public FormatoFachada() {
    }
    
    /**
     * Crea una instancia del formato solicitado
     * @param nombreFormato
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto crearFormato(String nombreFormato) throws ExceptionFatal, ExceptionWarn {
        try {
            return fabricaFormatos.crearFormato(nombreFormato);
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede crear el formato " + nombreFormato + ". " + e.getMessage());
        }
    }

    /**
     * Crea un formato a partir de todos los atributos del encabezado
     * @param nombreFormato
     * @param nombreProyecto
     * @param numCiclo
     * @param rolAutor
     * @param identificacionAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto crearFormato(String nombreFormato, String nombreProyecto, int numCiclo, String rolAutor, String identificacionAutor) throws ExceptionFatal, ExceptionWarn {
        try {
            return fabricaFormatos.crearFormato(nombreFormato, nombreProyecto, numCiclo, rolAutor, identificacionAutor);
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede crear el formato " + nombreFormato + ". " + e.getMessage());
        }
    }
    
    /**
     * Crea una instancia del formato solicitado con rol especificado
     * @param nombreFormato
     * @param rolAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto crearFormato(String nombreFormato, String rolAutor) throws ExceptionFatal, ExceptionWarn {
        try {
            return fabricaFormatos.crearFormato(nombreFormato, rolAutor);
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede crear el formato " + nombreFormato + ". " + e.getMessage());
        }
    }
    
    /**
     * Consulta un formato a partir de su nombre y rol
     * @param nombreFormato
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto consultarFormato(String nombreFormato, String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            return consultarFormato(crearFormato(nombreFormato,rol));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede cargar el formato. " + e.getMessage());
        }
    }
    
    /**
     * Permite consultar un formato a partir de trodos los atributos del encabezado
     * @param nombreFormato
     * @param nombreProyecto
     * @param numCiclo
     * @param rolAutor
     * @param identificacionAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto consultarFormato(String nombreFormato, String nombreProyecto, int numCiclo, String rolAutor, String identificacionAutor) throws ExceptionFatal, ExceptionWarn {
        try{
            return consultarFormato(crearFormato(nombreFormato, nombreProyecto, numCiclo, rolAutor, identificacionAutor));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede cargar el formato. " + e.getMessage());
        }
    }
    
    /**
     * Permit consultar todos los formatos a partir de todos los atributos del encabezado
     * @param nombresFormato
     * @param nombreProyecto
     * @param numCiclo
     * @param rolAutor
     * @param identificacionAutor
     * @param atributo
     * @param valor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List consultarFormatos(String nombresFormato, String nombreProyecto, int numCiclo, String rolAutor, String identificacionAutor, String atributo, String valor) throws ExceptionFatal, ExceptionWarn {
        try{
            FormatoConcreto formatoPlantilla = fabricaFormatos.crearFormato(nombresFormato, nombreProyecto, numCiclo, rolAutor, identificacionAutor);
            String jsonMatch = valor == null? "atributos:{$elemMatch:{atributo:'" + atributo + "',valor:" + valor + "}}" : "atributos:{$elemMatch:{atributo:'" + atributo + "',valor:'" + valor + "'}}";
            return FabricaConexiones.conectar("mongo").consultarLista(Helper.concatenarJSONS(formatoPlantilla.serialEncabezado(false), jsonMatch));
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede consultar los formatos. " + e.getMessage());
        }
    }
        
    /**
     * Carga un formatoa partir del nombre de éste
     * @param nombreFormato
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto cargarFormato(String nombreFormato) throws ExceptionFatal, ExceptionWarn {
        try{
            FormatoConcreto formatoCreado = crearFormato(nombreFormato);
            FormatoConcreto formatoConsultado = consultarFormato(formatoCreado);
            if(formatoConsultado != null)
                return formatoConsultado;
            return formatoCreado;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede cargar el formato. " + e.getMessage());
        }
    }

    /**
     * Permite cargar un formato teniendo por patrón de búsqueda el nombre y el rol
     * @param nombreFormato
     * @param rol
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto cargarFormato(String nombreFormato, String rol) throws ExceptionFatal, ExceptionWarn {
        try{
            FormatoConcreto formatoCreado = crearFormato(nombreFormato,rol);
            FormatoConcreto formatoConsultado = consultarFormato(formatoCreado);
            if(formatoConsultado != null)
                return formatoConsultado;
            return formatoCreado;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede cargar el formato. " + e.getMessage());
        }
    }
    
    /**
     * Carga un formato a partir del nombre identificándolo a partir del atributo y el valor
     * @param nombreFormato
     * @param atributo
     * @param valor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto cargarFormato(String nombreFormato, String atributo, String valor) throws ExceptionFatal, ExceptionWarn {
        try{
            FormatoConcreto formatoCreado = crearFormato(nombreFormato);
            FormatoConcreto formatoConsultado = consultarFormato(formatoCreado,atributo,valor);
            if(formatoConsultado != null) return formatoConsultado;
            return formatoCreado;
        }catch(ExceptionWarn e){
            throw e;
        }catch(Exception e){
            throw new ExceptionFatal("FormatoControl no puede cargar el formato. " + e.getMessage());
        }
    }

    /**
     * Trae el formato solicitado de la base de datos
     * @param formato
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto consultarFormato(FormatoConcreto formato) throws ExceptionFatal, ExceptionWarn {
        try {
            return (FormatoConcreto)FabricaConexiones.conectar("mongo").consultarObjeto(formato.serialEncabezado(false));
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede consultar el formato " + formato.getNombre() + ". " + e.getMessage());
        }
    }
    
    /**
     * Consultar formato que tenga un atributo en específico
     * @param formato
     * @param atributo
     * @param valor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public FormatoConcreto consultarFormato(FormatoConcreto formato, String atributo, String valor) throws ExceptionFatal, ExceptionWarn {
        try {
            return (FormatoConcreto)FabricaConexiones.conectar("mongo").consultarObjeto(Helper.concatenarJSONS(formato.serialEncabezado(false), "atributos:{$elemMatch:{atributo:'" + atributo + "',valor:'" + valor + "'}}"));
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede consultar el formato " + formato.getNombre() + ". " + e.getMessage());
        }
    }

    /**
     * Consulta los formatos sólo por el nombre
     * @param nombreFormatos
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List consultarFormatos(String nombreFormatos) throws ExceptionFatal, ExceptionWarn {
        try {
            FormatoConcreto formatoPlantilla = fabricaFormatos.crearFormato(nombreFormatos);
            return FabricaConexiones.conectar("mongo").consultarLista(formatoPlantilla.serialEncabezado(false));
        } catch (Exception e) {
            throw new ExceptionFatal("No se pueden consultar los formatos de nombre " + nombreFormatos + ". " + e.getMessage());
        }
    }

    /**
     * Obtiene la lista de formatos con base en todos los atributos del encabezado de un proyecto
     * @param nombreFormatos
     * @param nombreProyecto
     * @param numCiclo
     * @param rolAutor
     * @param identificacionAutor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List consultarFormatos(String nombreFormatos, String nombreProyecto, int numCiclo, String rolAutor, String identificacionAutor) throws ExceptionFatal, ExceptionWarn {
        try {
            FormatoConcreto formatoPlantilla = fabricaFormatos.crearFormato(nombreFormatos, nombreProyecto, numCiclo, rolAutor, identificacionAutor);
            return FabricaConexiones.conectar("mongo").consultarLista(formatoPlantilla.serialEncabezado(false));
        } catch (Exception e) {
            throw new ExceptionFatal("No se pueden consultar los formatos de nombre " + nombreFormatos + ". " + e.getMessage());
        }
    }
    
    /**
     * Permite consultar los formatos que contengan por nombre el enviado por parámetro y un atributo de valor específico
     * @param nombreFormatos
     * @param atributo
     * @param valor
     * @return
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public List consultarFormatos(String nombreFormatos, String atributo, String valor) throws ExceptionFatal, ExceptionWarn {
        try {
            FormatoConcreto formatoPlantilla = fabricaFormatos.crearFormato(nombreFormatos);
            String jsonMatch = valor == null? "atributos:{$elemMatch:{atributo:'" + atributo + "',valor:" + valor + "}}" : "atributos:{$elemMatch:{atributo:'" + atributo + "',valor:'" + valor + "'}}";
            return FabricaConexiones.conectar("mongo").consultarLista(Helper.concatenarJSONS(formatoPlantilla.serialEncabezado(false), jsonMatch));
        } catch (Exception e) {
            throw new ExceptionFatal("No se pueden consultar los formatos de nombre " + nombreFormatos + ". " + e.getMessage());
        }
    }

    /**
     * Inserta el formato en la base de datos (reemplaza)
     * @param formato
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void insertarFormato(FormatoConcreto formato) throws ExceptionFatal, ExceptionWarn {
        try {
            formato.setFecha(Helper.formatearDate(new Date(),"dd/M/yyyy"));
            FabricaConexiones.conectar("mongo").insertar(formato);
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede insertar el formato " + formato.getNombre() + ". " + e.getMessage());
        }
    }

    /**
     * Actualiza el formato en la base datos
     * @param formato
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void actualizarFormato(FormatoConcreto formato) throws ExceptionFatal, ExceptionWarn {
        try {
            DataSource ds = FabricaConexiones.conectar("mongo");
            if (!formato.estaGuardado()) {
                insertarFormato(formato);
                formato.setId(((FormatoConcreto) ds.consultarObjeto(formato.serialEncabezado(false))).getId());
            } else
                ds.actualizar(formato.getId() + "|" + formato.toSerial(false));
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede actualizar el formato " + formato.getNombre() + ". " + e.getMessage());
        }
    }

    /**
     * Elimina el formato de la base de datos
     * @param formato
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarFormato(FormatoConcreto formato) throws ExceptionFatal, ExceptionWarn {
        try {
            DataSource ds = FabricaConexiones.conectar("mongo");
            formato = (FormatoConcreto) ds.consultarObjeto(formato.toSerial(true));
            if(formato != null)
                FabricaConexiones.conectar("mongo").eliminar(formato.getId());
            else
                throw new ExceptionWarn("El formato " + formato.getNombre() + " no existe. ");
        } catch (ExceptionWarn warn) {
            throw warn;
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede eliminar el formato " + formato.getNombre() + ". " + e.getMessage());
        }
    }

    /**
     * Localiza el formato con base en el id y elimina los registros asociados al atributo y valor enviados
     * @param formato
     * @param atributo
     * @param valor
     * @throws ExceptionFatal
     * @throws ExceptionWarn 
     */
    public void eliminarRegistosFormato(FormatoConcreto formato, String atributo, String valor) throws ExceptionFatal, ExceptionWarn {
        try {
            String id = formato.getId();
            if (id != null)
                FabricaConexiones.conectar("mongo").actualizar(id + "|" + "{$pull:{atributos:{atributos:{atributo:'" + atributo + "',valor:'" + valor + "'}}}}");
            else
                throw new ExceptionWarn("El formato " + formato.getNombre() + " no está registrado. ");
        } catch (ExceptionWarn warn) {
            throw warn;
        } catch (Exception e) {
            throw new ExceptionFatal("No se puede eliminar los registros del formato " + formato.getNombre() + ". " + e.getMessage());
        }
    }
    
    public FabricaFormatos getFabricaFormatos() {
        return fabricaFormatos;
    }

    public void setFabricaFormatos(FabricaFormatos fabricaFormatos) {
        this.fabricaFormatos = fabricaFormatos;
    }   
}