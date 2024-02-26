// Listen for page load events
document.addEventListener("DOMContentLoaded", function () {
  // Show the loading animation when the DOM content is loaded
  showLoadingAnimation();
});

window.addEventListener("load", function () {
  // Hide the loading animation when all resources have finished loading
  hideLoadingAnimation();
});

// Function to show the loading animation
function showLoadingAnimation() {
  var loadingAnimation = document.getElementById("loadingAnimation");
  if (loadingAnimation) {
    loadingAnimation.style.display = "block";
  }
}

// Function to hide the loading animation
function hideLoadingAnimation() {
  var loadingAnimation = document.getElementById("loadingAnimation");
  if (loadingAnimation) {
    loadingAnimation.style.display = "none";
  }
}
