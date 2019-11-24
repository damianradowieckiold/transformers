package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.ScheduledAttackTaskAdapter;
import com.radowiecki.transformers.dao.FleetDao;
import com.radowiecki.transformers.dao.FleetFactoryDao;
import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.dao.ResourceFactoryDao;
import com.radowiecki.transformers.dao.ResourceStorageDao;
import com.radowiecki.transformers.dao.ScheduledAttackTaskDao;
import com.radowiecki.transformers.dao.ScheduledFleetBuildingTaskDao;
import com.radowiecki.transformers.dao.ScheduledFleetFactoryBuildingTaskDao;
import com.radowiecki.transformers.dao.ScheduledResourceFactoryBuildingTaskDao;
import com.radowiecki.transformers.dao.ScheduledResourceStorageUpgradingTaskDao;
import com.radowiecki.transformers.dao.UserDao;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.FleetFactory;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.ResourceFactory;
import com.radowiecki.transformers.model.ResourceStorage;
import com.radowiecki.transformers.model.ScheduledAttackTask;
import com.radowiecki.transformers.model.ScheduledFleetBuildingTask;
import com.radowiecki.transformers.model.ScheduledFleetFactoryBuildingTask;
import com.radowiecki.transformers.model.ScheduledResourceFactoryBuildingTask;
import com.radowiecki.transformers.model.ScheduledResourceStorageUpgradingTask;
import com.radowiecki.transformers.model.ScheduledTask;
import com.radowiecki.transformers.model.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class ScheduledTaskServiceImpl implements ScheduledTaskService {
	@Autowired
	private ScheduledFleetBuildingTaskDao scheduledFleetBuildingTaskDao;
	@Autowired
	private ScheduledFleetFactoryBuildingTaskDao scheduledFleetFactoryBuildingTaskDao;
	@Autowired
	private ScheduledAttackTaskDao scheduledAttackTaskDao;
	@Autowired
	private ScheduledResourceFactoryBuildingTaskDao scheduledResourceFactoryBuildingTaskDao;
	@Autowired
	private ScheduledResourceStorageUpgradingTaskDao scheduledResourceStorageUpgradingTaskDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private PlanetDao planetDao;
	@Autowired
	private FleetFactoryDao fleetFactoryDao;
	@Autowired
	private ResourceFactoryDao resourceFactoryDao;
	@Autowired
	private ResourceStorageDao resourceStorageDao;
	@Autowired
	private AttackService attackService;
	private Comparator<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTaskComparator = (sfbt1,
			sfbt2) -> {
		GregorianCalendar sfbt1Date = new GregorianCalendar();
		GregorianCalendar sfbt2Date = new GregorianCalendar();
		sfbt1Date.setTime(sfbt1.getExecutionDate());
		sfbt2Date.setTime(sfbt2.getExecutionDate());
		return Long.compare(sfbt1Date.getTimeInMillis(), sfbt2Date.getTimeInMillis());
	};
	private Comparator<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTaskComparator = (sfrbt1,
			srfbt2) -> {
		GregorianCalendar sfbt1Date = new GregorianCalendar();
		GregorianCalendar sfbt2Date = new GregorianCalendar();
		sfbt1Date.setTime(sfrbt1.getExecutionDate());
		sfbt2Date.setTime(srfbt2.getExecutionDate());
		return Long.compare(sfbt1Date.getTimeInMillis(), sfbt2Date.getTimeInMillis());
	};
	private Comparator<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTaskComparator = (
			srsutc1, srsutc2) -> {
		GregorianCalendar sfbt1Date = new GregorianCalendar();
		GregorianCalendar sfbt2Date = new GregorianCalendar();
		sfbt1Date.setTime(srsutc1.getExecutionDate());
		sfbt2Date.setTime(srsutc2.getExecutionDate());
		return Long.compare(sfbt1Date.getTimeInMillis(), sfbt2Date.getTimeInMillis());
	};

	@Before("execution(* com.radowiecki.transformers.dao.ScheduledFleetBuildingTaskDaoImpl.getByUserId(..))")
	public void deleteOutdatedFleetBuildingTasks() {
		List<ScheduledFleetBuildingTask> allFleetBuildingTasks = this.scheduledFleetBuildingTaskDao.getAll();
		List<ScheduledFleetBuildingTask> outdatedFleetBuildingTasks = (List) allFleetBuildingTasks.stream()
				.filter((task) -> {
					return task.getExecutionDate().compareTo(new Date()) < 0;
				}).collect(Collectors.toList());
		Iterator var3 = outdatedFleetBuildingTasks.iterator();

		while (var3.hasNext()) {
			ScheduledFleetBuildingTask sfbt = (ScheduledFleetBuildingTask) var3.next();
			this.scheduledFleetBuildingTaskDao.delete(sfbt);
		}

	}

	@Before("execution(* com.radowiecki.transformers.dao.ScheduledAttackTaskDaoImpl.getByAttackedUserId(..)) ||execution(* com.radowiecki.transformers.dao.ScheduledAttackTaskDaoImpl.getByAttackingUserId(..))")
	public void deleteOutdatedAttackTasks() {
		List<ScheduledAttackTask> scheduledTasks = this.scheduledAttackTaskDao.getAll();
		List<ScheduledAttackTask> outdatedTasks = (List) scheduledTasks.stream().filter((task) -> {
			return task.getExecutionDate().compareTo(new Date()) < 0;
		}).collect(Collectors.toList());
		Iterator var3 = outdatedTasks.iterator();

		while (var3.hasNext()) {
			ScheduledAttackTask sat = (ScheduledAttackTask) var3.next();
			this.scheduledAttackTaskDao.delete(sat);
		}

	}

	@Before("execution(* com.radowiecki.transformers.dao.ScheduledFleetFactoryBuildingTaskDaoImpl.getByUserId(..))")
	public void deleteOutdatedFleetFactoryBuildingTasks() {
		List<ScheduledFleetFactoryBuildingTask> allFleetFactoryBuildingTasks = this.scheduledFleetFactoryBuildingTaskDao
				.getAll();
		List<ScheduledFleetFactoryBuildingTask> outdatedFleetFactoryBuildingTasks = (List) allFleetFactoryBuildingTasks
				.stream().filter((task) -> {
					return task.getExecutionDate().compareTo(new Date()) < 0;
				}).collect(Collectors.toList());
		Iterator var3 = outdatedFleetFactoryBuildingTasks.iterator();

		while (var3.hasNext()) {
			ScheduledFleetFactoryBuildingTask sffbt = (ScheduledFleetFactoryBuildingTask) var3.next();
			this.scheduledFleetFactoryBuildingTaskDao.delete(sffbt);
		}

	}

	@Before("execution(* com.radowiecki.transformers.dao.ScheduledResourceFactoryBuildingTaskDaoImpl.getByUserId(..))")
	public void deleteOutdatedResourceFactoryBuildingTasks() {
		List<ScheduledResourceFactoryBuildingTask> allResourceFactoryBuildingTasks = this.scheduledResourceFactoryBuildingTaskDao
				.getAll();
		List<ScheduledResourceFactoryBuildingTask> outdatedResourceFactoryBuildingTasks = (List) allResourceFactoryBuildingTasks
				.stream().filter((task) -> {
					return task.getExecutionDate().compareTo(new Date()) < 0;
				}).collect(Collectors.toList());
		Iterator var3 = outdatedResourceFactoryBuildingTasks.iterator();

		while (var3.hasNext()) {
			ScheduledResourceFactoryBuildingTask srfbt = (ScheduledResourceFactoryBuildingTask) var3.next();
			this.scheduledResourceFactoryBuildingTaskDao.delete(srfbt);
		}

	}

	@Before("execution(* com.radowiecki.transformers.dao.ScheduledResourceStorageUpgradingTaskDaoImpl.getByUserId(..))")
	public void deleteOutdatedResrouceStorageFactoryUpgradingTasks() {
		List<ScheduledResourceStorageUpgradingTask> allResourceStorageUpgradingTasks = this.scheduledResourceStorageUpgradingTaskDao
				.getAll();
		List<ScheduledResourceStorageUpgradingTask> outdatedResourceStorageUpgradingTasks = (List) allResourceStorageUpgradingTasks
				.stream().filter((task) -> {
					return task.getExecutionDate().compareTo(new Date()) < 0;
				}).collect(Collectors.toList());
		Iterator var3 = outdatedResourceStorageUpgradingTasks.iterator();

		while (var3.hasNext()) {
			ScheduledResourceStorageUpgradingTask srsut = (ScheduledResourceStorageUpgradingTask) var3.next();
			this.scheduledResourceStorageUpgradingTaskDao.delete(srsut);
		}

	}

	public List<ScheduledAttackTask> getAllAttackTasksRelatedToPlanet(Planet planet) {
		List<ScheduledAttackTask> userScheduledAttackTasks = new ArrayList();
		userScheduledAttackTasks.addAll(this.scheduledAttackTaskDao.getByAttackedPlanetId(planet.getId()));
		userScheduledAttackTasks.addAll(this.scheduledAttackTaskDao.getByAttackingPlanetId(planet.getId()));
		return userScheduledAttackTasks;
	}

	public List<ScheduledAttackTaskAdapter> adaptScheduledAttackTasks(List<ScheduledAttackTask> scheduledAttackTasks) {
		List<ScheduledAttackTaskAdapter> scheduledAttackTaskAdapters = new ArrayList();
		Iterator var3 = scheduledAttackTasks.iterator();

		while (var3.hasNext()) {
			ScheduledAttackTask sat = (ScheduledAttackTask) var3.next();
			ScheduledAttackTaskAdapter scheduledAttackTaskAdapter = this.adaptScheduledAttackTask(sat);
			scheduledAttackTaskAdapters.add(scheduledAttackTaskAdapter);
		}

		return scheduledAttackTaskAdapters;
	}

	public List<ScheduledAttackTaskAdapter> getAdaptedScheduledAttackTasksRelatedToPlanet(Planet planet) {
		List<ScheduledAttackTask> scheduledAttackTasks = this.getAllAttackTasksRelatedToPlanet(planet);
		List<ScheduledAttackTaskAdapter> scheduledAttackTaskAdapters = this
				.adaptScheduledAttackTasks(scheduledAttackTasks);
		return scheduledAttackTaskAdapters;
	}

	public void persistScheduledFleetBuildingTasks(List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks) {
		Iterator var2 = scheduledFleetBuildingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledFleetBuildingTask sfbt = (ScheduledFleetBuildingTask) var2.next();
			this.scheduledFleetBuildingTaskDao.persist(sfbt);
		}

	}

	public void executeNotExecutedTasks() {
		this.executeNotExecutedFleetBuildingTasks();
		this.executeNotExecutedFleetFactoryBuildingTasks();
		this.executeNotExecutedResourceFactoryBuildingTasks();
		this.executeNotExecutedResourceStorageUpgradingTasks();
		this.executeNotExecutedAttackTasks();
	}

	public void executeNotExecutedFleetBuildingTasks() {
		List<ScheduledFleetBuildingTask> notExecutedFleetBuildingTasks = this.scheduledFleetBuildingTaskDao
				.getByDone(false);
		Iterator var2 = notExecutedFleetBuildingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledFleetBuildingTask sfbt = (ScheduledFleetBuildingTask) var2.next();
			if (!this.isExecutionDateAfterCurrentTime(sfbt)) {
				this.executeScheduledFleetBuildingTask(sfbt);
			}
		}

	}

	public void executeNotExecutedFleetFactoryBuildingTasks() {
		List<ScheduledFleetFactoryBuildingTask> notExecutedFleetFacotryBuildingTasks = this.scheduledFleetFactoryBuildingTaskDao
				.getByDone(false);
		Iterator var2 = notExecutedFleetFacotryBuildingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledFleetFactoryBuildingTask sffbt = (ScheduledFleetFactoryBuildingTask) var2.next();
			if (!this.isExecutionDateAfterCurrentTime(sffbt)) {
				this.executeScheduledFleetFactoryBuildingTask(sffbt);
			}
		}

	}

	public void executeScheduledFleetFactoryBuildingTask(
			ScheduledFleetFactoryBuildingTask scheduledFleetFactoryBuildingTask) {
		int planetId = scheduledFleetFactoryBuildingTask.getPlanetId();
		int fleetTypeId = scheduledFleetFactoryBuildingTask.getFleetTypeId();
		FleetFactory fleetFactory = this.fleetFactoryDao.getByPlanetIdAndFleetTypeId(planetId, fleetTypeId);
		int factoryLevel = fleetFactory.getLevel();
		fleetFactory.setLevel(factoryLevel + 1);
		this.fleetFactoryDao.update(fleetFactory);
		scheduledFleetFactoryBuildingTask.setDone(true);
		this.scheduledFleetFactoryBuildingTaskDao.update(scheduledFleetFactoryBuildingTask);
	}

	public void executeScheduledFleetBuildingTask(ScheduledFleetBuildingTask scheduledFleetBuildingTask) {
		int planetId = scheduledFleetBuildingTask.getPlanetId();
		int fleetTypeId = scheduledFleetBuildingTask.getFleetTypeId();
		Fleet fleet = this.fleetDao.getByPlanetIdAndFleetTypeId(planetId, fleetTypeId);
		int amount = fleet.getAmount();
		fleet.setAmount(amount + 1);
		this.fleetDao.update(fleet);
		scheduledFleetBuildingTask.setDone(true);
		this.scheduledFleetBuildingTaskDao.update(scheduledFleetBuildingTask);
	}

	public ScheduledFleetBuildingTask getTheLatestScheduledFleetBuildingTaskOnPlanet(int planetId) {
		List<ScheduledFleetBuildingTask> scheduledFleetBuildingTasks = this.scheduledFleetBuildingTaskDao
				.getByPlanetId(planetId);
		Comparator<ScheduledFleetBuildingTask> comparator = (sfbt1, sfbt2) -> {
			GregorianCalendar sfbt1Date = new GregorianCalendar();
			GregorianCalendar sfbt2Date = new GregorianCalendar();
			sfbt1Date.setTime(sfbt1.getExecutionDate());
			sfbt2Date.setTime(sfbt2.getExecutionDate());
			return Long.compare(sfbt1Date.getTimeInMillis(), sfbt2Date.getTimeInMillis());
		};

		try {
			return (ScheduledFleetBuildingTask) scheduledFleetBuildingTasks.stream().filter((sfbt) -> {
				return !sfbt.isDone();
			}).max(comparator).get();
		} catch (NoSuchElementException var5) {
			return null;
		}
	}

	public boolean isExecutionDateAfterCurrentTime(ScheduledTask scheduledTask) {
		if (scheduledTask != null) {
			GregorianCalendar now = new GregorianCalendar();
			long nowInMilliseconds = now.getTimeInMillis();
			GregorianCalendar taskExecutionDate = new GregorianCalendar();
			taskExecutionDate.setTime(scheduledTask.getExecutionDate());
			long taskExecutionDateInMilliseconds = taskExecutionDate.getTimeInMillis();
			return taskExecutionDateInMilliseconds > nowInMilliseconds;
		} else {
			return false;
		}
	}

	public ScheduledAttackTaskAdapter adaptScheduledAttackTask(ScheduledAttackTask scheduledAttackTask) {
		ScheduledAttackTaskAdapter scheduledAttackTaskAdapter = new ScheduledAttackTaskAdapter();
		scheduledAttackTaskAdapter.setStartDate(scheduledAttackTask.getStartDate());
		scheduledAttackTaskAdapter.setExecutionDate(scheduledAttackTask.getExecutionDate());
		Planet attackedPlanet = this.planetDao.getById(scheduledAttackTask.getAttackedPlanetId());
		Planet attackingPlanet = this.planetDao.getById(scheduledAttackTask.getAttackingPlanetId());
		User attackedPlanetUser = this.userDao.getById(attackedPlanet.getUserId());
		User attackingPlanetUser = this.userDao.getById(attackingPlanet.getUserId());
		scheduledAttackTaskAdapter.setAttackedPlanetName(attackingPlanet.getName());
		scheduledAttackTaskAdapter.setAttackingPlanetName(attackingPlanet.getName());
		scheduledAttackTaskAdapter.setAttackedUserUsername(attackedPlanetUser.getUsername());
		scheduledAttackTaskAdapter.setAttackingUserUsername(attackingPlanetUser.getUsername());
		return scheduledAttackTaskAdapter;
	}

	public ScheduledFleetFactoryBuildingTask getTheLatestScheduledFleetFactoryBuildingTaskOnPlanet(int planetId) {
		List<ScheduledFleetFactoryBuildingTask> scheduledFleetBuildingTasks = this.scheduledFleetFactoryBuildingTaskDao.getByPlanetId(planetId);

		try {
			return (ScheduledFleetFactoryBuildingTask) 
					scheduledFleetBuildingTasks
					.stream()
					.filter(ScheduledFleetFactoryBuildingTask::isDone)
					.max(this.scheduledFleetFactoryBuildingTaskComparator)
					.get();
		} catch (NoSuchElementException var4) {
			return null;
		}
	}

	public void persistScheduledFleetFactoryBuildingTasks(
			List<ScheduledFleetFactoryBuildingTask> scheduledFleetFactoryBuildingTasks) {
		Iterator var2 = scheduledFleetFactoryBuildingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledFleetFactoryBuildingTask sffbt = (ScheduledFleetFactoryBuildingTask) var2.next();
			this.scheduledFleetFactoryBuildingTaskDao.persist(sffbt);
		}

	}

	public ScheduledResourceFactoryBuildingTask getTheLatestScheduledResourceFactoryBuildingTaskOnPlanet(int planetId) {
		List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks = this.scheduledResourceFactoryBuildingTaskDao
				.getByPlanetId(planetId);

		try {
			return (ScheduledResourceFactoryBuildingTask) scheduledResourceFactoryBuildingTasks.stream()
					.filter((sfbt) -> {
						return !sfbt.isDone();
					}).max(this.scheduledResourceFactoryBuildingTaskComparator).get();
		} catch (NoSuchElementException var4) {
			return null;
		}
	}

	public void persistScheduledResourceFactoryBuildingTasks(
			List<ScheduledResourceFactoryBuildingTask> scheduledResourceFactoryBuildingTasks) {
		Iterator var2 = scheduledResourceFactoryBuildingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledResourceFactoryBuildingTask srfbt = (ScheduledResourceFactoryBuildingTask) var2.next();
			this.scheduledResourceFactoryBuildingTaskDao.persist(srfbt);
		}

	}

	public ScheduledResourceStorageUpgradingTask getTheLatestScheduledResourceStorageUpgradingTaskOnPlanet(
			int planetId) {
		List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks = this.scheduledResourceStorageUpgradingTaskDao
				.getByPlanetId(planetId);

		try {
			return (ScheduledResourceStorageUpgradingTask) scheduledResourceStorageUpgradingTasks.stream()
					.filter((sfbt) -> {
						return !sfbt.isDone();
					}).max(this.scheduledResourceStorageUpgradingTaskComparator).get();
		} catch (NoSuchElementException var4) {
			return null;
		}
	}

	public void persistScheduledResourceStorageUpgradingTasks(
			List<ScheduledResourceStorageUpgradingTask> scheduledResourceStorageUpgradingTasks) {
		Iterator var2 = scheduledResourceStorageUpgradingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledResourceStorageUpgradingTask srsut = (ScheduledResourceStorageUpgradingTask) var2.next();
			this.scheduledResourceStorageUpgradingTaskDao.persist(srsut);
		}

	}

	public void executeNotExecutedResourceFactoryBuildingTasks() {
		List<ScheduledResourceFactoryBuildingTask> notExecutedResourceFacotryBuildingTasks = this.scheduledResourceFactoryBuildingTaskDao
				.getByDone(false);
		Iterator var2 = notExecutedResourceFacotryBuildingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledResourceFactoryBuildingTask srfbt = (ScheduledResourceFactoryBuildingTask) var2.next();
			if (!this.isExecutionDateAfterCurrentTime(srfbt)) {
				this.executeScheduledResourceFactoryBuildingTask(srfbt);
			}
		}

	}

	public void executeScheduledResourceFactoryBuildingTask(
			ScheduledResourceFactoryBuildingTask scheduledResourceFactoryBuildingTask) {
		int planetId = scheduledResourceFactoryBuildingTask.getPlanetId();
		int resourceTypeId = scheduledResourceFactoryBuildingTask.getResourceTypeId();
		ResourceFactory resourceFactory = this.resourceFactoryDao.getByPlanetIdAndResourceTypeId(planetId,
				resourceTypeId);
		int factoryLevel = resourceFactory.getLevel();
		resourceFactory.setLevel(factoryLevel + 1);
		this.resourceFactoryDao.update(resourceFactory);
		scheduledResourceFactoryBuildingTask.setDone(true);
		this.scheduledResourceFactoryBuildingTaskDao.update(scheduledResourceFactoryBuildingTask);
	}

	public void executeNotExecutedResourceStorageUpgradingTasks() {
		List<ScheduledResourceStorageUpgradingTask> notExecutedResourceStorageUpgradingTasks = this.scheduledResourceStorageUpgradingTaskDao
				.getByDone(false);
		Iterator var2 = notExecutedResourceStorageUpgradingTasks.iterator();

		while (var2.hasNext()) {
			ScheduledResourceStorageUpgradingTask srsut = (ScheduledResourceStorageUpgradingTask) var2.next();
			if (!this.isExecutionDateAfterCurrentTime(srsut)) {
				this.executeScheduledResourceStorageUpgradingTask(srsut);
			}
		}

	}

	public void executeScheduledResourceStorageUpgradingTask(
			ScheduledResourceStorageUpgradingTask scheduledResourceStorageUpgradingTask) {
		int planetId = scheduledResourceStorageUpgradingTask.getPlanetId();
		int resourceTypeId = scheduledResourceStorageUpgradingTask.getResourceTypeId();
		ResourceStorage resourceStorage = this.resourceStorageDao.getByPlanetIdAndResourceTypeId(planetId,
				resourceTypeId);
		int maximumLoad = resourceStorage.getMaximumLoad();
		resourceStorage.setMaximumLoad(maximumLoad + 100);
		this.resourceStorageDao.update(resourceStorage);
		scheduledResourceStorageUpgradingTask.setDone(true);
		this.scheduledResourceStorageUpgradingTaskDao.update(scheduledResourceStorageUpgradingTask);
	}

	public void executeNotExecutedAttackTasks() {
		List<ScheduledAttackTask> notExecutedAttackTasks = this.scheduledAttackTaskDao.getByDone(false);
		Iterator var2 = notExecutedAttackTasks.iterator();

		while (var2.hasNext()) {
			ScheduledAttackTask sat = (ScheduledAttackTask) var2.next();
			if (!this.isExecutionDateAfterCurrentTime(sat)) {
				this.executeScheduledAttackTask(sat);
			}
		}

	}

	public void executeScheduledAttackTask(ScheduledAttackTask scheduledAttackTask) {
		int attackingPlanetId = scheduledAttackTask.getAttackingPlanetId();
		int attackedPlanetId = scheduledAttackTask.getAttackedPlanetId();
		this.attackService.attackPlanet(attackingPlanetId, attackedPlanetId, scheduledAttackTask.getFleetTypeId(),
				scheduledAttackTask.getFleetAmount());
		scheduledAttackTask.setDone(true);
		this.scheduledAttackTaskDao.update(scheduledAttackTask);
	}
}