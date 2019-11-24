package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fleetBuildingResourceDemands")
public class FleetBuildingResourceDemand {
	@Id
	private int id;
	private int fleetTypeId;
	private int demandedResourceTypeId;
	private int amount;

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

	public int getDemandedResourceTypeId() {
		return this.demandedResourceTypeId;
	}

	public void setDemandedResourceTypeId(int demandedResourceTypeId) {
		this.demandedResourceTypeId = demandedResourceTypeId;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}