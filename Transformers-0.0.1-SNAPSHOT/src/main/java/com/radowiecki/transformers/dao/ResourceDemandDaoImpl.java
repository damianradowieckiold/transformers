package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.FleetBuildingResourceDemand;
import com.radowiecki.transformers.model.FleetFactoryBuildingResourceDemand;
import com.radowiecki.transformers.model.ResourceFactoryBuildingResourceDemand;
import com.radowiecki.transformers.model.ResourceStorageUpgradingResourceDemand;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ResourceDemandDaoImpl implements ResourceDemandDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<FleetBuildingResourceDemand> getFleetBuildingResourceDemandsByFleetTypeId(int fleetTypeId) {
		List<FleetBuildingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession()
				.createQuery("from FleetBuildingResourceDemand where fleetTypeId = :fleetTypeId")
				.setParameter("fleetTypeId", fleetTypeId).list();
		return resourceDemands;
	}

	public List<FleetFactoryBuildingResourceDemand> getFleetFacotryBuildingResourceDemandsByFleetFactoryTypeId(
			int fleetFactoryTypeId) {
		List<FleetFactoryBuildingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession()
				.createQuery("from FleetFactoryBuildingResourceDemand where fleetFactoryTypeId = :fleetFactoryTypeId")
				.setParameter("fleetFactoryTypeId", fleetFactoryTypeId).list();
		return resourceDemands;
	}

	public List<ResourceFactoryBuildingResourceDemand> getResourceFacotryBuildingResourceDemandsByResourceFactoryTypeId(
			int resourceFactoryTypeId) {
		List<ResourceFactoryBuildingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession()
				.createQuery(
						"from ResourceFactoryBuildingResourceDemand where resourceFactoryTypeId = :resourceFactoryTypeId")
				.setParameter("resourceFactoryTypeId", resourceFactoryTypeId).list();
		return resourceDemands;
	}

	public List<ResourceStorageUpgradingResourceDemand> getResourceStorageUpgradingResourceDemandsByResourceTypeId(
			int resourceStorageFactoryTypeId) {
		List<ResourceStorageUpgradingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession()
				.createQuery(
						"from ResourceStorageUpgradingResourceDemand where resourceStorageFactoryTypeId = :resourceStorageFactoryTypeId")
				.setParameter("resourceStorageFactoryTypeId", resourceStorageFactoryTypeId).list();
		return resourceDemands;
	}

	public FleetBuildingResourceDemand getFleetBuildingResourceDemandByFleetTypeIdAndByDemandedResourceTypeId(
			int fleetTypeId, int demandedResourceTypeId) {
		List<FleetBuildingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession().createQuery(
				"from FleetBuildingResourceDemand where fleetTypeId = :fleetTypeId and demandedResourceTypeId = :demandedResourceTypeId")
				.setParameter("fleetTypeId", fleetTypeId).setParameter("demandedResourceTypeId", demandedResourceTypeId)
				.list();
		return resourceDemands.size() > 0 ? (FleetBuildingResourceDemand) resourceDemands.get(0) : null;
	}

	public FleetFactoryBuildingResourceDemand getFleetFactoryBuildingResourceDemandByFleetFactoryTypeIdAndByDemandedResourceTypeId(
			int fleetFactoryTypeId, int demandedResourceTypeId) {
		List<FleetFactoryBuildingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession().createQuery(
				"from FleetFactoryBuildingResourceDemand where fleetFactoryTypeId = :fleetFactoryTypeId and demandedResourceTypeId = :demandedResourceTypeId")
				.setParameter("fleetFactoryTypeId", fleetFactoryTypeId)
				.setParameter("demandedResourceTypeId", demandedResourceTypeId).list();
		return resourceDemands.size() > 0 ? (FleetFactoryBuildingResourceDemand) resourceDemands.get(0) : null;
	}

	public ResourceFactoryBuildingResourceDemand getResourceFactoryBuildingResourceDemandByResourceFactoryTypeIdAndByDemandedResourceTypeId(
			int resourceFactoryTypeId, int demandedResourceTypeId) {
		List<ResourceFactoryBuildingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession()
				.createQuery(
						"from ResourceFactoryBuildingResourceDemand where resourceFactoryTypeId = :resourceFactoryTypeId and demandedResourceTypeId = :demandedResourceTypeId")
				.setParameter("resourceFactoryTypeId", resourceFactoryTypeId)
				.setParameter("demandedResourceTypeId", demandedResourceTypeId).list();
		return resourceDemands.size() > 0 ? (ResourceFactoryBuildingResourceDemand) resourceDemands.get(0) : null;
	}

	public ResourceStorageUpgradingResourceDemand getResourceStorageUpgradingResourceDemandByResourceFactoryTypeIdAndByDemandedResourceTypeId(
			int resourceStorageFactoryTypeId, int demandedResourceTypeId) {
		List<ResourceStorageUpgradingResourceDemand> resourceDemands = this.sessionFactory.getCurrentSession()
				.createQuery(
						"from ResourceStorageUpgradingResourceDemand where resourceStorageFactoryTypeId = :resourceStorageFactoryTypeId and demandedResourceTypeId = :demandedResourceTypeId")
				.setParameter("resourceStorageFactoryTypeId", resourceStorageFactoryTypeId)
				.setParameter("demandedResourceTypeId", demandedResourceTypeId).list();
		return resourceDemands.size() > 0 ? (ResourceStorageUpgradingResourceDemand) resourceDemands.get(0) : null;
	}
}