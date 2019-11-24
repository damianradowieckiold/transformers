create table authorities(id integer primary key, authority text);

insert into authorities values (1, 'ADMIN');
insert into authorities values (2, 'USER');

create table users(
id integer primary key,
username text,
password text,
authorityId integer,
enabled boolean);

create sequence users_id_seq;

create table factoryTypes(
id integer primary key,
type text);

insert into factoryTypes values (1, 'krypton');
insert into factoryTypes values (2, 'astat');
insert into factoryTypes values (3, 'europium');
insert into factoryTypes values (4, 'prokatyn');
insert into factoryTypes values (5, 'titan');
insert into factoryTypes values (6, 'rhea');
insert into factoryTypes values (7, 'enceladus');
insert into factoryTypes values (8, 'calypso');

create table fleets(
id integer primary key,
fleetTypeId integer,
amount integer,
planetId integer);

create table factories(
id integer primary key,
krypton_factory_level integer,
astat_factory_level integer,
europium_factory_level integer,
prokartyn_factory_level integer,
titan_factory_level integer,
rhea_factory_level integer,
enceladus_factory_level integer,
calypso_factory_level integer,
username text);

create table fleetBuildingResourceDemands(
id integer primary key,
fleetTypeId integer,
demandedResourceTypeId integer,
amount integer
);

create table fleetFactories(
id integer primary key,
fleetTypeId integer,
level integer,
planetId integer
);

create sequence fleetFactories_id_seq;

create table fleetFactoryBuildingResourceDemands(
id integer primary key,
fleetFactoryTypeId integer,
demandeDresourceTypeId integer,
amount integer
);

create table fleetTypes(
id integer primary key,
name text
);

create table planets(
id integer primary key,
userId integer,
name text,
xCoordinate integer,
yCoordinate integer
);

create sequence planet_id_seq;

create table resources(
id integer primary key,
resourceTypeId integer,
amount integer,
planetId integer
);

create sequence resources_id_seq;

create table resourceFactories(
id integer primary key,
resourceTypeId integer,
level integer,
planetId integer
);
create sequence resourceFactories_id_seq;

create table resourceFactoryBuildingResourceDemands(
id integer primary key,
resourceFactoryTypeId integer,
demandedResourceTypeId integer,
amount integer
);

create table resourceStorages(
id integer primary key,
planetId integer,
resourceTypeId integer,
maximumLoad integer
);

create table resourceStorageUpgradingResourceDemands(
id integer primary key,
resourceStorageFactoryTypeId integer,
demandedResourceTypeId integer,
amount integer
);

create table resourceTypes(
id integer primary key,
name text);

insert into resourceTypes values (1, 'krypton');
insert into resourceTypes values (2, 'astat');
insert into resourceTypes values (3, 'europium');
insert into resourceTypes values (4, 'prokatyn');
insert into resourceTypes values (5, 'titan');
insert into resourceTypes values (6, 'rhea');
insert into resourceTypes values (7, 'enceladus');
insert into resourceTypes values (8, 'calypso');

create table scheduledattackTasks(
id integer primary key,
attackingPlanetId integer,
attackedPlanetId integer,
startDate timestamp,
fleetTypeId integer,
fleetAmount integer,
executionDate timestamp,
done boolean
);

create sequence scheduled_attack_seq_id;

create table scheduledFleetBuildingTasks(
id integer primary key,
startDate timestamp,
executionDate timestamp,
fleetTypeId integer,
planetId integer,
done boolean
);

create sequence tasks_seq_generator;

create table scheduledFleetFactoryBuildingTasks(
id integer primary key,
startDate timestamp,
executionDate timestamp,
fleetTypeId integer,
planetId integer,
done boolean
);

create sequence scheduledFleetFactoryBuildingTasks_seq;


create table scheduledResourceFactoryBuildingTasks(
id integer primary key,
startDate timestamp,
executionDate timestamp,
resourceTypeId integer,
planetId integer,
done boolean
);

create sequence scheduledresourcefactorybuildingtasks_seq;


create table scheduledResourceStorageUpgradingTasks(
id integer primary key,
startDate timestamp,
executionDate timestamp,
resourceTypeId integer,
planetId integer,
done boolean
);

create sequence scheduledresourcefactorybuildingtasks_seq;






