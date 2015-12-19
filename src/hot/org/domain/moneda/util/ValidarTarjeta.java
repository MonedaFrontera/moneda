package org.domain.moneda.util;

import static org.jboss.seam.ScopeType.SESSION;

import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.session.ReportesHome;
import org.domain.moneda.session.TarjetaHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;



/*
 * Clase que emplea una referencia a un objeto TarjetaCredito, y verifica la 
 * autenticidad de los digtitos de una tarjeta de credito Visa y Master
 * con base en el estandar ISO/IEC 7812-1 ( http://en.wikipedia.org/wiki/ISO/IEC_7812)
 * y el algoritmo de Luhn (http://es.wikipedia.org/wiki/Algoritmo_de_Luhn)check sum.
 * Este analisis es hecho en el metodo isValid. 
 * Para esta clase no se tiene en cuenta el numero IIN numero deideficador de emisor.
 * que son en su mayoria los seis primeros. La validacion del Bin se hace en la clase
 * Administrar Tarjeta
 */

/**
 *
 */
@Name("ValidarTarjeta")
@Scope(SESSION)
public class ValidarTarjeta {
 
    private String nroTarjeta;
    private String franquicia;
    private String bancoEmisor;
    
    @Logger private Log log;
    
	private long nroTc;
	
	@In
	private EntityManager entityManager;
	
	@In(create=true) @Out 
	private TarjetaHome tarjetaHome;
    
    
    public ValidarTarjeta( String nroTarjeta)
    {
        setTarjeta( nroTarjeta);        
        System.out.println("Establece Tarjeta		>" + getTarjeta());        
        if( getTarjeta()!=null){        	
        	setFranquicia( getTarjeta() );
        	System.out.println("Establece la Franquicia	>" + getFranquicia());
        	//setBancoEmisor( getTarjeta() );
        	//System.out.println(" Establece el Banco Emisor		> " + getBancoEmisor());
        }        
    }//fin del constructor

    /**
     * El metodo getDigitsOnly permite obtener solo los digitos
     * correspondiente al numero de tarjeta, crea un objeto StringBuffer
     * con el numero de tarjeta y lo retorna con el metodo toString()
     * @param s
     * @return
     */
    private String getDigitsOnly (String s) 
    {
        StringBuffer digitsOnly = new StringBuffer ();
        char c;
        for (int i = 0; i < s.length (); i++) {
          c = s.charAt (i);
          if (Character.isDigit(c)) {
            digitsOnly.append(c);
          }
        }
        return digitsOnly.toString ();
    }//fin del metodo getDigitsOnly 
    
    
  /**
   * Metodo predicado que evalua si el numro de la TC es valido.
   * </br>
   * Ulitma modificacion 09/09/2013
   * @param cardNumber
   * @return boolean 
   */
    public  boolean isValid (String cardNumber) 
    {
    	String digitsOnly = getDigitsOnly(cardNumber);
    	 
        switch ( digitsOnly.length() ){
            case 16://visa y mastercard      
                if( digitsOnly.substring(0, 1).equals("5")  || digitsOnly.substring(0, 1).equals("4") )
                {
                	int sum = 0;
                    int digit;
                    int addend;
                    boolean posicion = false;
                    for (int i = digitsOnly.length () - 1; i >= 0; i--) {                    
                    digit = Integer.parseInt (digitsOnly.substring (i, i + 1));                
                    if (posicion) {              
                        addend = digit * 2;
                        if (addend > 9) {
                        addend -= 9;
                        }//fin del if interno
                    }
                    else {
                        addend = digit;
                    }
                    sum += addend;                
                    posicion = !posicion;
                    }//fin del for
                    int modulus = sum % 10;
                    if( modulus == 0 && ( digitsOnly.substring(0, 1).equals( "5" ) 
                            || digitsOnly.substring(0, 1).equals("4") ) )
                    return true;
                }
                //para tarjetas de la franquicia Locatel modificacion 19/10/2012
                if( digitsOnly.substring(0, 1).equals("8") ){
                	return true;	
                }
                //para tarjetas debito Visa y MasterCard modificado 19/05/2015
                if( !digitsOnly.substring(0, 1).equals("4") && !digitsOnly.substring(0, 1).equals("5")  ){
                	System.out.println("INGRESE AL IF CUALQUIER NUMERO");
                	return true;	
                } 
                return false; 
            case 15://amex                
                if( digitsOnly.substring (0, 2).equals("37") )
                    return true;
            case 14://diners
                if( digitsOnly.substring (0, 1).equals("3") &&  !digitsOnly.substring (0, 2).equals("37"))
                    return true;                    
            default:
                return false;                                
        }//fin del switch        
    }//fin del metodo isValid

