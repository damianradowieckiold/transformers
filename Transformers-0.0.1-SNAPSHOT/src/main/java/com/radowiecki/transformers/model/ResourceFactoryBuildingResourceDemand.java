package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "resourceFactoryBuildingResourceDemands")
public class ResourceFactoryBuildingResourceDemand {
	@Id
	private int id;
	private int resourceFactoryTypeId;
	private int demandedResourceTypeId;
	private int amount;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResourceFactoryTypeId() {
		return this.resourceFactoryTypeId;
	}

	public void setResourceFactoryTypeId(int resourceFactoryTypeId) {
		this.resourceFactoryTypeId = resourceFactoryTypeId;
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