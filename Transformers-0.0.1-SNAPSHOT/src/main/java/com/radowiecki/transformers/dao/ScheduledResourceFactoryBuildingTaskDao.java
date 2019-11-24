package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledResourceFactoryBuildingTask;
import java.util.List;

public interface ScheduledResourceFactoryBuildingTaskDao {
	List<ScheduledResourceFactoryBuildingTask> getByPlanetId(int var1);

	void persist(ScheduledResourceFactoryBuildingTask var1);

	ScheduledResourceFactoryBuildingTask getById(int var1);

	List<ScheduledResourceFactoryBuildingTask> getAll();

	void delete(ScheduledResourceFactoryBuildingTask var1);

	List<ScheduledResourceFactoryBuildingTask> getByDone(boolean var1);

	void update(ScheduledResourceFactoryBuildingTask var1);
}