/**
 * Permite convertir la lista de proyectos
 */
package a_web.a_converters;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import c_negocio.a_relacional.Proyecto;

@FacesConverter("convertidorProyecto")
public class ConvertidorProyecto implements Converter {

    /**
     * Transforma el proyecto del usuario en objeto
     * @param context
     * @param component
     * @param value
     * @return 
     */
    @Override
    public Proyecto getAsObject(FacesContext context, UIComponent component, String value) {
        List<Proyecto> proyectos = (List<Proyecto>) context.getApplication().evaluateExpressionGet(context, "#{proyectoBean.proyectosUsuarioSesion}", List.class);
        for (Proyecto proyecto : proyectos)
            if (proyecto.toString().equals(value))
                return proyecto;
        return null;
    }

    /**
     * Transforma el proyecto del usuario en cadena
     * @param context
     * @param component
     * @param value
     * @return 
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            return value.toString();
        } catch (Exception e) {
            return "";
        }
    }
}