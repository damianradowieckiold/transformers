package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.FleetFactory;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FleetFactoryDaoImpl implements FleetFactoryDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<FleetFactory> getByPlanetId(int planetId) {
		return this.sessionFactory.getCurrentSession().createQuery("from FleetFactory where planetId = :planetId")
				.setParameter("planetId", planetId).list();
	}

	public void persist(FleetFactory fleetFactory) {
		this.sessionFactory.getCurrentSession().persist(fleetFactory);
	}

	public FleetFactory getByPlanetIdAndFleetTypeId(int planetId, int fleetTypeId) {
		List<FleetFactory> fleetFactories = this.sessionFactory.getCurrentSession()
				.createQuery("from FleetFactory where planetId= :planetId and fleetTypeId = :fleetTypeId")
				.setParameter("planetId", planetId).setParameter("fleetTypeId", fleetTypeId).list();
		return fleetFactories.size() > 0 ? (FleetFactory) fleetFactories.get(0) : null;
	}

	public void update(FleetFactory fleetFactory) {
		this.sessionFactory.getCurrentSession().update(fleetFactory);
	}
}