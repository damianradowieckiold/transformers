package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ResourceStorage;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ResourceStorageDaoImpl implements ResourceStorageDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<ResourceStorage> getByPlanetId(int planetId) {
		return this.sessionFactory.getCurrentSession().createQuery("from ResourceStorage where planetId = :planetId")
				.setParameter("planetId", planetId).list();
	}

	public void persist(ResourceStorage resourceStorage) {
		this.sessionFactory.getCurrentSession().persist(resourceStorage);
	}

	public ResourceStorage getByPlanetIdAndResourceTypeId(int planetId, int resourceTypeId) {
		List<ResourceStorage> resourceStorages = this.sessionFactory.getCurrentSession()
				.createQuery("from ResourceStorage where planetId = :planetId and resourceTypeId = :resourceTypeId")
				.setParameter("planetId", planetId).setParameter("resourceTypeId", resourceTypeId).list();
		return resourceStorages.size() > 0 ? (ResourceStorage) resourceStorages.get(0) : null;
	}

	public void update(ResourceStorage resourceStorage) {
		this.sessionFactory.getCurrentSession().update(resourceStorage);
	}
}