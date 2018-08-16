/**
 * Atributo que hace agregación de sí mismo
 */
package c_negocio.b_no_relacional.atributo;

import java.util.List;

public class AtributoCompuesto extends Atributo {
    
    private List<AtributoCompuesto> atributos;
    
    public AtributoCompuesto(){
    }

    public AtributoCompuesto(List atributos) {
        this.atributos = atributos;
    }

    public AtributoCompuesto(String atributo) {
        super(atributo);
        this.valor = null;
        this.atributos = null;
    }

    public AtributoCompuesto(String atributo, List atributos) {
        super(atributo);
        this.atributos = atributos;
    }

    public AtributoCompuesto(String atributo, String valor, List atributos) {
        super(atributo,valor);
        this.atributos = atributos;
    }

    public List<AtributoCompuesto> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<AtributoCompuesto> atributos) {
        this.atributos = atributos;
    }
    
    /**
     * Permite obtener un atributo de la lista de atributos de éste atributo
     * @param nombreAtributo
     * @return 
     */
    public AtributoCompuesto getSubAtributo(String nombreAtributo) {
        int t = this.atributos.size();
        AtributoCompuesto atr;
        for (int i = 0; i < t; i++) {
            atr = this.atributos.get(i);
            if (atr.getAtributo().equals(nombreAtributo))
                return atr;
        }
        return null;
    }
    
    /**
     * Permite obtener la lista de atributos del subatributo consultado a partir del nombre
     * @param nombreAtributo
     * @return 
     */
    public List obtLista(String nombreAtributo) {
        int t = this.atributos.size();
        AtributoCompuesto atr;
        for (int i = 0; i < t; i++) {
            try {
                atr = this.atributos.get(i);
                if (atr.getAtributo().equals(nombreAtributo))
                    return atr.getAtributos();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
