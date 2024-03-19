const employeeList = document.getElementById("employeeList");
const localVideo = document.getElementById("localVideo");
const remoteVideo = document.getElementById("remoteVideo");

let localStream;
let peerConnection;

async function startVideo() {
  try {
    localStream = await navigator.mediaDevices.getUserMedia({
      video: true,
      audio: true,
    });
    localVideo.srcObject = localStream;
  } catch (error) {
    console.error("Error accessing camera and microphone:", error);
  }
}

async function startVideoCall(userId) {
  try {
    const remoteStream = new MediaStream();
    remoteVideo.srcObject = remoteStream;

    const configuration = {
      iceServers: [
        { urls: "stun:stun.stunprotocol.org" }, // Use STUN server for NAT traversal
      ],
    };
    peerConnection = new RTCPeerConnection(configuration);

    localStream
      .getTracks()
      .forEach((track) => peerConnection.addTrack(track, localStream));

    peerConnection.ontrack = (event) => {
      event.streams[0]
        .getTracks()
        .forEach((track) => remoteStream.addTrack(track));
    };

    const offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);

    // Send offer to the remote user via backend
    const offerData = {
      userId: userId,
      sdp: offer.sdp,
    };
    // Send offerData to backend via WebSocket or HTTP request
  } catch (error) {
    console.error("Error initiating call:", error);
  }
}

// Function to handle incoming call and create answer
async function handleIncomingCall(offerData) {
  try {
    const remoteStream = new MediaStream();
    remoteVideo.srcObject = remoteStream;

    const configuration = {
      iceServers: [
        { urls: "stun:stun.stunprotocol.org" }, // Use STUN server for NAT traversal
      ],
    };
    peerConnection = new RTCPeerConnection(configuration);

    localStream
      .getTracks()
      .forEach((track) => peerConnection.addTrack(track, localStream));

    peerConnection.ontrack = (event) => {
      event.streams[0]
        .getTracks()
        .forEach((track) => remoteStream.addTrack(track));
    };

    await peerConnection.setRemoteDescription({
      type: "offer",
      sdp: offerData.sdp,
    });
    const answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);

    // Send answer to the caller via backend
    const answerData = {
      userId: offerData.userId,
      sdp: answer.sdp,
    };
    // Send answerData to backend via WebSocket or HTTP request
  } catch (error) {
    console.error("Error handling incoming call:", error);
  }
}

// Add event listener for user click to initiate call
employeeList.addEventListener("click", (event) => {
  if (event.target.tagName === "A") {
    const userId = event.target.dataset.userId;
    startVideoCall(userId);
  }
});

// Call startVideo function to initiate local video
startVideo();
