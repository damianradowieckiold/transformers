package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.User;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public User getById(int id) {
		new ArrayList();
		List<User> users = this.sessionFactory.getCurrentSession().createQuery("from User where id = ?")
				.setParameter(0, id).list();
		return users.isEmpty() ? null : (User) users.get(0);
	}

	@Transactional
	public User getByUsername(String username) {
		new ArrayList();
		List<User> users = this.sessionFactory.getCurrentSession().createQuery("from User where username = ?")
				.setParameter(0, username).list();
		return users.isEmpty() ? null : (User) users.get(0);
	}

	@Transactional
	public void persist(User user) {
		this.sessionFactory.getCurrentSession().persist(user);
	}

	@Transactional
	public List<User> getAll() {
		new ArrayList();
		List<User> allUsers = this.sessionFactory.getCurrentSession().createQuery("from User").list();
		return allUsers;
	}

	@Transactional
	public void update(User user) {
		this.sessionFactory.getCurrentSession().update(user);
	}

	@Transactional
	public void delete(User user) {
		this.sessionFactory.getCurrentSession().delete(user);
	}

	@Transactional
	public User getAttackingUserByScheduledAttackTaskId(int scheduledAttackTaskId) {
		return (User) this.sessionFactory.getCurrentSession().createQuery(
				"from User where id in (select userId from Planet where id in (select attackingPlanetId from ScheduledAttackTask where id = :scheduledAttackTaskId))")
				.setParameter("scheduledAttackTaskId", scheduledAttackTaskId).uniqueResult();
	}

	@Transactional
	public User getAttackedUserByScheduledAttackTaskId(int scheduledAttackTaskId) {
		return (User) this.sessionFactory.getCurrentSession().createQuery(
				"from User where id in (select userId from Planet where id in (select attackedPlanetId from ScheduledAttackTask where id = :scheduledAttackTaskId))")
				.setParameter("scheduledAttackTaskId", scheduledAttackTaskId).uniqueResult();
	}
}