$(document).ready(function () {
  //Only needed for the filename of export files.
  //Normally set in the title tag of your page.
  document.title = "Simple DataTable";
  // DataTable initialisation
  $("#example").DataTable({
    dom: '<"dt-buttons"Bf><"clear">lirtp',
    paging: false,
    autoWidth: true,
    columnDefs: [{ orderable: false, targets: 5 }],
    buttons: [
      "colvis",
      "copyHtml5",
      "csvHtml5",
      "excelHtml5",
      "pdfHtml5",
      "print",
    ],
  });
  //Add row button
  $(".dt-add").each(function () {
    $(this).on("click", function (evt) {
      //Create some data and insert it
      var rowData = [];
      var table = $("#example").DataTable();
      //Store next row number in array
      var info = table.page.info();
      rowData.push(info.recordsTotal + 1);
      //Some description
      rowData.push("New Order");
      //Random date
      var date1 = new Date(2016, 01, 01);
      var date2 = new Date(2018, 12, 31);
      var rndDate = new Date(+date1 + Math.random() * (date2 - date1)); //.toLocaleDateString();
      rowData.push(
        rndDate.getFullYear() +
          "/" +
          (rndDate.getMonth() + 1) +
          "/" +
          rndDate.getDate()
      );
      //Status column
      rowData.push("NEW");
      //Amount column
      rowData.push(Math.floor(Math.random() * 2000) + 1);
      //Inserting the buttons ???
      rowData.push(
        '<button type="button" class="btn btn-primary btn-xs dt-edit" style="margin-right:16px;"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button><button type="button" class="btn btn-danger btn-xs dt-delete"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>'
      );
      //Looping over columns is possible
      //var colCount = table.columns()[0].length;
      //for(var i=0; i < colCount; i++){			}

      //INSERT THE ROW
      table.row.add(rowData).draw(false);
      //REMOVE EDIT AND DELETE EVENTS FROM ALL BUTTONS
      $(".dt-edit").off("click");
      $(".dt-delete").off("click");
      //CREATE NEW CLICK EVENTS
      $(".dt-edit").each(function () {
        $(this).on("click", function (evt) {
          $this = $(this);
          var dtRow = $this.parents("tr");
          $("div.modal-body").innerHTML = "";
          $("div.modal-body").append(
            "Row index: " + dtRow[0].rowIndex + "<br/>"
          );
          $("div.modal-body").append(
            "Number of columns: " + dtRow[0].cells.length + "<br/>"
          );
          for (var i = 0; i < dtRow[0].cells.length; i++) {
            $("div.modal-body").append(
              "Cell (column, row) " +
                dtRow[0].cells[i]._DT_CellIndex.column +
                ", " +
                dtRow[0].cells[i]._DT_CellIndex.row +
                " => innerHTML : " +
                dtRow[0].cells[i].innerHTML +
                "<br/>"
            );
          }
          $("#myModal").modal("show");
        });
      });
      $(".dt-delete").each(function () {
        $(this).on("click", function (evt) {
          $this = $(this);
          var dtRow = $this.parents("tr");
          if (confirm("Are you sure to delete this row?")) {
            var table = $("#example").DataTable();
            table
              .row(dtRow[0].rowIndex - 1)
              .remove()
              .draw(false);
          }
        });
      });
    });
  });
  //Edit row buttons
  $(".dt-edit").each(function () {
    $(this).on("click", function (evt) {
      $this = $(this);
      var dtRow = $this.parents("tr");
      $("div.modal-body").innerHTML = "";
      $("div.modal-body").append("Row index: " + dtRow[0].rowIndex + "<br/>");
      $("div.modal-body").append(
        "Number of columns: " + dtRow[0].cells.length + "<br/>"
      );
      for (var i = 0; i < dtRow[0].cells.length; i++) {
        $("div.modal-body").append(
          "Cell (column, row) " +
            dtRow[0].cells[i]._DT_CellIndex.column +
            ", " +
            dtRow[0].cells[i]._DT_CellIndex.row +
            " => innerHTML : " +
            dtRow[0].cells[i].innerHTML +
            "<br/>"
        );
      }
      $("#myModal").modal("show");
    });
  });
  //Delete buttons
  $(".dt-delete").each(function () {
    $(this).on("click", function (evt) {
      $this = $(this);
      var dtRow = $this.parents("tr");
      if (confirm("Are you sure to delete this row?")) {
        var table = $("#example").DataTable();
        table
          .row(dtRow[0].rowIndex - 1)
          .remove()
          .draw(false);
      }
    });
  });
  $("#myModal").on("hidden.bs.modal", function (evt) {
    $(".modal .modal-body").empty();
  });
});

