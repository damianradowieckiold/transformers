package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.ScheduledAttackTask;
import java.util.List;

public interface ScheduledAttackTaskDao {
	List<ScheduledAttackTask> getAll();

	List<ScheduledAttackTask> getByAttackingPlanetId(int var1);

	List<ScheduledAttackTask> getByAttackedPlanetId(int var1);

	void persist(ScheduledAttackTask var1);

	void delete(ScheduledAttackTask var1);

	ScheduledAttackTask getById(int var1);

	void update(ScheduledAttackTask var1);

	List<ScheduledAttackTask> getByDone(boolean var1);
}