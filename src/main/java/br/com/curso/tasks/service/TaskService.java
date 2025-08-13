package br.com.curso.tasks.service;

import br.com.curso.tasks.model.Task;

import java.util.List;

public interface TaskService {

    public Task save(Task task);
    public Task findById(Long id);
    public List<Task> findAll();
    public Task findByTitle(String title);
    public Task update(Long id, Task task);
    public void delete(Long id);
}