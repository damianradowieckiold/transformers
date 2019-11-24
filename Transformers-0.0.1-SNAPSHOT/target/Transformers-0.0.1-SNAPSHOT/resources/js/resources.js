var UPDATING_RESOURCES_INTERVAL = 20000;

$(document).ready(function(){
	
	getResourceTypes(initializeResourceTypes);
	
	window.setInterval(function(){
			getUserResources(updateResources);
		}, UPDATING_RESOURCES_INTERVAL);
	
	
});

function getResourceTypes(callbackFunction) {
	console.log('getResourceTypes');
	$.ajax({
		type : 'GET',
		url : '/Transformers/resourceTypes'
	}).done(function(data, textStatus, jqXHR) {
		console.log(data);
		if (callbackFunction) {
			callbackFunction(data);
		}
		return data;
	});
}

function initializeResourceTypes(data) {
	console.log('initializeResourceTypes');
	console.log(data);
	resourceTypes = data;
}

function getUserResources(callbackFunction){
	
	console.log("getUserResources");
	//This ajax call returns values only if user is logged in. If not it return empty variable.
	$.ajax({
		type: 'GET',
		url: '/Transformers/userResources'
	}).done(function (data, textStatus, jqXHR) {
		callbackFunction(data);
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.error("Problem with requesting resources. TextStatus: " + textStatus);
	});
}

function updateResources(resources){
	console.log("updateResources");
	for(var i=0; i < resources.length ; i++){
		$('#'+resources[i].typeName+'').text(resources[i].amount);
	}
}

