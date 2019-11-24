package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.User;
import java.util.List;

public interface UserDao {
	User getById(int var1);

	User getByUsername(String var1);

	void persist(User var1);

	List<User> getAll();

	void update(User var1);

	void delete(User var1);

	User getAttackingUserByScheduledAttackTaskId(int var1);

	User getAttackedUserByScheduledAttackTaskId(int var1);
}