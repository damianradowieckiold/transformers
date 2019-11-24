$(document).ready(function(){
	
	$('#registerButtonId').click(function(){
		
		var login = $('#loginInputId').val();
		var password = $('#passwordInputId').val();
		var confirmPassword = $('#confirmPasswordId').val();
		
		return validateRegistrationForm(login, password, confirmPassword);
	});
	
	
});

function validateRegistrationForm(login, password, confirmPassword){
	var loginRegex = '[A-Za-z0-9_]{3,16}';
	var passwordRegex = '[A-Za-z0-9!@#\$%\^&\*]{6,20}';
	
	var result = login.match(loginRegex);
	if(result == null || (login!=result)){
		$('#invalidLogin').text('Incorrect login name.');
		return false;
	}
	else{
		$('#invalidLogin').html('&nbsp;');
	}
	
	var result = password.match(passwordRegex);
	if(result == null || (password!=result)){
		$('#invalidPassword').text('Invalid password');
		return false;
	}
	else{
		$('#invalidPassword').html('&nbsp;');
	}
	
	if(password != confirmPassword){
		$('#invalidConfirmPassword').text('Passwords are not the same.');
		return false;
	}
	else{
		$('#invalidConfirmPassword').html('&nbsp;');
	}
	
	return true;
}