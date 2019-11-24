package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ResourceAdapter;
import com.radowiecki.transformers.model.Resource;
import java.util.List;

public interface ResourceService {
	void persist(List<Resource> var1);

	void increaseResourceValues();

	List<ResourceAdapter> adapt(List<Resource> var1);
}