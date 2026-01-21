package br.com.curso.tasks.repository;

import br.com.curso.tasks.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByName(String name);
    public Optional<User> findById(Long id);
    public Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = "tasksCreated")
    Optional<User> findWithTasksCreatedById(Long id);
}