$(document).ready(function() {
    // Listen for form submission
    $('form.signup-modal').submit(function(e) {
      e.preventDefault(); // Prevent default form submission
  
      // Serialize form data into JSON object
      var formData = {};
      $(this).serializeArray().forEach(function(item) {
        formData[item.name] = item.value;
      });
      formData["category"] = "user";
  
      // Make POST request to the API
      $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/user/add',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
          console.log(response);
          alert('User registered successfully!');
          window.location.replace('../HTML/login.html'); // Redirect to login page
        },
        error: function(xhr, status, error) {
          console.log(xhr.responseJSON);
          alert('Failed to register user. Please try again.');
          console.error(error);
          window.location.replace('../HTML/signup.html'); // Redirect to signup page on error
        }
      });
    });
  });
  

  function closeModal() {
    window.location.replace('../HTML/login.html'); // Redirect to login page
  }