let stompClient = null;
let recipientId;
let senderId;
let senderName;
let recipientName;
// Connect to WebSocket endpoint
function connect() {
  const socket = new SockJS("/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log("Connected: " + frame);
    // Subscribe to the topic to receive messages
    stompClient.subscribe("/topic/messages", function (message) {
      handleIncomingMessage(JSON.parse(message.body));
    });
  });
}
// Function to display message in the chat history
// Function to display message in the chat history
function showMessage(message) {
  const chatHistoryElement = document.getElementById("chatHistory");
  const messageElement = document.createElement("div");
  messageElement.classList.add("chat-message"); // Add the chat message class
  let senderId = message.sender;

  fetch(`/chat/senderName/${senderId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.text(); // Since the response is a plain string, use response.text() to read it
    })
    .then((data) => {
      const senderName = data;
      const messageContent = message.content;
      const formattedTimestamp = new Date(message.timestamp).toLocaleString(); // Format timestamp
      messageElement.innerHTML = `<strong>${senderName} (${formattedTimestamp}):</strong> ${messageContent}`;
      chatHistoryElement.appendChild(messageElement);
    })
    .catch((error) => {
      console.error("Error fetching sender name:", error);
    });
}

// Function to handle incoming messages received from WebSocket
function handleIncomingMessage(message) {
  showMessage(message);
}

// Function to send a message
function sendMessage() {
  const messageContent = document.getElementById("messageInput").value;

  // Create the chat message object
  const chatMessage = {
    sender: senderId, // Set the sender's ID here
    recipient: recipientId,
    content: messageContent,
  };

  // Send the message to the server using WebSocket
  stompClient.send(
    "/app/sendMessage",
    {},
    JSON.stringify(chatMessage),
    function () {
      // Message sent successfully, show it in the chat history
      showMessage(chatMessage);

      // Clear the message input field after sending
      document.getElementById("messageInput").value = "";
    }
  );
}
// Function to fetch chat history for a specific user
// Function to fetch chat history for a specific user
function fetchChatHistory(userId, loggedId, sName, rName) {
  // Make an AJAX request to fetch chat history for the specified user
  recipientId = userId;
  senderId = loggedId;
  senderName = sName;
  recipientName = rName;
  fetch(`/chat/messages/${recipientId}`)
    .then((response) => response.json())
    .then((data) => {
      const senderMessages = data.Sender;
      const recipientMessages = data.Recipient;

      const chatHistoryElement = document.getElementById("chatHistory");
      chatHistoryElement.innerHTML = ""; // Clear existing chat history

      // Combine sender and recipient messages
      const allMessages = senderMessages.concat(recipientMessages);

      // Sort messages by timestamp
      allMessages.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

      // Append sorted messages
      allMessages.forEach((message) => {
        const senderName = message.sender;
        const messageContent = message.content;
        const formattedTimestamp = new Date(message.timestamp).toLocaleString(); // Format timestamp

        // Create message element
        const messageElement = document.createElement("div");
        messageElement.classList.add("chat-message"); // Add the chat message class
        messageElement.innerHTML = `<strong>${senderName} (${formattedTimestamp}):</strong> ${messageContent}`;
        chatHistoryElement.appendChild(messageElement);
      });
    })
    .catch((error) => {
      console.error("Error fetching chat history:", error);
    });
}

// Connect to WebSocket when the page loads
document.addEventListener("DOMContentLoaded", () => {
  connect();
});
