package com.radowiecki.transformers.controller;

import com.radowiecki.transformers.adapter.FleetFactoryAdapter;
import com.radowiecki.transformers.adapter.ScheduledFleetFactoryBuildingTaskAdapter;
import com.radowiecki.transformers.constant.BuildingStatus;
import com.radowiecki.transformers.dao.FleetFactoryDao;
import com.radowiecki.transformers.dao.ScheduledFleetFactoryBuildingTaskDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.model.FleetFactory;
import com.radowiecki.transformers.model.ScheduledFleetFactoryBuildingTask;
import com.radowiecki.transformers.model.TaskCompletionResponse;
import com.radowiecki.transformers.scheduling.FleetFactoryBuildingTaskScheduler;
import com.radowiecki.transformers.service.FleetFactoryService;
import com.radowiecki.transformers.service.LanguageServiceImpl;
import com.radowiecki.transformers.service.ScheduledTaskService;
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
public class FleetFacotryAJAXController {
	Logger logger = Logger.getLogger(FleetFacotryAJAXController.class);
	@Autowired
	private TransformersUtils controllersUtil;
	@Autowired
	private ScheduledFleetFactoryBuildingTaskDao scheduledFleetFactoryBuildingTaskDao;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	private FleetFactoryDao fleetFactoryDao;
	@Autowired
	private FleetFactoryService fleetFacotryService;
	@Autowired
	private LanguageServiceImpl languageServiceImpl;

	@RequestMapping({"/userFleetFactories"})
	public List<FleetFactoryAdapter> getUserFleetFactories() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		int chosenPlanetId = planetController.getChosenPlanetId();
		List<FleetFactory> planetFleets = this.fleetFactoryDao.getByPlanetId(chosenPlanetId);
		List<FleetFactoryAdapter> adaptedPlanetFleetFactories = this.fleetFacotryService.adapt(planetFleets);
		return adaptedPlanetFleetFactories;
	}

	@RequestMapping({"/isFleetFactoryBuildingTaskFinished"})
	public TaskCompletionResponse isFleetFactoryBuildingTaskFinished(@RequestParam int taskId) {
		this.scheduledTaskService.executeNotExecutedTasks();
		TaskCompletionResponse fleetFactoryBuildingTaskFinishedResponse = new TaskCompletionResponse();
		ScheduledFleetFactoryBuildingTask scheduledFleetFactoryBuildingTask = this.scheduledFleetFactoryBuildingTaskDao
				.getById(taskId);
		if (scheduledFleetFactoryBuildingTask != null) {
			long secondsToWait = TransformersUtils.computeRemainingSecondsToWait(scheduledFleetFactoryBuildingTask);
			if (!scheduledFleetFactoryBuildingTask.isDone() && secondsToWait != 0L) {
				scheduledFleetFactoryBuildingTask.setDone(false);
				fleetFactoryBuildingTaskFinishedResponse.setSecondsToWait(secondsToWait);
			} else {
				fleetFactoryBuildingTaskFinishedResponse.setDone(true);
			}
		} else {
			fleetFactoryBuildingTaskFinishedResponse.setTaskNotFound(true);
		}

		return fleetFactoryBuildingTaskFinishedResponse;
	}

	@RequestMapping({"/scheduledTasks/fleetFactoryBuildingTasks"})
	public List<ScheduledFleetFactoryBuildingTaskAdapter> getUserScheduledFleetFactoryBuildingTasks() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		List<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTasks = this.scheduledFleetFactoryBuildingTaskDao
				.getByPlanetId(planetController.getChosenPlanetId());
		List<ScheduledFleetFactoryBuildingTaskAdapter> scheduledFleetFactoryBuildingTaskAdatpers = ScheduledFleetFactoryBuildingTaskAdapter
				.adapt(scheduledFleetFactoryBuildingTasks, this.languageServiceImpl.getLanguagePropertiesMap());
		return scheduledFleetFactoryBuildingTaskAdatpers;
	}

	@RequestMapping({"/fleetFactoriesBuilding"})
	public List<ScheduledFleetFactoryBuildingTaskAdapter> startBuildingFleetFactories(
			@RequestParam Map<String, String> fleetBuildingRequest) {
		this.logger.info("fleetsBuilding request");
		Iterator var2 = fleetBuildingRequest.keySet().iterator();

		while (var2.hasNext()) {
			String key = (String) var2.next();
			String value = (String) fleetBuildingRequest.get(key);
			this.startBuildingFleetFactory(Integer.parseInt(key), Integer.parseInt(value));
		}

		return this.getUserScheduledFleetFactoryBuildingTasks();
	}

	@RequestMapping({"/fleetFactoryBuilding"})
	public String startBuildingFleetFactory(@RequestParam int fleetTypesId, @RequestParam int howMany) {
		try {
			FleetFactoryBuildingTaskScheduler fleetFactoryBuildingTaskScheduler = (FleetFactoryBuildingTaskScheduler) this.applicationContext
					.getBean("fleetFactoryBuildingTaskScheduler", FleetFactoryBuildingTaskScheduler.class);
			PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
					PlanetController.class);
			fleetFactoryBuildingTaskScheduler.setPlanet(planetController.getChosenPlanet());
			fleetFactoryBuildingTaskScheduler.setFleetTypeId(fleetTypesId);
			fleetFactoryBuildingTaskScheduler.setHowMany(howMany);
			fleetFactoryBuildingTaskScheduler.schedule();
		} catch (TooFewResourcesException var5) {
			return var5.prepareJSONResponse();
		} catch (InvalidParameterException var6) {
			return this.controllersUtil.prepareSimpleJSONResponse("STATUS",
					BuildingStatus.INVALID_PARAMETER.toString());
		}

		return this.controllersUtil.prepareSimpleJSONResponse("STATUS", BuildingStatus.OK.toString());
	}
}