package com.radowiecki.transformers.controller;

import com.radowiecki.transformers.adapter.ResourceStorageAdapter;
import com.radowiecki.transformers.adapter.ScheduledResourceStorageUpgradingTaskAdapter;
import com.radowiecki.transformers.constant.BuildingStatus;
import com.radowiecki.transformers.dao.ResourceStorageDao;
import com.radowiecki.transformers.dao.ScheduledResourceStorageUpgradingTaskDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.model.ResourceStorage;
import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;
import com.radowiecki.transformers.model.TaskCompletionResponse;
import com.radowiecki.transformers.scheduling.ResourceStorageUpgradingTaskScheduler;
import com.radowiecki.transformers.service.LanguageServiceImpl;
import com.radowiecki.transformers.service.ResourceStorageService;
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
public class ResourceStorageUpgradingAJAXController {
	Logger logger = Logger.getLogger(ResourceStorageUpgradingAJAXController.class);
	@Autowired
	private TransformersUtils controllersUtil;
	@Autowired
	private ScheduledResourceStorageUpgradingTaskDao scheduledResourceStorageUpgradingTaskDao;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	private ResourceStorageDao resourceStorageDao;
	@Autowired
	private ResourceStorageService resourceStorageService;
	@Autowired
	private LanguageServiceImpl languageServiceImpl;

	@RequestMapping({"/userResourceStorages"})
	public List<ResourceStorageAdapter> getUserResourceStorages() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		int chosenPlanetId = planetController.getChosenPlanetId();
		List<ResourceStorage> resourceStorages = this.resourceStorageDao.getByPlanetId(chosenPlanetId);
		List<ResourceStorageAdapter> adaptedPlanetResourceStorages = this.resourceStorageService
				.adapt(resourceStorages);
		return adaptedPlanetResourceStorages;
	}

	@RequestMapping({"/isResourceStorageBuildingTaskFinished"})
	public TaskCompletionResponse isResourceStorageFactoryBuildingTaskFinished(@RequestParam int taskId) {
		this.scheduledTaskService.executeNotExecutedTasks();
		TaskCompletionResponse resourceStorageBuildingTaskFinishedResponse = new TaskCompletionResponse();
		ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask = this.scheduledResourceStorageUpgradingTaskDao
				.getById(taskId);
		if (scheduledResourceStorageUpgradingTask != null) {
			long secondsToWait = TransformersUtils.computeRemainingSecondsToWait(scheduledResourceStorageUpgradingTask);
			if (!scheduledResourceStorageUpgradingTask.isDone() && secondsToWait != 0L) {
				scheduledResourceStorageUpgradingTask.setDone(false);
				resourceStorageBuildingTaskFinishedResponse.setSecondsToWait(secondsToWait);
			} else {
				resourceStorageBuildingTaskFinishedResponse.setDone(true);
			}
		} else {
			resourceStorageBuildingTaskFinishedResponse.setTaskNotFound(true);
		}

		return resourceStorageBuildingTaskFinishedResponse;
	}

	@RequestMapping({"/scheduledTasks/resoureStorageBuildingTasks"})
	public List<ScheduledResourceStorageUpgradingTaskAdapter> getUserScheduledResourceStorageUpgradingTasks() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks = this.scheduledResourceStorageUpgradingTaskDao
				.getByPlanetId(planetController.getChosenPlanetId());
		List<ScheduledResourceStorageUpgradingTaskAdapter> scheduledResourceStorageUpgradingTaskAdapters = ScheduledResourceStorageUpgradingTaskAdapter
				.adapt(scheduledResourceStorageUpgradingTasks, this.languageServiceImpl.getLanguagePropertiesMap());
		return scheduledResourceStorageUpgradingTaskAdapters;
	}

	@RequestMapping({"/resourceStoragesBuilding"})
	public List<ScheduledResourceStorageUpgradingTaskAdapter> startUpgradingResourceStorage(
			@RequestParam Map<String, String> resourceBuildingRequest) {
		Iterator var2 = resourceBuildingRequest.keySet().iterator();

		while (var2.hasNext()) {
			String key = (String) var2.next();
			String value = (String) resourceBuildingRequest.get(key);
			this.startUpgradingResourceStorage(Integer.parseInt(key), Integer.parseInt(value));
		}

		return this.getUserScheduledResourceStorageUpgradingTasks();
	}

	@RequestMapping({"/resourceStorageBuilding"})
	public String startUpgradingResourceStorage(@RequestParam int resourceTypesId, @RequestParam int howMany) {
		try {
			ResourceStorageUpgradingTaskScheduler resourceStorageUpgradingTaskScheduler = (ResourceStorageUpgradingTaskScheduler) this.applicationContext
					.getBean("resourceStorageUpgradingTaskScheduler", ResourceStorageUpgradingTaskScheduler.class);
			PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
					PlanetController.class);
			resourceStorageUpgradingTaskScheduler.setPlanet(planetController.getChosenPlanet());
			resourceStorageUpgradingTaskScheduler.setResourceTypeId(resourceTypesId);
			resourceStorageUpgradingTaskScheduler.setHowMany(howMany);
			resourceStorageUpgradingTaskScheduler.schedule();
		} catch (TooFewResourcesException var5) {
			return var5.prepareJSONResponse();
		} catch (InvalidParameterException var6) {
			return this.controllersUtil.prepareSimpleJSONResponse("STATUS",
					BuildingStatus.INVALID_PARAMETER.toString());
		}

		return this.controllersUtil.prepareSimpleJSONResponse("STATUS", BuildingStatus.OK.toString());
	}
}