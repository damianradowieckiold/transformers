package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "fleetFactories")
public class FleetFactory {
	@Id
	@SequenceGenerator(name = "fleetFactories_id_seq", sequenceName = "fleetFactories_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "fleetFactories_id_seq", strategy = GenerationType.SEQUENCE)
	private int id;
	private int fleetTypeId;
	private int level;
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

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPlanetId() {
		return this.planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}
}