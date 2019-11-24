package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledFleetBuildingTask;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ScheduledFleetBuildingTaskDaoImpl implements ScheduledFleetBuildingTaskDao {
	private static Logger logger = Logger.getLogger(ScheduledFleetBuildingTaskDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<ScheduledFleetBuildingTask> getByPlanetId(int planetId) {
		logger.info("Getting ScheduledFleetBuildingTask by planet id: " + planetId);
		String query = "from ScheduledFleetBuildingTask where planetId = :planetId";
		List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks = this.sessionFactory.getCurrentSession()
				.createQuery(query).setParameter("planetId", planetId).list();
		return scheduledFleetBuildingTasks;
	}

	@Transactional
	public void persist(ScheduledFleetBuildingTask scheduledFleetBuildingTask) {
		logger.info("Persisting scheduledFleetBuildingTask with id: " + scheduledFleetBuildingTask.getId());
		this.sessionFactory.getCurrentSession().persist(scheduledFleetBuildingTask);
	}

	@Transactional
	public List<ScheduledFleetBuildingTask> getAll() {
		List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledFleetBuildingTask").list();
		return scheduledFleetBuildingTasks;
	}

	@Transactional
	public void delete(ScheduledFleetBuildingTask scheduledFleetBuildingTask) {
		this.sessionFactory.getCurrentSession().delete(scheduledFleetBuildingTask);
	}

	@Transactional
	public ScheduledFleetBuildingTask getById(int id) {
		List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledFleetBuildingTask where id = :id").setParameter("id", id).list();
		return scheduledFleetBuildingTasks.size() > 0
				? (ScheduledFleetBuildingTask) scheduledFleetBuildingTasks.get(0)
				: null;
	}

	@Transactional
	public List<ScheduledFleetBuildingTask> getByDone(boolean done) {
		List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks = this.sessionFactory.getCurrentSession()
				.createQuery("from ScheduledFleetBuildingTask where done = :done").setParameter("done", done).list();
		return scheduledFleetBuildingTasks;
	}

	@Transactional
	public void update(ScheduledFleetBuildingTask scheduledFleetBuildingTask) {
		this.sessionFactory.getCurrentSession().update(scheduledFleetBuildingTask);
	}
}