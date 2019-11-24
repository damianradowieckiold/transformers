package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.FleetFactory;
import java.util.List;

public interface FleetFactoryDao {
	List<FleetFactory> getByPlanetId(int var1);

	void persist(FleetFactory var1);

	FleetFactory getByPlanetIdAndFleetTypeId(int var1, int var2);

	void update(FleetFactory var1);
}