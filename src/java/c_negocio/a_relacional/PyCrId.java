package c_negocio.a_relacional;
// Generated Jan 28, 2017 1:24:51 PM by Hibernate Tools 4.3.1



/**
 * PyCrId generated by hbm2java
 */
public class PyCrId  implements java.io.Serializable {


     private String proyecto;
     private String criterio;

    public PyCrId() {
    }

    public PyCrId(String proyecto, String criterio) {
       this.proyecto = proyecto;
       this.criterio = criterio;
    }
   
    public String getProyecto() {
        return this.proyecto;
    }
    
    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }
    public String getCriterio() {
        return this.criterio;
    }
    
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof PyCrId) ) return false;
		 PyCrId castOther = ( PyCrId ) other; 
         
		 return ( (this.getProyecto()==castOther.getProyecto()) || ( this.getProyecto()!=null && castOther.getProyecto()!=null && this.getProyecto().equals(castOther.getProyecto()) ) )
 && ( (this.getCriterio()==castOther.getCriterio()) || ( this.getCriterio()!=null && castOther.getCriterio()!=null && this.getCriterio().equals(castOther.getCriterio()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getProyecto() == null ? 0 : this.getProyecto().hashCode() );
         result = 37 * result + ( getCriterio() == null ? 0 : this.getCriterio().hashCode() );
         return result;
   }   


}


