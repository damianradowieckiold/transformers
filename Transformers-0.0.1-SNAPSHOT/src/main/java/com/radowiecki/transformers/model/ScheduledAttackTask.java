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
@Table(name = "scheduledattackTasks")
public class ScheduledAttackTask implements ScheduledTask {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduled_attack_seq_id")
	@SequenceGenerator(allocationSize = 1, sequenceName = "scheduledattacktasks_seq", name = "scheduled_attack_seq_id")
	private int id;
	private int attackingPlanetId;
	private int attackedPlanetId;
	private Date startDate;
	private int fleetTypeId;
	private int fleetAmount;
	private Date executionDate;
	private boolean done;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAttackingPlanetId() {
		return this.attackingPlanetId;
	}

	public void setAttackingPlanetId(int attackingPlanetId) {
		this.attackingPlanetId = attackingPlanetId;
	}

	public int getAttackedPlanetId() {
		return this.attackedPlanetId;
	}

	public void setAttackedPlanetId(int attackedPlanetId) {
		this.attackedPlanetId = attackedPlanetId;
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

	public int getFleetAmount() {
		return this.fleetAmount;
	}

	public void setFleetAmount(int fleetAmount) {
		this.fleetAmount = fleetAmount;
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