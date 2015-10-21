package org.domain.moneda.bussiness;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Pais;
import org.domain.moneda.entity.Paisiso;
import org.domain.moneda.session.PaisHome;
import org.domain.moneda.session.PorcentajecomisiontxHome;
import org.domain.moneda.util.ExpresionesRegulares;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("AdministrarPais")
@Scope(ScopeType.CONVERSATION)
public class AdministrarPais {
@Logger private Log log;
    
    @In
	private EntityManager entityManager;
    
    @In 
    StatusMessages statusMessages;
    
    @In 
    private FacesMessages facesMessages;
    
    @In(create=true) @Out 
	private PorcentajecomisiontxHome porcentajecomisiontxHome ;
    
    @In(create=true) @Out 
	private PaisHome paisHome;
    
    public void administrarPais()
    {
        // implement your business logic here
        log.info("AdministrarPais.administrarPais() action called");
        statusMessages.add("administrarPais");
    }
    
    /**
     * Valida que el codigo del pais no este repetido, 
     * el codigo esta compuesto por el estandar Codigos Pais 
     * ISO A2 mas dos digitos que representan los subpaises. 
     */
    public void validarCodigo()
    {    	
    	String codPais = paisHome.getInstance().getCodigopais().trim().toUpperCase();
    	String codFinal = codPais;
    	
    	try {
    		
    		if( codPais.length() < 4 )
        		facesMessages.addToControl("codigopais", 
           			 "El codigo ingresado no es valido, revise la informacion." );
    		
    		if( codPais.substring(2,4).equals("00") )
    			facesMessages.addToControl("codigopais", 
    					"El codigo del pais no debe tener digitos 00, cambielo por un " +
    			"valor entre 01 a 99");
    		
        	String codigo = 
        		(String) entityManager.createNativeQuery( "SELECT public.pais.codigopais " +
        		"FROM public.pais WHERE public.pais.codigopais = '" + codPais + "'").getSingleResult();    	
        	
        	//valida que el codigo del pais no este repetido, si esta repetido
        	//sugiere el nuevo codigo del pais
        	if( codigo != null ){
        		BigDecimal conse = (BigDecimal) entityManager.createNativeQuery(
        		"SELECT "+
        		"to_number( max(substr( public.pais.codigopais, 3,4)), '99G999D9S' ) + 1  "+
        		"FROM "+
        		"public.pais " +
        		"WHERE "+
        		"substr(public.pais.codigopais,1,2) = substr('" + codigo + "',1,2)").getSingleResult();
        		
        		if( conse.intValue() < 10 )
        		{        			
        			String consedos = "0" + conse.intValue();
        			codFinal = codPais.substring(0,2) + consedos;
        		}else{
        			codFinal = codPais.substring(0,2) + conse;            		        			
        		}          		
        		facesMessages.addToControl("codigopais", 
    			 "El codigo de este pais \""+ codPais  + "\" ya existe, se asignara " +
    			 		"el siguiente codigo, " + codFinal);
        		
        		paisHome.getInstance().setCodigopais(codFinal.toUpperCase() );
        	}
			
		} catch (Exception e) {
			
		}    	 
    	 paisHome.getInstance().setCodigopais(codFinal.toUpperCase() );
    }
    
    
    public String guardar(){
    	/*
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	Pais pais = entityManager.find(Pais.class, promotorcomisiontxHome.getInstance().getId().getCodpais());
    	
    	Promotor promotor = entityManager.find(Promotor.class, promotorcomisiontxHome.getInstance().getId().getDocumento()); 
    	
    	entityManager.createQuery("update Promotorcomisiontx " +
    			"set fechafin = to_date('"+sdf.format(promotorcomisiontxHome.getInstance().getId().getFechainicio())+"','dd/mm/yyyy') - 1" +
    					"where fechafin is null").executeUpdate();
    	promotorcomisiontxHome.getInstance().setPais(pais);
    	promotorcomisiontxHome.getInstance().setPromotor(promotor);
    	promotorcomisiontxHome.persist();
    	entityManager.flush();
    	entityManager.clear();*/
    	
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		
    	String codpais = "CO";
    	
    	System.out.println("Pais " + porcentajecomisiontxHome.getInstance().getId().getCodpais());
    	System.out.println("Pais Objeto " + porcentajecomisiontxHome.getInstance().getPais().getCodigopais());
    	System.out.println("Porcentaje " + porcentajecomisiontxHome.getInstance().getPorcentaje());
    	
    	if(porcentajecomisiontxHome.getInstance().getPais().getCodigopais() != null){
    		codpais = porcentajecomisiontxHome.getInstance().getPais().getCodigopais();
    	}
    	
    	System.out.println("Pais " + codpais);
    	
    	Pais pais = entityManager.find(Pais.class,codpais); 
    	
    	
    	entityManager.createQuery("update Porcentajecomisiontx p " +
    			"set p.fechafin = to_date('"+sdf.format(porcentajecomisiontxHome.getInstance().getId().getFechainicio())+"','dd/mm/yyyy') - 1" +
    					"where p.fechafin is null and " +
    					"p.id.codpais = '"+codpais+"'").executeUpdate();
    	
    	entityManager.flush();
    	entityManager.clear();
    	System.out.println("Probando la actualizacion");
    	porcentajecomisiontxHome.getInstance().getId().setCodpais(codpais);
    	porcentajecomisiontxHome.getInstance().setPais(pais);
    	porcentajecomisiontxHome.persist();
    	porcentajecomisiontxHome.clearInstance();
    	return "persisted";
    }
    
    
    // Pasa a mayuscula la primera letra del nombre - elimina espacios
	public void nombreMayuscula(String nombre) {
		System.out.println(">>>>>NOMBRE DE PAIS RECIBIDO: " + nombre);
		String nombreTemp = ExpresionesRegulares.eliminarEspacios(nombre, true);
		System.out.println(">>>>>NOMBRE DE PAIS EDITADO : " + nombreTemp);
		paisHome.getInstance().setNombre(nombreTemp);
		
	}
	
	
	public String guardarPais(){
		
		Paisiso paisIso = entityManager.find( 
				Paisiso.class, paisHome.getInstance().getCodigopais().substring(0, 2));
		
		paisHome.getInstance().setPaisiso( paisIso);
		String nombreTemp = ExpresionesRegulares.eliminarEspacios(paisHome.getInstance().getNombre(), true);
		paisHome.getInstance().setNombre(nombreTemp);
		entityManager.persist( paisHome.getInstance() );
		entityManager.flush();
		entityManager.clear();
		
		return "persisted";
		
	}
	
   public String actualizarPais(){
	   
	   String nombreTemp = ExpresionesRegulares.eliminarEspacios(paisHome.getInstance().getNombre(), true);
		paisHome.getInstance().setNombre(nombreTemp);
		entityManager.persist(paisHome.getInstance());
		entityManager.flush();
		entityManager.clear();
	   return "updated";
   }
}
