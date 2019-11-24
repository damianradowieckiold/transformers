package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ResourceFactory;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ResourceFactoryDaoImpl implements ResourceFactoryDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<ResourceFactory> getByPlanetId(int planetId) {
		return this.sessionFactory.getCurrentSession().createQuery("from ResourceFactory where planetId = :planetId")
				.setParameter("planetId", planetId).list();
	}

	public void persist(ResourceFactory resourceFactory) {
		this.sessionFactory.getCurrentSession().persist(resourceFactory);
	}

	public ResourceFactory getByPlanetIdAndResourceTypeId(int planetId, int resourceTypeId) {
		List<ResourceFactory> resourceFactories = this.sessionFactory.getCurrentSession()
				.createQuery("from ResourceFactory where planetId = :planetId and resourceTypeId = :resourceTypeId")
				.setParameter("planetId", planetId).setParameter("resourceTypeId", resourceTypeId).list();
		return resourceFactories.size() > 0 ? (ResourceFactory) resourceFactories.get(0) : null;
	}

	public void update(ResourceFactory resourceFactory) {
		this.sessionFactory.getCurrentSession().update(resourceFactory);
	}
}