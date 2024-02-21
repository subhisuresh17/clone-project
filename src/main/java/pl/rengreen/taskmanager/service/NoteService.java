package pl.rengreen.taskmanager.service;

import java.util.List;

import pl.rengreen.taskmanager.model.Notes;
import pl.rengreen.taskmanager.model.User;

public interface NoteService {
    void createNotes(Notes note);

    void updateNote(long id, Notes notes);

    void deleteNote(Long id);

    List<Notes> findAll();

    List<Notes> findByOwnerOrderByDateDesc(User user);

    long countNotes();
}
