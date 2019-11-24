package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Planet;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PlanetDaoImpl implements PlanetDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<Planet> getByUserId(int userId) {
		List<Planet> planets = this.sessionFactory.getCurrentSession().createQuery("from Planet where userid = :userId")
				.setParameter("userId", userId).list();
		return planets;
	}

	public void persist(Planet planet) {
		this.sessionFactory.getCurrentSession().persist(planet);
	}

	public List<Integer> getAllXCoordinates() {
		List<Integer> allXCoordinates = this.sessionFactory.getCurrentSession()
				.createQuery("select xCoordinate from Planet").list();
		return allXCoordinates;
	}

	public List<Integer> getAllYCoordinates() {
		List<Integer> allYCoordinates = this.sessionFactory.getCurrentSession()
				.createQuery("select yCoordinate from Planet").list();
		return allYCoordinates;
	}

	public Planet getById(int planetId) {
		List<Planet> planets = this.sessionFactory.getCurrentSession().createQuery("from Planet where id = :planetId")
				.setParameter("planetId", planetId).list();
		return planets.size() > 0 ? (Planet) planets.get(0) : null;
	}

	public void update(Planet planet) {
		this.sessionFactory.getCurrentSession().update(planet);
	}

	public List<Planet> getAll() {
		return this.sessionFactory.getCurrentSession().createQuery("from Planet").list();
	}
}