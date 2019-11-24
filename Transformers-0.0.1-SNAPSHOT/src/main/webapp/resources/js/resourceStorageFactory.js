var resourceStorageBuildWaitingRequests = 0;
var resourceStorageBuildingTaskIntervals = new Array();
var resourceStorageBuildingTaskCounters = new Array();

$(document).ready(function(){
	
	getScheduledResourceStorageBuildingTasks(insertScheduledResourceStorageBuildingTasksToTable, function(){});
	
	$('#buildResourceStorage').click(function(){
		buildResourceStorage();
	});
});

function buildResourceStorage(){
	console.log("buildResourceStorage");
	
	var resourceStoragesToBuild = getResourceStoragesToBuild();
	
	resourceStorageBuildWaitingRequests = resourceStoragesToBuild.size;
	
	startBuildingResourceStorages(resourceStoragesToBuild,
			function(){getScheduledResourceStorageBuildingTasks(insertScheduledResourceStorageBuildingTasksToTable, function(){})});
}

function startBuildingResourceStorages(resourceStorageBuildWaitingRequest, onDoneFirstFunction){
	console.log('startBuildingResourceStorages');
	
	console.log(resourceStorageBuildWaitingRequest);

	var token = $("meta[name='_csrf']").attr("content"); 
	
	$.ajax({
		url:'/Transformers/resourceStoragesBuilding',
		type: 'POST',
		data: {
			0 : resourceStorageBuildWaitingRequest[0],
			1 : resourceStorageBuildWaitingRequest[1],
			2 : resourceStorageBuildWaitingRequest[2],
			3 : resourceStorageBuildWaitingRequest[3]
	},
		headers: {'X-CSRF-TOKEN' : token}
	}).done(function(data, textStatus, jqXHR){
		onDoneFirstFunction();
	})
	.fail(function(jqXHR, textStatus, errorThrown) {});
}

function getResourceStoragesToBuild() {
	console.log("getResourceStoragesToBuild");

	var resourceTypeId_0 = resourceTypes[0].id;
	var resourceStorageValue_0 = $('#' + resourceTypes[0].name + 'ResourceStorageFactoryBuildHowMany').val();
	
	var resourceTypeId_1 = resourceTypes[1].id;
	var resourceStorageValue_1 = $('#' + resourceTypes[1].name + 'ResourceStorageFactoryBuildHowMany').val();
	
	var resourceTypeId_2 = resourceTypes[2].id;
	var resourceStorageValue_2 = $('#' + resourceTypes[2].name + 'ResourceStorageFactoryBuildHowMany').val();
	
	var resourceTypeId_3 = resourceTypes[3].id;
	var resourceStorageValue_3 = $('#' + resourceTypes[3].name + 'ResourceStorageFactoryBuildHowMany').val();
	
	var resourceStoragesToBuild = {
			[resourceTypeId_0] : resourceStorageValue_0,
			[resourceTypeId_1]: resourceStorageValue_1,
			[resourceTypeId_2] : resourceStorageValue_2,
			[resourceTypeId_3] : resourceStorageValue_3
	}

	return resourceStoragesToBuild;
}

function getScheduledResourceStorageBuildingTasks(doneFunction, failFunction){
	console.log('getScheduledResourceStorageBuildingTasks');
	$.ajax({
		type: 'GET',
		url: '/Transformers/scheduledTasks/resoureStorageBuildingTasks'
	}).done(doneFunction)
	.fail(failFunction);
}

function insertScheduledResourceStorageBuildingTasksToTable(data, textStatus, jqXHR){
	console.log('insertScheduledResourceStorageBuildingTasksToTable');
	console.log(data);
	
	var tableId = 'resourceStorageBuildingsTable';
	
	cleanTaskTable(tableId);
	clearIntervals(resourceStorageBuildingTaskIntervals);
	
	if(data.length>0){
		var now = Math.floor((new Date()).getTime() / 1000);
		data.sort(function(a,b){
			return (b.startDate - a.startDate);
		});
		
		for(var i=0 ; i<data.length ; i++){
			var startTimeInSeconds = Math.floor(data[i].startDate/1000);
			var executionTimeInSeconds = Math.floor(data[i].executionDate/1000);
			var secondsToTheEnd = executionTimeInSeconds - now;
			if(secondsToTheEnd > 0 && startTimeInSeconds <= now){
				var counter = insertOneScheduledResourceStorageBuildingTaskToTable(data[i].buildingTaskName, secondsToTheEnd, tableId, data[i].id);
				resourceStorageBuildingTaskCounters.push(counter);
			}
		}
	}
	
}





