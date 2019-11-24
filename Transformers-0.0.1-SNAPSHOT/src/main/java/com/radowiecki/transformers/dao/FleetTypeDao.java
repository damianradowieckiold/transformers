package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.FleetType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetTypeDao {
	List<FleetType> getAll();

	FleetType getById(int var1);
}