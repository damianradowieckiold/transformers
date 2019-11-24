package com.radowiecki.transformers.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.DateSerializer;

@Entity
@Table(name = "scheduledResourceStorageUpgradingTasks")
public class ScheduledResourceStorageUpgradingTask implements ScheduledTask {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduledresourcefactorybuildingtasks_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "scheduledresourcefactorybuildingtasks_seq", name = "scheduledresourcefactorybuildingtasks_seq")
	private int id;
	private Date startDate;
	private int resourceTypeId;
	private int planetId;
	private Date executionDate;
	private boolean done;

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

	public int getResourceTypeId() {
		return this.resourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public int getPlanetId() {
		return this.planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getExecutionDate() {
		return this.executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public boolean isDone() {
		return this.done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
}