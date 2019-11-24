package com.radowiecki.transformers.scheduling;

import com.radowiecki.transformers.dao.ResourceDao;
import com.radowiecki.transformers.dao.ResourceDemandDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ResourceFactoryBuildingResourceDemand;
import com.radowiecki.transformers.model.ScheduledResourceFactoryBuildingTask;
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
public class ResourceFactoryBuildingTaskScheduler {
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
			this.scheduleResourceFactoryBuildingTask();
		}

	}

	private void scheduleResourceFactoryBuildingTask() {
		List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks = new LinkedList();
		int secondsItTakes = this.calculateBuildingTime();
		ScheduledResourceFactoryBuildingTask theLatestscheduledResourceFactoryBuildingTask = this.scheduledTaskService
				.getTheLatestScheduledResourceFactoryBuildingTaskOnPlanet(this.planet.getId());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		if (this.scheduledTaskService.isExecutionDateAfterCurrentTime(theLatestscheduledResourceFactoryBuildingTask)) {
			gregorianCalendar.setTime(theLatestscheduledResourceFactoryBuildingTask.getExecutionDate());
			gregorianCalendar.add(13, 1);
		}

		for (int i = 0; i < this.howMany; ++i) {
			ScheduledResourceFactoryBuildingTask scheduledResourceFactoryBuildingTask = new ScheduledResourceFactoryBuildingTask();
			scheduledResourceFactoryBuildingTask.setResourceTypeId(this.resourceTypeId);
			scheduledResourceFactoryBuildingTask.setPlanetId(this.planet.getId());
			scheduledResourceFactoryBuildingTask.setStartDate(gregorianCalendar.getTime());
			gregorianCalendar.add(13, secondsItTakes);
			scheduledResourceFactoryBuildingTask.setExecutionDate(gregorianCalendar.getTime());
			gregorianCalendar.add(13, 1);
			scheduledResourceFactoryBuildingTask.setDone(false);
			scheduledResourceFactoryBuildingTasks.add(scheduledResourceFactoryBuildingTask);
		}

		this.scheduledTaskService.persistScheduledResourceFactoryBuildingTasks(scheduledResourceFactoryBuildingTasks);
	}

	private int calculateBuildingTime() {
		return 40;
	}

	private boolean isPossibleToBuild() throws TooFewResourcesException {
		List<ResourceFactoryBuildingResourceDemand> resourceFactoryBuildingResourceDemands = this.resourceDemandDao
				.getResourceFacotryBuildingResourceDemandsByResourceFactoryTypeId(this.resourceTypeId);
		List<Resource> userResources = this.resourceDao.getByPlanetId(this.planet.getId());
		Map<Resource, Boolean> enoughResources = new HashMap();
		boolean isPossibleToBuild = true;
		Iterator var5 = userResources.iterator();

		while (var5.hasNext()) {
			Resource r = (Resource) var5.next();
			ResourceFactoryBuildingResourceDemand fleetResourceDemand = (ResourceFactoryBuildingResourceDemand) ((List) resourceFactoryBuildingResourceDemands
					.stream().filter((frd) -> {
						return frd.getDemandedResourceTypeId() == r.getResourceTypeId();
					}).collect(Collectors.toList())).get(0);
			if (r.getAmount() - fleetResourceDemand.getAmount() * this.howMany < 0) {
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
			ResourceFactoryBuildingResourceDemand resourceFactoryBuildingResourceDemand = this.resourceDemandDao
					.getResourceFactoryBuildingResourceDemandByResourceFactoryTypeIdAndByDemandedResourceTypeId(
							this.resourceTypeId, resource.getResourceTypeId());
			int amount = resource.getAmount();
			amount -= resourceFactoryBuildingResourceDemand.getAmount() * this.howMany;
			resource.setAmount(amount);
		});
		this.resourceDao.update(resources);
	}
}