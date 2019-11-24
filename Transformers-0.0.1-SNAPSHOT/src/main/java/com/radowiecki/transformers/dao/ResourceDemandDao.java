package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.FleetBuildingResourceDemand;
import com.radowiecki.transformers.model.FleetFactoryBuildingResourceDemand;
import com.radowiecki.transformers.model.ResourceFactoryBuildingResourceDemand;
import com.radowiecki.transformers.model.ResourceStorageUpgradingResourceDemand;
import java.util.List;

public interface ResourceDemandDao {
	List<FleetBuildingResourceDemand> getFleetBuildingResourceDemandsByFleetTypeId(int var1);

	List<FleetFactoryBuildingResourceDemand> getFleetFacotryBuildingResourceDemandsByFleetFactoryTypeId(int var1);

	List<ResourceFactoryBuildingResourceDemand> getResourceFacotryBuildingResourceDemandsByResourceFactoryTypeId(
			int var1);

	List<ResourceStorageUpgradingResourceDemand> getResourceStorageUpgradingResourceDemandsByResourceTypeId(int var1);

	FleetBuildingResourceDemand getFleetBuildingResourceDemandByFleetTypeIdAndByDemandedResourceTypeId(int var1,
			int var2);

	FleetFactoryBuildingResourceDemand getFleetFactoryBuildingResourceDemandByFleetFactoryTypeIdAndByDemandedResourceTypeId(
			int var1, int var2);

	ResourceFactoryBuildingResourceDemand getResourceFactoryBuildingResourceDemandByResourceFactoryTypeIdAndByDemandedResourceTypeId(
			int var1, int var2);

	ResourceStorageUpgradingResourceDemand getResourceStorageUpgradingResourceDemandByResourceFactoryTypeIdAndByDemandedResourceTypeId(
			int var1, int var2);
}