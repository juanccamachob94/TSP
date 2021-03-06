package c_negocio.a_relacional;
// Generated Jan 28, 2017 1:24:51 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Documento generated by hbm2java
 */
public class Documento  implements java.io.Serializable {


     private int id;
     private Documento documento;
     private RlCl rlCl;
     private String nombre;
     private Date fechaCreacion;
     private String tipo;
     private String extension;
     private String url;
     private boolean editable;
     private Set documentos = new HashSet(0);

    public Documento() {
    }

	
    public Documento(int id, RlCl rlCl, String nombre, Date fechaCreacion, String tipo, String extension, boolean editable) {
        this.id = id;
        this.rlCl = rlCl;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.tipo = tipo;
        this.extension = extension;
        this.editable = editable;
    }
    public Documento(int id, Documento documento, RlCl rlCl, String nombre, Date fechaCreacion, String tipo, String extension, String url, boolean editable, Set documentos) {
       this.id = id;
       this.documento = documento;
       this.rlCl = rlCl;
       this.nombre = nombre;
       this.fechaCreacion = fechaCreacion;
       this.tipo = tipo;
       this.extension = extension;
       this.url = url;
       this.editable = editable;
       this.documentos = documentos;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Documento getDocumento() {
        return this.documento;
    }
    
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }
    public RlCl getRlCl() {
        return this.rlCl;
    }
    
    public void setRlCl(RlCl rlCl) {
        this.rlCl = rlCl;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isEditable() {
        return this.editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    public Set getDocumentos() {
        return this.documentos;
    }
    
    public void setDocumentos(Set documentos) {
        this.documentos = documentos;
    }




}


