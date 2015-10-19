package org.domain.moneda.session;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("cuentacreditoList")
@Scope(CONVERSATION)
public class CuentacreditoList extends EntityQuery<Cuentacredito> {

	private static final String EJBQL = "select cuentacredito from Cuentacredito cuentacredito";

	private static final String[] RESTRICTIONS = {
			"lower(cuentacredito.usuario) like lower(concat(#{cuentacreditoList.cuentacredito.usuario},'%'))",
			"lower(cuentacredito.cuenta.banco.codbanco) like lower(concat(#{cuentacreditoList.banco.codbanco},'%'))",
			"lower(cuentacredito.cuenta.numcuenta) like lower(concat(#{cuentacreditoList.cuenta.numcuenta},'%'))",
			"lower(cuentacredito.cuenta.nombre) like lower(concat(#{cuentacreditoList.cuenta.nombre},'%'))",
			"lower(cuentacredito.usuariomod) like lower(concat(#{cuentacreditoList.cuentacredito.usuariomod},'%'))", };

	private Cuentacredito cuentacredito = new Cuentacredito();
	
	private Banco banco = new Banco();
	
	private Cuenta cuenta = new Cuenta();

	public CuentacreditoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cuentacredito getCuentacredito() {
		return cuentacredito;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	
	
	
}
