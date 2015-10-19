package org.domain.moneda.bussiness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Establecimiento;
import org.domain.moneda.entity.FranquiciaestablecimientoId;
import org.domain.moneda.entity.GravamenestablecimientoId;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.FranquiciaestablecimientoHome;
import org.domain.moneda.session.GravamenestablecimientoHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.log.Log;
import org.jboss.seam.international.StatusMessages;

@Name("AdministrarGravamen")
@Scope(ScopeType.CONVERSATION)
public class AdministrarGravamen
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    private String sugestion = "";
    
    @In(create=true) @Out 
	private EstablecimientoHome	establecimientoHome;
    
    @In(create=true) @Out
    private GravamenestablecimientoHome gravamenestablecimientoHome;
    
    @In(create=true) @Out
    private FranquiciaestablecimientoHome franquiciaestablecimientoHome;
    
    @In
	private EntityManager entityManager;
    
    List<String> lista = new ArrayList<String>();
    
    public void administrarGravamen()
    {
        // implement your business logic here
        log.info("AdministrarGravamen.administrarGravamen() action called");
        statusMessages.add("administrarGravamen");
    }
    
    public void llenar(){
		entityManager.clear();
		List<String> resultList = entityManager.createQuery("select establecimiento.nombreestable from Establecimiento establecimiento").getResultList();
		lista = resultList;
	}
    
    public List<String> autocompletar(Object suggest) {
		// Metodo que carga la informacion de los establecimientos
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> iterator = lista.iterator();
		while (iterator.hasNext()) {
		String elem = ((String) iterator.next());
		if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
		|| "".equals(pref)) {
		result.add(elem);
		}
		}
		return result;
	
	}
    
    public void ubicarEstablecimiento(){

		entityManager.clear();
		List<Establecimiento> e = (ArrayList)entityManager
		.createQuery("select e from Establecimiento e where trim(e.nombreestable)=trim('"+this.sugestion+"')").getResultList();
		
		if (e.size() > 0) {
			Establecimiento es = e.get(0); 
			
			establecimientoHome.setEstablecimientoCodigounico(es.getCodigounico());
			establecimientoHome.setInstance(es);
		}
		
		
	}
    
    public void inicializar(){
    	gravamenestablecimientoHome.wire();
    	this.sugestion = establecimientoHome.getInstance().getNombreestable();
    	
    }
    
    public void inicializarFranquicia(){
    	franquiciaestablecimientoHome.wire();
    	this.sugestion = establecimientoHome.getInstance().getNombreestable();
    	
    }
    
    
    public String getSugestion() {
		return sugestion;
	}


	public void setSugestion(String sugestion) {
		this.sugestion = sugestion;
	}
	
	public String guardarGravamenEst(){
		gravamenestablecimientoHome.getInstance()
		.setEstablecimiento(establecimientoHome.getInstance());
		gravamenestablecimientoHome.getInstance()
		.setId(new GravamenestablecimientoId(establecimientoHome.getInstance().getCodigounico(),
				gravamenestablecimientoHome.getInstance().getGravamen().getCodigo()));
		/*
		entityManager.clear();
		entityManager.persist(gravamenestablecimientoHome.getInstance());
		entityManager.flush();
		*/
		Expressions expressions = new Expressions();
		ValueExpression mensaje;
		gravamenestablecimientoHome.setCreatedMessage(
				expressions.createValueExpression("Se ha creado el Gravamen para el Establecimiento de forma exitosa"));
		
		gravamenestablecimientoHome.persist();
		gravamenestablecimientoHome.clearInstance();
		return "persisted";
		
	}
    
	public String guardarFranquiciaEst(){
		franquiciaestablecimientoHome.getInstance()
		.setEstablecimiento(establecimientoHome.getInstance());
		franquiciaestablecimientoHome.getInstance()
		.setId(new FranquiciaestablecimientoId(franquiciaestablecimientoHome.getInstance().getFranquicia().getCodfranquicia(),
				establecimientoHome.getInstance().getCodigounico()));
		/*
		entityManager.clear();
		entityManager.persist(gravamenestablecimientoHome.getInstance());
		entityManager.flush();
		*/
		Expressions expressions = new Expressions();
		ValueExpression mensaje;
		franquiciaestablecimientoHome.setCreatedMessage(
				expressions.createValueExpression("Se ha creado la Franquicia para el Establecimiento de forma exitosa"));
		
		franquiciaestablecimientoHome.persist();
		franquiciaestablecimientoHome.clearInstance();
		return "persisted";
		
	}
    

    // add additional action methods
    
    

}
