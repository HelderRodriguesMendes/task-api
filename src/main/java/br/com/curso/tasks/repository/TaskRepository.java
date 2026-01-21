package br.com.curso.tasks.repository;

import br.com.curso.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);
    Optional<Task> findByTitle(String title);
    Optional<List<Task>> findByCreatedId_Id(Long id);
}