package org.domain.moneda.util;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


import org.domain.moneda.bussiness.AdministrarUsuario;
import org.domain.moneda.entity.Asesor;
import org.domain.moneda.session.ReportesHome;
import org.domain.moneda.session.TarjetaHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;


@Name("Reporteador")
@Scope(CONVERSATION)
public class Reporteador {
	 
	@In	
	private EntityManager entityManager;
	
	@In 
	private FacesMessages facesMessages;	
	
	String path = "../reportesmoneda/";
	
	@In(create=true) @Out 
	private ReportesHome reportesHome;
	
	@In(create=true) @Out
	private AdministrarUsuario AdministrarUsuario;
	
	public String generarReportePDFNombre(Object param1, Object param2, 
										  Object param3, Object param4, 
										  Object param5, String nombre){
		this.nombrereporte = nombre;
		
		generarReportePDF(param1, param2, 
				param3, param4, param5);
		return null;
	}
	
	
	public String generarReportePDFNombre6( Object param1, Object param2, 
											Object param3, Object param4, 
											Object param5, Object param5a, 
											String nombre){
		
		this.nombrereporte = nombre;		
		String param5p = "" + param5 + "-"+ param5a;
		
		return generarReportePDF(param1, param2, 
				param3, param4, param5p);
		
	}
	
	//Genera el balance del promotor y audita esta accion del usuario
	public String generarBalancePromotor( Object param1, Object param2, 
										  Object param3, Object param4, 
										  Object param5, Object param5a, 
										  String nombre){
		
		this.nombrereporte = nombre;		
		String param5p = "" + param5 + "-"+ param5a;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");//para el auditor				
/*		
		AdministrarUsuario.auditarUsuario( 24, "Genero balance en PDF del promotor: "+ param2+
					" en la fecha: " + sdf.format(param1) );
*/
		return generarReportePDF(param1, param2, 
				param3, param4, param5p);
	}
	
	
	
	public String generarBalanceCliente( )
	{
		String str = "";
		return str;
	}
	
	String reporten = "";	
	
	public String getReporten() {
		return reporten;
	}

	public void setReporten(String reporten) {
		this.reporten = reporten;
	}

