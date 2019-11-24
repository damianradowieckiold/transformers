package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledResourceFactoryBuildingTask;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ScheduledResourceFactoryBuildingTaskDaoImpl implements ScheduledResourceFactoryBuildingTaskDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<ScheduledResourceFactoryBuildingTask> getByPlanetId(int planetId) {
		List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledResourceFactoryBuildingTask where planetId = :planetId")
				.setParameter("planetId", planetId).list();
		return scheduledResourceFactoryBuildingTasks;
	}

	public void persist(ScheduledResourceFactoryBuildingTask scheduledResourceFactoryBuildingTask) {
		this.sessionFactory.getCurrentSession().persist(scheduledResourceFactoryBuildingTask);
	}

	public ScheduledResourceFactoryBuildingTask getById(int id) {
		List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledResourceFactoryBuildingTask where id = :id")
				.setParameter("id", id).list();
		return scheduledResourceFactoryBuildingTasks.size() > 0
				? (ScheduledResourceFactoryBuildingTask) scheduledResourceFactoryBuildingTasks.get(0)
				: null;
	}

	public List<ScheduledResourceFactoryBuildingTask> getAll() {
		List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledResourceFactoryBuildingTask").list();
		return scheduledResourceFactoryBuildingTasks;
	}

	public void delete(ScheduledResourceFactoryBuildingTask scheduledResourceFactoryBuildingTask) {
		this.sessionFactory.getCurrentSession().delete(scheduledResourceFactoryBuildingTask);
	}

	public List<ScheduledResourceFactoryBuildingTask> getByDone(boolean done) {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledResourceFactoryBuildingTask where done = :done").setParameter("done", done)
				.list();
	}

	public void update(ScheduledResourceFactoryBuildingTask scheduledResourceFactoryBuildingTask) {
		this.sessionFactory.getCurrentSession().update(scheduledResourceFactoryBuildingTask);
	}
}