package pl.rengreen.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.rengreen.taskmanager.model.Note;
import pl.rengreen.taskmanager.model.User;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("SELECT n FROM Note n WHERE n.note_owner = :user")
    List<Note> findByNoteOwner(@Param("user") User user);
}
