package com.radowiecki.transformers.service;

import com.radowiecki.transformers.dao.FleetDao;
import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.exception.TooFewFleetException;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttackServiceImpl implements AttackService {
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private PlanetDao planetDao;
	@Autowired
	private FleetService fleetService;

	public void attackPlanet(int attackingPlanetId, int attackedPlanetId, int fleetTypeId, int fleetAmount) {
		Fleet attackedPlanetFleet = this.fleetDao.getByPlanetIdAndFleetTypeId(attackedPlanetId, fleetTypeId);
		int allFleetsAmountOnAttackedPlanet;
		if (attackedPlanetFleet.getAmount() < fleetAmount) {
			allFleetsAmountOnAttackedPlanet = this.fleetService.getAllFleetsAmountOnPlanet(attackedPlanetId);
			if (allFleetsAmountOnAttackedPlanet < fleetAmount) {
				this.capturePlanet(attackingPlanetId, attackedPlanetId);
			} else {
				try {
					this.fleetService.reduceAmountByPlanetIdStartingFromFleetTypeWithId(attackedPlanetId, fleetTypeId,
							fleetAmount);
				} catch (TooFewFleetException var8) {
					var8.printStackTrace();
					this.capturePlanet(attackingPlanetId, attackedPlanetId);
				}
			}
		} else {
			allFleetsAmountOnAttackedPlanet = attackedPlanetFleet.getAmount() - fleetAmount;
			attackedPlanetFleet.setAmount(allFleetsAmountOnAttackedPlanet);
			this.fleetDao.update(attackedPlanetFleet);
		}

	}

	public void capturePlanet(int attackingPlanetId, int attackedPlanetId) {
		Planet attackingPlanet = this.planetDao.getById(attackingPlanetId);
		Planet attackedPlanet = this.planetDao.getById(attackedPlanetId);
		attackedPlanet.setUserId(attackingPlanet.getUserId());
		this.planetDao.update(attackedPlanet);
	}
}