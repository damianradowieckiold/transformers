package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.FleetFactoryAdapter;
import com.radowiecki.transformers.dao.FleetFactoryDao;
import com.radowiecki.transformers.dao.FleetTypeDao;
import com.radowiecki.transformers.model.FleetFactory;
import com.radowiecki.transformers.model.FleetType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FleetFactoryServiceImpl implements FleetFactoryService {
	@Autowired
	private FleetFactoryDao fleetFactoryDao;
	@Autowired
	private FleetTypeDao fleetTypeDao;

	public void persist(List<FleetFactory> fleetFactories) {
		fleetFactories.forEach(fleetFactoryDao::persist);
	}

	public FleetFactoryAdapter adapt(FleetFactory fleetFactory) {
		FleetFactoryAdapter fleetFactoryAdapter = new FleetFactoryAdapter();
		fleetFactoryAdapter.setLevel(fleetFactory.getLevel());
		FleetType fleetType = this.fleetTypeDao.getById(fleetFactory.getFleetTypeId());
		fleetFactoryAdapter.setFleetTypeName(fleetType.getName());
		return fleetFactoryAdapter;
	}

	public List<FleetFactoryAdapter> adapt(List<FleetFactory> fleetFactories) {
		List<FleetFactoryAdapter> fleetFactoryAdapters = new ArrayList();
		Iterator var3 = fleetFactories.iterator();

		while (var3.hasNext()) {
			FleetFactory ff = (FleetFactory) var3.next();
			FleetFactoryAdapter fleetFactoryAdapter = this.adapt(ff);
			fleetFactoryAdapters.add(fleetFactoryAdapter);
		}

		return fleetFactoryAdapters;
	}
}