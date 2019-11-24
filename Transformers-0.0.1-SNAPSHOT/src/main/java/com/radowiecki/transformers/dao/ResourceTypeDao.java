package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ResourceType;
import java.util.List;

public interface ResourceTypeDao {
	List<ResourceType> getAll();

	ResourceType getById(int var1);
}