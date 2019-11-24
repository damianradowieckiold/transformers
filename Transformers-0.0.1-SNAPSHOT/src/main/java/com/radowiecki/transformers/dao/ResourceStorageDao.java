package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ResourceStorage;
import java.util.List;

public interface ResourceStorageDao {
	List<ResourceStorage> getByPlanetId(int var1);

	void persist(ResourceStorage var1);

	ResourceStorage getByPlanetIdAndResourceTypeId(int var1, int var2);

	void update(ResourceStorage var1);
}