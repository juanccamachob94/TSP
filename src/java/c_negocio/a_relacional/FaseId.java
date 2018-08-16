package c_negocio.a_relacional;
// Generated 2/02/2017 03:59:54 PM by Hibernate Tools 4.3.1



/**
 * FaseId generated by hbm2java
 */
public class FaseId  implements java.io.Serializable {


     private String nombre;
     private byte NCiclo;
     private String proyecto;

    public FaseId() {
    }

    public FaseId(String nombre, byte NCiclo, String proyecto) {
       this.nombre = nombre;
       this.NCiclo = NCiclo;
       this.proyecto = proyecto;
    }
   
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public byte getNCiclo() {
        return this.NCiclo;
    }
    
    public void setNCiclo(byte NCiclo) {
        this.NCiclo = NCiclo;
    }
    public String getProyecto() {
        return this.proyecto;
    }
    
    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof FaseId) ) return false;
		 FaseId castOther = ( FaseId ) other; 
         
		 return ( (this.getNombre()==castOther.getNombre()) || ( this.getNombre()!=null && castOther.getNombre()!=null && this.getNombre().equals(castOther.getNombre()) ) )
 && (this.getNCiclo()==castOther.getNCiclo())
 && ( (this.getProyecto()==castOther.getProyecto()) || ( this.getProyecto()!=null && castOther.getProyecto()!=null && this.getProyecto().equals(castOther.getProyecto()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getNombre() == null ? 0 : this.getNombre().hashCode() );
         result = 37 * result + this.getNCiclo();
         result = 37 * result + ( getProyecto() == null ? 0 : this.getProyecto().hashCode() );
         return result;
   }   


}


