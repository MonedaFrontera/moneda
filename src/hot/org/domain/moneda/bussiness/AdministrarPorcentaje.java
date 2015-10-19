package org.domain.moneda.bussiness;

import javax.persistence.EntityManager;

import org.domain.moneda.session.PorcentajecomisiontxHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("AdministrarPorcentaje")
@Scope(ScopeType.CONVERSATION)
public class AdministrarPorcentaje {
	
	@Logger private Log log;

    @In StatusMessages statusMessages; 

    public void administrarPorcentaje()
    {
        // implement your business logic here
        log.info("AdministrarPorcentaje.administrarPOrcentaje() action called");
        statusMessages.add("administrarPorcentaje");
    } 
    
    @In private FacesMessages facesMessages;
    
    @In
	private EntityManager entityManager;
    
    @In(create=true) @Out 
    PorcentajecomisiontxHome porcentajecomisiontxHome;
    
    public String guardar(){
    	
    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
    	
    	String sql = "update public.porcentajecomisiontx set " +
    	"fechafin = to_date('" + sdf.format(porcentajecomisiontxHome.getInstance().getId().getFechainicio()) + "','dd/mm/yyyy') - 1 " +
    	"where fechafin is null and fechainicio < to_date('"+sdf.format(porcentajecomisiontxHome.getInstance().getId().getFechainicio())+"','dd/mm/yyyy') ";
    	
    	
    	
    	porcentajecomisiontxHome.persist();
    	
    	return null;
    }

}
