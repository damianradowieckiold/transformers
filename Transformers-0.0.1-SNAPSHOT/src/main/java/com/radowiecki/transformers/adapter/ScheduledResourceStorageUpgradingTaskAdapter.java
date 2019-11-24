package com.radowiecki.transformers.adapter;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.DateSerializer;

import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;

public class ScheduledResourceStorageUpgradingTaskAdapter {
	private ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask;
	private Map<String, String> languageProperties;
	private int id;
	private Date startDate;
	private String buildingTaskName;
	private Date executionDate;

	public static List<ScheduledResourceStorageUpgradingTaskAdapter> adapt(
			List<ScheduledResourceStorageUpgradingTask> scheduledResourceFactoryBuildingTasks,
			Map<String, String> lanugageProperties) {
		return (List) scheduledResourceFactoryBuildingTasks.stream().map((srfbt) -> {
			return new ScheduledResourceStorageUpgradingTaskAdapter(srfbt, lanugageProperties);
		}).collect(Collectors.toList());
	}

	public ScheduledResourceStorageUpgradingTaskAdapter(
			ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask,
			Map<String, String> languageProperties) {
		this.scheduledResourceStorageUpgradingTask = scheduledResourceStorageUpgradingTask;
		this.languageProperties = languageProperties;
		this.prepareBuildingTaskName();
		this.startDate = scheduledResourceStorageUpgradingTask.getStartDate();
		this.executionDate = scheduledResourceStorageUpgradingTask.getExecutionDate();
		this.id = scheduledResourceStorageUpgradingTask.getId();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExecutionDate() {
		return this.executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getBuildingTaskName() {
		return this.buildingTaskName;
	}

	public void setBuildingTaskName(String buildingTaskName) {
		this.buildingTaskName = buildingTaskName;
	}

	private void prepareBuildingTaskName() {
		String key = this.prepareMapKey();
		String value = (String) this.languageProperties.get(key);
		this.buildingTaskName = value;
	}

	private String prepareMapKey() {
		String key = "resource.storage.upgradingtype.bytypesid."
				+ this.scheduledResourceStorageUpgradingTask.getResourceTypeId();
		return key;
	}
}
