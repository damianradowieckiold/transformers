package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "resources")
public class Resource {
	@Id
	@SequenceGenerator(name = "resources_id_seq", sequenceName = "resources_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resources_id_seq")
	private int id;
	private int resourceTypeId;
	private int amount;
	private int planetId;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResourceTypeId() {
		return this.resourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
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