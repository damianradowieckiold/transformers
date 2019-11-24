package com.radowiecki.transformers.controller;

import com.radowiecki.transformers.adapter.FleetAdapter;
import com.radowiecki.transformers.adapter.ResourceAdapter;
import com.radowiecki.transformers.adapter.ScheduledAttackTaskAdapter;
import com.radowiecki.transformers.adapter.ScheduledFleetBuildingTaskAdapter;
import com.radowiecki.transformers.constant.BuildingStatus;
import com.radowiecki.transformers.dao.FleetDao;
import com.radowiecki.transformers.dao.FleetTypeDao;
import com.radowiecki.transformers.dao.ResourceDao;
import com.radowiecki.transformers.dao.ResourceTypeDao;
import com.radowiecki.transformers.dao.ScheduledAttackTaskDao;
import com.radowiecki.transformers.dao.ScheduledFleetBuildingTaskDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.language.Language;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.FleetType;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ResourceType;
import com.radowiecki.transformers.model.ScheduledAttackTask;
import com.radowiecki.transformers.model.ScheduledFleetBuildingTask;
import com.radowiecki.transformers.model.TaskCompletionResponse;
import com.radowiecki.transformers.scheduling.AttackTaskScheduler;
import com.radowiecki.transformers.scheduling.FleetBuildingTaskScheduler;
import com.radowiecki.transformers.service.FleetService;
import com.radowiecki.transformers.service.LanguageServiceImpl;
import com.radowiecki.transformers.service.ResourceService;
import com.radowiecki.transformers.service.ScheduledTaskService;
import com.radowiecki.transformers.service.UserService;
import com.radowiecki.transformers.utils.TransformersUtils;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AJAXController {
	Logger logger = Logger.getLogger(AJAXController.class);
	@Autowired
	private TransformersUtils controllersUtil;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ScheduledFleetBuildingTaskDao scheduledFleetBuildingTaskDao;
	@Autowired
	private FleetTypeDao fleetTypeDao;
	@Autowired
	private LanguageServiceImpl languageServiceImpl;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	private UserService userService;
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private FleetService fleetService;
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	@Autowired
	private ScheduledAttackTaskDao scheduledAttackTaskDao;

	@RequestMapping({"/userResources"})
	public List<ResourceAdapter> getUserResources() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		List<Resource> planetResources = this.resourceDao.getByPlanetId(planetController.getChosenPlanetId());
		List<ResourceAdapter> adaptedPlanetResources = this.resourceService.adapt(planetResources);
		return adaptedPlanetResources;
	}

	@RequestMapping({"/changeLanguage"})
	public String changeLanguage(@RequestParam String language) {
		this.languageServiceImpl.changeLanguage(Language.valueOf(language));
		return "CHANGED_LANGUAGE TO " + language;
	}

	@RequestMapping({"/fleetsBuilding"})
	public List<ScheduledFleetBuildingTaskAdapter> startBuildingFleets(
			@RequestParam Map<String, String> fleetBuildingRequest) {
		this.logger.info("fleetsBuilding request");
		Iterator var2 = fleetBuildingRequest.keySet().iterator();

		while (var2.hasNext()) {
			String key = (String) var2.next();
			String value = (String) fleetBuildingRequest.get(key);
			this.startBuildingFleet(Integer.parseInt(key), Integer.parseInt(value));
		}

		return this.getUserScheduledFleetBuildingTasks();
	}

	@RequestMapping({"/fleetBuilding"})
	public String startBuildingFleet(@RequestParam int fleetTypesId, @RequestParam int howMany) {
		try {
			FleetBuildingTaskScheduler fleetBuildingTaskScheduler = (FleetBuildingTaskScheduler) this.applicationContext
					.getBean("fleetBuildingTaskScheduler", FleetBuildingTaskScheduler.class);
			PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
					PlanetController.class);
			fleetBuildingTaskScheduler.setPlanet(planetController.getChosenPlanet());
			fleetBuildingTaskScheduler.setFleetTypeId(fleetTypesId);
			fleetBuildingTaskScheduler.setHowMany(howMany);
			fleetBuildingTaskScheduler.schedule();
		} catch (TooFewResourcesException var5) {
			return var5.prepareJSONResponse();
		} catch (InvalidParameterException var6) {
			return this.controllersUtil.prepareSimpleJSONResponse("STATUS",
					BuildingStatus.INVALID_PARAMETER.toString());
		}

		return this.controllersUtil.prepareSimpleJSONResponse("STATUS", BuildingStatus.OK.toString());
	}

	@RequestMapping({"/attack"})
	public void attack(@RequestParam int planetId, @RequestParam int fleetTypesId, @RequestParam int amount) {
		AttackTaskScheduler attackTaskScheduler = (AttackTaskScheduler) this.applicationContext
				.getBean("attackTaskScheduler", AttackTaskScheduler.class);
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		attackTaskScheduler.setAttackingPlanetId(planetController.getChosenPlanet().getId());
		attackTaskScheduler.setAttackedPlanetId(planetId);
		attackTaskScheduler.setFleetTypeId(fleetTypesId);
		attackTaskScheduler.setAmountOfAttackingFleet(amount);
		attackTaskScheduler.schedule();
	}

	@RequestMapping({"/isAttackTaskFinished"})
	public TaskCompletionResponse attack(@RequestParam int taskId) {
		this.scheduledTaskService.executeNotExecutedTasks();
		TaskCompletionResponse attackTaskFinishedResponse = new TaskCompletionResponse();
		ScheduledAttackTask scheduledAttackTask = this.scheduledAttackTaskDao.getById(taskId);
		if (scheduledAttackTask != null) {
			long secondsToWait = TransformersUtils.computeRemainingSecondsToWait(scheduledAttackTask);
			if (!scheduledAttackTask.isDone() && secondsToWait != 0L) {
				attackTaskFinishedResponse.setDone(false);
				attackTaskFinishedResponse.setSecondsToWait(secondsToWait);
			} else {
				attackTaskFinishedResponse.setDone(true);
			}
		} else {
			attackTaskFinishedResponse.setTaskNotFound(true);
		}

		return attackTaskFinishedResponse;
	}

	@RequestMapping({"/fleetTypes"})
	public List<FleetType> getFleetTypes() {
		return this.fleetTypeDao.getAll();
	}

	@RequestMapping({"/resourceTypes"})
	public List<ResourceType> getResourceTypes() {
		return this.resourceTypeDao.getAll();
	}

	@RequestMapping({"/userFleets"})
	public List<FleetAdapter> getUserFleets() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		int chosenPlanetId = planetController.getChosenPlanetId();
		List<Fleet> planetFleets = this.fleetDao.getByPlanetId(chosenPlanetId);
		List<FleetAdapter> adaptedPlanetFleets = this.fleetService.adapt(planetFleets);
		return adaptedPlanetFleets;
	}

	@RequestMapping({"/scheduledTasks/fleetBuildingTasks"})
	public List<ScheduledFleetBuildingTaskAdapter> getUserScheduledFleetBuildingTasks() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks = this.scheduledFleetBuildingTaskDao
				.getByPlanetId(planetController.getChosenPlanetId());
		List<ScheduledFleetBuildingTaskAdapter> scheduledFleetBuildingTaskAdatpers = ScheduledFleetBuildingTaskAdapter
				.adapt(scheduledFleetBuildingTasks, this.languageServiceImpl.getLanguagePropertiesMap());
		return scheduledFleetBuildingTaskAdatpers;
	}

	@RequestMapping({"/scheduledTasks/attackTasks"})
	public List<ScheduledAttackTaskAdapter> getUserScheduledAttackTasks() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		List<ScheduledAttackTaskAdapter> userScheduledAttackTasks = this.scheduledTaskService
				.getAdaptedScheduledAttackTasksRelatedToPlanet(planetController.getChosenPlanet());
		return userScheduledAttackTasks;
	}

	@RequestMapping({"/planet/chosenPlanet"})
	public String setChosenPlanet(@RequestParam int planetId) {
		if (this.userService.canUserAskForPlanetWithId(planetId)) {
			PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
					PlanetController.class);
			planetController.setChosenPlanetId(planetId);
			return String.valueOf(planetController.getChosenPlanetId());
		} else {
			return "NO_ACCESS";
		}
	}

	@RequestMapping({"/isFleetBuildingTaskFinished"})
	public TaskCompletionResponse isFleetBuildingTaskFinished(@RequestParam int taskId) {
		this.scheduledTaskService.executeNotExecutedTasks();
		TaskCompletionResponse fleetBuildingTaskFinishedResponse = new TaskCompletionResponse();
		ScheduledFleetBuildingTask scheduledFleetBuildingTask = this.scheduledFleetBuildingTaskDao.getById(taskId);
		if (scheduledFleetBuildingTask != null) {
			long secondsToWait = TransformersUtils.computeRemainingSecondsToWait(scheduledFleetBuildingTask);
			if (!scheduledFleetBuildingTask.isDone() && secondsToWait != 0L) {
				fleetBuildingTaskFinishedResponse.setDone(false);
				fleetBuildingTaskFinishedResponse.setSecondsToWait(secondsToWait);
			} else {
				fleetBuildingTaskFinishedResponse.setDone(true);
			}
		} else {
			fleetBuildingTaskFinishedResponse.setTaskNotFound(true);
		}

		return fleetBuildingTaskFinishedResponse;
	}
}