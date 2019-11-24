$(document).ready(function(){
	
	$('#polish_language').click(function(){
		console.log("changing language to polish");
		changeLanguage("POLISH");
	});
	
	$('#spanish_language').click(function(){
		console.log("changing language to spanish");
		changeLanguage("SPANISH");
	});
	
	$('#english_language').click(function(){
		console.log("changing language to english");
		changeLanguage("ENGLISH");
	})
});

function changeLanguage(language){
	//TODO error handling
	data = "language=" + language;
	console.log("ajax call data:"+data);
	$.ajax({
		type: 'GET',
		url: '/Transformers/changeLanguage',
		data: data
		}).done(function(data, textStatus, jqXHR) {
			location.reload();
		});
}


