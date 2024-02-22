package pl.rengreen.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.rengreen.taskmanager.model.Note;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.NoteService;
import pl.rengreen.taskmanager.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class NotesController {
    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    // Controller method to display all notes of the logged-in user
    @GetMapping("/notes")
    public String getAllNotes(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Note> notes = noteService.getAllNotesByUser(user);
        model.addAttribute("notes", notes);
        return "views/notes";
    }

    // Controller method to display the form for creating a new note
    @GetMapping("/notes/create")
    public String showNoteForm(Model model) {
        model.addAttribute("note", new Note());
        return "views/create-notes";
    }

    // Controller method to handle the submission of the new note form
    // Controller method to handle the submission of the new note form
    @PostMapping("/notes/create")
    public String createNote(@ModelAttribute("note") Note note, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        note.setNote_owner(user);
        note.setDate(LocalDate.now()); // Set the current date for the note
        noteService.createNote(note);
        return "redirect:/notes"; // Correct redirection to the notes page

    }

    // Controller method to display a specific note
    @GetMapping("/notes/{id}")
    public String getNoteById(@PathVariable("id") Long id, Model model) {
        Optional<Note> noteOptional = noteService.getNoteById(id);
        if (noteOptional.isPresent()) {
            model.addAttribute("note", noteOptional.get());
            return "note";
        } else {
            return "error"; // or redirect to a different page
        }
    }

   
    // Controller method to delete a note
    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable("id") Long id) {
        noteService.deleteNoteById(id);
        return "redirect:/notes";
    }
}
