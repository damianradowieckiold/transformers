package com.radowiecki.transformers.adapter;

public class ResourceStorageAdapter {
	private String resourceTypeName;
	private int maximumLoad;
	private int id;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceTypeName() {
		return this.resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public int getMaximumLoad() {
		return this.maximumLoad;
	}

	public void setMaximumLoad(int maximumLoad) {
		this.maximumLoad = maximumLoad;
	}
}
