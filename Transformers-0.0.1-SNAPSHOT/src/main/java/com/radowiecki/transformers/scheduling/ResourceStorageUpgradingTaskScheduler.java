package com.radowiecki.transformers.scheduling;

import com.radowiecki.transformers.dao.ResourceDao;
import com.radowiecki.transformers.dao.ResourceDemandDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ResourceStorageUpgradingResourceDemand;
import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;
import com.radowiecki.transformers.service.ScheduledTaskService;
import java.security.InvalidParameterException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class ResourceStorageUpgradingTaskScheduler {
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceDemandDao resourceDemandDao;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	private Planet planet;
	private int resourceTypeId;
	private int howMany;

	public void setHowMany(int howMany) {
		this.howMany = howMany;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public void schedule() throws InvalidParameterException, TooFewResourcesException {
		if (this.isPossibleToBuild()) {
			this.reduceResources();
			this.scheduleResourceStorageUpgradingTask();
		}

	}

	private void scheduleResourceStorageUpgradingTask() {
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks = new LinkedList();
		int secondsItTakes = this.calculateBuildingTime();
		ScheduledResourceStorageUpgradingTask theLatestscheduledResourceStorageUpgradingTask = this.scheduledTaskService
				.getTheLatestScheduledResourceStorageUpgradingTaskOnPlanet(this.planet.getId());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		if (this.scheduledTaskService.isExecutionDateAfterCurrentTime(theLatestscheduledResourceStorageUpgradingTask)) {
			gregorianCalendar.setTime(theLatestscheduledResourceStorageUpgradingTask.getExecutionDate());
			gregorianCalendar.add(13, 1);
		}

		for (int i = 0; i < this.howMany; ++i) {
			ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask = new ScheduledResourceStorageUpgradingTask();
			scheduledResourceStorageUpgradingTask.setResourceTypeId(this.resourceTypeId);
			scheduledResourceStorageUpgradingTask.setPlanetId(this.planet.getId());
			scheduledResourceStorageUpgradingTask.setStartDate(gregorianCalendar.getTime());
			gregorianCalendar.add(13, secondsItTakes);
			scheduledResourceStorageUpgradingTask.setExecutionDate(gregorianCalendar.getTime());
			gregorianCalendar.add(13, 1);
			scheduledResourceStorageUpgradingTask.setDone(false);
			scheduledResourceStorageUpgradingTasks.add(scheduledResourceStorageUpgradingTask);
		}

		this.scheduledTaskService.persistScheduledResourceStorageUpgradingTasks(scheduledResourceStorageUpgradingTasks);
	}

	private int calculateBuildingTime() {
		return 40;
	}

	private boolean isPossibleToBuild() throws TooFewResourcesException {
		List<ResourceStorageUpgradingResourceDemand> resourceStorageUpgradingResourceDemands = this.resourceDemandDao
				.getResourceStorageUpgradingResourceDemandsByResourceTypeId(this.resourceTypeId);
		List<Resource> userResources = this.resourceDao.getByPlanetId(this.planet.getId());
		Map<Resource, Boolean> enoughResources = new HashMap();
		boolean isPossibleToBuild = true;
		Iterator var5 = userResources.iterator();

		while (var5.hasNext()) {
			Resource r = (Resource) var5.next();
			ResourceStorageUpgradingResourceDemand resourceStorageUpgradingDemand = (ResourceStorageUpgradingResourceDemand) ((List) resourceStorageUpgradingResourceDemands
					.stream().filter((frd) -> {
						return frd.getDemandedResourceTypeId() == r.getResourceTypeId();
					}).collect(Collectors.toList())).get(0);
			if (r.getAmount() - resourceStorageUpgradingDemand.getAmount() * this.howMany < 0) {
				enoughResources.put(r, false);
				isPossibleToBuild = false;
			} else {
				enoughResources.put(r, true);
			}
		}

		if (isPossibleToBuild) {
			return true;
		} else {
			TooFewResourcesException tooFewResourcesException = new TooFewResourcesException();
			tooFewResourcesException.setEnoughResources(enoughResources);
			throw tooFewResourcesException;
		}
	}

	private void reduceResources() {
		List<Resource> resources = this.resourceDao.getByPlanetId(this.planet.getId());
		resources.stream().forEach((resource) -> {
			ResourceStorageUpgradingResourceDemand resourceStorageUpgradingResourceDemand = this.resourceDemandDao
					.getResourceStorageUpgradingResourceDemandByResourceFactoryTypeIdAndByDemandedResourceTypeId(
							this.resourceTypeId, resource.getResourceTypeId());
			int amount = resource.getAmount();
			amount -= resourceStorageUpgradingResourceDemand.getAmount() * this.howMany;
			resource.setAmount(amount);
		});
		this.resourceDao.update(resources);
	}
}