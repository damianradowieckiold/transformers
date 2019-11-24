package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ResourceStorageAdapter;
import com.radowiecki.transformers.model.ResourceStorage;
import java.util.List;

public interface ResourceStorageService {
	void persist(List<ResourceStorage> var1);

	List<ResourceStorageAdapter> adapt(List<ResourceStorage> var1);
}