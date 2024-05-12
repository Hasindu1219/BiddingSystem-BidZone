// Function to extract query parameter value from URL
function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
  }
  
  // Wait for the DOM to fully load
  document.addEventListener('DOMContentLoaded', function() {
      // Retrieve userId from localStorage
      const userId = localStorage.getItem("userid");
      console.log("User ID:", userId);
  
      // Retrieve listingId, listingName, and bidAmount from URL parameters
      const listingId = getQueryParam("listingId");
      const listingName = getQueryParam("listingName");
      const bidAmount = getQueryParam("bidPrice");
  
      // Set userId, listingId, listingName, and bidAmount in the HTML
      document.getElementById("userIdSpan").textContent = userId;
      document.getElementById("listingIdSpan").textContent = listingId;
      document.getElementById("listingNameSpan").textContent = listingName;
      document.getElementById("AmountSpan").textContent = bidAmount;
  });
  


// Function to validate card number
function isValidCardNumber(cardNumber) {
    // Card number validation logic (e.g., Luhn algorithm)
    const trimmedCardNumber = cardNumber.replace(/\s/g, ''); // Remove spaces from card number
    const cardNumberPattern = /^[0-9]{12,19}$/; // Define a pattern for valid card numbers (adjust as needed)

    if (!cardNumberPattern.test(trimmedCardNumber)) {
        return false;
    }

    // Perform Luhn algorithm for card number validation
    // Note: This is a simplified implementation, you may want to use a more robust library for production use
    let sum = 0;
    let doubleUp = false;
    for (let i = trimmedCardNumber.length - 1; i >= 0; i--) {
        let digit = parseInt(trimmedCardNumber.charAt(i), 10);
        if (doubleUp) {
            digit *= 2;
            if (digit > 9) {
                digit -= 9;
            }
        }
        sum += digit;
        doubleUp = !doubleUp;
    }
    return sum % 10 === 0;
}


// Function to validate CVV
function isValidCVV(cvv) {
    // CVV validation logic (e.g., check length)
    const cvvPattern = /^[0-9]{3,4}$/; // Define a pattern for valid CVV (adjust as needed)

    return cvvPattern.test(cvv);
}

function handlePaymentFormSubmit(event) {
    event.preventDefault(); // Prevent the default form submission

    // Retrieve form input values
    const cardNumber = document.getElementById("card-number").value;
    const expiryDate = document.getElementById("expiry-date").value;
    const cvv = document.getElementById("cvv").value;

    // Validate card number and CVV
    if (!isValidCardNumber(cardNumber)) {
        alert("Please enter a valid card number (12-19 digits).");
        return;
    }

    if (!isValidCVV(cvv)) {
        alert("Please enter a valid CVV (3-4 digits).");
        return;
    }

    // Retrieve selected payment method
    let selectedValue;
    const paymentMethodRadios = document.getElementsByName('payment-method');

    for (let i = 0; i < paymentMethodRadios.length; i++) {
        if (paymentMethodRadios[i].checked) {
            selectedValue = paymentMethodRadios[i].value; // Get the value of the checked radio button
            break;
        }
    }

    // Prepare payment data
    const paymentData = {
        listing_Id: parseInt(getQueryParam("listingId")),
        user_Id: parseInt(localStorage.getItem("userid")),
        bid_Id: parseInt(getQueryParam("bidId")),
        paid_time: new Date().toISOString(),
        payment_type: selectedValue,
        price: parseFloat(getQueryParam("bidPrice"))
    };

    console.log("Payment Data:", paymentData);

    // Send payment data to backend using AJAX
    const url = "http://localhost:8080/payment/addpayment";
    const requestOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(paymentData)
    };

    fetch(url, requestOptions)
        .then(response => {
            if (response.ok) {
                alert("Payment processed successfully!");

                // Extract bidId from paymentData
                const bidId = parseInt(getQueryParam("bidId"));
                console.log(bidId);

                // Execute another API to complete the bid
                const completeBidUrl = `http://localhost:8080/bid/completebid/${bidId}`;
                const completeBidOptions = {
                    method: "PUT" // Use PUT method to update bid status
                };

                return fetch(completeBidUrl, completeBidOptions);
            } else {
                throw new Error("Failed to process payment.");
            }
        })
        .then(completeBidResponse => {
            if (completeBidResponse.ok) {
                alert("Bid completed successfully!");
                // Additional actions after completing the bid
                window.location.replace("../User/bidpayments.html");
            } else {
                throw new Error("Failed to complete the bid.");
            }
        })
        .catch(error => {
            console.error("Payment error:", error.message);
            alert("Payment or bid completion failed. Please try again.");
        });
}

// Add event listener to the form for handling submission
document.addEventListener("DOMContentLoaded", function() {
    const paymentForm = document.querySelector(".payment-form");
    paymentForm.addEventListener("submit", handlePaymentFormSubmit);
});
