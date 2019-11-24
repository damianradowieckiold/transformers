package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ResourceStorageAdapter;
import com.radowiecki.transformers.dao.ResourceStorageDao;
import com.radowiecki.transformers.dao.ResourceTypeDao;
import com.radowiecki.transformers.model.ResourceStorage;
import com.radowiecki.transformers.model.ResourceType;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceStorageServiceImpl implements ResourceStorageService {
	@Autowired
	private ResourceStorageDao resourceStorageDao;
	@Autowired
	private ResourceTypeDao resourceTypeDao;

	public void persist(List<ResourceStorage> resourceStorages) {
		resourceStorages.forEach(resourceStorageDao::persist);
	}

	public List<ResourceStorageAdapter> adapt(List<ResourceStorage> resourceStorages) {
		List<ResourceStorageAdapter> resourceStorageAdapters = (List) resourceStorages.stream().map((r) -> {
			return this.adapt(r);
		}).collect(Collectors.toList());
		return resourceStorageAdapters;
	}

	private ResourceStorageAdapter adapt(ResourceStorage resourceStorage) {
		ResourceStorageAdapter resourceStorageAdapter = new ResourceStorageAdapter();
		resourceStorageAdapter.setMaximumLoad(resourceStorage.getMaximumLoad());
		resourceStorageAdapter.setId(resourceStorage.getId());
		ResourceType resourceType = this.resourceTypeDao.getById(resourceStorage.getResourceTypeId());
		resourceStorageAdapter.setResourceTypeName(resourceType.getName());
		return resourceStorageAdapter;
	}
}