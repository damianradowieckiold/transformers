package com.radowiecki.transformers.service;

import com.radowiecki.transformers.adapter.FleetFactoryAdapter;
import com.radowiecki.transformers.model.FleetFactory;
import java.util.List;

public interface FleetFactoryService {
	void persist(List<FleetFactory> var1);

	FleetFactoryAdapter adapt(FleetFactory var1);

	List<FleetFactoryAdapter> adapt(List<FleetFactory> var1);
}