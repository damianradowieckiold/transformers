package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ResourceType;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ResourceTypeDaoImpl implements ResourceTypeDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<ResourceType> getAll() {
		return this.sessionFactory.getCurrentSession().createQuery("from ResourceType").list();
	}

	@Transactional
	public ResourceType getById(int id) {
		List<ResourceType> resourceTypes = this.sessionFactory.getCurrentSession()
				.createQuery("from ResourceType where id = :id").setParameter("id", id).list();
		return resourceTypes.size() > 0 ? (ResourceType) resourceTypes.get(0) : null;
	}
}