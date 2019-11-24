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
@Table(name = "scheduledFleetBuildingTasks")
public class ScheduledFleetBuildingTask implements ScheduledTask {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_seq_generator")
	@SequenceGenerator(name = "tasks_seq_generator", sequenceName = "scheduledFleetBuildingTasks_seq", allocationSize = 1)
	private int id;
	private Date startDate;
	private Date executionDate;
	private int fleetTypeId;
	private int planetId;
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

	@JsonSerialize(using = DateSerializer.class)
	public Date getExecutionDate() {
		return this.executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
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

	public boolean isDone() {
		return this.done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
}