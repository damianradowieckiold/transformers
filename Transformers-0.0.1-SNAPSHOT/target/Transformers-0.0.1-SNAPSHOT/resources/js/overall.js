var attackCounters = new Array();

$(document).ready(function() {

	setInterval(doInOneSecondInterval,1000);
	
});

function doInOneSecondInterval(){
	console.log('doInOneSecondInterval');
	decreaseFleetBuildingCounters();
	refreshFleetBuildingCounters();
	
	decreaseFleetFactoryBuildingCounters();
	refreshFleetFactoryBuildingCounters();
	
	decreaseResourceFactoryBuildingCounters();
	refreshResourceFactoryBuildingCounters();
	
	decreaseResourceStorageUpgradingCounters();
	refreshResourceStorageUpgradingCounters();
	
	decreaseAttackCounters();
	refreshAttackCounters();
}

function prepareTime(secondsToTheEnd){
	console.log('prepareTime');
	var seconds = secondsToTheEnd%60;
	var minutesToTheEnd = Math.floor(secondsToTheEnd/60);
	var minutes = minutesToTheEnd%60;
	var hoursToTheEnd = Math.floor(minutes/60);
	//I assume that no time will be greater than 24 hours
	var hours = hoursToTheEnd;
	
	var hoursText = getWellPreparedTime(String(hours));
	var minutesText = getWellPreparedTime(String(minutes));
	var secondsText = getWellPreparedTime(String(seconds));
	
	return hoursText + ":" + minutesText + ":" + secondsText;
}



function getWellPreparedTime(timeElement){
	console.log('getWellPreparedTime');
	if(timeElement.length <2){
		return '0' + timeElement;
	}
	else{
		return timeElement;
	}
}

function clearIntervals(intervals){
	console.log('clearIntervals');
	for(var i=0 ; i<intervals.length ; i++){
		clearInterval(intervals[i]);
	}
}

function cleanTaskTable(tableId){
	console.log('cleanTaskTable(' + tableId + ")");

	var table = document.getElementById(tableId);
	var columns = table.getElementsByTagName('tr');
	var length = columns.length;
	for(var i=length-1 ; i>0 ; i--){
		columns[i].parentNode.removeChild(columns[i]);
	}

}

function attachCountdownCounterToCell(secondsToTheEnd, elementId, row, taskId){
	console.log('attachTimer');
	var interval = setInterval(function(){
		if(secondsToTheEnd >= 0){
			var time = prepareTime(secondsToTheEnd--);
			$('#'+elementId+'').text(time);
		}
		else{
			clearInterval(interval);
			row.parentNode.removeChild(row);
			isFleetBuildingTaskFinished(taskId);
		}
	},1000);
	return interval;
}

