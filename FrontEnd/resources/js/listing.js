const listingId = parseInt(localStorage.getItem('listingId'));
const userId = parseInt(localStorage.getItem('userid'));

console.log(typeof(listingId)+" listingId: "+listingId);
console.log(typeof(userId)+" userId: "+userId);

var listedPrice, highestBid;

fetchListingData(listingId);

async function fetchListingData(listingId) {
    try {
        const response = await fetch(`http://localhost:8080/listing/search-by-id/${listingId}`);
        if (!response.ok) {
            console.error(`Failed to fetch listing data: ${response.status}`);
            return;
        }
        const listing = await response.json();
        listedPrice = listing.content.price;
        highestBid = listedPrice;

        console.log(typeof(listedPrice)+ " listed Price: " + listedPrice);

        document.getElementById('item-image').src = `file:${listing.content.image}`;
        document.getElementById('item-name').innerHTML = listing.content.name;
        document.getElementById('category-details').innerHTML = listing.content.category;
        document.getElementById('description-details').innerHTML = listing.content.description;
        document.getElementById('ending-datetime').innerHTML = formatDate(listing.content.ending);
        document.getElementById('listed-price').innerHTML = listing.content.price;

        // Passing time-remain element to updateTimeRemaining function
        updateTimeRemaining(listing.content.ending, document.getElementById('time-remain'));

        // Fetch bid data after listing data is fetched
        await fetchBidData(listingId);
    } catch (error) {
        console.error('Error fetching listing data:', error);
    }
}

async function fetchBidData(listId) {
    try {
        const response = await fetch(`http://localhost:8080/bid/search-by-list-id/${listId}`);
        if (!response.ok) {
            console.error(`Failed to fetch bid data: ${response.status}`);
            return;
        }
        const data = await response.json();
        const bids = data.content;
        const parentContainer = document.getElementById('bids-for-item');
        // Clear previous bid data
        parentContainer.innerHTML = "";
        if (Array.isArray(bids) && bids.length > 0) {
            bids.forEach(bid => {
                const bidElement = document.createElement('li');
                bidElement.classList.add('list-group-item');
                bidElement.textContent = bid.price;
                parentContainer.appendChild(bidElement);
                // Update highest bid if the new bid is higher
                if (bid.price >= highestBid) {
                    highestBid = bid.price;
                }
            });
        } else {
            document.getElementById('bids-for-item').textContent = "No Bids";
        }
        
        document.getElementById('your-bid').setAttribute('min', parseInt(highestBid) + 1);
        document.getElementById('your-bid').setAttribute('placeholder', parseInt(highestBid) + 1);
        console.log(typeof(highestBid) + " Highest Price: " + highestBid);

        // After updating highest bid, call placeBid
        placeBid();
    } catch (error) {
        console.error("Error fetching bids:", error);
    }
}

// Function to send bid to the backend
async function placeBid() {
    // Get bid price from input field
    const bidPrice = document.getElementById('your-bid').value;
    if (bidPrice > highestBid) {
        // Get current timestamp
        const now = new Date();
        const year = now.getFullYear();
        const month = ('0' + (now.getMonth() + 1)).slice(-2); // Adding 1 because month index starts from 0
        const day = ('0' + now.getDate()).slice(-2);
        const hours = ('0' + now.getHours()).slice(-2);
        const minutes = ('0' + now.getMinutes()).slice(-2);
        const seconds = ('0' + now.getSeconds()).slice(-2);
        const timestamp = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;

        // Create JSON object
        const bidData = {
            "listingId": listingId,
            "userId": userId,
            "timestamp": timestamp,
            "price": parseFloat(bidPrice),
            "status":"pending"
        };

        console.log(bidData);

        // Send bid data to backend
        fetch('http://localhost:8080/bid/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bidData)
        })
            .then(response => response.json())
            .then(data => { console.log('Bid placed:', data);
            location.reload();
            }
            ).catch(error => console.error('Error placing bid:', error));
    } else {

    }
}

// Event listener for bid submission
//document.getElementById('btn-place-bid').addEventListener('click', function () { placeBid() });

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

// Function to calculate and update remaining time
function updateTimeRemaining(endTimeString, timeRemainElement) {
    // Creating a Date object from the extracted string
    var endTime = new Date(endTimeString);

    var updateInterval = setInterval(function () {
        var now = new Date();
        var timeDiff = endTime - now;

        if (timeDiff <= 0) {
            timeRemainElement.innerHTML = "Expired"; // Change text if time is expired
            clearInterval(updateInterval); // Stop updating time when expired
            // document.querySelector('.btn-view').disabled = true;
        } else {
            var days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
            var hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

            timeRemainElement.innerHTML = "Time remaining: " + days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        }
    }, 1000);
}
