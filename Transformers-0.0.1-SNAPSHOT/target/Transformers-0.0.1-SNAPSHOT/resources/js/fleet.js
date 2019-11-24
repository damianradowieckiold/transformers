var fleetBuildWaitingRequests = 0;
var fleetBuildingTaskIntervals = new Array();
var fleetBuildingCounters = new Array();

$(document).ready(function() {

	getFleetTypes(initializeFleetTypes);
	getScheduledFleetBuildingTasks(insertScheduledFleetBuildingTasksToTable, function(){});

	$('#buildFleet').click(function() {
		buildFleets();
	});
});

function getFleetTypes(callbackFunction) {
	console.log('getFleetTypes');
	$.ajax({
		type : 'GET',
		url : '/Transformers/fleetTypes'
	}).done(function(data, textStatus, jqXHR) {
		console.log(data);
		if (callbackFunction) {
			callbackFunction(data);
		}
		return data;
	});
}

function initializeFleetTypes(data) {
	console.log('initializeFleetTypes');
	console.log(data);
	fleetTypes = data;
}

function buildFleets() {
	console.log("buildFleet");
	
	var fleetsToBuild = getFleetsToBuild();
	fleetBuildWaitingRequests = fleetsToBuild.size;
	
	console.log(fleetsToBuild);
	
	startBuildingFleets(fleetsToBuild,
			function(){getScheduledFleetBuildingTasks(insertScheduledFleetBuildingTasksToTable, function(){})});
	
}

function getFleetsToBuild() {
	console.log("getFleetsToBuild");

	var fleetTypeId_0 = fleetTypes[0].id;
	var fleetValue_0 = $('#' + fleetTypes[0].name + 'BuildHowMany').val();
	
	var fleetTypeId_1 = fleetTypes[1].id;
	var fleetValue_1 = $('#' + fleetTypes[1].name + 'BuildHowMany').val();
	
	var fleetTypeId_2 = fleetTypes[2].id;
	var fleetValue_2 = $('#' + fleetTypes[2].name + 'BuildHowMany').val();
	
	var fleetTypeId_3 = fleetTypes[3].id;
	var fleetValue_3 = $('#' + fleetTypes[3].name + 'BuildHowMany').val();
	
	var fleetsToBuild = {
			[fleetTypeId_0] : fleetValue_0,
			[fleetTypeId_1]: fleetValue_1,
			[fleetTypeId_2] : fleetValue_2,
			[fleetTypeId_3] : fleetValue_3
	}

	return fleetsToBuild;
}

function strMapToJson(strMap) {
	console.log('strMapToJson');
    return JSON.stringify(strMapToObj(strMap));
}

function strMapToObj(strMap) {
	console.log('strMapToObj');
    let obj = Object.create(null);
    for (let [k,v] of strMap) {
        // We don’t escape the key '__proto__'
        // which can cause problems on older engines
        obj[k] = v;
    }
    return obj;
}

function startBuildingFleets(fleetBuildingRequest, onDoneFirstFunction){
	console.log('startBuildingFleets');
	
	console.log(fleetBuildingRequest);

	var token = $("meta[name='_csrf']").attr("content"); 
	
	$.ajax({
		url:'/Transformers/fleetsBuilding',
		type: 'POST',
		data: {
			0 : fleetBuildingRequest[0],
			1 : fleetBuildingRequest[1],
			2 : fleetBuildingRequest[2],
			3 : fleetBuildingRequest[3]
	},
		headers: {'X-CSRF-TOKEN' : token}
	}).done(function(data, textStatus, jqXHR){
		onDoneFirstFunction();
	})
	.fail(function(jqXHR, textStatus, errorThrown) {});
}


function getScheduledFleetBuildingTasks(doneFunction, failFunction){
	console.log('getScheduledFleetBuildingTasks');
	$.ajax({
		type: 'GET',
		url: '/Transformers/scheduledTasks/fleetBuildingTasks'
	}).done(doneFunction)
	.fail(failFunction);
}

function insertScheduledFleetBuildingTasksToTable(data, textStatus, jqXHR){
	console.log('insertScheduledFleetBuildingTasksToTable');
	console.log(data);
	
	var tableId = 'fleetBuildingsTable';
	
	cleanTaskTable(tableId);
	clearIntervals(fleetBuildingTaskIntervals);
	
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
				var counter = insertOneFleetBuildingTaskToTable(data[i].buildingTaskName, secondsToTheEnd, tableId, data[i].id);
				fleetBuildingCounters.push(counter);
			}
		}
	}
	
}


function insertOneFleetBuildingTaskToTable(taskName, secondsToTheEnd, tableId, taskId){
	console.log('insertOneFleetBuildingTaskToTable');
	
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

function decreaseFleetBuildingCounters(){
	console.log('decreaseFleetBuildingCounters');
	for(var i=0 ; i<fleetBuildingCounters.length;i++){
		fleetBuildingCounters[i].secondsToTheEnd = fleetBuildingCounters[i].secondsToTheEnd - 1;
		if(fleetBuildingCounters[i].secondsToTheEnd < 0){
			isFleetBuildingTaskFinished(fleetBuildingCounters[i].taskId, function(){
				getUserFleets(updateFleets);
				getScheduledFleetBuildingTasks(insertScheduledFleetBuildingTasksToTable);
			});
			var index = fleetBuildingCounters.indexOf(fleetBuildingCounters[i]);
			fleetBuildingCounters.splice(index, 1);
		}
	}
}

function refreshFleetBuildingCounters(){
	console.log('refreshFleetBuildingCounters');
	for(var i=0 ; i<fleetBuildingCounters.length;i++){
		var time = prepareTime(fleetBuildingCounters[i].secondsToTheEnd);
		$('#'+fleetBuildingCounters[i].elementId+'').text(time);
	}
}



function isFleetBuildingTaskFinished(id, callback){
	console.log('isFleetBuildingTaskFinished');
	var data = 'taskId=' + id;
	$.ajax(
		{
			url: '/Transformers/isFleetBuildingTaskFinished',
			type: 'GET',
			data: data
		}	
	).done(function(data, textStatus, jqXHR){
		if(data.done){
			getUserResources(updateResources);
			getScheduledFleetBuildingTasks(insertScheduledFleetBuildingTasksToTable, function(){});
			callback();
		}
		else if(data.done == false && data.taskNotFound == false){
			var timeToWait = data.secondsToWait;
			setTimeout(isFleetBuildingTaskFinished(id,callback),	timeToWait);
		}	
	});
}

function getUserFleets(doneFunction, failFunction){
	console.log('getUserFleets');
	$.ajax({
		url : '/Transformers/userFleets',
		type : 'GET'
	}).done(function (data, textStatus, jqXHR){
		doneFunction(data, textStatus, jqXHR);
	}).fail(function (jqXHR, textStatus, errorThrown){
		failFunction(jqXHR, textStatus, errorThrown);
	});
	
}

function updateFleets(fleets){
	console.log('updateFleets');
	for(var i=0;i<fleets.length ; i++){
		$('#' + fleets[i].typeName).text(fleets[i].amount);
	}
}