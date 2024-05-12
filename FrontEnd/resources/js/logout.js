// JavaScript code for home page functionality
function logout() {
    // Clear localStorage data
    localStorage.removeItem('username');
    localStorage.removeItem('password'); 
    localStorage.removeItem('userid');
    localStorage.removeItem('name');

    // Navigate to the login page
    window.location.href = '../HTML/login.html';
}

function logoutuser() {
    // Clear localStorage data
    localStorage.removeItem('username');
    localStorage.removeItem('password'); 
    localStorage.removeItem('userid');
    localStorage.removeItem('name');

    // Navigate to the login page
    window.location.href = '../../HTML/login.html';
}
