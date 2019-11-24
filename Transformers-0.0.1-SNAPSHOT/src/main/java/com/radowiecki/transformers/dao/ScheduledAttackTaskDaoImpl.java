package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledAttackTask;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ScheduledAttackTaskDaoImpl implements ScheduledAttackTaskDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<ScheduledAttackTask> getByAttackingPlanetId(int attackingPlanetId) {
		List<ScheduledAttackTask> scheduledAttackTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledAttackTask where attackingPlanetId = :attackingPlanetId")
				.setParameter("attackingPlanetId", attackingPlanetId).list();
		return scheduledAttackTasks;
	}

	public List<ScheduledAttackTask> getByAttackedPlanetId(int attackedPlanetId) {
		List<ScheduledAttackTask> scheduledAttackTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledAttackTask where attackedPlanetId = :attackedPlanetId")
				.setParameter("attackedPlanetId", attackedPlanetId).list();
		return scheduledAttackTasks;
	}

	public List<ScheduledAttackTask> getAll() {
		List<ScheduledAttackTask> scheduleAttacks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledAttackTask").list();
		return scheduleAttacks;
	}

	public void persist(ScheduledAttackTask scheduledAttackTask) {
		this.sessionFactory.getCurrentSession().persist(scheduledAttackTask);
	}

	public void delete(ScheduledAttackTask scheduledAttackTask) {
		this.sessionFactory.getCurrentSession().delete(scheduledAttackTask);
	}

	public ScheduledAttackTask getById(int taskId) {
		List<ScheduledAttackTask> scheduledAttackTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledAttackTask where id = :id").setParameter("id", taskId).list();
		return scheduledAttackTasks.size() > 0 ? (ScheduledAttackTask) scheduledAttackTasks.get(0) : null;
	}

	public void update(ScheduledAttackTask scheduledAttackTask) {
		this.sessionFactory.getCurrentSession().update(scheduledAttackTask);
	}

	public List<ScheduledAttackTask> getByDone(boolean done) {
		List<ScheduledAttackTask> scheduledAttackTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledAttackTask where done = :done").setParameter("done", done).list();
		return scheduledAttackTasks;
	}
}