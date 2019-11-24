package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledFleetFactoryBuildingTask;
import java.util.List;

public interface ScheduledFleetFactoryBuildingTaskDao {
	List<ScheduledFleetFactoryBuildingTask> getByPlanetId(int var1);

	void persist(ScheduledFleetFactoryBuildingTask var1);

	ScheduledFleetFactoryBuildingTask getById(int var1);

	List<ScheduledFleetFactoryBuildingTask> getAll();

	void delete(ScheduledFleetFactoryBuildingTask var1);

	List<ScheduledFleetFactoryBuildingTask> getByDone(boolean var1);

	void update(ScheduledFleetFactoryBuildingTask var1);
}