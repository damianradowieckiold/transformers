function changePlanet(id) {
	console.log('changePlanet');

	var data = 'planetId=' + id;
	var token = $("meta[name='_csrf']").attr("content"); 

	$.ajax({
		type : 'POST',
		url : '/Transformers/planet/chosenPlanet',
		data : data,
		headers : { 'X-CSRF-TOKEN' : token }
	}).done(function(data, textStatus, jqXHR) {
		console.log('changedPlanet:' + data);
		getUserResources(updateResources);
		getScheduledFleetBuildingTasks(insertScheduledFleetBuildingTasksToTable, function(){});
		getScheduledFleetFactoryBuildingTasks(insertScheduledFleetFactoryBuildingTasksToTable, function(){});
		getScheduledResourceFactoryBuildingTasks(insertScheduledResourceFactoryBuildingTasksToTable, function(){});
		getScheduledResourceStorageBuildingTasks(insertScheduledResourceStorageBuildingTasksToTable, function(){});
		getScheduledAttackTasks(insertScheduledAttackTasksToTable, function() {});
		getUserFleets(updateFleets);
	}).fail(function(xhr, textStatus, errorThrown){
		console.log(errorThrown);
	});

	return false;
}