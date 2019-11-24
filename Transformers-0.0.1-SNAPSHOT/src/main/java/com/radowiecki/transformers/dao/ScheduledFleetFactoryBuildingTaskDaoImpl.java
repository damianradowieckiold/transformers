package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledFleetFactoryBuildingTask;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ScheduledFleetFactoryBuildingTaskDaoImpl implements ScheduledFleetFactoryBuildingTaskDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<ScheduledFleetFactoryBuildingTask> getByPlanetId(int planetId) {
		List<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledFleetFactoryBuildingTask where planetId= :planetId")
				.setParameter("planetId", planetId).list();
		return scheduledFleetFactoryBuildingTasks;
	}

	public void persist(ScheduledFleetFactoryBuildingTask scheduledFleetFactoryBuildingTask) {
		this.sessionFactory.getCurrentSession().persist(scheduledFleetFactoryBuildingTask);
	}

	public ScheduledFleetFactoryBuildingTask getById(int id) {
		List<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledFleetFactoryBuildingTask where id = :id")
				.setParameter("id", id).list();
		return scheduledFleetFactoryBuildingTasks.size() > 0
				? (ScheduledFleetFactoryBuildingTask) scheduledFleetFactoryBuildingTasks.get(0)
				: null;
	}

	public List<ScheduledFleetFactoryBuildingTask> getAll() {
		List<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTasks = this.sessionFactory
				.getCurrentSession().createQuery("from ScheduledFleetFactoryBuildingTask").list();
		return scheduledFleetFactoryBuildingTasks;
	}

	public void delete(ScheduledFleetFactoryBuildingTask scheduledFleetFactoryBuildingTask) {
		this.sessionFactory.getCurrentSession().delete(scheduledFleetFactoryBuildingTask);
	}

	public List<ScheduledFleetFactoryBuildingTask> getByDone(boolean done) {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledFleetFactoryBuildingTask where done = :done").setParameter("done", done)
				.list();
	}

	public void update(ScheduledFleetFactoryBuildingTask scheduledFleetFactoryBuildingTask) {
		this.sessionFactory.getCurrentSession().update(scheduledFleetFactoryBuildingTask);
	}
}