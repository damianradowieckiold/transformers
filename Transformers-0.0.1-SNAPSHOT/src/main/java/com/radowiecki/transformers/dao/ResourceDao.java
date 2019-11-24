package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Resource;
import java.util.List;

public interface ResourceDao {
	Resource getById(int var1);

	List<Resource> getAll();

	List<Resource> getByPlanetId(int var1);

	void update(List<Resource> var1);

	void persist(Resource var1);
}