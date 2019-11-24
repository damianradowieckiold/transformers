package com.radowiecki.transformers.scheduling;

import com.radowiecki.transformers.dao.ResourceDao;
import com.radowiecki.transformers.dao.ResourceDemandDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.model.FleetFactoryBuildingResourceDemand;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ScheduledFleetFactoryBuildingTask;
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
public class FleetFactoryBuildingTaskScheduler {
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceDemandDao resourceDemandDao;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	private Planet planet;
	private int fleetTypeId;
	private int howMany;

	public void setHowMany(int howMany) {
		this.howMany = howMany;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	public void setFleetTypeId(int fleetTypeId) {
		this.fleetTypeId = fleetTypeId;
	}

	public void schedule() throws InvalidParameterException, TooFewResourcesException {
		if (this.isPossibleToBuild()) {
			this.reduceResources();
			this.scheduleFleetFactoryBuildingTask();
		}

	}

	private void scheduleFleetFactoryBuildingTask() {
		List<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTasks = new LinkedList();
		int secondsItTakes = this.calculateBuildingTime();
		ScheduledFleetFactoryBuildingTask theLatestscheduledFleetFactoryBuildingTask = this.scheduledTaskService
				.getTheLatestScheduledFleetFactoryBuildingTaskOnPlanet(this.planet.getId());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		if (this.scheduledTaskService.isExecutionDateAfterCurrentTime(theLatestscheduledFleetFactoryBuildingTask)) {
			gregorianCalendar.setTime(theLatestscheduledFleetFactoryBuildingTask.getExecutionDate());
			gregorianCalendar.add(13, 1);
		}

		for (int i = 0; i < this.howMany; ++i) {
			ScheduledFleetFactoryBuildingTask scheduledFleetFactoryBuildingTask = new ScheduledFleetFactoryBuildingTask();
			scheduledFleetFactoryBuildingTask.setFleetTypeId(this.fleetTypeId);
			scheduledFleetFactoryBuildingTask.setPlanetId(this.planet.getId());
			scheduledFleetFactoryBuildingTask.setStartDate(gregorianCalendar.getTime());
			gregorianCalendar.add(13, secondsItTakes);
			scheduledFleetFactoryBuildingTask.setExecutionDate(gregorianCalendar.getTime());
			gregorianCalendar.add(13, 1);
			scheduledFleetFactoryBuildingTask.setDone(false);
			scheduledFleetFactoryBuildingTasks.add(scheduledFleetFactoryBuildingTask);
		}

		this.scheduledTaskService.persistScheduledFleetFactoryBuildingTasks(scheduledFleetFactoryBuildingTasks);
	}

	private int calculateBuildingTime() {
		return 40;
	}

	private boolean isPossibleToBuild() throws TooFewResourcesException {
		List<FleetFactoryBuildingResourceDemand> fleetFactoryBuildingResourceDemands = this.resourceDemandDao
				.getFleetFacotryBuildingResourceDemandsByFleetFactoryTypeId(this.fleetTypeId);
		List<Resource> userResources = this.resourceDao.getByPlanetId(this.planet.getId());
		Map<Resource, Boolean> enoughResources = new HashMap();
		boolean isPossibleToBuild = true;
		Iterator var5 = userResources.iterator();

		while (var5.hasNext()) {
			Resource r = (Resource) var5.next();
			FleetFactoryBuildingResourceDemand fleetResourceDemand = (FleetFactoryBuildingResourceDemand) ((List) fleetFactoryBuildingResourceDemands
					.stream().filter((frd) -> {
						return frd.getDemandeDresourceTypeId() == r.getResourceTypeId();
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
			FleetFactoryBuildingResourceDemand fleetResourceDemand = this.resourceDemandDao
					.getFleetFactoryBuildingResourceDemandByFleetFactoryTypeIdAndByDemandedResourceTypeId(
							this.fleetTypeId, resource.getResourceTypeId());
			int amount = resource.getAmount();
			amount -= fleetResourceDemand.getAmount() * this.howMany;
			resource.setAmount(amount);
		});
		this.resourceDao.update(resources);
	}
}