package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ResourceFactory;
import java.util.List;

public interface ResourceFactoryDao {
	List<ResourceFactory> getByPlanetId(int var1);

	void persist(ResourceFactory var1);

	ResourceFactory getByPlanetIdAndResourceTypeId(int var1, int var2);

	void update(ResourceFactory var1);
}