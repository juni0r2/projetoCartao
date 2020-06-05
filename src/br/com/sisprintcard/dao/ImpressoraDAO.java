package br.com.sisprintcard.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sisprintcard.model.EnImpressora;

public class ImpressoraDAO {

	private EntityManager em;
	
	public ImpressoraDAO(EntityManager em) {
		this.em = em;
	}
	
	public EnImpressora buscaPorId(Long id) {
		Query query = em.createQuery("SELECT i FROM EnImpressora i WHERE i.id =: id");
		query.setParameter("id", id);
		return (EnImpressora) query.getSingleResult();
	}
}
