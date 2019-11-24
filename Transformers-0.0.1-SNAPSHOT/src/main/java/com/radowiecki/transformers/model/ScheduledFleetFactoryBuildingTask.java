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
@Table(name = "scheduledFleetFactoryBuildingTasks")
public class ScheduledFleetFactoryBuildingTask implements ScheduledTask {
	@Id
	@GeneratedValue(generator = "scheduledFleetFactoryBuildingTasks_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, sequenceName = "scheduledFleetFactoryBuildingTasks_seq", name = "scheduledFleetFactoryBuildingTasks_seq")
	private int id;
	private Date startDate;
	private int fleetTypeId;
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

	public int getFleetTypeId() {
		return this.fleetTypeId;
	}

	public void setFleetTypeId(int fleetTypeId) {
		this.fleetTypeId = fleetTypeId;
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