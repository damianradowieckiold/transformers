package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.FleetAdapter;
import com.radowiecki.transformers.exception.FleetNotFoundException;
import com.radowiecki.transformers.exception.TooFewFleetException;
import com.radowiecki.transformers.exception.TooManyFleetsWithTheSameTypeException;
import com.radowiecki.transformers.model.Fleet;
import java.util.List;

public interface FleetService {
	void reduceAmountByPlanetIdAndFleetTypeId(int var1, int var2, int var3) throws TooFewFleetException;

	void update(List<Fleet> var1);

	void persist(List<Fleet> var1);

	List<FleetAdapter> adapt(List<Fleet> var1);

	int getAllFleetsAmountOnPlanet(int var1);

	void reduceAmountByPlanetIdStartingFromFleetTypeWithId(int var1, int var2, int var3) throws TooFewFleetException;

	void setFleetWithTypeIdAsFirstInList(int var1, List<Fleet> var2)
			throws TooManyFleetsWithTheSameTypeException, FleetNotFoundException;
}