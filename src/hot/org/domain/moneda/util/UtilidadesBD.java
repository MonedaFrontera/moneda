package org.domain.moneda.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class UtilidadesBD {

	public Connection obtenerConnection() throws SQLException{
		DataSource datasource = null;
        try {
            InitialContext context = new InitialContext();
            datasource = (DataSource)context.lookup("java:/monedaDatasource");
        } catch(NamingException e) {
        	//System.out.println("Exception de la Liquidacion");    
        	assert false : "Unable to create directory context!";
        }        
        return datasource.getConnection();
	}
	
	public void mostrarFormato(String path, String doc) throws IOException{
		String DOWNLOAD_PATH = path+doc;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext(); 
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		
		File file = new File(DOWNLOAD_PATH);		

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
			os.write(fileData);
			os.close();
			facesContext.responseComplete();
		}	
	}
	
	public void openClietMail(String path,  URI uri) throws IOException{
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext(); 
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		ServletOutputStream os = response.getOutputStream();	
		response.setContentType("application/browser");
		facesContext.responseComplete();
		
	}
	
	
	
	public void backup(){
		try {
	        String path = "/prueba.backup";
	        Runtime r = Runtime.getRuntime();

	        //PostgreSQL variables
	        String user = "monedafront";
	        String dbase = "monedafront3";
	        String password = "moneda";
	        Process p;
	        ProcessBuilder pb;

	        /**
	* Ejecucion del proceso de respaldo
	*/
	        r = Runtime.getRuntime();
	        pb = new ProcessBuilder("pg_dump", "-v", "-D", "-f", path, "-U", user, dbase);
	        pb.environment().put("PGPASSWORD", password);
	        pb.redirectErrorStream(true);
	        p = pb.start();

	    } catch (Exception e) {
	}
	}
	
}
