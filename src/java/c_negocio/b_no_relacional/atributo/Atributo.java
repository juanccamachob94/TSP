/**
 * Atributo de formato.
 */
package c_negocio.b_no_relacional.atributo;


public class Atributo {
    protected String atributo;
    protected String valor;

    public Atributo() {
    }

    public Atributo(String atributo) {
        this.atributo = atributo;
    }

    public Atributo(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
    }
    
    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }    
}
