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
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

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
	 
	@Logger
	private Log log;
	
	@In	
	private EntityManager entityManager;
	
	@In 
	private FacesMessages facesMessages;	
	
	String path = "../reportesmoneda/";
	
	@In(create=true) @Out 
	private ReportesHome reportesHome;
	
	@In(create=true) @Out
	private AdministrarUsuario AdministrarUsuario;

	private Date fechainicio;
	private Date fechafin;
	private String nombrereporte;
    private Asesor asesor;
	
	
	
	String reporten = "";	
	
	public String getReporten() {
		return reporten;
	}

	public void setReporten(String reporten) {
		this.reporten = reporten;
	}
	
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
	
	/**
	 * Genera un reporte en formato PDF, recibiendo parametros de longitud variable
	 * <a href="http://docs.oracle.com/javase/1.5.0/docs/guide/language/varargs.html" target="_blank" >(Elipsis)</a>. 
	 * Se envia como primer parametro el nombre del reporte, y un numero arbitrario 
	 * de parametros, segun los que establezca el reporte a generar.
	 * @param nombreReporte
	 * @param params
	 * @return
	 */
	public String generarReportePDFElipsis( String nombreReporte, Object...params){
		
		//Se establece el nombre del reporte
		if(this.getNombrereporte() == null ){
			this.setNombrereporte(nombreReporte);
		}
		//Nombre y ubicacion del reporte a utlizar
		String documento = "/ReportesMoneda/"+this.getNombrereporte()+".jasper";
		
		log.info("[Path-] " + documento);		
		long inicio = System.currentTimeMillis();
		String doc = this.getNombrereporte()+inicio+".pdf"; 
		//Destino y nombre del reporte en el servidor
		String destFileNamePdf = path + doc;
		Map parameters = new HashMap();
		for(int i = 0; i< params.length ; i++){
			parameters.put("param"+ (i+1), params[i]);
			log.info("param"+ (i+1)  + ": " + params[i]);
		}
		try {
			UtilidadesBD u = new UtilidadesBD();			
			log.info("Creando Conexion...");
			Connection c = u.obtenerConnection();			
			log.info("Creando el reporte: " + documento);
			JasperPrint jasperPrint = 
						JasperFillManager.fillReport(documento,  parameters, c);			
			log.info("Exportando el informe...");
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);			
			c.close();
			u.mostrarFormato(path,doc);//			
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
		try {
			UtilidadesBD u = new UtilidadesBD();
			Connection c = u.obtenerConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(documento,  parameters, c);
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf);
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

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch (JRException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
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
	
}//fin de la clase Reporteador
