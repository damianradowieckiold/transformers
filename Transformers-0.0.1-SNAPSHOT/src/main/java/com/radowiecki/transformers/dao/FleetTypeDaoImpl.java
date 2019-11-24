package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.FleetType;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class FleetTypeDaoImpl implements FleetTypeDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<FleetType> getAll() {
		List<FleetType> fleetTypes = this.sessionFactory.getCurrentSession().createQuery("from FleetType").list();
		return fleetTypes;
	}

	@Transactional
	public FleetType getById(int id) {
		List<FleetType> fleetTypes = this.sessionFactory.getCurrentSession()
				.createQuery("from FleetType where id = :id").setParameter("id", id).list();
		return fleetTypes.size() > 0 ? (FleetType) fleetTypes.get(0) : null;
	}
}