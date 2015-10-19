package org.domain.moneda.session;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("cuentaList")
@Scope(CONVERSATION)
public class CuentaList extends EntityQuery<Cuenta> {

	private static final String EJBQL = "select cuenta from Cuenta cuenta";

	private static final String[] RESTRICTIONS = {
			"lower(cuenta.numcuenta) like lower(concat(#{cuentaList.cuenta.numcuenta},'%'))",
			"lower(cuenta.banco.codbanco) like lower(concat(#{cuentaList.banco.codbanco},'%'))",
			"lower(cuenta.nombre) like lower(concat(#{cuentaList.cuenta.nombre},'%'))", };

	private Cuenta cuenta = new Cuenta();
	
	private Banco banco = new Banco();

	public CuentaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cuenta getCuenta() {
		return cuenta;
	}
	
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}
}
