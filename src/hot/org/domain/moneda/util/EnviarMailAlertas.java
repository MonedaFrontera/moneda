package org.domain.moneda.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.adrianwalker.multilinestring.Multiline;
import org.domain.moneda.entity.Activacion;
import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;

@Name("enviarMailAlertas")
@Scope(ScopeType.CONVERSATION)
public class EnviarMailAlertas {

	@In
	private EntityManager entityManager;

	@In
	Identity identity;

	/**
	 * 
	 * @param cardNumber
	 * @param pais
	 * @param banco
	 */
	public void enviarEmailAlertaTarjeta(String usuario, String cardNumber,
			String pais, String banco, String franquicia) {
		try {
			// Propiedades de la conexión
			System.out.println("Enviar Correo ");

			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");
			props.setProperty("mail.smtp.user",
					"clientes-noreply@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);

			// Construimos el mensaje
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"clientes-noreply@monedafrontera.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"lfernandortiz@hotmail.com"));
			message.setSubject("ALERTA DE SEGURIDAD - SISTEMA MONEDA FRONTERA");

			SimpleDateFormat sdf = new SimpleDateFormat(
					"dd/MM/yyyy - hh:mm:ss aaa");

			String mensaje = String.format(
					"Alerta de seguridad al registrar la Tarjeta %s\n"
							+ "Pais origen de la tarjeta: %s\n"
							+ "Banco Emisor: \t\t%s\n" + "Franquicia: \t\t%s\n"
							+ "Alerta generdada el: \t\t%s\n"
							+ "Usuario que registro la tarjeta: \t\t%s\n\n"
							+ "No responda a este mensaje.\n"
							+ "Enviado desde el SIC Moneda Frontera",
					cardNumber, pais, banco.toUpperCase(), franquicia
							.toUpperCase(), sdf.format(new Date()), usuario);

			System.out.println(mensaje);
			message.setText(mensaje);

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("clientes-noreply@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();
			System.out.println("Conexion cerrada");
		} catch (Exception e) {
			System.out.println("Falla al enviar al correo ingrese al catch");
			e.printStackTrace();
		}
	}

	/*
	 * Variables Multilineas que contienen el comienzo y fin del mensaje HTML
	 * https
	 * ://github.com/benelog/multiline/wiki/Non-Maven-Java-project-with-Eclipse
	 */

/**
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />        
        <link rel="shortcut icon" href="favicon.ico"/>
        <title>Alerta Moneda Frontera</title>
		<style type="text/css">
			
			#outlook a{padding:0;} 
			body{width:100% !important;} .ReadMsgBody{width:100%;} .ExternalClass{width:100%;} /
			body{-webkit-text-size-adjust:none;} 
						
			body{margin:0; padding:0;}
			img{border:0; height:auto; line-height:100%; outline:none; text-decoration:none;}
			table td{border-collapse:collapse;}
			#backgroundTable{ background-color:#999999; height:100% !important; margin:0; padding:0; width:100% !important;}
			
			body, #backgroundTable{
				 background-color:#FAFAFA;
			}
			
			
			#templateContainer{
				 border: 1px solid #DDDDDD;
			}
			
			
			h1, .h1{
				color:#202020;
				display:block;
				font-family:Arial;
				font-size:34px;
				font-weight:bold;
				line-height:100%;
				margin-top:0;
				margin-right:0;
				margin-bottom:10px;
				margin-left:0;
				text-align:left;
			}
			
			h2, .h2{
				color:#202020;
				display:block;
				font-family:Arial;
				font-size:30px;
				font-weight:bold;
				line-height:100%;
				margin-top:0;
				margin-right:0;
				margin-bottom:10px;
				margin-left:0;
				text-align:left;
			}
			
			h3, .h3{
				color:#202020;
				display:block;
				font-family:Arial;
				font-size:26px;
				font-weight:bold;
				line-height:100%;
				margin-top:0;
				margin-right:0;
				margin-bottom:10px;
				margin-left:0;
				text-align:left;
			}

			h4, .h4{
				color:#202020;
				display:block;
				font-family:Arial;
				font-size:22px;
				font-weight:bold;
				line-height:100%;
				margin-top:0;
				margin-right:0;
				margin-bottom:10px;
				margin-left:0;
				text-align:left;
			}
			
			
			#templatePreheader{
				 background-color:#FAFAFA;
			}
			
			
			.preheaderContent div{
			 color:#505050;
			 font-family:Arial;
			 font-size:10px;
			 line-height:100%;
			 text-align:left;
			}			
			
			.preheaderContent div a:link, .preheaderContent div a:visited,  .preheaderContent div a .yshortcuts 
			{
				color:#336699;
				font-weight:normal;
				text-decoration:underline;
			}			
			
			#templateHeader{
				background-color:#FFFFFF;
				border-bottom:0;
			}
			
			
			.headerContent{
				color:#202020;
				font-family:Arial;
				font-size:34px;
				font-weight:bold;
				line-height:100%;
				padding:0;
				text-align:center;
				vertical-align:middle;
			}			
			
			.headerContent a:link, .headerContent a:visited, 
			.headerContent a .yshortcuts {
				color:#336699;
				font-weight:normal;
				text-decoration:underline;
			}
			
			#headerImage{
				height:auto;
				max-width:600px;
			}
			
			
			#templateContainer, .bodyContent{
				background-color:#FFFFFF;
			}
			
			
			.bodyContent div{
				color:#505050;
				font-family:Arial;
				font-size:12px;
				line-height:150%;
				text-align:left;
			}
			
			.bodyContent div a:link, .bodyContent div a:visited, 
			.bodyContent div a .yshortcuts 
			{
				color:#336699;
				font-weight:normal;
				text-decoration:underline;
			}
			
			.bodyContent img{
				display:inline;
				height:auto;
			}
						
			#templateFooter{
				background-color:#FFFFFF;
				border-top:0;
			}
						
			.footerContent div{
				color:#707070;
				font-family:Arial;
				font-size:12px;
				line-height:125%;
				text-align:left;
			}
			
			
			.footerContent div a:link, .footerContent div a:visited,
			.footerContent div a .yshortcuts {
				color:#336699;
				font-weight:normal;
				text-decoration:underline;
			}
			
			.footerContent img{
				display:inline;
			}
			

			.gridtable {
				font-family: arial,arial,sans-serif;
				text-align:left;
				font-size:16px;
				color:#333333;
				border-width: 1px;
				border-color: #666666;
				border-collapse: collapse;
			}
			.gridtable td {
				border-width: 1px;
				padding: 4px;
				border-style: solid;
				border-color: #666666;
				background-color: #ffffff;
			}
	
			#social{
				background-color:#FAFAFA;
				border:0;
			}
			
			#social div{
				text-align:center;
			}
			
			
			#utility{
				background-color:#FFFFFF;
				border:0;
			}

			#utility div{
				text-align:center;
			}
			
			#monkeyRewards img{
				max-width:190px;
			}
			
		</style>
	</head>
    <body leftmargin="0" marginwidth="0" topmargin="0" marginheight="0" offset="0">
    	<center>
        	<table bgcolor="#00CC33" border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" id="backgroundTable">
            	<tr bgcolor="#999999">                
                	<td align="center" valign="top">                    
                        
                        <table border="0" cellpadding="5" cellspacing="0" width="600" id="templatePreheader">
                            <tr>
                                <td valign="top" class="preheaderContent">                                
                                	
                                    <table border="0" cellpadding="5" cellspacing="0" width="100%">
                                    	<tr>
                                        	<td valign="top">
                                            	<div mc:edit="std_preheader_content">
                                                	 Alerta de seguridad al registrar  una tarjeta de credito. en el sistema de Moneda Frontera.
                                                </div>
                                            </td>
                                           
											<td valign="top" width="190">
                                            	<div mc:edit="std_preheader_links">
                                                	
                                                </div>
                                            </td>											
                                        </tr>
                                    </table>
                                    </td>
                            </tr>
                        </table>
                        
                    	<table border="0" cellpadding="0" cellspacing="0" width="600" id="templateContainer">
                        	<tr>
                            	<td align="center" valign="top">
                                  
                                	<table border="0" cellpadding="0" cellspacing="0" width="600" id="templateHeader">
                                        <tr>
                                            <td class="headerContent">
                                            
                                            
                                            	<img src="http://www.monedafrontera.com/sicmonedafrontera/placeholder.jpg"
                                                style="max-width:600px;" 
                                                id="headerImage campaign-icon" 
                                                mc:label="header_image" 
                                                mc:edit="header_image" 
                                                mc:allowdesigner mc:allowtext />
                                            	
                                            
                                            </td>
                                        </tr>
                                    </table>
                                   
                                </td>
                            </tr>
                        	<tr>
                            	<td align="center" valign="top">
                                   
                                	<table border="0" cellpadding="0" cellspacing="0" width="600" id="templateBody">
                                    	<tr>
                                            <td valign="top" class="bodyContent">                             
                                              <table border="0" cellpadding="20" cellspacing="0" width="100%">
                                                    <tr mc:repeatable>
                                                        <td valign="top">
                                                        	<div mc:edit="postcard_heading00">
                                                                <h4 class="h4">Alerta al registrar la siguiente tarjeta de cr&eacute;dito:</h4>
                                                            </div>
                                                            <br />

	*/
	@Multiline
	String inicioHTML;

	/**
	 * <div mc:edit="std_content00"> <br />
	 * 
	 * <strong>Informaci&oacute;n Adicional:</strong> Su correo electronico esta
	 * inscrito en la base de datos para emisi&oacute;n de alertas en el sistema
	 * de Moneda Frontera. Esta Alerta, se genero por que la tarjeta que se
	 * describe anteriormente no cumple con una de las dos directrices de
	 * seguridad m&aacute;s importantes, No es emitida por un banco de
	 * Venezuela, o no se pudo determinar el pais de emisi&oacute;n de la
	 * tarjeta. <br />
	 * </div> </td> </tr> </table>
	 * 
	 * </td> </tr> </table>
	 * 
	 * </td> </tr>
	 * <tr>
	 * <td align="center" valign="top">
	 * 
	 * <table border="0" cellpadding="10" cellspacing="0" width="600" id="templateFooter">
	 * <tr>
	 * <td valign="top" class="footerContent">
	 * 
	 * 
	 * <table border="0" cellpadding="10" cellspacing="0" width="100%">
	 * <tr>
	 * <td valign="top" width="350">
	 * <div mc:edit="std_footer">
	 * <em>Informaci&oacute;n de uso esclusivo para Moneda Frontera.</em> <br />
	 * | (057)(7) 5822620 |&nbsp;| Sistema Integral de Clientes | <br />
	 * <strong>Email de contacto:</strong> <br />
	 * |<strong> gerencia@monedafrontera.com
	 * </strong>|&nbsp;|C&uacute;cuta|&nbsp;|Colombia| </div></td>
	 * <td valign="top" width="190" id="monkeyRewards">
	 * <div mc:edit="monkeyrewards">
	 * <p>
	 * Para mas informaci&oacute;n sobre analisis de bines de tarjetas de
	 * cr&eacute;dito visite a <a href="http://www.binbase.com/search.html"
	 * target="_blank">binbase.com.</a>
	 * </p>
	 * </div></td>
	 * </tr>
	 * <tr>
	 * <td bgcolor="#FF0000" colspan="2" valign="middle" id="utility">
	 * <div style=" font-size:8px"> &nbsp; <a
	 * href="mailto:lfernandortiz@hotmail.com" title="correo contacto"
	 * style="color: #0066cc">Contacto con el administrador del sistema.</a>
	 * &nbsp; </div></td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * </td>
	 * </tr>
	 * </table> <br />
	 * </td> </tr> </table> </center> </body> </html>
	 */
	@Multiline
	String finalHTML;

	public void enviarEmailAlertaHTML(String usuario, String cardNumber,
			String pais, String banco, String franquicia) {
		try {
			// System.out.println(finalHTML);
			// Propiedades de la conexión
			System.out.println("Enviar Correo ");

			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
					"clientes-noreply@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);

			// Construimos el mensaje
			// multiples direcciones
			String[] to = { "lfernandortiz@hotmail.com",
							"lfernandortiz@gmail.com", 
							"lortiz@monedafrontera.com",
							"gerencia@monedafrontera.com"};// aca se debe cambiar
			// por una consulta

			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"clientes-noreply@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);// se
			// envia
			// a
			// varias
			// direcciones
			message.setSubject("ALERTA DE SEGURIDAD - SISTEMA MONEDA FRONTERA");

			SimpleDateFormat sdf = new SimpleDateFormat(
					"dd/MM/yyyy - hh:mm:ss aaa");

			String datosHTML = String.format("<center> "
					+ "<table class=\"gridtable\"> " + "<tr> "
					+ "<td>N&uacute;mero de Tarjeta:</td> "
					+ "<td><strong>%s</strong></td>" + "</tr> " + "<tr> "
					+ "<td>Franquicia:</td> " + "<td><strong>%s</strong></td> "
					+ "</tr> " + "<tr> " + "<td>Banco Emisor:</td> "
					+ "<td><strong>%s</strong></td> " + "</tr> " + "<tr> "
					+ "<td>Pais origen:</td> "
					+ "<td><strong>%s</strong></td> " + "</tr> " + "<tr> "
					+ "<td>Alerta generada el:</td> " + "<td>%s</td> "
					+ "</tr> " + "<tr> "
					+ "<td>Usuario que registro la tarjeta:</td> "
					+ "<td>%s</td> " + "</tr> " + "</table> " + "</center> ",
					cardNumber, franquicia.toUpperCase(), banco.toUpperCase(),
					pais, sdf.format(new Date()), usuario);

			message.setContent(inicioHTML + datosHTML + finalHTML,
					"text/html; charset=utf-8");

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("clientes-noreply@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();
			System.out.println("Conexion cerrada");
		} catch (Exception e) {
			System.out.println("Falla al enviar al correo ingrese al catch");
			e.printStackTrace();
		}
	}
	
	
	
	public void enviarEmailAlertaHTMLV2(String usuario, String cardNumber,
			String pais, String banco, String franquicia) {
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yyyy - hh:mm:ss aaa");
			// 1. Leemos el archivo HTML del correo desde el disco
			File inputHtml = new File(
					"/EmailMonedaFrontera/alertaseguridadregistrotc.html");
			// Asginamos el archivo al objeto analizador Document
			Document doc = Jsoup.parse(inputHtml, "UTF-8");
			// obtengo los id's del DOM a los que deseo insertar los valores
			// mediante el metodo append() se insertan los valores obtenidos de
			// la consulta
			Element nroTc = doc.select("span#nrotc").first();
			nroTc.append(cardNumber);
			
			Element franq = doc.select("span#franquicia").first();
			franq.append(franquicia);
			
			Element bank = doc.select("span#banco").first();
			bank.append(banco);
		
			Element country = doc.select("span#pais").first();
			country.append(pais);
			
			Element fechat = doc.select("span#fechahora").first();
			fechat.append(sdf.format(new Date()));
			
			Element usuariot = doc.select("span#usuario").first();
			usuariot.append(usuario);
			
			// envia el mail

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
								"notificaciones@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);
			// Construimos el mensaje

			/**
			 * Ojo aca reemplazar por consulta
			 */
			// multiples direcciones
		/**	String[] to = { "lfernandortiz@hotmail.com", 
							"lfernandortiz@gmail.com",
							"lfernandortiz@yahoo.es",
							"lortiz@monedafrontera.com",
							"gerencia@monedafrontera.com"
							};*/
		
			entityManager.clear();
			//
			List<String> resultList = 
				entityManager.createQuery("select p.correo from Personal p where p.correosalerta=true").getResultList();
			
			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[resultList.size()];
			for (int i = 0; i < resultList.size(); i++) {
				addressTo[i] = new InternetAddress(resultList.get(i));
			}
			
			
			// se compone el mensaje (Asunto, cuerpo del mensaje y direccion origen)
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"notificaciones@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);
			message.setSubject("ALERTA DE SEGURIDAD - SISTEMA MONEDA FRONTERA");
			message.setContent(doc.html(), "text/html; charset=utf-8");

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("notificaciones@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());
			
			// Cierre de la conexion
			t.close();
			System.out.println("Conexion cerrada");
			
		}catch(Exception e){
			System.out.println("Falla en el envio del correo:");
			e.printStackTrace();
		}
		
		
	}

	

	
	/**
	 * 
	 * @param activacion
	 * Envia un correo al promotor si no se puede validar el usuaio
	 * de cencoex.
	 */
	public void enviarEmailActivaciones(Activacion activacion) {
		try {
			// Ubicamos la asesora correspondiente a la activacion y los datos
			// de la misma para el correo
			String correoAsesora = activacion.getPromotor().getAsesor()
					.getPersonal().getCorreo();

			String correoPromo = activacion.getPromotor().getPersonal()
					.getCorreo();
			if (correoPromo == null || "".equals(correoPromo)) {
				correoPromo = correoAsesora;
			}

			Asesor asesora = activacion.getPromotor().getAsesor();
			String nombrePromotor = activacion.getPromotor().getPersonal()
					.getNombre()
					+ " "
					+ activacion.getPromotor().getPersonal().getApellido();

			//datos de la activacion
			String tarjetaHabiente = activacion.getNombre();
			String cedulaTarjetaH = activacion.getCedula();
			String correoActivacion = activacion.getCorreo();
			String claveCadivi = activacion.getClave();
			String nombreAsesor = asesora.getPersonal().getNombre() + " "
					+ asesora.getPersonal().getApellido();
			String extensionAs = asesora.getExtension().toString();
			String correoAsesor = asesora.getPersonal().getCorreo();

			// 1. Leemos el archivo HTML del correo desde el disco y lo
			// pasamaos al objeto analizador Document
			File inputHtml = new File(
					"/EmailMonedaFrontera/activacionescadivi.html");
			Document doc = Jsoup.parse(inputHtml, "UTF-8");
			// obtengo los id's del DOM a los que deseo insertar los valores
			// mediante el metodo append() se insertan los valores obtenidos de
			// la consulta
			Element nomC = doc.select("span#nombreCliente").first();
			nomC.append(nombrePromotor);

			Element tarjetaH = doc.select("span#tarjte1").first();
			tarjetaH.append(tarjetaHabiente);

			Element tarjetaH2 = doc.select("span#tarjte2").first();
			tarjetaH2.append(tarjetaHabiente);

			Element cdula = doc.select("span#cedulaTe").first();
			cdula.append(cedulaTarjetaH);

			Element emailTte = doc.select("span#emailTarjeta").first();
			emailTte.append(correoActivacion);

			Element clave = doc.select("span#claveCa").first();
			clave.append(claveCadivi);

			Element asesor = doc.select("span#nomAser").first();
			asesor.append(nombreAsesor);

			Element ext = doc.select("span#extensionA").first();
			ext.append(extensionAs);
			// aca cambiamos la propiedad de un componente html <a> para que
			// incorpore
			// la direccion de correo en el atributo href
			doc.select("span#emailas a").attr("href", "mailto:" + correoAsesor);
			Element emailAs = doc.select("span#emailas2").first();
			emailAs.append(correoAsesor);

			// enviamos el mail

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
					"clientes-noreply@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);
			// Construimos el mensaje

			// multiples direcciones
			String[] to = { correoPromo, correoAsesora,
					"notificaciones@monedafrontera.com" };

			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"clientes-noreply@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);

			message.setSubject("MONEDA FRONTERA - ACTIVACION "
					+ tarjetaHabiente);
			// Aca se coloca el cuerpo del mensaje en HTML
			message.setContent(doc.html(), "text/html; charset=utf-8");

			// Se envia el mensaje.
			Transport t = session.getTransport("smtp");
			t.connect("clientes-noreply@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre de sesion.
			t.close();
			System.out.println("Conexion cerrada");

		} catch (Exception e) {
			System.out.println("Error al enviar correo");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param activacion
	 * Envia un correo al promotor y al asesor si la activacion no ingresa
	 * a la banca en linea
	 * 
	 */
	public void enviarEmailBancaLinea(Activacion activacion) {
		try {
			// Ubicamos la asesora correspondiente a la activacion y los datos
			// de la misma para el correo
			String correoAsesora = activacion.getPromotor().getAsesor()
					.getPersonal().getCorreo();

			String correoPromo = activacion.getPromotor().getPersonal()
					.getCorreo();
			if (correoPromo == null || "".equals(correoPromo)) {
				correoPromo = correoAsesora;
			}

			Asesor asesora = activacion.getPromotor().getAsesor();
			String nombrePromotor = activacion.getPromotor().getPersonal()
					.getNombre()
					+ " "
					+ activacion.getPromotor().getPersonal().getApellido();

			String tarjetaHabiente = activacion.getNombre();
			String bancoEmisor = activacion.getBanco().getNombrebanco();
			//debo colocar un if, si son los bancos BDT or IND se trae el usuario
			// si son BB or BDV el campo de provinet
			String usuarioBanca = null;
			if( activacion.getBanco().getCodbanco().equals("BDT") || 
					activacion.getBanco().getCodbanco().equals("IND")){
				usuarioBanca = this.usuarioOculto(activacion.getUsuariobanco()) ;
			}
			if( activacion.getBanco().getCodbanco().equals("BB") || 
					activacion.getBanco().getCodbanco().equals("BDV")){
				usuarioBanca = this.usuarioOculto(activacion.getProvinet());
			}
			
			String claveBanca = activacion.getClaveprovinet();
			String nombreAsesor = asesora.getPersonal().getNombre() + " "
					+ asesora.getPersonal().getApellido();
			System.out.println("ASESOR: " + activacion.getPromotor().getAsesor().getDocumento());
			String extensionAs = asesora.getExtension().toString();
			String correoAsesor = asesora.getPersonal().getCorreo();

			// 1. Leemos el archivo HTML del correo desde el disco y lo
			// pasamaos al objeto analizador Document
			File inputHtml = new File(
					"/EmailMonedaFrontera/activacionbancalinea.html");
			Document doc = Jsoup.parse(inputHtml, "UTF-8");
			// obtengo los id's del DOM a los que deseo insertar los valores
			// mediante el metodo append() se insertan los valores obtenidos de
			// la consulta
			Element nomC = doc.select("span#nombreCliente").first();
			nomC.append(nombrePromotor);

			Element tarjetaH = doc.select("span#tarjte1").first();
			tarjetaH.append(tarjetaHabiente);

			Element tarjetaH2 = doc.select("span#tarjte2").first();
			tarjetaH2.append(tarjetaHabiente);

			Element cdula = doc.select("span#bancoE").first();
			cdula.append(bancoEmisor);

			Element emailTte = doc.select("span#usuarioB").first();
			emailTte.append(usuarioBanca);

			Element clave = doc.select("span#claveB").first();
			clave.append(claveBanca);

			Element asesor = doc.select("span#nomAser").first();
			asesor.append(nombreAsesor);

			Element ext = doc.select("span#extensionA").first();
			ext.append(extensionAs);
			// aca cambiamos la propiedad de un componente html <a> para que
			// incorpore
			// la direccion de correo en el atributo href
			doc.select("span#emailas a").attr("href", "mailto:" + correoAsesor);
			Element emailAs = doc.select("span#emailas2").first();
			emailAs.append(correoAsesor);

			// enviamos el mail

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
					"clientes-noreply@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);
			// Construimos el mensaje

			// multiples direcciones
			String[] to = { correoPromo, correoAsesora,
					"notificaciones@monedafrontera.com" };// aca se debe cambiar

			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"clientes-noreply@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);

			message.setSubject("MONEDA FRONTERA - ACTIVACION "
					+ tarjetaHabiente);

			message.setContent(doc.html(), "text/html; charset=utf-8");

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("clientes-noreply@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();
			System.out.println("Conexion cerrada");

		} catch (Exception e) {
			System.out.println("Error al enviar correo");
			e.printStackTrace();
		}
	}
	
	/**
	 * Retorna los caracteres del usario ocultos, si es un nro de tarjeta
	 * devuelve asteriscos y los 4 ultimos numeros, si es un usuario retorna
	 * los 4 ultimas letras. 
	 * @param usuario
	 * @return
	 */
	public String usuarioOculto( String usuario){
		StringBuffer encrypt = new StringBuffer();
		char c;
		for (int i = 0; i < usuario.length()-4; i++) {
			c = 'X';
			encrypt.append(c);
		}
		String endUser = encrypt.toString() +
							usuario.substring(usuario.length()-4, usuario.length() );
		return endUser;
	}
	

	/**
	 * Envia un correo cada vez que la asesora se logea por primera vez. Esta
	 * validacion se hace en el metodo enviarEmailActivaciones() de la clase
	 * Administrar Usuario.
	 * @param proximas
	 * @param pasadas
	 * @param asesor
	 */
	public void enviarReporteAsesoras(List<Activacion> proximas,
											List<Object[]> pasadas, Asesor asesor) {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			Document doc = null;

			// Con base en las colecciones recibidas elegimos la plantilla y el
			// contenido del mensaje
			if (!proximas.isEmpty() && !pasadas.isEmpty()) {//Envia si existen ambos listados
				File inputHtml = new File(
						"/EmailMonedaFrontera/asesorasactivaciones.html");
				doc = Jsoup.parse(inputHtml, "UTF-8");
				
				String bothM = " <p>Se relaciona a continuaci&oacute;n dos listados de "
						+ "Activaciones. Uno son las Activaciones proximas a comenzar viaje, "
						+ "dentro de los siguientes 7 D&iacute;as. Y el segundo son activaciones"
						+ " que ya comenzaron viaje y no estan facturando.</p>";

				Element fec = doc.select("span#fecha").first();
				fec.append(sdf.format(new Date()));

				Element nomA = doc.select("span#nombreAsesor").first();   
				nomA.append(asesor.getPersonal().getNombre() + " " + asesor.getPersonal().getApellido());

				// Aca establece el mensaje, y los listados a mostrar con base 
				// en las colecciones recibidas
				Element mensaje = doc.select("span#mensaje").first();
				mensaje.append(bothM);
				
				Element rowProx = doc.select("table#detalleprox tbody").first();
				StringBuilder detalle = new StringBuilder();
				for( int i=0; i<proximas.size(); i++){
					System.out.println(">>consecutivo: "  +proximas.get(i).getConsecutivo()+ "fechafin: " + proximas.get(i).getFechaFinViaje());
					detalle.append(
							"<tr>" +
							"<td style=\"padding: 2px 3px 2px 3px; text-align:center;  border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							 (i+1) +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							proximas.get(i).getCedula() +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							 proximas.get(i).getNombre() +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							proximas.get(i).getBanco().getNombrebanco() +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px;  border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							"<strong>"+
							sdf.format(proximas.get(i).getFechaInicioViaje()) +
							"</strong>"+
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							sdf.format(proximas.get(i).getFechaFinViaje()) +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; text-align:center; border-color:#666666; \">"+
							"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							((proximas.get(i).getCupoaprobado() == null )? "": proximas.get(i).getCupoaprobado() )+
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							proximas.get(i).getPromotor().getPersonal().getNombre()+" "+
							proximas.get(i).getPromotor().getPersonal().getApellido()+
							"</span>" +
							"</td>" +
							"</tr>");					
				}//fin del ciclo que imprime la tabla
				
				rowProx.html(detalle.toString());
				
				Element rowPasa = doc.select("table#detallepasadas tbody").first();
				StringBuilder detalleP = new StringBuilder();
				for( int i=0; i<pasadas.size(); i++){
					detalleP.append(
							"<tr>" +
							"<td style=\"padding: 2px 3px 2px 3px; text-align:center; border-color:#666666; \">"+
							"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif;  display:block; color:#333333\">"+
							(i+1) +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							pasadas.get(i)[0] +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							pasadas.get(i)[1] +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							pasadas.get(i)[2] +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							sdf.format(pasadas.get(i)[3]) +
							"</span>" +
							"</td>" +
							"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
							"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
							pasadas.get(i)[4] +
							"</span>" +
							"</td>"+"</tr>");		
				}//fin del ciclo que imprime la tabla
				rowPasa.append(detalleP.toString());

			} else {
				//Envia si existen solo por empezar
				
				if (!proximas.isEmpty() && pasadas.isEmpty()) {
					System.out.println("Ingrese IF Proximas Activaciones");
					
					File inputHtml = new File(
							"/EmailMonedaFrontera/asesorasactivacionesprox.html");
					doc = Jsoup.parse(inputHtml, "UTF-8");

					String proxM = " <p>Se relaciona a continuaci&oacute;n listado de Activaciones "
						+ "proximas a comenzar viaje, dentro de los siguientes 7 D&iacute;as.</p>";
					
					Element fec = doc.select("span#fecha").first();
					fec.append(sdf.format(new Date()));

					Element nomA = doc.select("span#nombreAsesor").first();
					nomA.append(asesor.getPersonal().getNombre() + " "
							+ asesor.getPersonal().getApellido());

					// Aca establece el mensaje, y los listados a mostrar con
					// base en las colecciones recibidas
					Element mensaje = doc.select("span#mensaje").first();
					mensaje.append(proxM);

					Element rowProx = doc.select("table#detalleprox tbody").first();
					StringBuilder detalle = new StringBuilder();
					for( int i=0; i<proximas.size(); i++){
						detalle.append(
								"<tr>" +
								"<td style=\"padding: 2px 3px 2px 3px; text-align:center; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif;  display:block; color:#333333\">"+
								(i+1) +
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								 proximas.get(i).getCedula() +
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								 proximas.get(i).getNombre() +
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								proximas.get(i).getBanco().getNombrebanco() +
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								"<strong>"+
								sdf.format(proximas.get(i).getFechaInicioViaje()) +
								"</strong>"+
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								sdf.format(proximas.get(i).getFechaFinViaje()) +
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; text-align:center; border-color:#666666; \">"+
								"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								((proximas.get(i).getCupoaprobado() == null )? "": proximas.get(i).getCupoaprobado() )+
								"</span>" +
								"</td>" +
								"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
								"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
								proximas.get(i).getPromotor().getPersonal().getNombre()+" "+
								proximas.get(i).getPromotor().getPersonal().getApellido()+
								"</span>" +
								"</td>" +
								"</tr>");	 
						
					}//fin del ciclo que imprime la tabla
					
					rowProx.html(detalle.toString());					
					
				} else {
					//Envia si existen activaciones pasadas
					
					if (proximas.isEmpty() && !pasadas.isEmpty()) {
						
						System.out.println("Ingrese IF Pasdas Activaciones");

						File inputHtml = new File(
						"/EmailMonedaFrontera/asesorasactivacionespasada.html");
						doc = Jsoup.parse(inputHtml, "UTF-8");

						String pasadaM = " <p>Se relaciona a continuaci&oacute;n listado de Activaciones "
							+ "que ya comenzaron viaje y aun no han empezado a facturar.</p>";

						Element fec = doc.select("span#fecha").first();
						fec.append(sdf.format(new Date()));

						Element nomA = doc.select("span#nombreAsesor").first();
						nomA.append(asesor.getPersonal().getNombre() + " "
								+ asesor.getPersonal().getApellido());

						// Aca establece el mensaje, y los listados a mostrar
						// con
						// base en las colecciones recibidas
						Element mensaje = doc.select("span#mensaje").first();
						mensaje.append(pasadaM);

						Element rowPasa = doc.select("table#detallepasadas tbody").first();
						StringBuilder detalleP = new StringBuilder();
						for( int i=0; i<pasadas.size(); i++){
							detalleP.append(
									"<tr>" +
									"<td style=\"padding: 2px 3px 2px 3px; text-align:center; border-color:#666666; \">"+
									"<span style=\"font-size: 11px; font-family: Arial,Helvetica,sans-serif;  display:block; color:#333333\">"+
									(i+1) +
									"</span>" +
									"</td>" +
									"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
									"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
									pasadas.get(i)[0] +
									"</span>" +
									"</td>" +
									"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
									"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
									pasadas.get(i)[1] +
									"</span>" +
									"</td>" +
									"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
									"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
									pasadas.get(i)[2] +
									"</span>" +
									"</td>" +
									"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
									"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
									sdf.format(pasadas.get(i)[3]) +
									"</span>" +
									"</td>" +
									"<td style=\"padding: 2px 3px 2px 3px; border-color:#666666; \">"+
									"<span style=\"font-size: 12px; font-family: Arial,Helvetica,sans-serif; display:block; color:#333333\">"+
									pasadas.get(i)[4] +
									"</span>" +
									"</td>"+"</tr>");		
						}
						rowPasa.append(detalleP.toString());
						
					}
				}
			}//fin del else principal

			// envia el mail

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
					"notificaciones@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);
			// Construimos el mensaje

			// multiples direcciones
			String[] to = { asesor.getPersonal().getCorreo(), /*"lortiz@monedafrontera.com",*/
							"notificaciones@monedafrontera.com" };

			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"notificaciones@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);
			message.setSubject("REPORTE DE ACTIVACIONES -"
					+ sdf.format(new Date()));
			message.setContent(doc.html(), "text/html; charset=utf-8");

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("notificaciones@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();
			System.out.println("Conexion cerrada");

		} catch (Exception e) {
			System.out.println("Error al enviar correo");
			e.printStackTrace();
		}

	} 
	
	
	
	/**
	 * 
	 * @param activacion
	 */
	public void enviarEmailContactoGerencia(Promotor pr) {
		try {
			
			String correoAsesora = pr.getAsesor().getPersonal().getCorreo();
			String correoPromo = pr.getPersonal().getCorreo();
			String nombrePromotor = pr.getPersonal().getNombre();
			
			// 1. Leemos el archivo HTML del correo desde el disco y lo
			// pasamaos al objeto analizador Document 
			File inputHtml = new File(
					"/EmailMonedaFrontera/informacioncontacto.html");
			Document doc = Jsoup.parse(inputHtml, "UTF-8");
			
			// obtengo los id's del DOM a los que deseo insertar los valores
			Element nomC = doc.select("span#nombreCliente").first();
			nomC.append(nombrePromotor);

			// enviamos el mail

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
					"clientes-noreply@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);
			// Construimos el mensaje

			// multiples direcciones			
			String[] to = {correoPromo, "notificaciones@monedafrontera.com" };
			
			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"clientes-noreply@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);

			message.setSubject("INFORMACION IMPORTANTE - MONDEA FRONTERA");

			message.setContent(doc.html(), "text/html; charset=utf-8");

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("clientes-noreply@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();
			System.out.println("Conexion cerrada");

		} catch (Exception e) {
			System.out.println("Error al enviar correo");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * @param activacion
	 */
	public void emailAlertLoginUsuario(String usuario, Date lastDate, String lastIp, 
													Date currentDate, String currentIp) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yyyy - hh:mm:ss aaa");
			
			// 1. Leemos el archivo HTML del correo desde el disco y lo
			// pasamaos al objeto analizador Document 
			File inputHtml = new File(
					"/EmailMonedaFrontera/usuariobloqueado.html");
			Document doc = Jsoup.parse(inputHtml, "UTF-8");
			
			// obtengo los id's del DOM a los que deseo insertar los valores
			Element nomUser = doc.select("span#user").first();
			nomUser.append(usuario);
			
			Element userDetail= doc.select("span#user2").first();
			userDetail.append(usuario);
			
			Element horaAnterio = doc.select("span#horaanterior").first();
			horaAnterio.append(sdf.format(lastDate));
			
			Element ipAnterior = doc.select("span#ipini").first();
			ipAnterior.append(lastIp);
			
			Element horaActual = doc.select("span#ultimahora").first();
			horaActual.append(sdf.format(currentDate));
			
			Element ipActual = doc.select("span#ipfin").first();
			ipActual.append(currentIp);
			
			
			// enviamos el mail

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "192.168.1.6");
			props.setProperty("mail.smtp.port", "25");// puerto de salida, de
			// entrada 110
			props.setProperty("mail.smtp.user",
					"notificaciones@monedafrontera.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.transport.protocol.", "smtp");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);
			// Construimos el mensaje

			// multiples direcciones			
			String[] to = {	"lortiz@monedafrontera.com",
							"gerencia@monedafrontera.com"};
			
			// arreglo con las direcciones de correo
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(
					"notificaciones@monedafrontera.com"));
			message.setRecipients(Message.RecipientType.BCC, addressTo);

			message.setSubject("Se ha evitado un inicio de sesion sospechoso");

			message.setContent(doc.html(), "text/html; charset=utf-8");

			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect("notificaciones@monedafrontera.com", "Carlos0411");
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();
			System.out.println("Conexion cerrada");

		} catch (Exception e) {
			System.out.println("Error al enviar correo");
			e.printStackTrace();
		}
		
	}
	
	
	
	

}// fin de la clase EnviarMailAlertas
