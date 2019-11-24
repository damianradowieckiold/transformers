package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledFleetBuildingTask;
import java.util.List;

public interface ScheduledFleetBuildingTaskDao {
	List<ScheduledFleetBuildingTask> getByPlanetId(int var1);

	void persist(ScheduledFleetBuildingTask var1);

	List<ScheduledFleetBuildingTask> getAll();

	void delete(ScheduledFleetBuildingTask var1);

	ScheduledFleetBuildingTask getById(int var1);

	List<ScheduledFleetBuildingTask> getByDone(boolean var1);

	void update(ScheduledFleetBuildingTask var1);
}