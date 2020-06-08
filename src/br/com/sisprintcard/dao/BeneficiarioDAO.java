package br.com.sisprintcard.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;

public class BeneficiarioDAO {
	
	static final Logger logger = LogManager.getLogger(BeneficiarioDAO.class.getName());

	private EntityManager em;

	public BeneficiarioDAO(EntityManager em) {
		this.em  = em;
	}
	
	public EnSamBeneficiarioCartaoIdentif buscaPorId(Long id) {
		Query query = em.createQuery("SELECT i FROM EnSamBeneficiarioCartaoIdentif i WHERE i.handle =: id");
		query.setParameter("id", id);
		return (EnSamBeneficiarioCartaoIdentif) query.getSingleResult();
	}
}
