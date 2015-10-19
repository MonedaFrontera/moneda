package org.domain.moneda.session;

import org.domain.moneda.bussiness.AdministrarEnvios;
import org.domain.moneda.bussiness.AdministrarVariable;
import org.domain.moneda.entity.*;
import org.domain.moneda.util.CargarObjetos;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

import java.util.Arrays;

@Name("enviosList")
public class EnviosList extends EntityQuery<Envios> {

	private static final String EJBQL = "select envios from Envios envios";

	private static final String[] RESTRICTIONS = {
			"lower(envios.envia) like lower(concat(#{enviosList.envios.envia},'%'))",
			"lower(envios.recibe) like lower(concat(#{enviosList.envios.recibe},'%'))",
			"lower(envios.oficina) like lower(concat(#{enviosList.envios.oficina},'%'))",
			"lower(envios.ciudad) like lower(concat(#{enviosList.envios.ciudad},'%'))",
			"lower(asesor.documento) like lower(concat(#{enviosList.asesor.documento},'%'))",
			"lower(promotor.documento) like lower(concat(#{enviosList.promotor.documento},'%'))",
			"lower(replace((promotor.personal.nombre || ' ' || promotor.personal.apellido),' ','')) like lower(replace(concat(#{enviosList.nombre},'%'),' ',''))",
			"envios.fecha = #{enviosList.envios.fecha}",
			"envios.fechaenvio = #{enviosList.envios.fechaenvio}",
			"lower(envios.nrocupon) like lower(concat(#{enviosList.envios.nrocupon},'%'))", };
	
	@In Identity identity;

	private Envios envios = new Envios();
	
	private Asesor asesor = new Asesor();
	
	private Promotor promotor = new Promotor();
	
	private String nombre = new String();
	
	private String estado = "t";
	
	

	public EnviosList() {
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		
	}


	public Envios getEnvios() {
		return envios;
	}
	
	public Asesor getAsesor() { 
		//asesor = AdministrarVariable.getAsesor();
		asesor.setDocumento(identity.getUsername());
		
		return asesor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void cambiarEstado(String estado) {
		this.estado = estado;
		AdministrarEnvios.setEstado(estado);
		
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Promotor getPromotor() {
		return promotor;
	}

	public void setPromotor(Promotor promotor) {
		this.promotor = promotor;
	}



	@In(create=true)
	@Out
	private AdministrarEnvios AdministrarEnvios;
	
    
   

	
	
}
