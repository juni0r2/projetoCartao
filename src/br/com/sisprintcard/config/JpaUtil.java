package br.com.sisprintcard.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

	private static EntityManagerFactory factory;
	public static EntityManager forneceEntityManager() {
		factory = Persistence.createEntityManagerFactory("conta");
		EntityManager createEntityManager = factory.createEntityManager();
		return createEntityManager;
	}
	
	public static void fechaConexa() {
		factory.close();
	}
}
