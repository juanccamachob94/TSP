/**
 * Estructura para la gestión de formatos
 */
package c_negocio.b_no_relacional.formato;


public class Formato {
    
    protected String nombre;
    protected String fecha;
    protected String proyecto;
    protected int ciclo;

    public Formato() {
    }
    
    public Formato(String nombre, String proyecto, int ciclo){
        this.nombre = nombre;
        this.proyecto = proyecto;
        this.ciclo = ciclo;
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }
}