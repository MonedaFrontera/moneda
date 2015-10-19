package org.domain.moneda.bussiness;

import org.domain.moneda.entity.Asesor;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarVariable")
@Scope(ScopeType.SESSION)
public class AdministrarVariable
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    Asesor asesor = new Asesor();
    
    String nombreusuario = "";

    public void administrarVariable()
    {
        // implement your business logic here
        log.info("AdministrarVariable.administrarVariable() action called");
        statusMessages.add("administrarVariable");
    }

	public Asesor getAsesor() {
		return asesor;
	}

	public void setAsesor(Asesor asesor) {
		this.asesor = asesor;
	}

	public String getNombreusuario() {
		
		return nombreusuario;
	}

	public void setNombreusuario(String nombreusuario) {
		this.nombreusuario = nombreusuario;
	}
	
	

    // add additional action methods
    
    

}
