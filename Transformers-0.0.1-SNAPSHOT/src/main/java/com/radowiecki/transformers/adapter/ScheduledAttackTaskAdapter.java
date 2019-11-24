package com.radowiecki.transformers.adapter;

import java.util.Date;

public class ScheduledAttackTaskAdapter {
	private String attackingUserUsername;
	private String attackedUserUsername;
	private String attackingPlanetName;
	private String attackedPlanetName;
	private Date startDate;
	private Date executionDate;

	public String getAttackingUserUsername() {
		return this.attackingUserUsername;
	}

	public void setAttackingUserUsername(String attackingUserUsername) {
		this.attackingUserUsername = attackingUserUsername;
	}

	public String getAttackedUserUsername() {
		return this.attackedUserUsername;
	}

	public void setAttackedUserUsername(String attackedUserUsername) {
		this.attackedUserUsername = attackedUserUsername;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExecutionDate() {
		return this.executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getAttackingPlanetName() {
		return this.attackingPlanetName;
	}

	public void setAttackingPlanetName(String attackingPlanetName) {
		this.attackingPlanetName = attackingPlanetName;
	}

	public String getAttackedPlanetName() {
		return this.attackedPlanetName;
	}

	public void setAttackedPlanetName(String attackedPlanetName) {
		this.attackedPlanetName = attackedPlanetName;
	}
}
