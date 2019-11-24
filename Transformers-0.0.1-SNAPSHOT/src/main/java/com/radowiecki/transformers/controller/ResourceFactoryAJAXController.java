package com.radowiecki.transformers.controller;

import com.radowiecki.transformers.adapter.ResourceFactoryAdapter;
import com.radowiecki.transformers.adapter.ScheduledResourceFactoryBuildingTaskAdapter;
import com.radowiecki.transformers.constant.BuildingStatus;
import com.radowiecki.transformers.dao.ResourceFactoryDao;
import com.radowiecki.transformers.dao.ScheduledResourceFactoryBuildingTaskDao;
import com.radowiecki.transformers.exception.TooFewResourcesException;
import com.radowiecki.transformers.model.ResourceFactory;
import com.radowiecki.transformers.model.ScheduledResourceFactoryBuildingTask;
import com.radowiecki.transformers.model.TaskCompletionResponse;
import com.radowiecki.transformers.scheduling.ResourceFactoryBuildingTaskScheduler;
import com.radowiecki.transformers.service.LanguageServiceImpl;
import com.radowiecki.transformers.service.ResourceFactoryService;
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
public class ResourceFactoryAJAXController {
	Logger logger = Logger.getLogger(ResourceFactoryAJAXController.class);
	@Autowired
	private TransformersUtils controllersUtil;
	@Autowired
	private ScheduledResourceFactoryBuildingTaskDao scheduledResourceFactoryBuildingTaskDao;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	private ResourceFactoryDao resourceFactoryDao;
	@Autowired
	private ResourceFactoryService resourceFacotryService;
	@Autowired
	private LanguageServiceImpl languageServiceImpl;

	@RequestMapping({"/userResourceFactories"})
	public List<ResourceFactoryAdapter> getUserResourceFactories() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		int chosenPlanetId = planetController.getChosenPlanetId();
		List<ResourceFactory> planetFleets = this.resourceFactoryDao.getByPlanetId(chosenPlanetId);
		List<ResourceFactoryAdapter> adaptedPlanetResourceFactories = this.resourceFacotryService.adapt(planetFleets);
		return adaptedPlanetResourceFactories;
	}

	@RequestMapping({"/isResourceFactoryBuildingTaskFinished"})
	public TaskCompletionResponse isResourceFactoryBuildingTaskFinished(@RequestParam int taskId) {
		this.scheduledTaskService.executeNotExecutedTasks();
		TaskCompletionResponse resourceFactoryBuildingTaskFinishedResponse = new TaskCompletionResponse();
		ScheduledResourceFactoryBuildingTask scheduledResourceFactoryBuildingTask = this.scheduledResourceFactoryBuildingTaskDao
				.getById(taskId);
		if (scheduledResourceFactoryBuildingTask != null) {
			long secondsToWait = TransformersUtils.computeRemainingSecondsToWait(scheduledResourceFactoryBuildingTask);
			if (!scheduledResourceFactoryBuildingTask.isDone() && secondsToWait != 0L) {
				scheduledResourceFactoryBuildingTask.setDone(false);
				resourceFactoryBuildingTaskFinishedResponse.setSecondsToWait(secondsToWait);
			} else {
				resourceFactoryBuildingTaskFinishedResponse.setDone(true);
			}
		} else {
			resourceFactoryBuildingTaskFinishedResponse.setTaskNotFound(true);
		}

		return resourceFactoryBuildingTaskFinishedResponse;
	}

	@RequestMapping({"/scheduledTasks/resourceFactoryBuildingTasks"})
	public List<ScheduledResourceFactoryBuildingTaskAdapter> getUserScheduledResourceFactoryBuildingTasks() {
		PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
				PlanetController.class);
		List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks = this.scheduledResourceFactoryBuildingTaskDao
				.getByPlanetId(planetController.getChosenPlanetId());
		List<ScheduledResourceFactoryBuildingTaskAdapter> scheduledResourceFactoryBuildingTaskAdatpers = ScheduledResourceFactoryBuildingTaskAdapter
				.adapt(scheduledResourceFactoryBuildingTasks, this.languageServiceImpl.getLanguagePropertiesMap());
		return scheduledResourceFactoryBuildingTaskAdatpers;
	}

	@RequestMapping({"/resourceFactoriesBuilding"})
	public List<ScheduledResourceFactoryBuildingTaskAdapter> startBuildingResourceFactories(
			@RequestParam Map<String, String> resourceBuildingRequest) {
		Iterator var2 = resourceBuildingRequest.keySet().iterator();

		while (var2.hasNext()) {
			String key = (String) var2.next();
			String value = (String) resourceBuildingRequest.get(key);
			this.startBuildingResourceFactory(Integer.parseInt(key), Integer.parseInt(value));
		}

		return this.getUserScheduledResourceFactoryBuildingTasks();
	}

	@RequestMapping({"/resourceFactoryBuilding"})
	public String startBuildingResourceFactory(@RequestParam int resourceTypesId, @RequestParam int howMany) {
		try {
			ResourceFactoryBuildingTaskScheduler resourceFactoryBuildingTaskScheduler = (ResourceFactoryBuildingTaskScheduler) this.applicationContext
					.getBean("resourceFactoryBuildingTaskScheduler", ResourceFactoryBuildingTaskScheduler.class);
			PlanetController planetController = (PlanetController) this.applicationContext.getBean("planetController",
					PlanetController.class);
			resourceFactoryBuildingTaskScheduler.setPlanet(planetController.getChosenPlanet());
			resourceFactoryBuildingTaskScheduler.setResourceTypeId(resourceTypesId);
			resourceFactoryBuildingTaskScheduler.setHowMany(howMany);
			resourceFactoryBuildingTaskScheduler.schedule();
		} catch (TooFewResourcesException var5) {
			return var5.prepareJSONResponse();
		} catch (InvalidParameterException var6) {
			return this.controllersUtil.prepareSimpleJSONResponse("STATUS",
					BuildingStatus.INVALID_PARAMETER.toString());
		}

		return this.controllersUtil.prepareSimpleJSONResponse("STATUS", BuildingStatus.OK.toString());
	}
}