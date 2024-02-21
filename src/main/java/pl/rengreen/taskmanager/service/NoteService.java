package pl.rengreen.taskmanager.service;

import java.util.List;
import java.util.Optional;

import pl.rengreen.taskmanager.model.Note;
import pl.rengreen.taskmanager.model.User;

public interface NoteService {

    List<Note> getAllNotesByUser(User user);

    Optional<Note> getNoteById(Long id);

    Note createNote(Note note);

    Note updateNote(Note note);

    void deleteNoteById(Long id);
}
