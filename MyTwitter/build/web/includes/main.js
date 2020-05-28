

function listenComboBox(question) {

    var answers = document.getElementById('answerDiv');
    if (question.value == "") {
        answers.style.display = "none";
        document.getElementById('answer').required = false;          // set the attribute of answer textfield

    } else {
        answers.style.display = "inline";
        document.getElementById('answer').required = true;          // set the attribute of answer textfield
    }
}


function validateForm()
{

    var answer = document.getElementById('answer');
    var flag = true;

    if (validateFullname() > 0)
        flag = false;

    else if (validateUsername() > 0)
        flag = false;

    else if (validatePassword() > 0)
        flag = false;
    else
        flag = true;

    return flag;

}



function validateFullname() {
    var errorMessage = document.getElementById('errorMsg');
    var fullname = document.getElementById('fullname');
    var errorFullname = document.getElementById('fullName_error');
    var status = 0;

    var invalidLength = 1;
    var invalidInput = 2;

    if (containInvalidCharacter(fullname) == true) {
        showMessage(errorMessage, 'Input has invalid characters');
        fullname.style.background = 'Yellow';
        errorFullname.style.display = "inline";
        status = invalidInput;

    } else if (fullname.value.length <= 1) {
        showMessage(errorMessage, 'Full name is not valid');
        fullname.style.background = 'Yellow';
        errorFullname.style.display = "inline";
        status = invalidLength;
    } else {
        showMessage(errorMessage, ' ');
        errorFullname.style.display = 'none';
        fullname.style.backgroundColor = 'white';

        status = 0;
    }

    return status;
}


function validateUsername() {
    var errorMessage = document.getElementById('errorMsg');
    var username = document.getElementById('username');
    var username_error = document.getElementById('username_error');
    var status = 0;

    var invalidLength = 1;
    var invalidInput = 2;

    if (containInvalidCharacter(username) == true) {
        showMessage(errorMessage, 'Input has invalid characters');
        username.style.background = 'Yellow';
        username_error.style.display = "inline";
        status = invalidInput;

    } else if (username.value.length <= 1) {
        showMessage(errorMessage, 'Username is not valid');
        username.style.background = 'Yellow';
        username_error.style.display = "inline";
        status = invalidLength;
    } else {
        showMessage(errorMessage, ' ');
        username_error.style.display = 'none';
        username.style.backgroundColor = 'white';
        status = 0;
    }

    return status;
}

function validatePassword() {
    var errorMessage = document.getElementById('errorMsg');
    var mismatch = 1;
    var invalidInput = 2;
    var passReq = 3;
    var status = 0;

    var password = document.getElementById('password');
    var passwordError = document.getElementById('password_error');

    var confirmPassword = document.getElementById('confirmpassword');
    var confErrorPassword = document.getElementById('confirmpassword_error');

    if (password.value != confirmPassword.value) {
        showMessage(errorMessage, 'Error! Password and confirm password do not match!');
        confirmPassword.style.backgroundColor = "yellow";
        confErrorPassword.style.display = "inline";
        status = mismatch;

    } else if (containInvalidCharacter(password) == true) {
        showMessage(errorMessage, 'Input has invalid characters');
        password.style.background = 'Yellow';
        passwordError.style.display = "inline";
        status = invalidInput;

    } else if ((password.value.search(/[a-zA-Z]+/) == -1) || (password.value.search(/[0-9]+/) == -1)) {
        showMessage(errorMessage, 'Password must contain a small letter, a capital letter, and a number');
        password.style.background = 'Yellow';
        passwordError.style.display = "inline";
        status = passReq;

    } else {

        showMessage(errorMessage, ' ');
        passwordError.style.display = 'none';
        password.style.backgroundColor = 'white';

        confErrorPassword.style.display = 'none';
        confirmPassword.style.backgroundColor = 'white';
        status = 0;

    }

    return status;

}

function displayError(element, errorElement) {
    element.style.backgroundColor = "yellow";
    errorElement.style.display = "inline";
}

function containInvalidCharacter(element) {
    var contain = false;
    if (element.value.indexOf('1=1') >= 0 || element.value.indexOf("\'") >= 0)
        contain = true;
    return contain;
}

function showMessage(panel, message) {
    panel.style.display = "block";
    panel.innerHTML = message;
}

