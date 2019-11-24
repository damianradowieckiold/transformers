package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ScheduledResourceStorageUpgradingTaskDaoImpl implements ScheduledResourceStorageUpgradingTaskDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<ScheduledResourceStorageUpgradingTask> getByPlanetId(int planetId) {
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession()
				.createQuery("from ScheduledResourceStorageUpgradingTask where planetId = :planetId")
				.setParameter("planetId", planetId).list();
		return scheduledResourceFactoryBuildingTasks;
	}

	public void persist(ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask) {
		this.sessionFactory.getCurrentSession().persist(scheduledResourceStorageUpgradingTask);
	}

	public ScheduledResourceStorageUpgradingTask getById(int id) {
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledResourceStorageUpgradingTask where id = :id")
				.setParameter("id", id).list();
		return scheduledResourceStorageUpgradingTasks.size() > 0
				? (ScheduledResourceStorageUpgradingTask) scheduledResourceStorageUpgradingTasks.get(0)
				: null;
	}

	public void delete(ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask) {
		this.sessionFactory.getCurrentSession().delete(scheduledResourceStorageUpgradingTask);
	}

	public List<ScheduledResourceStorageUpgradingTask> getAll() {
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledResourceStorageUpgradingTask").list();
		return scheduledResourceStorageUpgradingTasks;
	}

	public List<ScheduledResourceStorageUpgradingTask> getByDone(boolean done) {
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledResourceStorageUpgradingTask where done = :done")
				.setParameter("done", done).list();
		return scheduledResourceStorageUpgradingTasks;
	}

	public void update(ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask) {
		this.sessionFactory.getCurrentSession().update(scheduledResourceStorageUpgradingTask);
	}
}