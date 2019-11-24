package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.PlanetAdapter;
import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.dao.UserDao;
import com.radowiecki.transformers.model.Planet;
import com.radowiecki.transformers.model.User;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanetServiceImpl implements PlanetService {
	public static final int MAXIMUM_EAST_X_COORDINATE = 100;
	public static final int MAXIMUM_WEST_X_COORDINATE = -100;
	public static final int MAXIMUM_NORHT_Y_COORDINATE = 100;
	public static final int MAXIMUM_SOUTH_Y_COORDINATE = -100;
	@Autowired
	private PlanetDao planetDao;
	@Autowired
	private UserDao userDao;

	public Point2D getRandomFreeCoordinates() {
		List<Integer> allFreeAndPossibleXCoordinates = this.getAllFreeAndPossibleXCoordinates();
		List<Integer> allFreeAndPossibleYCoordinates = this.getAllFreeAndPossibleYCoordinates();
		Random random = new Random();
		int randomXCoordinatesIndex = random.nextInt(allFreeAndPossibleXCoordinates.size());
		int randomYCoordinatesIndex = random.nextInt(allFreeAndPossibleYCoordinates.size());
		Integer xCoordinate = (Integer) allFreeAndPossibleXCoordinates.get(randomXCoordinatesIndex);
		Integer yCoordinate = (Integer) allFreeAndPossibleYCoordinates.get(randomYCoordinatesIndex);
		return new Float((float) xCoordinate, (float) yCoordinate);
	}

	private List<Integer> getAllFreeAndPossibleXCoordinates() {
		List<Integer> allNotFreeXCoordinates = this.planetDao.getAllXCoordinates();
		List<Integer> allPossibleXCoordinates = this.getSubsequentNumbersInRange(-100, 100);
		return (List) allPossibleXCoordinates.stream().filter((c) -> {
			return !allNotFreeXCoordinates.contains(c);
		}).collect(Collectors.toList());
	}

	private List<Integer> getAllFreeAndPossibleYCoordinates() {
		List<Integer> allNotFreeYCoordinates = this.planetDao.getAllYCoordinates();
		List<Integer> allPossibleYCoordinates = this.getSubsequentNumbersInRange(-100, 100);
		return (List) allPossibleYCoordinates.stream().filter((c) -> {
			return !allNotFreeYCoordinates.contains(c);
		}).collect(Collectors.toList());
	}

	private List<Integer> getSubsequentNumbersInRange(int start, int finish) {
		List<Integer> numbers = new ArrayList();

		for (int i = start; i <= finish; ++i) {
			numbers.add(i);
		}

		return numbers;
	}

	public List<Planet> getUsersPlanets(List<Integer> usersIds) {
		List<Planet> usersPlanets = new ArrayList();
		Iterator var3 = usersIds.iterator();

		while (var3.hasNext()) {
			int i = (Integer) var3.next();
			List<Planet> userPlanet = this.planetDao.getByUserId(i);
			usersPlanets.addAll(userPlanet);
		}

		return usersPlanets;
	}

	public PlanetAdapter adapt(Planet planet) {
		PlanetAdapter planetAdapter = new PlanetAdapter();
		planetAdapter.setId(planet.getId());
		planetAdapter.setName(planet.getName());
		User user = this.userDao.getById(planet.getUserId());
		planetAdapter.setUsername(user.getUsername());
		return planetAdapter;
	}

	public List<PlanetAdapter> adapt(List<Planet> planets) {
		List<PlanetAdapter> planetAdapters = new ArrayList();
		Iterator var3 = planets.iterator();

		while (var3.hasNext()) {
			Planet p = (Planet) var3.next();
			PlanetAdapter planetAdapter = this.adapt(p);
			planetAdapters.add(planetAdapter);
		}

		return planetAdapters;
	}
}