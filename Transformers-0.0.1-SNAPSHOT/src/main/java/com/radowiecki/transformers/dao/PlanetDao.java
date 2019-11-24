package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Planet;
import java.util.List;

public interface PlanetDao {
	Planet getById(int var1);

	List<Planet> getByUserId(int var1);

	void persist(Planet var1);

	List<Integer> getAllXCoordinates();

	List<Integer> getAllYCoordinates();

	void update(Planet var1);

	List<Planet> getAll();
}