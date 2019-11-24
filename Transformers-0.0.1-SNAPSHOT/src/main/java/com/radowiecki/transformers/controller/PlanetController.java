package com.radowiecki.transformers.controller;

import com.radowiecki.transformers.dao.PlanetDao;
import com.radowiecki.transformers.model.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class PlanetController {
	@Autowired
	private PlanetDao planetDao;
	private int chosenPlanetId;

	public int getChosenPlanetId() {
		return this.chosenPlanetId;
	}

	public void setChosenPlanetId(int chosenPlanetId) {
		this.chosenPlanetId = chosenPlanetId;
	}

	public Planet getChosenPlanet() {
		return this.planetDao.getById(this.chosenPlanetId);
	}
}