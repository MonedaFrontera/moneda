package org.domain.moneda.bussiness;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Banner;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("AdministrarVista")
@Scope(ScopeType.CONVERSATION)
public class AdministrarVista {
	@Logger private Log log;

    @In StatusMessages statusMessages;
    
    @In
	private EntityManager entityManager;
    
    @In
    private FacesMessages facesMessages;
    
    List<Banner> listaBanner = new ArrayList<Banner>();

	public List<Banner> getListaBanner() {
		listaBanner = entityManager.createQuery("select b from Banner b where b.estado = true and " +
				"now() between b.fechainicio and b.fechafin").getResultList();
		return listaBanner;
	}

	public void setListaBanner(List<Banner> listaBanner) {
		this.listaBanner = listaBanner;
	}     
     
}
