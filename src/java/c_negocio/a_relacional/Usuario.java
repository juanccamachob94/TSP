package c_negocio.a_relacional;
// Generated Jan 28, 2017 1:24:51 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Usuario generated by hbm2java
 */
public class Usuario  implements java.io.Serializable {


     private String identificacion;
     private String nombre;
     private String apellido;
     private String email;
     private String telefono;
     private String clave;
     private Set rlCls = new HashSet(0);
     private Set proyectos = new HashSet(0);

    public Usuario() {
    }

	
    public Usuario(String identificacion, String nombre, String apellido, String email, String clave) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.clave = clave;
    }
    public Usuario(String identificacion, String nombre, String apellido, String email, String clave, Set rlCls, Set proyectos) {
       this.identificacion = identificacion;
       this.nombre = nombre;
       this.apellido = apellido;
       this.email = email;
       this.clave = clave;
       this.rlCls = rlCls;
       this.proyectos = proyectos;
    }
   
    public String getIdentificacion() {
        return this.identificacion;
    }
    
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return this.apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    public Set getRlCls() {
        return this.rlCls;
    }
    
    public void setRlCls(Set rlCls) {
        this.rlCls = rlCls;
    }
    public Set getProyectos() {
        return this.proyectos;
    }
    
    public void setProyectos(Set proyectos) {
        this.proyectos = proyectos;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
