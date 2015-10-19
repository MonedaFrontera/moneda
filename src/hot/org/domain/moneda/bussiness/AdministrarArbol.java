package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.math.BigInteger;
import java.util.Iterator;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Asesor;
import org.domain.moneda.entity.Promotor;
import org.domain.moneda.entity.Tarjeta;
import org.domain.moneda.session.AsesorList;
import org.domain.moneda.session.CuentacreditoHome;
import org.domain.moneda.session.PromotorList;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.richfaces.model.TreeNodeImpl;

@Scope(CONVERSATION)
@Name("AdministrarArbol")
public class AdministrarArbol {
	@Logger private Log log;

    @In StatusMessages statusMessages;

    @In
    private FacesMessages facesMessages;
    
    @In
	private EntityManager entityManager;
    
    @In (create=true)
	private AsesorList asesorList;
    
    @In (create=true)
	private PromotorList promotorList;
    
    public void administrarArbol()
    {
        // implement your business logic here
        log.info("AdministrarCuenta.administrarCuenta() action called");
        statusMessages.add("administrarCuenta");
    }
    

    private TreeNodeImpl<String> asesoras = new TreeNodeImpl<String>();
    private TreeNodeImpl<String> clientes = new TreeNodeImpl<String>();
  
    

    private TreeNodeImpl<String> stationNodes = new TreeNodeImpl<String>(); 



	public TreeNodeImpl<String> getStationNodes() {
		asesoras.setData("Asesoras");
		clientes.setData("Clientes");
		stationNodes.addChild(0, asesoras);
		stationNodes.addChild(1, clientes);
		int s = 0;
		for (int i = 0; i < asesorList.getResultCount(); i++){
		    TreeNodeImpl<String> child = new TreeNodeImpl<String>();
		    Asesor a = asesorList.getResultList().get(i);
		    if(!a.getPromotors().isEmpty()){
			    child.setData(a.getPersonal().getNombre());
			    
			    Iterator<Promotor> p = a.getPromotors().iterator();
			    int j = 0;
			    while (p.hasNext()){
			    	Promotor prom = p.next();
			    	TreeNodeImpl<String> child2 = new TreeNodeImpl<String>();
			    	child2.setData(prom.getPersonal().getNombre() + " " + prom.getPersonal().getApellido());
			    	child.addChild(j, child2);
			    	j++;
			    }
			    
			    asesoras.addChild(s, child);
			    s++;
		    }
		}
		
		for (int i = 0; i < promotorList.getResultCount(); i++){
		    TreeNodeImpl<String> child = new TreeNodeImpl<String>();
		    Promotor p = promotorList.getResultList().get(i);
		    
			    child.setData(p.getPersonal().getNombre() + " " +p.getPersonal().getApellido());
			    
			    
			    clientes.addChild(i, child);
			    
		    
		}
		
		return stationNodes;
	}

	public void setStationNodes(TreeNodeImpl<String> stationNodes) {
		this.stationNodes = stationNodes;
	}
    
    
    
}
