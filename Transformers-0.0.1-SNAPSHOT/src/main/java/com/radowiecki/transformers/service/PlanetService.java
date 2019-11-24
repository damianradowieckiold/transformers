package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.PlanetAdapter;
import com.radowiecki.transformers.model.Planet;
import java.awt.geom.Point2D;
import java.util.List;

public interface PlanetService {
	Point2D getRandomFreeCoordinates();

	List<Planet> getUsersPlanets(List<Integer> var1);

	PlanetAdapter adapt(Planet var1);

	List<PlanetAdapter> adapt(List<Planet> var1);
}