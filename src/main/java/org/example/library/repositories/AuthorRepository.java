package org.example.library.repositories;
import org.example.library.models.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
/*Repositories, which interacts with objects in the DB.
Allows the APP to perform. CRUD queries.
*/