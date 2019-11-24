package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ResourceFactoryAdapter;
import com.radowiecki.transformers.model.ResourceFactory;
import java.util.List;

public interface ResourceFactoryService {
	List<ResourceFactory> getByPlanetId(int var1);

	ResourceFactory getByPlanetIdAndResourceTypeId(int var1, int var2);

	void persist(ResourceFactory var1);

	void persist(List<ResourceFactory> var1);

	List<ResourceFactoryAdapter> adapt(List<ResourceFactory> var1);
}