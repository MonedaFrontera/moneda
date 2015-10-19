package org.domain.moneda.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.domain.moneda.entity.Promotor;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/** 
 * @author Chuidiang
 *
 */
@Name("EnviarMail") 
@Scope(ScopeType.CONVERSATION)
public class EnviarMailAdjunto
{
    /**
     * 
     * @param destino
     * @param mensaje
     * @param archivo
     * @param fechaInforme
     */
    public void enviarMail(String destino, String mensaje, String archivo,
    					   String fechaInforme)
    {
        try
        {
        	// se obtiene el objeto Session. La configuración es para
        	// una cuenta de gmail.
        	//gmail: "smtp.gmail.com"
        	//mail.monedafrontera.com -> direccion IP 66.7.204.235
        	
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "192.168.1.6");
            props.setProperty("mail.smtp.port", "25");
            props.setProperty("mail.smtp.user", "clientes-noreply@monedafrontera.com");
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.transport.protocol.", "smtp"); 
            
            
            
            //imprimo las propiedades de conexion actual
            Set< Object > claves = props.keySet();//vista de las claves            
            for( Object clave: claves)
            {
            	System.out.println(clave +": "+ props.getProperty((String) clave ));
            }
            
            Session session = Session.getDefaultInstance(props, null);
            System.out.println("Sesion Activa>>>");
            // session.setDebug(true);
            

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            System.out.println("Se compone el mensaje>>>");
            
            // Se compone el adjunto con la imagen
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(
                new DataHandler(new FileDataSource(archivo)));
            adjunto.setFileName("MovimientoDiario" + fechaInforme  + ".pdf");
            System.out.println("Se compone el archivo adjunto>>>");
            
            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);
            System.out.println("Se compone el mensaje y el adjunto>>>");

            // Se compone el correo dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("clientes-noreply@monedafrontera.com"));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(destino));
            message.setSubject("Movimiento Diario - <" + fechaInforme +">");
            message.setContent(multiParte);
            System.out.println("Se crea el mensje para enviar>>>");
            
            // Se envia el correo.
            Transport t = session.getTransport("smtp");
            t.connect("clientes-noreply@monedafrontera.com", "Carlos0411" );
            System.out.println("Conexion a cuenta de Correo>>>>");
            t.sendMessage(message, message.getAllRecipients());  
            System.out.println("Se envio correo a la direcion >> " + destino);
            t.close();
            System.out.println("Conexion cerrada");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
        
    }//fin del metodo enviarMail
    
    
    /**
     * nombreCliente, correoCliente, path, fecha
     * 
     */
    public void enviarReporteMail( Promotor promotor, String pathReporte, String fechaInforme )
    {
        try
        {       
        	// 1. Leemos el archivo HTML del correo desde el disco y lo
			// pasamaos al objeto analizador Document
			File inputHtml = new File(
					"/EmailMonedaFrontera/extractodiariointerfaz.html");
			Document doc = Jsoup.parse(inputHtml, "UTF-8");
			
			// obtengo los id's del DOM a los que deseo insertar los valores
			Element nomC = doc.select("span#nombreCliente").first();
			nomC.append(promotor.getPersonal().getNombre());

			Element fechaIni = doc.select("span#fechaIni").first();
			fechaIni.append(fechaInforme);
        	        	
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "192.168.1.6");
            props.setProperty("mail.smtp.port", "25");
            props.setProperty("mail.smtp.user", "clientes-noreply@monedafrontera.com");
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.transport.protocol.", "smtp"); 
                        
            //imprimo las propiedades de conexion actual
            Set< Object > claves = props.keySet();//vista de las claves            
            for( Object clave: claves)
            {
            	System.out.println(clave +": "+ props.getProperty((String) clave ));
            }
            
            Session session = Session.getDefaultInstance(props, null);
            System.out.println("Sesion Activa>>>");
            // session.setDebug(true);
           
         // establecemos las direcciones a las que se enviara el correo
         			String[] to = { promotor.getPersonal().getCorreo(), 
         					promotor.getAsesor().getPersonal().getCorreo()};

			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			System.out.println("Se compone el archivo adjunto>>>");
			// Se compone el adjunto con la imagen
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(pathReporte)));
			adjunto.setFileName("MovimientoDiario_" + fechaInforme + ".pdf");

			//Se compone el contenido HTML
			BodyPart mensajeHtml = new MimeBodyPart();
			mensajeHtml.setContent(doc.html(), "text/html; charset=utf-8");
			
			// Una MultiParte para agrupar el mensaje Html y el pdf adjunto
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(mensajeHtml);
            multiParte.addBodyPart(adjunto);
			
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"clientes-noreply@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);
			message.setSubject("Movimiento Diario - <" + fechaInforme +">");
			// Aca se establece el MimeMultiPart con el contenido y el adjunto
			message.setContent(multiParte);
			

			Transport t = session.getTransport("smtp");
			System.out.println("Enviamos el mensaje>>>>");
			t.connect("clientes-noreply@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());		   	            	
          
              
			//*Aca se debe cerrar el ciclo*
			t.close();
            System.out.println("Conexion cerrada");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
        
    }//fin del metodo enviarMail
    
    
    
    
    public void enviarMailGmail(String destino, String mensaje, String archivo,
			   String fechaInforme)
    {
    	try{
    		// se obtiene el objeto Session. La configuración es para
			// una cuenta de gmail.
			//gmail: "smtp.gmail.com"
			//mail.monedafrontera.com -> direccion IP 67.7.204.235			
			
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.user", "soportesicnoreply@gmail.com");
			props.setProperty("mail.smtp.auth", "true");
			
			Set< Object > claves = props.keySet();            
			for( Object clave: claves)
			{
				System.out.println(clave +": "+ props.getProperty((String) clave ));
			}
			
			Session session = Session.getDefaultInstance(props, null);
			// session.setDebug(true);			
			
			// Se compone la parte del texto
			BodyPart texto = new MimeBodyPart();
			texto.setText(mensaje);
			
			// Se compone el adjunto con la imagen
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(
			 new DataHandler(new FileDataSource(archivo)));
			adjunto.setFileName("MovimientoDiario" + fechaInforme  + ".pdf");
			
			// Una MultiParte para agrupar texto e imagen.
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);
			
			// Se compone el correo, dando to, from, subject y el
			// contenido.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("soportesicnoreply@gmail.com"));
			message.addRecipient(
			 Message.RecipientType.TO,
			 new InternetAddress(destino));
			message.setSubject("Movimiento Diario - <" + fechaInforme +">");
			message.setContent(multiParte);
			
			// Se envia el correo.
			Transport t = session.getTransport("smtp");
			t.connect("soportesicnoreply@gmail.com", "carlos0411" );
			t.sendMessage(message, message.getAllRecipients());  
			System.out.println("Se envio correo a la direcion >> " + destino);
			t.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		System.out.println("Conexion cerrada");
    }//fin del metodo enviarMail
    
    
    
    
    
}//fin de la clase EnviarMailAdjunto
