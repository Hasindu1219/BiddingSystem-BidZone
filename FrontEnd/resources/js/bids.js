// Function to fetch bid data from API and populate the table
function fetchAndPopulateBidData() {
    // Retrieve userId from localStorage and parse it as an integer
    const userIdStr = localStorage.getItem('userid');
    if (!userIdStr) {
      console.error('User ID not found in localStorage.');
      return; // Exit early if userId is not found
    }
  
    const url = `http://localhost:8080/bid/search-by-user-id/${userIdStr}`;
  
    // Fetch data from the API
    fetch(url)
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        const bidDTOList = data.content;
  
        // Get reference to table body
        const tableBody = document.getElementById('bidTableBody');
        tableBody.innerHTML = ''; // Clear existing table rows
  
        // Populate table rows with bid data
        bidDTOList.forEach(bid => {
          const row = document.createElement('tr');
          row.innerHTML = `
            <td>${bid.id}</td>
            <td>${bid.listingId}</td>
            <td>${formatDate(bid.timestamp)}</td>
            <td>${bid.price}</td>
          `;
          tableBody.appendChild(row);
        });
  
        // Display success message (if needed)
        console.log(data.message);
      })
      .catch(error => {
        console.error('Error fetching and populating bid data:', error);
      });
  }
  
  // Usage: Call this function to fetch and populate bid data for the stored user
  fetchAndPopulateBidData();
  



//function for format date
function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // Adding 1 because month index starts from 0
    const day = ('0' + date.getDate()).slice(-2);
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    const seconds = ('0' + date.getSeconds()).slice(-2);

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}


function navigateToBidPaymentsPage() {
  // Define the URL of the bid payments page
  const bidPaymentsPageUrl = "../HTML/bidpayments.html"; // Update with the actual URL

  // Navigate to the bid payments page
  window.location.href = bidPaymentsPageUrl;
}


function navigateToBidPaymentsUserPage() {
  // Define the URL of the bid payments page
  console.log("User clicked");
  const bidPaymentsPageUrl = "../../HTML/User/bidpayments.html"; // Update with the actual URL

  // Navigate to the bid payments page
  window.location.href = bidPaymentsPageUrl;
}
