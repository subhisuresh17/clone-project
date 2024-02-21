package pl.rengreen.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.Notes;
import pl.rengreen.taskmanager.model.User;

public interface NotesRepository extends JpaRepository<Notes,Long>{
        List<Notes> findByOwnerOrderByDateDesc(User user);
}
