/**
 * Permite crear/cargar un revisor de código de acuerdo al lenguaje de programación solicitado
 */
package b_controlador.c_fabricas.c_fabrica_revisorescodigo;

import c_negocio.c_otros.RevisorCodigo;
import c_negocio.c_otros.RevisorCodigoJava;

public class FabricaRevisoresCodigo extends FabricaAbstractaRevisCodigo {
    /**
     * Permite obtener la instancia de un revisor de código
     * @param lenguaje
     * @return 
     */
    public static RevisorCodigo crearRevisorCodigo(String lenguaje){
        switch(lenguaje){
            case "java":
                return RevisorCodigoJava.getInstancia();
        }
        return null;
    }
}
