package org.examples.springrestapitest.service;

import org.examples.springrestapitest.dto.TaskDto;
import org.examples.springrestapitest.entity.Task;
import org.examples.springrestapitest.exception.TaskNotFoundException;
import org.examples.springrestapitest.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private AutoCloseable closeable;
    private List<Task> mockTasks;
    private Task newTask;

    @BeforeEach
    void setUp() {
        // This returns an AutoCloseable that we can close later
        closeable = MockitoAnnotations.openMocks(this);

        // Set up shared mock data
        newTask = new Task(3L, "Watch Water usage", "fix any plumbing issues", true);
        mockTasks = new ArrayList<>();
        mockTasks.add(new Task(1L, "call electrician", "fix switch board", false));
        mockTasks.add(new Task(2L, "call bank", "check FD interest", false));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Closes mocks and cleans up resources
        closeable.close();
    }

    @Test
    void testgetAllTasks() {
        when(taskRepository.findAll()).thenReturn(mockTasks);
        List<TaskDto> tasksFromServiceCall = taskService.getAllTasks();
        assertEquals(2, tasksFromServiceCall.size());
        assertEquals("call electrician", tasksFromServiceCall.get(0).getTaskName());

        assertThat(tasksFromServiceCall)
                .hasSize(2)
                .extracting(TaskDto::getTaskName)
                .containsExactly("call electrician", "call bank");

    }

    @Test
    void getTaskByName() {

        when(taskRepository.findByTaskNameContainingIgnoreCase("call")).thenReturn(mockTasks);
        List<TaskDto> tasksFromServiceCall = taskService.getTaskByName("call");
        assertEquals(2, tasksFromServiceCall.size());
        assertThat(tasksFromServiceCall).hasSize(2).extracting(TaskDto::getTaskName)
                .allMatch(name -> name.toLowerCase().contains("call"));
    }
    @Test
    void getTaskById() {
        when(taskRepository.findById(2L)).thenReturn(Optional.of(mockTasks.get(1)));
        TaskDto taskById = taskService.getTaskById(2L);
        assertThat(taskById.getTaskName()).isEqualTo("call bank");
        assertThat(taskById.getTaskDescription()).isEqualTo("check FD interest");
    }




    @Test
    void addTask() {
// Arrange

        TaskDto newTaskDto = TaskDto.builder()
                .taskId(3L)
                .taskName("Watch Water usage")
                .taskDescription("fix any plumbing issues")
                .isCompleted(true)
                .build();
        // Mock repository.save to return the entity (matches any Task passed)
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // Act: call service with DTO
        TaskDto savedDto = taskService.addTask(newTaskDto);

        // Assert: service returns DTO with correct values
        assertThat(savedDto.getTaskId()).isEqualTo(3L);
        assertThat(savedDto.getTaskName()).isEqualTo("Watch Water usage");
        assertThat(savedDto.getTaskDescription()).isEqualTo("fix any plumbing issues");
        assertThat(savedDto.getIsCompleted()).isTrue();

    }

    @Test
    void deleteTask_shouldCallRepositoryWithConvertedEntity() {
        // Arrange: create DTO to delete
        TaskDto deleteTaskDto = TaskDto.builder()
                .taskId(1L)
                .taskName("call electrician")
                .taskDescription("fix switch board")
                .isCompleted(false)
                .build();

        // Act: call service
        taskService.deleteTask(deleteTaskDto);

        // Assert: verify repository.delete was called with an entity matching the DTO
        verify(taskRepository, times(1)).delete(argThat(task ->
                task.getId().equals(deleteTaskDto.getTaskId()) &&
                        task.getTaskName().equals(deleteTaskDto.getTaskName()) &&
                        task.getDescription().equals(deleteTaskDto.getTaskDescription()) &&
                        task.isCompleted() == deleteTaskDto.getIsCompleted()
        ));
    }

    @Test
    void getTaskById_shouldThrowException_whenTaskNotFound() {
        // given
        long id = 99L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(id));

        // verify repository was called
        verify(taskRepository, times(1)).findById(id);
    }
}
