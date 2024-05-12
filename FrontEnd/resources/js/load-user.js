//get all user
$(document).ready(function () {
  // Fetch users from the API
  $.ajax({
    url: "http://localhost:8080/user/view",
    type: "GET",
    dataType: "json",
    success: function (response) {
      if (response.code === "00") {
        // If successful, loop through the users and add them to the table
        response.content.forEach(function (user) {
          $("#managementTable").append(
            "<tr>" +
              "<td>" +
              user.id +
              "</td>" +
              "<td>" +
              user.username +
              "</td>" +
              "<td>" +
              user.email +
              "</td>" +
              "<td>" +
              user.name +
              "</td>" +
              "<td>" +
              user.surname +
              "</td>" +
              "<td>" +
              user.age +
              "</td>" +
              "<td>" +
              user.gender +
              "</td>" +
              "<td>" +
              user.likedCategories +
              "</td>" +
              "<td>" +
              '<button type="button" class="btn btn-primary editButton" data-toggle="modal" data-target="#userUpdateModal" data-user-id="' +
              user.id +
              '">Edit</button>' +
              '<button type="button" class="btn btn-primary button-red deleteButton" data-toggle="modal" data-target="#deleteModal" data-user-id="' +
              user.id +
              '">Delete</button>' +
              "</td>" +
              "</tr>"
          );
        });
      } else {
        // Handle error
        console.error("Failed to fetch users: " + response.message);
      }
    },
    error: function (xhr, status, error) {
      // Handle error
      console.error("Failed to fetch users: " + error);
    },
  });

  // Add user form submission
  $("#userForm").submit(function (event) {
    // Prevent default form submission
    event.preventDefault();

    // Gather user data from form
    var userData = {
      username: $("#username").val(),
      password: $("#password").val(),
      email: $("#email").val(),
      name: $("#name").val(),
      surname: $("#surname").val(),
      age: $("#age").val(),
      gender: $("input[name='gender']:checked").val(),
      likedCategories: $("select[name='likedCategory']").val(),
      category:"user"
    };
    console.log(userData);

    // Send user data to the backend
    $.ajax({
      url: "http://localhost:8080/user/add",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(userData),
      success: function (response) {
        // Handle success
        console.log("User added successfully");
        alert("User added successfully");

        // Reset form
        $("#userForm")[0].reset();

        // Reload the page after successful addition
        location.reload();
      },
      error: function (xhr, status, error) {
        // Handle error
        console.error("Failed to add user: " + error);
      },
    });
  });

  $("#userUpdateForm").submit(function (event) {
    // Prevent default form submission
    event.preventDefault();

    // Get the user ID from the data attribute
    var userId = localStorage.getItem("updateUserId");
    console.log("user id" + userId);

    // Gather updated user data from the form
    var userData = {
      id: userId,
      username: $("#usernameEdit").val(),
      password: $("#passwordEdit").val(),
      email: $("#emailEdit").val(),
      name: $("#nameEdit").val(),
      surname: $("#surnameEdit").val(),
      age: $("#ageEdit").val(),
      gender: $("input[name='gender']:checked").val(),
      likedCategories: $("select[name='likedCategoryEdit']").val(),
      category:"user",
    };
    console.log(userData);

    // Send updated user data to the backend API using AJAX
    $.ajax({
      url: "http://localhost:8080/user/update",
      type: "PUT",
      contentType: "application/json",
      data: JSON.stringify(userData),
      success: function (response) {
        // Handle success response from the API
        console.log("User details updated successfully:", response);

        // Optionally, close the modal or perform other actions
        $("#userUpdateModal").modal("hide");

        // Clear the form fields
        $("#usernameEdit").val("");
        $("#passwordEdit").val("");
        $("#emailEdit").val("");
        $("#nameEdit").val("");
        $("#surnameEdit").val("");
        $("#ageEdit").val("");
        $("input[name='gender']").prop("checked", false);
        $("select[name='likedCategoryEdit']").val("");

        // Refresh the page or perform other actions after successful update
        location.reload();
      },
      error: function (xhr, status, error) {
        // Handle error response from the API
        console.error("Failed to update user details:", error);

        // Optionally, display an error message to the user
        // Example: alert("Failed to update user details. Please try again.");
      },
    });
  });
});
