package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Fleet;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class FleetDaoImpl implements FleetDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public Fleet getById(int id) {
		new ArrayList();
		List<Fleet> fleets = this.sessionFactory.getCurrentSession().createQuery("from Fleet where id = ?")
				.setParameter(0, id).list();
		return fleets.isEmpty() ? null : (Fleet) fleets.get(0);
	}

	@Transactional
	public void update(Fleet fleet) {
		this.sessionFactory.getCurrentSession().update(fleet);
	}

	@Transactional
	public List<Fleet> getByPlanetId(int planetId) {
		List<Fleet> fleets = this.sessionFactory.getCurrentSession()
				.createQuery("from Fleet where planetId = :planetId").setParameter("planetId", planetId).list();
		return fleets;
	}

	@Transactional
	public void persist(Fleet fleet) {
		this.sessionFactory.getCurrentSession().persist(fleet);
	}

	@Transactional
	public Fleet getByPlanetIdAndFleetTypeId(int planetId, int fleetTypeId) {
		List<Fleet> fleets = this.sessionFactory.getCurrentSession()
				.createQuery("from Fleet where planetId= :planetId and fleetTypeId = :fleetTypeId")
				.setParameter("planetId", planetId).setParameter("fleetTypeId", fleetTypeId).list();
		return fleets.size() > 0 ? (Fleet) fleets.get(0) : null;
	}

	@Transactional
	public List<Fleet> getAll() {
		List<Fleet> allFleets = this.sessionFactory.getCurrentSession().createQuery("from Fleet").list();
		return allFleets;
	}
}