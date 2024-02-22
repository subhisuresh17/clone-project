var noteId;

function openModal(button) {
    noteId = button.getAttribute('data-id');
    var title = button.getAttribute('data-title');
    var description = button.getAttribute('data-description');
    var date = button.getAttribute('data-date');

    document.getElementById('modal-title').innerText = title;
    document.getElementById('modal-description').innerText = description;
    document.getElementById('modal-date').innerText = date;
    document.getElementById('noteModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('noteModal').style.display = 'none';
}

function editNote() {
    // Add functionality to edit note here
}

function deleteNote(button) {
    noteId = button.getAttribute('data-id');
var confirmation = confirm("Are you sure you want to delete this note?");
if (confirmation) {
fetch(`/notes/${noteId}/delete`, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': getCsrfToken()
    }
})
.then(response => {
    if (response.ok) {
        // Reload the page or update the UI as needed
        location.reload();
    } else {
        console.error('Error deleting note:', response.statusText);
        // Handle error (e.g., display an error message to the user)
    }
})
.catch(error => {
    console.error('Error deleting note:', error);
    // Handle error (e.g., display an error message to the user)
});
}
}


function getCsrfToken() {
    var csrfTokenElement = document.querySelector("meta[name='_csrf']");
    return csrfTokenElement ? csrfTokenElement.getAttribute("content") : null;
}