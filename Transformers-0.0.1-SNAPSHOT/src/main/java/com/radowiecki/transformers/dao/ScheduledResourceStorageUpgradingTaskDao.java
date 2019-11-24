package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;
import java.util.List;

public interface ScheduledResourceStorageUpgradingTaskDao {
	List<ScheduledResourceStorageUpgradingTask> getByPlanetId(int var1);

	void persist(ScheduledResourceStorageUpgradingTask var1);

	ScheduledResourceStorageUpgradingTask getById(int var1);

	List<ScheduledResourceStorageUpgradingTask> getAll();

	void delete(ScheduledResourceStorageUpgradingTask var1);

	List<ScheduledResourceStorageUpgradingTask> getByDone(boolean var1);

	void update(ScheduledResourceStorageUpgradingTask var1);
}