package br.com.curso.tasks.service.contract;

import br.com.curso.tasks.dto.request.TaskRequestDTO;
import br.com.curso.tasks.entity.Task;

import java.util.List;

public interface TaskService {

    public Task save(TaskRequestDTO taskRequestDTO);
    public Task findById(Long id);
    public List<Task> findAll();
    public Task findByTitle(String title);
    public Task update(Long id, TaskRequestDTO taskRequestDTO);
    public void delete(Long id);
    public List<Task> getTasksByUserId(Long userId) ;
}