function openModal() {
  var modal = document.getElementById("avatarModal");
  modal.style.display = "block";
}

// Function to close the modal
function closeModal() {
  var modal = document.getElementById("avatarModal");
  modal.style.display = "none";
}
const cloudinary = cloudinary.Cloudinary.config({
  cloudName: "dgmaiid9c",
  apiKey: "214859887372126",
  apiSecret: "htsY9BPAEB_DPjDr8zg7tK91Xcs",
});

var selectedAvatarUrl = "";

// Function to select an avatar
function selectAvatar(avatarUrl) {
  // Update the selectedAvatarUrl variable
  selectedAvatarUrl = avatarUrl;
  var images = document.getElementsByClassName("avatar");
  // Remove 'active' class from all images
  for (var i = 0; i < images.length; i++) {
    images[i].classList.remove("active");
  }
  event.target.classList.add("active");
  console.log(selectedAvatarUrl);
}
function updateProfilePic(picUrl) {
  var csrfToken = $("meta[name='_csrf']").attr("content");
  var csrfHeader = $("meta[name='_csrf_header']").attr("content");
  $.ajax({
    url: "/profile/profilePicUpdate",
    type: "POST",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(csrfHeader, csrfToken);
    },
    data: JSON.stringify(picUrl),
    success: function (response) {
      console.log(response);
      // Handle success, such as displaying a success message
    },
    error: function (xhr, status, error) {
      console.error(xhr.responseText);
      // Handle error, such as displaying an error message
    },
  });
}

function handleFileUpload(event) {
  const file = event.target.files[0];
  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", "your_upload_preset"); // Replace with your Cloudinary preset

  cloudinary.uploader.upload(formData, (error, result) => {
    if (error) {
      console.error(error);
      // Handle upload error
      return;
    }

    // Upload successful, get the image URL from the result
    const imageUrl = result.secure_url;
    selectedAvatarUrl = imageUrl;

    // Update the profile picture preview with the uploaded image URL
    document.getElementById("profilePic").src = imageUrl;
  });
}

function confirmAvatar() {
  var modal = document.getElementById("avatarModal");
  modal.style.display = "none";

  // Check if an avatar is selected
  if (selectedAvatarUrl) {
    // Update the profile picture with the selected avatar URL
    document.getElementById("profilePic").src = selectedAvatarUrl;

    // Call the function to update profile picture in the database
    updateProfilePic(selectedAvatarUrl);

    // Reset the selectedAvatarUrl variable
    selectedAvatarUrl = "";

    // Here you can send the selected avatar URL to your backend to update the user's profile picture in the database
    // For now, let's just log the selected avatar URL
    console.log("Selected profile picture:", selectedAvatarUrl);
    // window.location.reload();
  } else {
    console.log("No avatar selected.");
  }
}

function showModal(button) {
  var taskId = button.getAttribute("data-taskid");
  console.log(taskId);
  // AJAX call to fetch all tasks
  $.ajax({
    url: "/calendartasks", // Update the URL to match your endpoint
    type: "GET",
    success: function (tasks) {
      // Filter the tasks based on the provided task ID
      var task = tasks.find((t) => t.id == taskId);
      if (task) {
        // Populate modal with task details
        $("#taskTitle").text(task.name);
        $("#taskDescription").text(task.description);
        $("#taskDate").text(task.date);
        $("#taskCreator").text(task.creatorName);
        $("#taskCompleted").text(task.completed ? "Completed" : "In Progress");

        // Show the modal
        $("#taskModal").modal("show");
      } else {
        alert("Task not found");
      }
    },
    error: function () {
      alert("Failed to fetch tasks");
    },
  });
}

// Replace these values with your Cloudinary credentials
const cloudName = "dgmaiid9c";
const apiKey = "214859887372126";
const apiSecret = "htsY9BPAEB_DPjDr8zg7tK91Xcs";
const uploadPreset = "ykzpka7w";

cloudinary.Cloudinary.config({
  cloud_name: cloudName,
  api_key: apiKey,
  api_secret: apiSecret,
});

function uploadImage() {
  const fileInput = document.getElementById("fileInput");
  const file = fileInput.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", uploadPreset);

  fetch(`https://api.cloudinary.com/v1_1/${cloudName}/image/upload`, {
    method: "POST",
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      const imageUrl = data.secure_url;
      const previewImage = document.getElementById("previewImage");
      previewImage.src = imageUrl;
      previewImage.style.display = "block";
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
