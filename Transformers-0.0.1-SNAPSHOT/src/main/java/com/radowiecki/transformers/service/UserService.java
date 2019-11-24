package com.radowiecki.transformers.service;

import com.radowiecki.transformers.model.User;
import java.util.List;

public interface UserService {
	User addNewUser(String var1, String var2);

	User getCurrent();

	List<User> getOtherUsers();

	boolean userExists(String var1);

	boolean isAnyUserAuthenticated();

	boolean canUserAskForPlanetWithId(int var1);
}