package org.domain.moneda.session;

import org.domain.moneda.bussiness.AdministrarGasto;
import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("gastosHome")
public class GastosHome extends EntityHome<Gastos> {

	@In(create = true)
	PersonalHome personalHome;
	@In(create = true)
	GastosHome gastosHome;
	@In(create = true)
	TransferenciaHome transferenciaHome;
	
	@In(create=true) @Out 
	private AdministrarGasto AdministrarGasto;
	
	@In
	private EntityManager entityManager;
	
	@In Identity identity;

	public void setGastosConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getGastosConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Gastos createInstance() {
		Gastos gastos = new Gastos();
		return gastos;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	} 

	public void wire() {
		getInstance();

		// Aca se establecen los datos para la auditoria como 
		// usuario que la modifico y la fecha de la modificacion o creacion
		getInstance().setUsuariomod(this.identity.getUsername());
		getInstance().setFechamod(new Date());
		
		if(getInstance().getFecha()==null) 
			getInstance().setFecha(new Date());
		
		System.out.println("GastosHome wire"); 
		Personal personal = personalHome.getDefinedInstance();
		if (personal != null) {
			getInstance().setPersonal(personal);
			System.out.println("Nombre Gastos " + personal.getNombre());
			AdministrarGasto.setNombre(personal.getNombre() + " " + personal.getApellido());
		}else{
			if(this.getInstance().getPersonal()!=null){
			Personal p = entityManager.find(Personal.class, this.getInstance().getPersonal().getDocumento());
				System.out.println("Nombre Gastos " + p.getNombre());
				AdministrarGasto.setNombre(p.getNombre() + " " + p.getApellido());
			}
		}
		Transferencia transferencia = transferenciaHome.getDefinedInstance();
		if (transferencia != null) {
			getInstance().setTransferencia(transferencia);
		}
		
	}

	public boolean isWired() {
		return true;
	}

	public Gastos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