function insertOneScheduledResourceStorageBuildingTaskToTable(taskName, secondsToTheEnd, tableId, taskId){
	console.log('insertOneScheduledResourceStorageBuildingTaskToTable');
	
	var table = document.getElementById(tableId);
	var rowLength = table.getElementsByTagName('tbody')[0].getElementsByTagName('tr').length;
	var row = table.insertRow(rowLength);
	var cell_1 = row.insertCell(0);
	cell_1.innerHTML = taskName;
	var cell_2 = row.insertCell(1);
	cell_2.id = "row_" + rowLength + "cell_2" + tableId;
	
	var counter = {secondsToTheEnd:secondsToTheEnd, elementId:cell_2.id, row:row, taskId:taskId};
	return counter;
}

function isResourceStorageBuildingTaskFinished(id, callback){
	console.log('isResourceStorageBuildingTaskFinished');
	var data = 'taskId=' + id;
	setTimeout(function(){}, 1000);
	$.ajax(
		{
			url: '/Transformers/isResourceStorageBuildingTaskFinished',
			type: 'GET',
			data: data
		}	
	).done(function(data, textStatus, jqXHR){
		if(data.done){
			getUserResources(updateResources);
			getScheduledResourceStorageBuildingTasks(insertScheduledResourceStorageBuildingTasksToTable, function(){});
			callback();
		}
		else if(data.done == false && data.taskNotFound == false){
			var timeToWait = data.secondsToWait;
			setTimeout(isResourceStorageBuildingTaskFinished(id, callback),	timeToWait);
		}
	});
}

function getUserResourceStorages(doneFunction, failFunction){
	console.log('getUserResourceStorages');
	$.ajax({
		url : '/Transformers/userResourceStorages',
		type : 'GET'
	}).done(function (data, textStatus, jqXHR){
		doneFunction(data, textStatus, jqXHR);
	}).fail(function (jqXHR, textStatus, errorThrown){
		failFunction(jqXHR, textStatus, errorThrown);
	});
	
}

function updateResourceStorages(resourceStorages){
	console.log('updateResourceStorages');
	for(var i=0;i<resourceStorages.length ; i++){
		$('#' + resourceStorages[i].resourceTypeName + 'ResourceStorage').text(resourceStorages[i].maximumLoad);
	}
}

function decreaseResourceStorageUpgradingCounters(){
	console.log("decreaseResourceStorageUpgradingCounters");
	for(var i=0 ; i<resourceStorageBuildingTaskCounters.length;i++){
		resourceStorageBuildingTaskCounters[i].secondsToTheEnd = resourceStorageBuildingTaskCounters[i].secondsToTheEnd - 1;
		if(resourceStorageBuildingTaskCounters[i].secondsToTheEnd < 0){
			isResourceStorageBuildingTaskFinished(resourceStorageBuildingTaskCounters[i].taskId, function(){
				getUserResourceStorages(updateResourceStorages, function(){});
				getScheduledResourceStorageBuildingTasks(insertScheduledResourceStorageBuildingTasksToTable, function(){});
			});
			var index = resourceStorageBuildingTaskCounters.indexOf(resourceStorageBuildingTaskCounters[i]);
			resourceStorageBuildingTaskCounters.splice(index, 1);
		}
	}
}

function refreshResourceStorageUpgradingCounters(){
	console.log("refreshResourceStorageUpgradingCounters");
	for(var i=0 ; i<resourceStorageBuildingTaskCounters.length;i++){
		var time = prepareTime(resourceStorageBuildingTaskCounters[i].secondsToTheEnd);
		$('#'+resourceStorageBuildingTaskCounters[i].elementId+'').text(time);
	}
}