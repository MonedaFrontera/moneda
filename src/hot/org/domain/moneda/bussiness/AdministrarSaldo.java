package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Saldo;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;




@Name("AdministrarSaldo")
@Scope(CONVERSATION)
public class AdministrarSaldo {
	
	@In
	private EntityManager entityManager;
	
	
	private EntityQuery<Saldo> saldoList = new EntityQuery<Saldo>();
	
	private String nombre;
	private String apellido;
	
	public EntityQuery<Saldo> getSaldoList() {
		return saldoList;
	}

	public void setSaldoList(EntityQuery<Saldo> saldoList) {
		this.saldoList = saldoList;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void buscarSaldo(){
		
		String queryString = "select s from Saldo where 1=1 ";
		
		if( !this.getNombre().equals("") && this.getNombre()!= null ){
			queryString += "and lower(s.personal.nombre) like '%" + this.getNombre().toLowerCase().trim() + "'%";
		}
		
		if( !this.getApellido().equals("") && this.getApellido() != null ){
			queryString += "and lower(s.personal.apellido) like '%" +this.getApellido().toLowerCase().trim() +"'%";
		}
		
		saldoList.setEjbql(queryString);
		
		if(saldoList.getResultCount() < 25)
			saldoList.setFirstResult(0);
		
		saldoList.setMaxResults(25);
		
	}
	

}
