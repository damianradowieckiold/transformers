package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Fleet;
import java.util.List;

public interface FleetDao {
	Fleet getByPlanetIdAndFleetTypeId(int var1, int var2);

	Fleet getById(int var1);

	List<Fleet> getByPlanetId(int var1);

	void update(Fleet var1);

	void persist(Fleet var1);

	List<Fleet> getAll();
}