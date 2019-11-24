package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fleetFactoryBuildingResourceDemands")
public class FleetFactoryBuildingResourceDemand {
	@Id
	private int id;
	private int fleetFactoryTypeId;
	private int demandeDresourceTypeId;
	private int amount;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFleetFactoryTypeId() {
		return this.fleetFactoryTypeId;
	}

	public void setFleetFactoryTypeId(int fleetFactoryTypeId) {
		this.fleetFactoryTypeId = fleetFactoryTypeId;
	}

	public int getDemandeDresourceTypeId() {
		return this.demandeDresourceTypeId;
	}

	public void setDemandeDresourceTypeId(int demandeDresourceTypeId) {
		this.demandeDresourceTypeId = demandeDresourceTypeId;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}