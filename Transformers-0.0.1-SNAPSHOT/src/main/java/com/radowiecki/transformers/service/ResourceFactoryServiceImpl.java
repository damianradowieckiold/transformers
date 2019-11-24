package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ResourceFactoryAdapter;
import com.radowiecki.transformers.dao.ResourceFactoryDao;
import com.radowiecki.transformers.dao.ResourceTypeDao;
import com.radowiecki.transformers.model.ResourceFactory;
import com.radowiecki.transformers.model.ResourceType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceFactoryServiceImpl implements ResourceFactoryService {
	@Autowired
	private ResourceFactoryDao resourceFactoryDao;
	@Autowired
	private ResourceTypeDao resourceTypeDao;

	public List<ResourceFactory> getByPlanetId(int userId) {
		return this.resourceFactoryDao.getByPlanetId(userId);
	}

	public ResourceFactory getByPlanetIdAndResourceTypeId(int planetId, int resourceTypeId) {
		List<ResourceFactory> resourceFactories = this.resourceFactoryDao.getByPlanetId(planetId);
		List<ResourceFactory> filteredResourceFactories = (List) resourceFactories.stream().filter((resource) -> {
			return resource.getResourceTypeId() == resourceTypeId;
		}).collect(Collectors.toList());
		return filteredResourceFactories.size() > 0 ? (ResourceFactory) filteredResourceFactories.get(0) : null;
	}

	public void persist(ResourceFactory resourceFactory) {
		this.resourceFactoryDao.persist(resourceFactory);
	}

	public void persist(List<ResourceFactory> resourceFactories) {
		resourceFactories.forEach(resourceFactoryDao::persist);
	}

	public List<ResourceFactoryAdapter> adapt(List<ResourceFactory> resourceFactories) {
		List<ResourceFactoryAdapter> resourceFactoryAdapters = new ArrayList();
		Iterator var3 = resourceFactories.iterator();

		while (var3.hasNext()) {
			ResourceFactory rf = (ResourceFactory) var3.next();
			ResourceFactoryAdapter resourceFactoryAdapter = new ResourceFactoryAdapter();
			resourceFactoryAdapter.setLevel(rf.getLevel());
			ResourceType resourceType = this.resourceTypeDao.getById(rf.getResourceTypeId());
			resourceFactoryAdapter.setResourceTypeName(resourceType.getName());
			resourceFactoryAdapters.add(resourceFactoryAdapter);
		}

		return resourceFactoryAdapters;
	}
}