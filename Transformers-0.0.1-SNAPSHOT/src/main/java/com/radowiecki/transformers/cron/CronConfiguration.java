package com.radowiecki.transformers.cron;

import com.radowiecki.transformers.service.ResourceService;
import com.radowiecki.transformers.service.ScheduledTaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CronConfiguration {
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	private final Logger logger = Logger.getLogger(CronConfiguration.class);

	@Scheduled(cron = "0/60 * * * * ?")
	public void increaseResourceValues() {
		this.logger.info("Increasing resource values.");
		this.resourceService.increaseResourceValues();
	}

	@Scheduled(cron = "0 0/5 * * * ?")
	public void deleteOutdatedTasks() {
		this.logger.info("Deleting outdated tasks");
		this.scheduledTaskService.deleteOutdatedFleetBuildingTasks();
		this.scheduledTaskService.deleteOutdatedAttackTasks();
		this.scheduledTaskService.deleteOutdatedFleetFactoryBuildingTasks();
		this.scheduledTaskService.deleteOutdatedResourceFactoryBuildingTasks();
		this.scheduledTaskService.deleteOutdatedResrouceStorageFactoryUpgradingTasks();
	}
}