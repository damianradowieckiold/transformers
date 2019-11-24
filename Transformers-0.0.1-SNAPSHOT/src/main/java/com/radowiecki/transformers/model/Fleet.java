package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "fleets")
public class Fleet {
	@Id
	@SequenceGenerator(name = "fleet_id_seq", sequenceName = "fleet_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fleet_id_seq")
	private int id;
	private int fleetTypeId;
	private int amount;
	private int planetId;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFleetTypeId() {
		return this.fleetTypeId;
	}

	public void setFleetTypeId(int fleetTypeId) {
		this.fleetTypeId = fleetTypeId;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPlanetId() {
		return this.planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}
}