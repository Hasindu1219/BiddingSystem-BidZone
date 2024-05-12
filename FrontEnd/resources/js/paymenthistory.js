// Ensure the DOM content is fully loaded before executing JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Function to fetch and populate the payment table
    function populateTable() {
      // Retrieve userid from localStorage
      const userid = localStorage.getItem('userid');
  
      // API endpoint URL with userid parameter
      const apiUrl = `http://localhost:8080/payment/paymentsearch/${userid}`;
  
      // Fetch data from API
      fetch(apiUrl)
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          // Check if data contains content array
          if (data && data.content && Array.isArray(data.content)) {
            const paymentTableBody = document.querySelector('#paymentBody');
            paymentTableBody.innerHTML = ''; // Clear existing table rows
  
            // Loop through each payment object in the content array
            data.content.forEach(payment => {
              const row = paymentTableBody.insertRow();
              // Populate row cells with payment data
              row.innerHTML = `
                <td>${payment.id}</td>
                <td>${payment.listing_Id}</td>
                <td>${payment.bid_Id}</td>
                <td>${new Date(payment.paid_time).toLocaleString()}</td>
                <td>${payment.payment_type}</td>
                <td>${payment.price}</td>
              `;
            });
          } else {
            console.error('Invalid data format');
          }
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    }
  
    // Call populateTable function when the page is fully loaded
    populateTable();
  });
  