package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.util.ArrayList;
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
	
	private List<String> restrictions = new ArrayList<String>() ;
	
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
		System.out.println("Ingrese a buscar...");
		
		String queryString =
			"select saldo from Saldo saldo where year( saldo.id.fecha ) = year( current_date() ) ";
		
		if( !this.getNombre().equals("") && this.getNombre()!= null ){
			queryString += " and lower(saldo.personal.nombre) like '%" + 
							this.getNombre().toLowerCase().trim() + "%'" ;
		}
		
		if( !this.getApellido().equals("") && this.getApellido() != null ){
			queryString += "and lower(saldo.personal.apellido) like '%" + 
					this.getApellido().toLowerCase().trim() +"'%";
		}
		saldoList.setEjbql(queryString);
		System.out.println("EJBQL: " + saldoList.getEjbql());
		if(saldoList.getResultCount() < 25)
			saldoList.setFirstResult(0);
		
		saldoList.setMaxResults(25);
		
	}
	

}