----
insert into FleetBuildingResourceDemands values (1,1,1,40);
insert into FleetBuildingResourceDemands values (1,1,2,40);
insert into FleetBuildingResourceDemands values (2,1,2,40);
insert into FleetBuildingResourceDemands values (3,1,3,40);
insert into FleetBuildingResourceDemands values (4,1,4,40);
insert into FleetBuildingResourceDemands values (5,2,1,40);
insert into FleetBuildingResourceDemands values (6,2,2,40);
insert into FleetBuildingResourceDemands values (7,2,3,40);
insert into FleetBuildingResourceDemands values (8,2,4,40);
insert into FleetBuildingResourceDemands values (9,3,1,40);
insert into FleetBuildingResourceDemands values (10,3,2,40);
insert into FleetBuildingResourceDemands values (11,3,3,40);
insert into FleetBuildingResourceDemands values (12,3,4,40);
insert into FleetBuildingResourceDemands values (13,4,1,40);
insert into FleetBuildingResourceDemands values (14,4,2,40);
insert into FleetBuildingResourceDemands values (15,4,3,40);
insert into FleetBuildingResourceDemands values (16,4,4,40);

  insert into fleetFactoryBuildingResourceDemands values (1,1,1,40);
  insert into fleetFactoryBuildingResourceDemands values (1,1,2,40);
  insert into fleetFactoryBuildingResourceDemands values (2,1,2,40);
  insert into fleetFactoryBuildingResourceDemands values (3,1,3,40);
  insert into fleetFactoryBuildingResourceDemands values (4,1,4,40);
  insert into fleetFactoryBuildingResourceDemands values (5,2,1,40);
  insert into fleetFactoryBuildingResourceDemands values (6,2,2,40);
  insert into fleetFactoryBuildingResourceDemands values (7,2,3,40);
  insert into fleetFactoryBuildingResourceDemands values (8,2,4,40);
  insert into fleetFactoryBuildingResourceDemands values (9,3,1,40);
  insert into fleetFactoryBuildingResourceDemands values (10,3,2,40);
  insert into fleetFactoryBuildingResourceDemands values (11,3,3,40);
  insert into fleetFactoryBuildingResourceDemands values (12,3,4,40);
  insert into fleetFactoryBuildingResourceDemands values (13,4,1,40);
  insert into fleetFactoryBuildingResourceDemands values (14,4,2,40);
  insert into fleetFactoryBuildingResourceDemands values (15,4,3,40);
  insert into fleetFactoryBuildingResourceDemands values (16,4,4,40);

    insert into resourceFactoryBuildingResourceDemands values (1,1,1,40);
    insert into resourceFactoryBuildingResourceDemands values (1,1,2,40);
    insert into resourceFactoryBuildingResourceDemands values (2,1,2,40);
    insert into resourceFactoryBuildingResourceDemands values (3,1,3,40);
    insert into resourceFactoryBuildingResourceDemands values (4,1,4,40);
    insert into resourceFactoryBuildingResourceDemands values (5,2,1,40);
    insert into resourceFactoryBuildingResourceDemands values (6,2,2,40);
    insert into resourceFactoryBuildingResourceDemands values (7,2,3,40);
    insert into resourceFactoryBuildingResourceDemands values (8,2,4,40);
    insert into resourceFactoryBuildingResourceDemands values (9,3,1,40);
    insert into resourceFactoryBuildingResourceDemands values (10,3,2,40);
    insert into resourceFactoryBuildingResourceDemands values (11,3,3,40);
    insert into resourceFactoryBuildingResourceDemands values (12,3,4,40);
    insert into resourceFactoryBuildingResourceDemands values (13,4,1,40);
    insert into resourceFactoryBuildingResourceDemands values (14,4,2,40);
    insert into resourceFactoryBuildingResourceDemands values (15,4,3,40);
    insert into resourceFactoryBuildingResourceDemands values (16,4,4,40);

      insert into resourceStorageUpgradingResourceDemands values (1,1,1,40);
      insert into resourceStorageUpgradingResourceDemands values (1,1,2,40);
      insert into resourceStorageUpgradingResourceDemands values (2,1,2,40);
      insert into resourceStorageUpgradingResourceDemands values (3,1,3,40);
      insert into resourceStorageUpgradingResourceDemands values (4,1,4,40);
      insert into resourceStorageUpgradingResourceDemands values (5,2,1,40);
      insert into resourceStorageUpgradingResourceDemands values (6,2,2,40);
      insert into resourceStorageUpgradingResourceDemands values (7,2,3,40);
      insert into resourceStorageUpgradingResourceDemands values (8,2,4,40);
      insert into resourceStorageUpgradingResourceDemands values (9,3,1,40);
      insert into resourceStorageUpgradingResourceDemands values (10,3,2,40);
      insert into resourceStorageUpgradingResourceDemands values (11,3,3,40);
      insert into resourceStorageUpgradingResourceDemands values (12,3,4,40);
      insert into resourceStorageUpgradingResourceDemands values (13,4,1,40);
      insert into resourceStorageUpgradingResourceDemands values (14,4,2,40);
      insert into resourceStorageUpgradingResourceDemands values (15,4,3,40);
      insert into resourceStorageUpgradingResourceDemands values (16,4,4,40);
