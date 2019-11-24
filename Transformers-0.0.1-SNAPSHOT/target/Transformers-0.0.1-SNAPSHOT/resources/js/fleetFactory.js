var fleetFactoryBuildWaitingRequests = 0;
var fleetFactoryBuildingTaskIntervals = new Array();
var fleetFactoryBuildingCounters = new Array();

$(document).ready(function(){
	
	getScheduledFleetFactoryBuildingTasks(insertScheduledFleetFactoryBuildingTasksToTable, function(){});
	
	$('#buildFleetFactory').click(function(){
		buildFleetFactory();
	});
});

function buildFleetFactory(){
	console.log("buildFleetFactory");
	
	var fleetFactoriesToBuild = getFleetFactoriesToBuild();
	
	fleetBuildWaitingRequests = fleetFactoriesToBuild.size;
	
	startBuildingFleetFactories(fleetFactoriesToBuild, function(){getUserFleetFactories(updateFleetFactories, function(){});}, 
			function(){getScheduledFleetFactoryBuildingTasks(insertScheduledFleetFactoryBuildingTasksToTable, function(){})});
}

function startBuildingFleetFactories(fleetFactoryBuildingRequest, onDoneFirstFunction, onDoneSecondFunction){
	console.log('startBuildingFleets');
	
	console.log(fleetFactoryBuildingRequest);

	var token = $("meta[name='_csrf']").attr("content"); 
	
	$.ajax({
		url:'/Transformers/fleetFactoriesBuilding',
		type: 'POST',
		data: {
			0 : fleetFactoryBuildingRequest[0],
			1 : fleetFactoryBuildingRequest[1],
			2 : fleetFactoryBuildingRequest[2],
			3 : fleetFactoryBuildingRequest[3]
	},
		headers: {'X-CSRF-TOKEN' : token}
	}).done(function(data, textStatus, jqXHR){
		onDoneFirstFunction(data);
		onDoneSecondFunction();
	})
	.fail(function(jqXHR, textStatus, errorThrown) {});
}

function getFleetFactoriesToBuild() {
	console.log("getFleetFactoriesToBuild");

	var fleetTypeId_0 = fleetTypes[0].id;
	var fleetFactoryValue_0 = $('#fleetFactory' + fleetTypes[0].name + 'BuildHowMany').val();
	
	var fleetTypeId_1 = fleetTypes[1].id;
	var fleetFactoryValue_1 = $('#fleetFactory' + fleetTypes[1].name + 'BuildHowMany').val();
	
	var fleetTypeId_2 = fleetTypes[2].id;
	var fleetFactoryValue_2 = $('#fleetFactory' + fleetTypes[2].name + 'BuildHowMany').val();
	
	var fleetTypeId_3 = fleetTypes[3].id;
	var fleetFactoryValue_3 = $('#fleetFactory' + fleetTypes[3].name + 'BuildHowMany').val();
	
	var fleetsToBuild = {
			[fleetTypeId_0] : fleetFactoryValue_0,
			[fleetTypeId_1]: fleetFactoryValue_1,
			[fleetTypeId_2] : fleetFactoryValue_2,
			[fleetTypeId_3] : fleetFactoryValue_3
	}

	return fleetsToBuild;
}

function getScheduledFleetFactoryBuildingTasks(doneFunction, failFunction){
	console.log('getScheduledFleetFactoryBuildingTasks');
	$.ajax({
		type: 'GET',
		url: '/Transformers/scheduledTasks/fleetFactoryBuildingTasks'
	}).done(doneFunction)
	.fail(failFunction);
}

function insertScheduledFleetFactoryBuildingTasksToTable(data, textStatus, jqXHR){
	console.log('insertScheduledFleetFactoryBuildingTasksToTable');
	console.log(data);
	
	var tableId = 'fleetFactoryBuildingsTable';
	
	cleanTaskTable(tableId);
	clearIntervals(fleetFactoryBuildingTaskIntervals);
	
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
				var counter = insertOneScheduledFleetFactoryBuildingTaskToTable(data[i].buildingTaskName, secondsToTheEnd, tableId, data[i].id);
				fleetFactoryBuildingCounters.push(counter);
			}
		}
	}
	
}


function insertOneScheduledFleetFactoryBuildingTaskToTable(taskName, secondsToTheEnd, tableId, taskId){
	console.log('insertOneScheduledFleetFactoryBuildingTaskToTable');
	
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


function isFleetFactoryBuildingTaskFinished(id, callback){
	console.log('isFleetFactoryBuildingTaskFinished');
	var data = 'taskId=' + id;
	$.ajax(
		{
			url: '/Transformers/isFleetFactoryBuildingTaskFinished',
			type: 'GET',
			data: data
		}	
	).done(function(data, textStatus, jqXHR){
		if(data.done){
			getUserResources(updateResources);
			getScheduledFleetFactoryBuildingTasks(insertScheduledFleetFactoryBuildingTasksToTable, function(){});
			callback();
		}
		else if(data.done == false && data.taskNotFound == false){
			var timeToWait = data.secondsToWait;
			setTimeout(isFleetFactoryBuildingTaskFinished(id,callback),	timeToWait);
		}	
	});
}

function getUserFleetFactories(doneFunction, failFunction){
	console.log('getUserFleetFactories');
	$.ajax({
		url : '/Transformers/userFleetFactories',
		type : 'GET'
	}).done(function (data, textStatus, jqXHR){
		doneFunction(data, textStatus, jqXHR);
	}).fail(function (jqXHR, textStatus, errorThrown){
		failFunction(jqXHR, textStatus, errorThrown);
	});
	
}

function updateFleetFactories(fleetFactories){
	console.log('updateFleetFactories');
	for(var i=0;i<fleetFactories.length ; i++){
		$('#' + fleetFactories[i].typeName + 'FleetFactory').text(fleetFactories[i].amount);
	}
}

function decreaseFleetFactoryBuildingCounters(){
	console.log('decreaseFleetFactoryBuildingCounters');
	for(var i=0 ; i<fleetFactoryBuildingCounters.length;i++){
		fleetFactoryBuildingCounters[i].secondsToTheEnd = fleetFactoryBuildingCounters[i].secondsToTheEnd - 1;
		if(fleetFactoryBuildingCounters[i].secondsToTheEnd < 0){
			isFleetFactoryBuildingTaskFinished(fleetFactoryBuildingCounters[i].taskId,function(){
				getUserFleetFactories(updateFleetFactories, function(){})
				getScheduledFleetFactoryBuildingTasks(insertScheduledFleetFactoryBuildingTasksToTable);
			});
			var index = fleetFactoryBuildingCounters.indexOf(fleetFactoryBuildingCounters[i]);
			fleetFactoryBuildingCounters.splice(index, 1);
		}
	}
}

function refreshFleetFactoryBuildingCounters(){
	console.log('refreshFleetFactoryBuildingCounters');
	for(var i=0 ; i<fleetFactoryBuildingCounters.length;i++){
		var time = prepareTime(fleetFactoryBuildingCounters[i].secondsToTheEnd);
		$('#'+fleetFactoryBuildingCounters[i].elementId+'').text(time);
	}
}