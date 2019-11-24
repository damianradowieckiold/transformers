package com.radowiecki.transformers.scheduling.task;

import org.openqa.selenium.InvalidArgumentException;

@Deprecated
public enum TaskType {
	ENCELADUS_BUILDING, TITAN_BUILDING, RHEA_BUILDING, CALYPSO_BUILDING;

	public static TaskType getTaskTypeByFleetTypesId(int fleetTypesId) {
		switch (fleetTypesId) {
			case 0 :
				return TITAN_BUILDING;
			case 1 :
				return RHEA_BUILDING;
			case 2 :
				return ENCELADUS_BUILDING;
			case 3 :
				return CALYPSO_BUILDING;
			default :
				throw new InvalidArgumentException(
						"Invalid fleetTypeId. There is no task type for id: " + fleetTypesId);
		}
	}
}