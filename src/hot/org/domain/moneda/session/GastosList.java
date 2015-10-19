package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("gastosList")
public class GastosList extends EntityQuery<Gastos> {

	private static final String EJBQL = "select gastos from Gastos gastos";

	private static final String[] RESTRICTIONS = { 
		"lower(gastos.observacion) like lower(concat(#{gastosList.gastos.observacion},'%'))", 
		"lower(personal.documento) like lower(concat(#{gastosList.personal.documento},'%'))",
		"lower(personal.nombre || ' ' || personal.apellido) like lower(concat(#{gastosList.nombre},'%'))",
		
	};

	private Gastos gastos = new Gastos();
	
	private Personal personal = new Personal();
	
	private String nombre = new String();
	
	public GastosList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(90);
	}

	public Gastos getGastos() {
		return gastos;
	}

	public Personal getPersonal() {
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
