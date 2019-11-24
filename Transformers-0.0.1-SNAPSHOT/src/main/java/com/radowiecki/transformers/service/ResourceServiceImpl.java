package com.radowiecki.transformers.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radowiecki.transformers.adapter.ResourceAdapter;
import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.dao.ResourceDao;
import com.radowiecki.transformers.dao.ResourceStorageDao;
import com.radowiecki.transformers.dao.ResourceTypeDao;
import com.radowiecki.transformers.dao.UserDao;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ResourceFactory;
import com.radowiecki.transformers.model.ResourceStorage;
import com.radowiecki.transformers.model.ResourceType;
import com.radowiecki.transformers.model.User;

@Service
public class ResourceServiceImpl implements ResourceService {
	private static final int RESOURCES_INCRESING_SPEED = 2;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ResourceFactoryService resourceFactoryService;
	@Autowired
	private PlanetDao planetDao;
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	@Autowired
	private ResourceStorageDao resourceStorageDao;

	public void persist(List<Resource> resources) {
		resources.forEach(resourceDao::persist);
	}

	public void increaseResourceValues() {
		List<User> allUsers = this.userDao.getAll();
		allUsers.stream().forEach((user) -> {
			this.increaseResourcesValues(user);
		});
	}

	private void increaseResourcesValues(User user) {
		List<Planet> userPlanets = this.planetDao.getByUserId(user.getId());
		userPlanets.forEach((p) -> {
			this.increaseResourcesValues(p);
		});
	}

	private void increaseResourcesValues(Planet planet) {
		int planetId = planet.getId();
		List<Resource> resources = this.resourceDao.getByPlanetId(planetId);
		resources.stream().forEach((resource) -> {
			int amount = resource.getAmount();
			int resourceTypeId = resource.getResourceTypeId();
			ResourceFactory resourceFactory = this.resourceFactoryService.getByPlanetIdAndResourceTypeId(planetId,
					resourceTypeId);
			ResourceStorage resourceStorage = this.resourceStorageDao.getByPlanetIdAndResourceTypeId(planetId,
					resourceTypeId);
			amount += resourceFactory.getLevel() * 2;
			int maximumStorageLoad = resourceStorage.getMaximumLoad();
			if (amount <= maximumStorageLoad) {
				resource.setAmount(amount);
			} else {
				resource.setAmount(maximumStorageLoad);
			}

		});
		this.resourceDao.update(resources);
	}

	public List<ResourceAdapter> adapt(List<Resource> resources) {
		List<ResourceAdapter> resourceAdapters = (List) resources.stream().map((r) -> {
			return this.adapt(r);
		}).collect(Collectors.toList());
		return resourceAdapters;
	}

	private ResourceAdapter adapt(Resource resource) {
		ResourceAdapter resourceAdapter = new ResourceAdapter();
		resourceAdapter.setAmount(resource.getAmount());
		resourceAdapter.setId(resource.getId());
		ResourceType resourceType = this.resourceTypeDao.getById(resource.getResourceTypeId());
		resourceAdapter.setTypeName(resourceType.getName());
		return resourceAdapter;
	}
}