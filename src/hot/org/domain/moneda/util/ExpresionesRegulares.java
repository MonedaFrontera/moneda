package org.domain.moneda.util;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.util.StringTokenizer;

import javax.persistence.EntityManager;

import org.domain.moneda.session.TarjetaHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

/**
 * @author Luis Fernando Ortiz Vera | @lfernandortiz
 *         <p>
 *         La clase <code>ExpresionesRegulares</code> contiene metodos
 *         <code>static</code> para manipular cadenas y patrones. Tambien
 *         contiene metodos para validar cadenas.
 *         </p>
 */

@Name("ExpresionesRegulares")
@Scope(CONVERSATION)
public class ExpresionesRegulares {

	/**
	 * Pasa la primera letra a mayuscula si recibe un valor booleano
	 * <code>true</code> como segundo argumento; si el valor es
	 * <code>false</code> solo elimina los espacios en blanco y deja la cadena
	 * en minuscula
	 */
	public static String eliminarEspacios(String texto, boolean flag) {
		StringTokenizer tokens = new StringTokenizer(texto.toLowerCase());
		StringBuilder textoFinal = new StringBuilder();

		while (tokens.hasMoreTokens()) {
			String palabra = tokens.nextToken();
			if (flag)
				textoFinal.append(" ").append(aMayusculaPrimera(palabra));
			else
				textoFinal.append(" ").append(palabra);
		}
		return textoFinal.toString().trim();
	}

	/**
	 * El metodo <tt>aMayusculaPrimera</tt> pasa a mayuscula la primera letra de una 
	 * cadena. Este metodo puede ser usado como axuliar de otros que esten iterando un 
	 * objeto por ejemplo <tt>StringTokenizer</tt>.
	 * @param palabra
	 * @return
	 */
	public static String aMayusculaPrimera(String palabra) {
		StringBuilder palabraFinal = new StringBuilder();
		Character letra = palabra.charAt(0);

		palabraFinal.append(Character.toUpperCase(letra));
		palabraFinal.append(palabra.substring(1, palabra.length()));
		return palabraFinal.toString();
	}

	/**
	 * El metodo <tt>getDigitsOnly </tt>retorna los digitos sin espeacios en una
	 * cadena, puede ser usado para extraer los digitos en numeros telfonicos
	 * cuando estos registrados se hacen con espacios.
	 * 
	 * @param String
	 * @return String con solo digitos
	 */
	public static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;

		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString(); 
	}// fin del metodo getDigitsOnly - obtener solo digitos

		
	/**
	 * El metodo <tt>validateEmail </tt> retorna true si la direccion de correo 
	 * electronico es una direccion valida. Este metodo usa expresiones regulares
	 * (regex) para hacer el analisis.
	 * 
	 * http://docs.oracle.com/javase/tutorial/essential/regex/intro.html
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean validateEmail(String mail){
		
		if (!mail
				.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
			return false;
		} else {
			return true;
		}		
		
	}

}
