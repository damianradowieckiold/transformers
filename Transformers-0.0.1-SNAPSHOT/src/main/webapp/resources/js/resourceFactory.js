var resourceFactoryBuildWaitingRequests = 0;
var resourceFactoryBuildingTaskIntervals = new Array();
var resourceFactoryBuildingCounters = new Array();

$(document).ready(function(){
	
	getScheduledResourceFactoryBuildingTasks(insertScheduledResourceFactoryBuildingTasksToTable, function(){});
	
	$('#buildResourceFactory').click(function(){
		buildResourceFactory();
	});
});

function buildResourceFactory(){
	console.log("buildResourceFactory");
	
	var resourceFactoriesToBuild = getResourceFactoriesToBuild();
	
	resourceFactoryBuildWaitingRequests = resourceFactoriesToBuild.size;
	
	startBuildingResourceFactories(resourceFactoriesToBuild, function(){getUserResourceFactories(updateResourceFactories)}, 
			function(){getScheduledResourceFactoryBuildingTasks(insertScheduledResourceFactoryBuildingTasksToTable, function(){})});
}

function startBuildingResourceFactories(resourceFactoryBuildingRequest, onDoneFirstFunction, onDoneSecondFunction){
	console.log('startBuildingResourceFactories');
	
	console.log(resourceFactoryBuildingRequest);

	var token = $("meta[name='_csrf']").attr("content"); 
	
	$.ajax({
		url:'/Transformers/resourceFactoriesBuilding',
		type: 'POST',
		data: {
			0 : resourceFactoryBuildingRequest[0],
			1 : resourceFactoryBuildingRequest[1],
			2 : resourceFactoryBuildingRequest[2],
			3 : resourceFactoryBuildingRequest[3]
	},
		headers: {'X-CSRF-TOKEN' : token}
	}).done(function(data, textStatus, jqXHR){
		onDoneFirstFunction(data);
		onDoneSecondFunction();
	})
	.fail(function(jqXHR, textStatus, errorThrown) {
		console.log(textStatus);
		console.log(errorThrown);
	});
}

function getResourceFactoriesToBuild() {
	console.log("getResourceFactoriesToBuild");

	var resourceTypeId_0 = resourceTypes[0].id;
	var resourceFactoryValue_0 = $('#' + resourceTypes[0].name + 'ResourceFactoryBuildHowMany').val();
	
	var resourceTypeId_1 = resourceTypes[1].id;
	var resourceFactoryValue_1 = $('#' + resourceTypes[1].name + 'ResourceFactoryBuildHowMany').val();
	
	var resourceTypeId_2 = resourceTypes[2].id;
	var resourceFactoryValue_2 = $('#' + resourceTypes[2].name + 'ResourceFactoryBuildHowMany').val();
	
	var resourceTypeId_3 = resourceTypes[3].id;
	var resourceFactoryValue_3 = $('#' + resourceTypes[3].name + 'ResourceFactoryBuildHowMany').val();
	
	var resourcesToBuild = {
			[resourceTypeId_0] : resourceFactoryValue_0,
			[resourceTypeId_1]: resourceFactoryValue_1,
			[resourceTypeId_2] : resourceFactoryValue_2,
			[resourceTypeId_3] : resourceFactoryValue_3
	}

	return resourcesToBuild;
}

function getScheduledResourceFactoryBuildingTasks(doneFunction, failFunction){
	$.ajax({
		type: 'GET',
		url: '/Transformers/scheduledTasks/resourceFactoryBuildingTasks'
	}).done(doneFunction)
	.fail(failFunction);
}

function insertScheduledResourceFactoryBuildingTasksToTable(data, textStatus, jqXHR){
	console.log('insertScheduledResourceFactoryBuildingTasksToTable');
	console.log(data);
	
	var tableId = 'resourceFactoryBuildingsTable';
	
	cleanTaskTable(tableId);
	clearIntervals(resourceFactoryBuildingTaskIntervals);
	
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
				var counter = insertOneScheduledResourceFactoryBuildingTaskToTable(data[i].buildingTaskName, secondsToTheEnd, tableId, data[i].id);
				resourceFactoryBuildingCounters.push(counter);
			}
		}
	}
	
}


function insertOneScheduledResourceFactoryBuildingTaskToTable(taskName, secondsToTheEnd, tableId, taskId){
	console.log('insertOneScheduledResourceFactoryBuildingTaskToTable');
	
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


function isResourceFactoryBuildingTaskFinished(id, callback){
	console.log('isResourceFactoryBuildingTaskFinished');
	var data = 'taskId=' + id;
	setTimeout(function(){}, 1000);
	$.ajax(
		{
			url: '/Transformers/isResourceFactoryBuildingTaskFinished',
			type: 'GET',
			data: data
		}	
	).done(function(data, textStatus, jqXHR){
		if(data.done){
			getUserResources(updateResources);
			getScheduledResourceFactoryBuildingTasks(insertScheduledResourceFactoryBuildingTasksToTable, function(){});
			callback();
		}
		else if(data.done == false && data.taskNotFound == false){
			var timeToWait = data.secondsToWait;
			setTimeout(isResourceFactoryBuildingTaskFinished(id, callback),	timeToWait);
		}
	});
}

function getUserResourceFactories(doneFunction, failFunction){
	console.log("getUserResourceFactories");
	$.ajax({
		url : '/Transformers/userResourceFactories',
		type : 'GET'
	}).done(function (data, textStatus, jqXHR){
		doneFunction(data, textStatus, jqXHR);
	}).fail(function (jqXHR, textStatus, errorThrown){
		failFunction(jqXHR, textStatus, errorThrown);
	});
	
}

function updateResourceFactories(resourceFactories){
	console.log("updateResourceFactories");
	for(var i=0;i<resourceFactories.length ; i++){
		$('#' + resourceFactories[i].typeName + 'ResourceFactory').text(resourceFactories[i].amount);
	}
}

function decreaseResourceFactoryBuildingCounters(){
	console.log("decreaseResourceFactoryBuildingCounters");
	for(var i=0 ; i<resourceFactoryBuildingCounters.length;i++){
		resourceFactoryBuildingCounters[i].secondsToTheEnd = resourceFactoryBuildingCounters[i].secondsToTheEnd - 1;
		if(resourceFactoryBuildingCounters[i].secondsToTheEnd < 0){
			isResourceFactoryBuildingTaskFinished(resourceFactoryBuildingCounters[i].taskId, function(){
				getUserResourceFactories(updateResourceFactories);
				getScheduledResourceFactoryBuildingTasks(insertScheduledResourceFactoryBuildingTasksToTable, function(){});
			});
			var index = resourceFactoryBuildingCounters.indexOf(resourceFactoryBuildingCounters[i]);
			resourceFactoryBuildingCounters.splice(index, 1);
		}
	}
}

function refreshResourceFactoryBuildingCounters(){
	console.log("refreshResourceFactoryBuildingCounters");
	for(var i=0 ; i<resourceFactoryBuildingCounters.length;i++){
		var time = prepareTime(resourceFactoryBuildingCounters[i].secondsToTheEnd);
		$('#'+resourceFactoryBuildingCounters[i].elementId+'').text(time);
	}
}