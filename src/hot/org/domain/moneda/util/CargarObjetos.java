package org.domain.moneda.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Cuenta;
import org.domain.moneda.entity.Gestor;
import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("CargarObjetos") 
@Scope(ScopeType.CONVERSATION)
public class CargarObjetos {
	
	@Logger private Log log;
 
    @In StatusMessages statusMessages;
    
    @In
	private EntityManager entityManager;
	
	public Promotor ubicarPromotor(String nombre){

		entityManager.clear();
		List<Promotor> p = (ArrayList)entityManager
		.createQuery("select p from Promotor p where  " +
				"replace(trim(p.personal.nombre)||' '||trim(p.personal.apellido),' ','') = " +
				"replace(trim('"+nombre+"'),' ','')").getResultList();
		
		if (p.size() > 0) {
			Promotor pr = (Promotor)entityManager
			.createQuery("select p from Promotor p where replace(trim(p.personal.nombre)||' '||trim(p.personal.apellido),' ','')" +
					"=replace(trim('"+nombre+"'),' ','')")
			.getSingleResult();
			
			return pr;
		}else{
			return null;
		}		
	}
	
	
	public Gestor ubicarGestor(String nombre)
	{		
		entityManager.clear();
		List<Gestor> p = (ArrayList)entityManager
		.createQuery("select p from Gestor p where replace(trim(p.personal.nombre)||' '||trim(p.personal.apellido),' ','')=" +
				"replace(trim('"+nombre+"'),' ','')").getResultList();
		
		if (p.size() > 0) {
			Gestor pr = (Gestor)entityManager
			.createQuery("select p from Gestor p where replace(trim(p.personal.nombre)||' '||trim(p.personal.apellido),' ','')=" +
					"replace(trim('"+nombre+"'),' ','')")
			.getSingleResult();
			
			return pr;
		}else{
			return null;
		}		
	}
	
	public Personal ubicarPersonal(String nombre)
	{
		entityManager.clear();
		List<Personal> p = (ArrayList)entityManager
		.createQuery("select p from Personal p where replace(trim(p.nombre)||' '||trim(p.apellido),' ','')=" +
				"replace(trim('"+nombre+"'),' ','')").getResultList();
		
		if (p.size() > 0) {
			Personal pr = (Personal)p.get(0);
			
			return pr;
		}else{
			return null;
		}		
	}
	
	
	public Cuenta ubicarCuentas(String nombre)
	{
		entityManager.clear();
		List<Cuenta> c = (ArrayList)entityManager
		.createQuery("select c from Cuenta c where replace(trim(c.nombre),' ','')=" +
				"replace(trim('"+nombre+"'),' ','')").getResultList();
		
		if (c.size() > 0) {
			Cuenta cta = (Cuenta)c.get(0);
			
			return cta;
		}else{
			return null;
		}		
	}
	
	
	
}
