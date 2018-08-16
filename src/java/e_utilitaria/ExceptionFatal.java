/**
 * Excepción creada para las alertas en rojo.
 */
package e_utilitaria;

public class ExceptionFatal extends Exception{
	public ExceptionFatal(String msg){
		super(msg);
	}
}