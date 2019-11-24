var attackWaitingRequests = 0;
var attackTaskIntervals = [];
var attackCounters = new Array();

$(document).ready(function() {

	getScheduledAttackTasks(insertScheduledAttackTasksToTable, function() {});
	
	$('#fleetAttack').click(function() {
		attack();
	});
	
});



function getScheduledAttackTasks(doneFunction, failFunction) {
	console.log('getScheduledAttackTasks');
	
	$.ajax({
		url : '/Transformers/scheduledTasks/attackTasks',
		type : 'GET'
	}).done(doneFunction).fail(failFunction);
}

function attackUser(planetId, fleetTypesId, amount, callbackFunction) {
	console.log('attackUser');

	var data = "planetId=" + planetId + "&fleetTypesId=" + fleetTypesId
			+ "&amount=" + amount;

	$.ajax({
		url : '/Transformers/attack',
		type : 'GET',
		data : data
	}).done(function(data, textStatus, jqXHR){
		attackWaitingRequests--;
		if(callbackFunction && attackWaitingRequests == 0){
			callbackFunction(data, textStatus, jqXHR);
		}
		
	});
}

function attack() {
	console.log('attack');
	
	var attacks = getAttacks();
	attackWaitingRequests = attacks.size;

	var planetIdToAttack = $('#planetToAttack').val();

	for (var[key, value]of attacks) {
		attackUser(key, value[0], value[1], function(){
			getUserFleets(updateFleets, function(){});
			getScheduledAttackTasks(insertScheduledAttackTasksToTable);
		});
	}
}

function getAttacks(){
	console.log('getAttacks');
	
	var attacks = new Map();
	var planetIdToAttack = $('#planetToAttack').val();
	
	for (var i = 0; i < fleetTypes.length; i++) {
		if ($('#' + fleetTypes[i].name + 'ToAttack').length) {
			var amount = $('#' + fleetTypes[i].name + 'ToAttack').val();
			if (amount > 0) {
				attacks.set(planetIdToAttack, [fleetTypes[i].id, amount]);
			}
		}
	}
	
	return attacks;
}


function insertOneAttackTaskToTable(attacking, attacked, secondsToTheEnd,
		tableId) {
	console.log('insertOneAttackTaskToTable');

	var table = document.getElementById(tableId);
	var rowLength = table.getElementsByTagName('tbody')[0]
			.getElementsByTagName('tr').length;
	var row = table.insertRow(rowLength);
	var cell_1 = row.insertCell(0);
	cell_1.innerHTML = attacked;
	var cell_2 = row.insertCell(1);
	cell_2.innerHTML = attacking;
	var cell_3 = row.insertCell(2);
	cell_3.id = "row_" + rowLength + "cell_3";

	return attachAttackCountdownCounterToCell(secondsToTheEnd, cell_3.id, row);
};

function attachAttackCountdownCounterToCell(secondsToTheEnd, elementId, row) {
	console.log('attachAttackCountdownCounterToCell');
	var interval = setInterval(function() {
		if (secondsToTheEnd >= 0) {
			var time = prepareTime(secondsToTheEnd--);
			$('#' + elementId + '').text(time);
		} else {
			clearInterval(interval);
			row.parentNode.removeChild(row);
			getScheduledAttackTasks(insertScheduledAttackTasksToTable, function() {
			});
		}
	}, 1000);
	//TODO
	return interval;
};


function insertScheduledAttackTasksToTable(data, textStatus, jqXHR) {
	console.log('insertScheduledAttackTasksToTable');

	var tableId = 'attacksTable';
	cleanTaskTable(tableId);

	if (data.length > 0) {
		var now = Math.floor((new Date()).getTime() / 1000);
		data.sort(function(a, b) {
			return (b.executionDate - a.executionDate);
		});

		for (var i = 0; i < data.length; i++) {
			var secondsToTheEnd = Math.floor(data[i].executionDate / 1000)
					- now;
			if (secondsToTheEnd > 0) {
				var counter = insertOneAttackTaskToTable(
						data[i].attackingPlanetName + " (" + data[i].attackingUserUsername + ")", 
						data[i].attackedPlanetName + " (" + data[i].attackedUserUsername + ")",
						secondsToTheEnd, tableId);
				attackCounters.push(counter);
			}
		}
	}
}

function isAttackTaskFinished(id, callback){
	console.log('isAttackTaskFinished');
	var data = 'taskId=' + id;
	$.ajax(
		{	
			url: '/Transformers/isAttackTaskFinished',
			type: 'GET',
			data: data
		}	
	).done(function(data, textStatus, jqXHR){
		if(data.done){
			getUserFleets(updateFleets, function(){});
			getScheduledAttackTasks(insertScheduledAttackTasksToTable, function(){});
			callback();
		}
		else if(data.done == false && data.taskNotFound == false){
			var timeToWait = data.secondsToWait;
			setTimeout(isAttackTaskFinished(id,callback),	timeToWait);
		}	
	});
}

function decreaseAttackCounters(){
	console.log('decreaseAttackCounters');
	for(var i=0 ; i<attackCounters.length;i++){
		attackCounters[i].secondsToTheEnd = attackCounters[i].secondsToTheEnd - 1;
		if(attackCounters[i].secondsToTheEnd < 0){
			isAttackTaskFinished(attackCounters[i].taskId,function(){
				getUserFleets(updateFleets, function(){});
				getScheduledAttackTasks(insertScheduledAttackTasksToTable, function() {});
			});
			var index = attackCounters.indexOf(attackCounters[i]);
			attackCounters.splice(index, 1);
		}
	}
}

function refreshAttackCounters(){
	console.log('refreshAttackCounters');
	for(var i=0 ; i<attackCounters.length;i++){
		var time = prepareTime(attackCounters[i].secondsToTheEnd);
		$('#'+attackCounters[i].elementId+'').text(time);
	}
}