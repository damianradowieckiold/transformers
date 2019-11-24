package com.radowiecki.transformers.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "resourceStorageUpgradingResourceDemands")
public class ResourceStorageUpgradingResourceDemand {
	@Id
	private int id;
	private int resourceStorageFactoryTypeId;
	private int demandedResourceTypeId;
	private int amount;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResourceStorageFactoryTypeId() {
		return this.resourceStorageFactoryTypeId;
	}

	public void setResourceStorageFactoryTypeId(int resourceStorageFactoryTypeId) {
		this.resourceStorageFactoryTypeId = resourceStorageFactoryTypeId;
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