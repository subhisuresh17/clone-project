package pl.rengreen.taskmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.rengreen.taskmanager.model.Notes;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.NotesRepository;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    public NoteServiceImpl(NotesRepository notesRepository){
        this.notesRepository=notesRepository;
    }

    @Override
    public void createNotes(Notes note) {
       notesRepository.save(note);
    }

    @Override
    public void updateNote(long id, Notes notes) {
        Notes note= notesRepository.getById(id);
        note.setName(notes.getName());
        note.setDescription(notes.getDescription());
        notesRepository.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        notesRepository.deleteById(id);;
    }

    @Override
    public List<Notes> findAll() {
       return notesRepository.findAll();
    }

    @Override
    public List<Notes> findByOwnerOrderByDateDesc(User user) {
       return notesRepository.findByOwnerOrderByDateDesc(user);
    }

    @Override
    public long countNotes() {
       return notesRepository.count();
    }
    
}