	public void unreporte(OutputStream stream, Object object){
		path = "../server/default/deploy/moneda.war/facturas/";
		String documento = "/ReportesMoneda/unreporte.jasper";
		//String ficheros = "ruta2.jasper";
		long inicio = System.currentTimeMillis();
		String doc = "unreporte"+inicio+".pdf"; 

		String destFileNamePdf = path+doc;
		Map parameters = new HashMap();
		
		
		System.out.println("*****************+*++carlosrene");
		try {

			UtilidadesBD u = new UtilidadesBD();
			System.out.println("*****************+*++MonedaFront");
			Connection c = u.obtenerConnection();
			System.out.println("*****************+*++MonedaFront"+documento);
			JasperPrint jasperPrint = JasperFillManager.fillReport(documento,  parameters, c);
			System.out.println("Aca vamos");
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);
			System.out.println("*****************+*++MonedaFront"+documento);
			c.close();
			
			String DOWNLOAD_PATH = path+doc;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext(); 
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			//DOWNLOAD_PATH = "../reportesmoneda/unreporte1354382641071.pdf";
			File file = new File(DOWNLOAD_PATH);
			
			System.out.println(path+doc);
			
			if(file != null)//fileURL != null) 
			{
				byte[] fileData = new byte[(int)file.length()];
				FileInputStream inputStream = new FileInputStream(file);
				inputStream.read(fileData);
				ServletOutputStream os = response.getOutputStream();	
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachement; filename="+doc+"");
				int readBytes = 0;
				//read from the file; write to the ServletOutputStream
				int i = 0;
				System.out.println(fileData);
				stream.write(fileData);
				os.write(fileData);
				os.close();
				System.out.println("Finaliza");
				reporten = doc;
				//facesContext.responseComplete();
				System.out.println("Nom reporte "+reporten);

			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch (JRException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		}  
		//System.out.println("Generando Nomina 5");
	}
	
	public void generarFactura(Integer numtransaccion){
		path = "../server/default/deploy/moneda.war/facturas/";
		String documento = "/ReportesMoneda/factura001.jasper";
		//String ficheros = "ruta2.jasper";
		long inicio = System.currentTimeMillis();
		String doc = "factura"+inicio+".pdf"; 

		String destFileNamePdf = path+doc;
		Map parameters = new HashMap();
		parameters.put("param1", numtransaccion);
		
		System.out.println("*****************+*++carlosrene");
		try {

			UtilidadesBD u = new UtilidadesBD();
			System.out.println("*****************+*++MonedaFront");
			Connection c = u.obtenerConnection();
			System.out.println("*****************+*++MonedaFront"+documento);
			JasperPrint jasperPrint = JasperFillManager.fillReport(documento,  parameters, c);
			System.out.println("Aca vamos");
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);
			System.out.println("*****************+*++MonedaFront"+documento);
			c.close();
			
			String DOWNLOAD_PATH = path+doc;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext(); 
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			
			HttpServletRequestWrapper h = new HttpServletRequestWrapper((HttpServletRequest) externalContext.getRequest());
			
			System.out.println(h.getRequestURI());
			System.out.println(h.getContextPath());
			System.out.println(h.getLocalPort());
			System.out.println(h.getServletPath());
			
			//DOWNLOAD_PATH = "../reportesmoneda/unreporte1354382641071.pdf";
			File file = new File(DOWNLOAD_PATH);
			
			reporten = doc;
			
			InetAddress addr = InetAddress.getLocalHost();


            System.out.println("IP: " + addr.getHostAddress());
            System.out.println("IP: " + addr.getHostName());
            System.out.println("IP: " + addr.getCanonicalHostName());
            			
			Desktop.getDesktop().browse(new URI("http://localhost:"+h.getLocalPort()+""+h.getContextPath()+"/facturas/"+doc));
			/*
			
			System.out.println(path+doc);
			if(file != null)//fileURL != null) 
			{
				byte[] fileData = new byte[(int)file.length()];
				FileInputStream inputStream = new FileInputStream(file);
				inputStream.read(fileData);
				ServletOutputStream os = response.getOutputStream();	
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachement; filename="+doc+"");
				int readBytes = 0;
				//read from the file; write to the ServletOutputStream
				int i = 0;
				System.out.println(fileData);
				
				os.write(fileData);
				os.close();
				System.out.println("Finaliza");
				reporten = doc;
				//facesContext.responseComplete();
				System.out.println("Nom reporte "+reporten);

			}
			
			*/
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch (JRException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String generarReportePDF(Object param1, Object param2, 
									Object param3, Object param4, 
									Object param5){
		
		System.out.println("Generando el reporte.........");
		if(reportesHome.getInstance().getCodreporte()!=null){
			this.nombrereporte = reportesHome.getInstance().getCodreporte();
		}
		
		String documento = "/ReportesMoneda/"+this.nombrereporte+".jasper";
		
		System.out.println("[Path] " + documento);
		
		//String ficheros = "ruta2.jasper";
		long inicio = System.currentTimeMillis();
		String doc = nombrereporte+inicio+".pdf"; 
		
		

		String destFileNamePdf = path + doc;
		Map parameters = new HashMap();
		
		parameters.put("param1", param1);
		parameters.put("param2", param2);
		parameters.put("param3", param3);
		parameters.put("param4", param4);
		parameters.put("param5", param5);
		System.out.println("param1: " + param1);
		System.out.println("param2: " + param2);
		System.out.println("param3: " + param3);
		System.out.println("param4: " + param4);
		System.out.println("param5: " + param5);
		try {

			UtilidadesBD u = new UtilidadesBD();
			
			System.out.println("Creando Conexion...");
			Connection c = u.obtenerConnection();
			
			System.out.println("Creando el reporte: "+documento);
			JasperPrint jasperPrint = 
						JasperFillManager.fillReport(documento,  parameters, c);
			
			System.out.println("Exportando el informe...");
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);
			
			c.close();
			u.mostrarFormato(path,doc);//
			
			return destFileNamePdf;

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		} catch (JRException e) {
//			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}	
		//System.out.println("Generando Nomina 5");
	}	
	
	/**
	 * Genera la consulta actual de la 
	 * pagina ActivacionList.
	 * @param param1 documento promotor
	 * @param param2 documento gestor
	 * @param param3 documento asesor
	 * @param param4 codigo banco
	 * @param param5 codigo estado
	 * @param param6 true/false rusad 
	 * @param param7 fecha inicio
	 * @param param8 fecha fin
	 * @param param9 true/false documentos completos
	 * @param param10 nombre del reporte
	 * @return path
	 */
	public String generarReporteActivaciones( Object param1, Object param2,
											  Object param3, Object param4,
											  Object param5, Object param6,
											  Object param7, Object param8,
											  Object param9, String nombreReporte){
		
		this.nombrereporte = nombreReporte;
		
		/*if( reportesHome.getInstance().getCodreporte()!=null ){
			this.nombrereporte = reportesHome.getInstance().getCodreporte();
		}*/
		
		String documento = "/ReportesMoneda/"+this.nombrereporte+".jasper";
		
		System.out.println("URL documento> " + documento);		
		
		long inicio = System.currentTimeMillis();
		
		String docSalida = nombrereporte+inicio+".pdf"; 

		String destFileNamePdf = path + docSalida;
		Map<String, Object > parameters = new HashMap< String, Object >();
		
		parameters.put("param1", param1);
		parameters.put("param2", param2);
		parameters.put("param3", param3);
		parameters.put("param4", param4);
		parameters.put("param5", param5);
		parameters.put("param6", param6);
		parameters.put("param7", param7);
		parameters.put("param8", param8);
		parameters.put("param9", param9);
		System.out.println("Tabla de Parametros");
		System.out.println("******* promotor  " + param1);
		System.out.println("******* gestor    " + param2);
		System.out.println("******* asesor    " + param3);
		System.out.println("******* banco     " + param4);
		System.out.println("******* estado    " + param5);
		System.out.println("******* rusad     " + param6);
		System.out.println("******* fechaini  " + param7);
		System.out.println("******* fechafin  " + param8);
		System.out.println("******* doccomple " + param9);
		System.out.println("");
		try {

			UtilidadesBD u = new UtilidadesBD();
			System.out.println("***********Aca vamos1");
			Connection conex = u.obtenerConnection();
			System.out.println("***********Aca vamos2 documento>>" + documento);
			JasperPrint jasperPrint = JasperFillManager.fillReport(documento,  parameters, conex);
			System.out.println("***********Aca vamos3");
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);
			conex.close();
			u.mostrarFormato(path,docSalida);
			return destFileNamePdf;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		} catch (JRException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}		
	}//fin del metodo
	
	
	
	
	public String generarReporteXls(Object param1, Object param2, 
			Object param3, Object param4, Object param5){
		
		String documento = "/ReportesMoneda/"+this.nombrereporte+".jasper";
		//String ficheros = "ruta2.jasper";
		long inicio = System.currentTimeMillis();
		String doc = nombrereporte+inicio+".xls"; 

		String destFileNamePdf = path+doc;
		Map parameters = new HashMap();
		
		parameters.put("param1", param1);
		parameters.put("param2", param2);
		parameters.put("param3", param3);
		parameters.put("param4", param4);
		parameters.put("param5", param5);
		
		try {
			UtilidadesBD u = new UtilidadesBD();
			Connection c = u.obtenerConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(documento,  parameters, c);
			System.out.println("Aca vamos");
			
			JExcelApiExporter xlsExporter = new JExcelApiExporter();
			JRXlsExporter exporterXLS = new JRXlsExporter();

		    xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.TRUE);
			xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,destFileNamePdf);
			
			xlsExporter.exportReport();			
			
			//JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);
			c.close();
			u.mostrarFormato(path,doc);
			return null;			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		} 
		catch (JRException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}  catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		} 
		//return null;
		//System.out.println("Generando Nomina 5");
	}
	
	
	Date fechainicio;
	Date fechafin;
	String nombrereporte;
	
    Asesor asesor;	
	
	public Asesor getAsesor() {
		return asesor;
	}

	public void setAsesor(Asesor asesor) {
		this.asesor = asesor;
	}
	

	public Date getFechainicio() {	
		return fechainicio;
	}
	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}
	public Date getFechafin() {
		return fechafin;
	}
	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}
	public String getNombrereporte() {
		return nombrereporte;
	}
	public void setNombrereporte(String nombrereporte) {
		this.nombrereporte = nombrereporte;
	}
	
	public Boolean renderParametros(Short ingreso, Integer comparacion){
		System.out.println("Ingreso " + ingreso);
		if(ingreso>0)
		if(ingreso%comparacion == 0){
			return true;
		}
		return false;
	}
	
	//agrupa los numeros de tarjeta con base en la distribucion de los 
	//numeros en el plastico
	public String formatearTarjeta(String numero)
	{
		String numeroformateado = null;
		
		switch( numero.length())
		{
		case 16://formatea Visa y MasterCard
			numeroformateado =  numero.substring(0, numero.length()-12) + " " +
			numero.substring(numero.length()-12, numero.length()-8) + " " +
			numero.substring(numero.length()-8, numero.length()-4) + " " +
			numero.substring(numero.length()-4, numero.length());			
			return numeroformateado;			
			
		case 15://formatea American Express
			numeroformateado =  numero.substring(0, numero.length()-11) + " " +
			numero.substring(numero.length()-11, numero.length()-5) + " " +
			numero.substring(numero.length()-5, numero.length());			
			return numeroformateado;
			
		case 14://formatea Diners
			numeroformateado =  numero.substring(0, numero.length()-11) + " " +
			numero.substring(numero.length()-11, numero.length()-5) + " " +
			numero.substring(numero.length()-5, numero.length());			
			return numeroformateado;
			
		default://Sin numero de tarjeta
			return numeroformateado;
		}//fin del switch		
	}//fin del metodo formatearTarjetaSistemas
	
	
	
	/*
	public String formatearTarjeta(String numero)
	{
		String numeroformateado = null;
		
			numeroformateado =  numero.substring(0, numero.length()-12) + " " +
			numero.substring(numero.length()-12, numero.length()-8) + " " +
			numero.substring(numero.length()-8, numero.length()-4) + " " +
			numero.substring(numero.length()-4, numero.length());			
		
		return numeroformateado; 
	}//fin del metodo formatearTarjeta
	*/
	
	
	public void salida(Date Fechini, Date Fechafin)
	{
		System.out.println("Impresion de prueba " + Fechini + " "+ Fechafin);
		
	}
	

	
}//fin de la clase Reporteador
