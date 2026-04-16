//package br.com.curso.tasks.service;
//
//import br.com.curso.tasks.TaskManagementApplication;
//import br.com.curso.tasks.entity.Task;
//import br.com.curso.tasks.repository.TaskRepository;
//import br.com.curso.tasks.service.impl.TaskServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//
//@SpringBootTest(classes = TaskManagementApplication.class)
//public class TaskServiceTest {
//
//    @InjectMocks
//    private TaskServiceImpl taskService;
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    Task taskSave = new Task(1L, "AtividadeTeste", "teste", "localteste", LocalDateTime.now());
//    Task taskRequest = new Task(null, "AtividadeTeste", "teste", "localteste", LocalDateTime.now());
//
//    @Test
//    public void saveTaskTestSuccess() {
//        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(taskSave);
//        Task savedTask = taskRepository.save(taskRequest);
//        Assertions.assertNotNull(savedTask);
//        Assertions.assertNotNull(savedTask.getId());
//        Assertions.assertEquals(taskSave.getId(), savedTask.getId());
//    }
//
//
//}
