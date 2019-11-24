package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.FleetAdapter;
import com.radowiecki.transformers.dao.FleetDao;
import com.radowiecki.transformers.dao.FleetTypeDao;
import com.radowiecki.transformers.exception.FleetNotFoundException;
import com.radowiecki.transformers.exception.TooFewFleetException;
import com.radowiecki.transformers.exception.TooManyFleetsWithTheSameTypeException;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.FleetType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FleetServiceImpl implements FleetService {
	private static Logger logger = Logger.getLogger(FleetServiceImpl.class);
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private FleetTypeDao fleetTypeDao;

	public void update(List<Fleet> fleets) {
		fleets.stream().forEach((fleet) -> {
			this.fleetDao.update(fleet);
		});
	}

	public void persist(List<Fleet> fleets) {
		fleets.forEach(fleetDao::persist);
	}

	public void reduceAmountByPlanetIdAndFleetTypeId(int planetId, int fleetTypeId, int amount)
			throws TooFewFleetException {
		Fleet fleet = this.fleetDao.getByPlanetIdAndFleetTypeId(planetId, fleetTypeId);
		int fleetAmount = fleet.getAmount() - amount;
		if (fleetAmount >= 0) {
			fleet.setAmount(fleetAmount);
			this.fleetDao.update(fleet);
		} else {
			throw new TooFewFleetException();
		}
	}

	public List<FleetAdapter> adapt(List<Fleet> fleets) {
		return (List) fleets.stream().map((f) -> {
			return this.adapt(f);
		}).collect(Collectors.toList());
	}

	private FleetAdapter adapt(Fleet fleet) {
		FleetAdapter fleetAdapter = new FleetAdapter();
		fleetAdapter.setId(fleet.getId());
		fleetAdapter.setAmount(fleet.getAmount());
		FleetType fleetType = this.fleetTypeDao.getById(fleet.getFleetTypeId());
		fleetAdapter.setTypeName(fleetType.getName());
		return fleetAdapter;
	}

	public int getAllFleetsAmountOnPlanet(int planetId) {
		return this.fleetDao.getByPlanetId(planetId).stream().mapToInt((f) -> {
			return f.getAmount();
		}).sum();
	}

	public void reduceAmountByPlanetIdStartingFromFleetTypeWithId(int attackedPlanetId, int fleetTypeId, int amount)
			throws TooFewFleetException {
		List attackedPlanetFleets = this.fleetDao.getByPlanetId(attackedPlanetId);

		try {
			this.setFleetWithTypeIdAsFirstInList(fleetTypeId, attackedPlanetFleets);
		} catch (FleetNotFoundException | TooManyFleetsWithTheSameTypeException var7) {
			logger.error("Fleet reducing without special order. Excpetion: " + var7.getMessage());
		}

		Fleet f;
		for (Iterator var5 = attackedPlanetFleets.iterator(); var5.hasNext(); this.fleetDao.update(f)) {
			f = (Fleet) var5.next();
			if (amount == 0) {
				return;
			}

			if (amount < 0) {
				throw new TooFewFleetException();
			}

			if (amount >= f.getAmount()) {
				amount -= f.getAmount();
				f.setAmount(0);
			} else {
				f.setAmount(f.getAmount() - amount);
				amount = 0;
			}
		}

	}

	public void setFleetWithTypeIdAsFirstInList(int fleetTypeId, List<Fleet> fleets)
			throws TooManyFleetsWithTheSameTypeException, FleetNotFoundException {
		List<Fleet> fleetsWithChosenType = (List) fleets.stream().filter((f) -> {
			return f.getFleetTypeId() == fleetTypeId;
		}).collect(Collectors.toList());
		if (fleetsWithChosenType.size() > 1) {
			throw new TooManyFleetsWithTheSameTypeException();
		} else if (fleetsWithChosenType.size() == 0) {
			throw new FleetNotFoundException();
		} else {
			List<Fleet> otherFleets = (List) fleets.stream().filter((f) -> {
				return f.getFleetTypeId() != fleetTypeId;
			}).collect(Collectors.toList());
			List<Fleet> specialSortedFleets = new ArrayList();
			specialSortedFleets.add(fleetsWithChosenType.get(0));
			specialSortedFleets.addAll(otherFleets);
		}
	}
}