    public String getBancoEmisor() {
		return bancoEmisor;
	}
    
   
    public void setBancoEmisor(String nroTarjeta) 
    {
    	String tarjetabin = nroTarjeta.substring( 0, 6 );//bin de la tarjeta
    	System.out.println( "Bin de la tarjeta		> "+ tarjetabin); 
    	
    	List<String> bin = entityManager.createNativeQuery(
    			"select codbanco from bancobin where bin =" +
    			"'" + tarjetabin + "'" ).getResultList();     	
    	
    	if( bin.size()>0){
    		System.out.printf("Banco Emisor		> %s\n", bin.get(0) );    	
    		this.bancoEmisor =(String) bin.get(0);
    	}
	}	
    
    public String getFranquicia() 
    {
        return franquicia;
    }
    
    public void setFranquicia(String nroTarjeta) {
    	
    	if(getTarjeta()!=null){
    		
    		int digit = Integer.parseInt( getTarjeta().substring(0,1) );
    		
    		if( digit == 5 ){
    			this.franquicia = "M"; //Mastercard
            }
    		if( digit == 4){
    			this.franquicia = "V"; //Visa
            }
    		if( digit == 8){
    			this.franquicia = "L"; //Locatel
            }
    		if ( digit == 3 && getTarjeta().length()== 14){
    			this.franquicia = "D"; //Dinners
            }
    		
    		int digitAmex = Integer.parseInt( getTarjeta().substring(0,2));        
    		if( digitAmex == 37 && getTarjeta().length() == 15){
    			this.franquicia = "A"; //American Express
            }    
    	}//fin del if externo
    }//fin del metodo setFranquicia


    /**
     * Retorna el n&uacute;mero de la tarjeta dejando visible 
     * solo sus cuatro ulitmos digitos
     * @return
     */
    public String getTarjetaSeguro()
    {
    	if( getFranquicia().equals("V") && getFranquicia().equals("M") )
    		return String.format("XXXX XXXX XXXX %s",
                getTarjeta().substring(12, 16));
    	
    	if( getFranquicia().equals("A"))
    		return String.format("XXXX XXXXXX X", getTarjeta().substring(11, 15));
    	
    	if( getFranquicia().equals("D"))
    		return String.format("XXXX XXXXXX %S", getTarjeta().substring(10, 14));
    	
    	//opcion defecto 16 digitos
    	return String.format("XXXX XXXX XXXX %s",
                getTarjeta().substring(12, 16));
    }    
    
    
    public String getTarjeta() {
        return nroTarjeta;
    }
    
    
    
    public void setTarjeta(String tcNumero) {
        if( isValid( tcNumero) ){
            this.nroTarjeta = getDigitsOnly( tcNumero );
        }else{
        	/*
            JOptionPane.showMessageDialog(null, "Numero de Tarjeta NO Valido", 
                    "Moneda Frontera", JOptionPane.ERROR_MESSAGE);
             */
        }        
    }//fin del metodo setTarjeta
    
    
    
    /**
     * Muestra los nros de las tarjetas como aparecen en el plastico
     * @param numero
     * @return
     */
    public String formatearTarjeta(String numero)
	{
		String numeroformateado = null;
		
		switch( numero.length())
		{
		case 16://formatea Visa y MasterCard 4 4 4 4
			numeroformateado =  numero.substring(0, numero.length()-12) + " " +
			numero.substring(numero.length()-12, numero.length()-8) + " " +
			numero.substring(numero.length()-8, numero.length()-4) + " " +
			numero.substring(numero.length()-4, numero.length());			
			return numeroformateado += "*";			
			
		case 15://formatea American Express 4 6 5
			numeroformateado =  numero.substring(0, numero.length()-11) + " " +
			numero.substring(numero.length()-11, numero.length()-5) + " " +
			numero.substring(numero.length()-5, numero.length());			
			return numeroformateado;
			
		case 14://formatea Diners 4 6 4
			numeroformateado =  numero.substring(0, numero.length()-11) + " " +
			numero.substring(numero.length()-11, numero.length()-5) + " " +
			numero.substring(numero.length()-5, numero.length());			
			return numeroformateado;
			
		default://Sin numero de tarjeta
			return numeroformateado;
		}//fin del switch		
	}//fin del metodo formatearTarjetaSistemas
    
    
    public String toString()
    {
        return String.format( "Tarjeta Nro: %s\nFranquicia: %s\n\n",
                getTarjeta(), getFranquicia());
    }//fin de toString
}
