package com.radowiecki.transformers.scheduling;

import com.radowiecki.transformers.dao.FleetDao;
import com.radowiecki.transformers.dao.ScheduledAttackTaskDao;
import com.radowiecki.transformers.model.Fleet;
import com.radowiecki.transformers.model.ScheduledAttackTask;
import java.util.GregorianCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class AttackTaskScheduler {
	@Autowired
	private ScheduledAttackTaskDao scheduledAttackTaskDao;
	@Autowired
	private FleetDao fleetDao;
	private int attackingPlanetId;
	private int attackedPlanetId;
	private int fleetTypeId;
	private int amountOfAttackingFleet;
	private Fleet fleet;

	public void setAttackedPlanetId(int attackedPlanetId) {
		this.attackedPlanetId = attackedPlanetId;
	}

	public void setAttackingPlanetId(int attackingPlanetId) {
		this.attackingPlanetId = attackingPlanetId;
	}

	public void setFleetTypeId(int fleetTypeId) {
		this.fleetTypeId = fleetTypeId;
	}

	public void setAmountOfAttackingFleet(int amountOfAttackingFleet) {
		this.amountOfAttackingFleet = amountOfAttackingFleet;
	}

	public void schedule() {
		if (this.isPossibleToAttack()) {
			this.reduceFleet();
			this.scheduleAttackTask();
		}

	}

	private boolean isPossibleToAttack() {
		this.fleet = this.fleetDao.getByPlanetIdAndFleetTypeId(this.attackingPlanetId, this.fleetTypeId);
		return this.fleet.getAmount() - this.amountOfAttackingFleet >= 0;
	}

	private void reduceFleet() {
		int currentAmount = this.fleet.getAmount();
		this.fleet.setAmount(currentAmount - this.amountOfAttackingFleet);
		this.fleetDao.update(this.fleet);
	}

	private void scheduleAttackTask() {
		ScheduledAttackTask scheduledAttackTask = new ScheduledAttackTask();
		int secondsToAttack = this.calculateAttackTime();
		scheduledAttackTask.setDone(false);
		scheduledAttackTask.setAttackingPlanetId(this.attackingPlanetId);
		scheduledAttackTask.setAttackedPlanetId(this.attackedPlanetId);
		scheduledAttackTask.setFleetTypeId(this.fleetTypeId);
		scheduledAttackTask.setFleetAmount(this.amountOfAttackingFleet);
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		scheduledAttackTask.setStartDate(gregorianCalendar.getTime());
		gregorianCalendar.add(13, secondsToAttack);
		scheduledAttackTask.setExecutionDate(gregorianCalendar.getTime());
		this.scheduledAttackTaskDao.persist(scheduledAttackTask);
	}

	private int calculateAttackTime() {
		return 15;
	}
}