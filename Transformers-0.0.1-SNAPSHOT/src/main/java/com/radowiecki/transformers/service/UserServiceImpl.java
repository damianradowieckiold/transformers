package com.radowiecki.transformers.service;

import com.radowiecki.transformers.dao.AuthorityDao;
import com.radowiecki.transformers.dao.FleetTypeDao;
import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.dao.ResourceTypeDao;
import com.radowiecki.transformers.dao.UserDao;
import com.radowiecki.transformers.model.Authority;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.FleetFactory;
import com.radowiecki.transformers.model.FleetType;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.Resource;
import com.radowiecki.transformers.model.ResourceFactory;
import com.radowiecki.transformers.model.ResourceType;
import com.radowiecki.transformers.model.User;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private static final int INITIAL_RESOURCE_FACTORY_LEVEL = 2;
	private static final int INITIAL_RESOURCE_AMOUNT = 200;
	private static final int INITIAL_FLEET_FACTORY_LEVEL = 2;
	private static final int INITIAL_FLEET_AMOUNT = 40;
	private List<ResourceType> resourceTypes;
	private List<FleetType> fleetTypes;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AuthorityDao authorityDao;
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	@Autowired
	private ResourceFactoryService resourceFactoryService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private FleetTypeDao fleetTypeDao;
	@Autowired
	private FleetService fleetService;
	@Autowired
	private FleetFactoryService fleetFactoryService;
	@Autowired
	private PlanetService planetService;
	@Autowired
	private PlanetDao planetDao;

	public User addNewUser(String username, String password) {
		this.initializeResourceTypes();
		this.initializeFleetTypes();
		User user = this.prepareUser(username, password);
		this.userDao.persist(user);
		Planet planet = this.preparePlanetForUser(user);
		this.planetDao.persist(planet);
		List<ResourceFactory> resourceFactories = this.prepareInitialResourceFactories(planet);
		this.resourceFactoryService.persist(resourceFactories);
		List<Resource> resources = this.prepareInitialResources(planet);
		this.resourceService.persist(resources);
		List<FleetFactory> fleetFactories = this.prepareInitialFleetFactories(planet);
		this.fleetFactoryService.persist(fleetFactories);
		List<Fleet> fleets = this.prepareInitialFleets(planet);
		this.fleetService.persist(fleets);
		return user;
	}

	private Planet preparePlanetForUser(User user) {
		Planet planet = new Planet();
		planet.setUserId(user.getId());
		planet.setName("My Planet");
		this.setRandomizedCoordinates(planet);
		return planet;
	}

	private void setRandomizedCoordinates(Planet planet) {
		Point2D point = this.planetService.getRandomFreeCoordinates();
		planet.setxCoordinate((int) point.getX());
		planet.setxCoordinate((int) point.getY());
	}

	public User getCurrent() {
		String username = this.getAuthenticatedUsername();
		User user = this.userDao.getByUsername(username);
		return user;
	}

	public List<User> getOtherUsers() {
		User currentUser = this.getCurrent();
		List<User> users = this.userDao.getAll();
		users.remove(currentUser);
		return users;
	}

	public boolean userExists(String username) {
		User user = this.userDao.getByUsername(username);
		return user != null;
	}

	public boolean isAnyUserAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}

	private List<FleetFactory> prepareInitialFleetFactories(Planet planet) {
		List<FleetFactory> fleetFactories = new ArrayList();
		Iterator var3 = this.fleetTypes.iterator();

		while (var3.hasNext()) {
			FleetType ft = (FleetType) var3.next();
			FleetFactory fleetFactory = new FleetFactory();
			fleetFactory.setPlanetId(planet.getId());
			fleetFactory.setLevel(2);
			fleetFactory.setFleetTypeId(ft.getId());
			fleetFactories.add(fleetFactory);
		}

		return fleetFactories;
	}

	private void initializeFleetTypes() {
		this.fleetTypes = this.fleetTypeDao.getAll();
	}

	private List<Fleet> prepareInitialFleets(Planet planet) {
		List<Fleet> fleets = new ArrayList();
		Iterator var3 = this.fleetTypes.iterator();

		while (var3.hasNext()) {
			FleetType ft = (FleetType) var3.next();
			Fleet fleet = new Fleet();
			fleet.setPlanetId(planet.getId());
			fleet.setFleetTypeId(ft.getId());
			fleet.setAmount(40);
			fleets.add(fleet);
		}

		return fleets;
	}

	private void initializeResourceTypes() {
		this.resourceTypes = this.resourceTypeDao.getAll();
	}

	private List<Resource> prepareInitialResources(Planet planet) {
		List<Resource> resources = new ArrayList();
		Iterator var3 = this.resourceTypes.iterator();

		while (var3.hasNext()) {
			ResourceType rt = (ResourceType) var3.next();
			Resource resource = new Resource();
			resource.setPlanetId(planet.getId());
			resource.setAmount(200);
			resource.setResourceTypeId(rt.getId());
			resources.add(resource);
		}

		return resources;
	}

	private User prepareUser(String username, String password) {
		String encodedPassword = this.encodePassword(password);
		List<Authority> allAuthorities = this.authorityDao.getAll();
		List<Authority> filteredAuthorities = (List) allAuthorities.stream().filter((a) -> {
			return a.getAuthority().equals("USER");
		}).collect(Collectors.toList());
		Authority authority = (Authority) filteredAuthorities.get(0);
		boolean enabled = true;
		User user = new User();
		user.setUsername(username);
		user.setPassword(encodedPassword);
		user.setEnabled(enabled);
		user.setAuthorityId(authority.getId());
		return user;
	}

	private String encodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		return encodedPassword;
	}

	private List<ResourceFactory> prepareInitialResourceFactories(Planet planet) {
		List<ResourceFactory> resourceFactories = new ArrayList();

		for (int i = 0; i < this.resourceTypes.size(); ++i) {
			ResourceFactory resourceFactory = new ResourceFactory();
			resourceFactory.setPlanetId(planet.getId());
			resourceFactory.setResourceTypeId(((ResourceType) this.resourceTypes.get(i)).getId());
			resourceFactory.setLevel(2);
			resourceFactories.add(resourceFactory);
		}

		return resourceFactories;
	}

	private String getAuthenticatedUsername() {
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}

	public boolean canUserAskForPlanetWithId(int planetId) {
		User currentUser = this.getCurrent();
		List<Planet> userPlanets = this.planetDao.getByUserId(currentUser.getId());
		List<Integer> userPlanetIds = (List) userPlanets.stream().map((u) -> {
			return u.getId();
		}).collect(Collectors.toList());
		return userPlanetIds.contains(planetId);
	}
}