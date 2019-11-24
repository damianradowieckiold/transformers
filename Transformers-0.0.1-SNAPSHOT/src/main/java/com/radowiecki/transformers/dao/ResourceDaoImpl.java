package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ResourceDaoImpl implements ResourceDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public Resource getById(int id) {
		new ArrayList();
		List<Resource> resources = this.sessionFactory.getCurrentSession().createQuery("from Resources where id = ?")
				.setParameter(0, id).list();
		return resources.isEmpty() ? null : (Resource) resources.get(0);
	}

	@Transactional
	public List<Resource> getByPlanetId(int planetId) {
		List<Resource> resources = this.sessionFactory.getCurrentSession()
				.createQuery("from Resource where planetId = :planetId").setParameter("planetId", planetId).list();
		return resources;
	}

	@Transactional
	public void update(List<Resource> resources) {
		Session session = this.sessionFactory.getCurrentSession();
		Iterator var3 = resources.iterator();

		while (var3.hasNext()) {
			Resource r = (Resource) var3.next();
			session.createQuery("update Resource set amount = :amount where id = :id")
					.setParameter("amount", r.getAmount()).setParameter("id", r.getId()).executeUpdate();
		}

	}

	@Transactional
	public List<Resource> getAll() {
		List<Resource> allResources = this.sessionFactory.getCurrentSession().createQuery("from Resource").list();
		return allResources;
	}

	@Transactional
	public void persist(Resource resource) {
		this.sessionFactory.getCurrentSession().persist(resource);
	}
}