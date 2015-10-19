package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

import java.util.Arrays;

@Name("bancoList")
public class BancoList extends EntityQuery<Banco> {
	
	@In Identity identity;

	private static final String EJBQL = "select banco from Banco banco";	

	private static final String[] RESTRICTIONS = {
			"lower(banco.codbanco) like lower(concat(#{bancoList.banco.codbanco},'%'))",
			"lower(banco.nombrebanco) like lower(concat(#{bancoList.banco.nombrebanco},'%'))"};


	private Banco banco = new Banco();

	public BancoList() {
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("nombrebanco"); 
		setMaxResults(30);
		
		try{

			
		}catch(Exception e){
			System.out.println("ERROR AL EVALUAR EL ROL");
			e.printStackTrace();
		}
		
//		if( identity.hasRole("Registrar Activacion")){
//			System.out.println("Ingrese al if");
//			
//		}else{
//			System.out.println("Ingrese al else");
//			
//		}
		
		
	}

	public Banco getBanco() {
		return banco;
	}
}
