package com.radowiecki.transformers.controller;

import com.radowiecki.transformers.adapter.FleetAdapter;
import com.radowiecki.transformers.adapter.FleetFactoryAdapter;
import com.radowiecki.transformers.adapter.PlanetAdapter;
import com.radowiecki.transformers.adapter.ResourceAdapter;
import com.radowiecki.transformers.adapter.ResourceFactoryAdapter;
import com.radowiecki.transformers.adapter.ResourceStorageAdapter;
import com.radowiecki.transformers.dao.FleetDao;
import com.radowiecki.transformers.dao.FleetFactoryDao;
import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.dao.ResourceDao;
import com.radowiecki.transformers.dao.ResourceFactoryDao;
import com.radowiecki.transformers.dao.ResourceStorageDao;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.FleetFactory;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ResourceFactory;
import com.radowiecki.transformers.model.ResourceStorage;
import com.radowiecki.transformers.model.User;
import com.radowiecki.transformers.service.FleetFactoryService;
import com.radowiecki.transformers.service.FleetService;
import com.radowiecki.transformers.service.LanguageServiceImpl;
import com.radowiecki.transformers.service.PlanetService;
import com.radowiecki.transformers.service.ResourceFactoryService;
import com.radowiecki.transformers.service.ResourceService;
import com.radowiecki.transformers.service.ResourceStorageService;
import com.radowiecki.transformers.service.ScheduledTaskService;
import com.radowiecki.transformers.service.UserService;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
	@Autowired
	private LanguageServiceImpl languageServiceImpl;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private UserService userService;
	@Autowired
	private PlanetDao planetDao;
	@Autowired
	private ApplicationContext applicatioContext;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private FleetService fleetService;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	private PlanetService planetService;
	@Autowired
	private FleetFactoryDao fleetFactoryDao;
	@Autowired
	private FleetFactoryService fleetFactoryService;
	@Autowired
	private ResourceFactoryDao resourceFactoryDao;
	@Autowired
	private ResourceFactoryService resourceFactoryService;
	@Autowired
	private ResourceStorageDao resourceStorageDao;
	@Autowired
	private ResourceStorageService resourceStorageService;

	@RequestMapping({"/"})
	public ModelAndView getHomePage() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("languagePropertiesMap", this.languageServiceImpl.getLanguagePropertiesMap());
		return this.userService.isAnyUserAuthenticated() ? this.getGamePage() : modelAndView;
	}

	@RequestMapping({"/login"})
	public ModelAndView getLoginPage() {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("languagePropertiesMap", this.languageServiceImpl.getLanguagePropertiesMap());
		return modelAndView;
	}

	@RequestMapping({"/registration"})
	public ModelAndView getRegistrationPage() {
		ModelAndView modelAndView = new ModelAndView("registration");
		modelAndView.addObject("languagePropertiesMap", this.languageServiceImpl.getLanguagePropertiesMap());
		return modelAndView;
	}

	@RequestMapping({"/game"})
	public ModelAndView getGamePage() {
		this.scheduledTaskService.executeNotExecutedTasks();
		ModelAndView modelAndView = new ModelAndView("game");
		User currentUser = this.userService.getCurrent();
		PlanetController planetController = (PlanetController) this.applicatioContext.getBean("planetController",
				PlanetController.class);
		List<Planet> planets = this.planetDao.getByUserId(currentUser.getId());
		int chosenPlanetId = planets.get(0).getId();
		planetController.setChosenPlanetId(chosenPlanetId);
		List<Resource> resources = this.resourceDao.getByPlanetId(chosenPlanetId);
		List<ResourceAdapter> resourceAdapters = this.resourceService.adapt(resources);
		List<Fleet> fleets = this.fleetDao.getByPlanetId(chosenPlanetId);
		List<FleetAdapter> fleetAdapters = this.fleetService.adapt(fleets);
		List<User> otherUsers = this.userService.getOtherUsers();
		List<Integer> otherUsersIds = (List) otherUsers.stream().map((u) -> {
			return u.getId();
		}).collect(Collectors.toList());
		List<Planet> otherUsersPlanets = this.planetService.getUsersPlanets(otherUsersIds);
		List<PlanetAdapter> adaptedOtherUsersPlanets = this.planetService.adapt(otherUsersPlanets);
		List<FleetFactory> fleetFactories = this.fleetFactoryDao.getByPlanetId(chosenPlanetId);
		List<FleetFactoryAdapter> fleetFactoryAdapters = this.fleetFactoryService.adapt(fleetFactories);
		List<ResourceFactory> resourceFactories = this.resourceFactoryDao.getByPlanetId(chosenPlanetId);
		List<ResourceFactoryAdapter> resourceFactoryAdapters = this.resourceFactoryService.adapt(resourceFactories);
		List<ResourceStorage> resourceStorages = this.resourceStorageDao.getByPlanetId(chosenPlanetId);
		List<ResourceStorageAdapter> resourceStorageAdapters = this.resourceStorageService.adapt(resourceStorages);
		modelAndView.addObject("otherUsersPlanets", adaptedOtherUsersPlanets);
		modelAndView.addObject("user", this.userService.getCurrent());
		modelAndView.addObject("resources", resourceAdapters);
		modelAndView.addObject("fleets", fleetAdapters);
		modelAndView.addObject("languagePropertiesMap", this.languageServiceImpl.getLanguagePropertiesMap());
		modelAndView.addObject("planets", planets);
		modelAndView.addObject("fleetFactories", fleetFactoryAdapters);
		modelAndView.addObject("resourceFactories", resourceFactoryAdapters);
		modelAndView.addObject("resourceStorages", resourceStorageAdapters);
		return modelAndView;
	}

	@RequestMapping({"/registerUser"})
	public ModelAndView registerUser(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		if (!this.userService.userExists(username)) {
			this.userService.addNewUser(username, password);
			return new ModelAndView("home");
		} else {
			ModelAndView modelAndView = new ModelAndView("registration");
			modelAndView.addObject("ERROR_MESSAGE", "User with this name already exists");
			modelAndView.addObject("languagePropertiesMap", this.languageServiceImpl.getLanguagePropertiesMap());
			return modelAndView;
		}
	}
}