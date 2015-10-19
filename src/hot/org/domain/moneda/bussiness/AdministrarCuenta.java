package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import org.domain.moneda.session.CuentacreditoHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Scope(CONVERSATION)
@Name("AdministrarCuenta")
public class AdministrarCuenta {
	@Logger
	private Log log;

	@In
	StatusMessages statusMessages;

	@In
	private FacesMessages facesMessages;

	@In
	private EntityManager entityManager;

	public void administrarCuenta() {
		// implement your business logic here
		log.info("AdministrarCuenta.administrarCuenta() action called");
		statusMessages.add("administrarCuenta");
	}

	@In(create = true)
	@Out
	private CuentacreditoHome cuentacreditoHome;

	public String guardarMovimiento() {

		BigInteger query = (BigInteger) entityManager.createNativeQuery(
				"select nextval('cuentacredito_consecutivo_seq')")
				.getSingleResult();

		cuentacreditoHome.getInstance().setConsecutivo(query.intValue());
		cuentacreditoHome.persist();
		return "persisted";
	}

}
