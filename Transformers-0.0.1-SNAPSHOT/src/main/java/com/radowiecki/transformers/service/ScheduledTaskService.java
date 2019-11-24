package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ScheduledAttackTaskAdapter;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.ScheduledAttackTask;
import com.radowiecki.transformers.model.ScheduledFleetBuildingTask;
import com.radowiecki.transformers.model.ScheduledFleetFactoryBuildingTask;
import com.radowiecki.transformers.model.ScheduledResourceFactoryBuildingTask;
import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;
import com.radowiecki.transformers.model.ScheduledTask;
import java.util.List;

public interface ScheduledTaskService {
	void deleteOutdatedFleetBuildingTasks();

	void deleteOutdatedAttackTasks();

	void deleteOutdatedFleetFactoryBuildingTasks();

	void deleteOutdatedResourceFactoryBuildingTasks();

	void deleteOutdatedResrouceStorageFactoryUpgradingTasks();

	List<ScheduledAttackTask> getAllAttackTasksRelatedToPlanet(Planet var1);

	ScheduledAttackTaskAdapter adaptScheduledAttackTask(ScheduledAttackTask var1);

	List<ScheduledAttackTaskAdapter> adaptScheduledAttackTasks(List<ScheduledAttackTask> var1);

	List<ScheduledAttackTaskAdapter> getAdaptedScheduledAttackTasksRelatedToPlanet(Planet var1);

	void persistScheduledFleetBuildingTasks(List<ScheduledFleetBuildingTask> var1);

	void persistScheduledFleetFactoryBuildingTasks(List<ScheduledFleetFactoryBuildingTask> var1);

	void executeNotExecutedTasks();

	void executeScheduledFleetBuildingTask(ScheduledFleetBuildingTask var1);

	void executeNotExecutedFleetBuildingTasks();

	void executeNotExecutedResourceFactoryBuildingTasks();

	void executeScheduledResourceFactoryBuildingTask(ScheduledResourceFactoryBuildingTask var1);

	void executeNotExecutedResourceStorageUpgradingTasks();

	void executeScheduledResourceStorageUpgradingTask(ScheduledResourceStorageUpgradingTask var1);

	void executeNotExecutedFleetFactoryBuildingTasks();

	void executeScheduledFleetFactoryBuildingTask(ScheduledFleetFactoryBuildingTask var1);

	void executeNotExecutedAttackTasks();

	void executeScheduledAttackTask(ScheduledAttackTask var1);

	ScheduledFleetBuildingTask getTheLatestScheduledFleetBuildingTaskOnPlanet(int var1);

	ScheduledFleetFactoryBuildingTask getTheLatestScheduledFleetFactoryBuildingTaskOnPlanet(int var1);

	boolean isExecutionDateAfterCurrentTime(ScheduledTask var1);

	ScheduledResourceFactoryBuildingTask getTheLatestScheduledResourceFactoryBuildingTaskOnPlanet(int var1);

	void persistScheduledResourceFactoryBuildingTasks(List<ScheduledResourceFactoryBuildingTask> var1);

	ScheduledResourceStorageUpgradingTask getTheLatestScheduledResourceStorageUpgradingTaskOnPlanet(int var1);

	void persistScheduledResourceStorageUpgradingTasks(List<ScheduledResourceStorageUpgradingTask> var1);
}