// Function to fetch and display bid data
function fetchAndDisplayBids(userId) {
    // Construct the API URL using the provided userId
    const apiUrl = `http://localhost:8080/bid/highestbidaccordingtouser/${userId}/${getCurrentDateTime()}`;

    // Make an AJAX request to the API
    $.ajax({
        url: apiUrl,
        type: "GET",
        success: function(response) {
            // Check if the API response is successful
            if (response.code === "00") {
                // Clear existing table rows
                $("#bidTableBody").empty();

                // Iterate over the bid items in the response content
                response.content.forEach(function(bid) {
                    // Create a new table row and populate it with bid data
                    const row = `
                        <tr>
                            <td>${bid.bidId}</td>
                            <td>${bid.listingId}</td>
                            <td>${bid.listingName}</td>
                            <td>${bid.listingCategory}</td>
                            <td>${bid.listingDescription}</td>
                            <td>${formatDateTime(bid.listingEnding)}</td>
                            <td>${formatDateTime(bid.bidTimestamp)}</td>
                            <td>${bid.bidPrice}</td>
                            <td><button class="btn btn-primary" onclick="processBid(${bid.bidId}, ${bid.listingId}, ${bid.bidPrice},'${bid.listingName}')">Pay</button></td>
                        </tr>
                    `;

                    // Append the row to the table body
                    $("#bidTableBody").append(row);
                });
            } else {
                console.error("Failed to fetch bid data:", response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error("API request failed:", error);
        }
    });
}

// Function to get the current date and time in the required format
function getCurrentDateTime() {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    const hours = String(currentDate.getHours()).padStart(2, '0');
    const minutes = String(currentDate.getMinutes()).padStart(2, '0');
    const seconds = String(currentDate.getSeconds()).padStart(2, '0');

    // Format the date-time string in the required format for your API
    const currentDateTimeString = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;

    return currentDateTimeString;
}

// Function to format date and time from ISO format to a readable format
function formatDateTime(dateTimeString) {
    const dateTime = new Date(dateTimeString);
    return dateTime.toLocaleString(); // Convert to local date and time format
}

// Function to navigate to the payment page and pass bid data
function processBid(bidId, listingId, bidPrice, listingName) {
    console.log("Processing bid:", bidId, listingId, bidPrice, listingName);

    // Encode the listingName parameter
    const encodedListingName = encodeURIComponent(listingName);

    // Construct the payment page URL with query parameters
    const paymentUrl = `../../HTML/User/paymentgateway.html?bidId=${bidId}&listingId=${listingId}&listingName=${encodedListingName}&bidPrice=${bidPrice}`;

    // Navigate to the payment page
    window.location.href = paymentUrl;
}

// Example usage:
$(document).ready(function() {
    // Retrieve userId from localStorage
    const userId = localStorage.getItem("userid");
    console.log("User ID:", userId);

    // Fetch and display bids for the logged-in user
    fetchAndDisplayBids(userId);
});

function navigateToBidPayments() {
    // Define the URL of the bid payments page
    const bidPaymentsPageUrl = "../HTML/paidhistory.html"; // Update with the actual URL
  
    // Navigate to the bid payments page
    window.location.href = bidPaymentsPageUrl;
  }
  
  
  function navigateToBidPaymentsUser() {
    // Define the URL of the bid payments page
    console.log("User clicked");
    const bidPaymentsPageUrl = "../../HTML/User/paidhistory.html"; // Update with the actual URL
  
    // Navigate to the bid payments page
    window.location.href = bidPaymentsPageUrl;
  }
