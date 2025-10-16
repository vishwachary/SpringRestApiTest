package org.examples.springrestapitest.service;

import org.examples.springrestapitest.dto.TaskDto;
import org.examples.springrestapitest.entity.Task;
import org.examples.springrestapitest.exception.TaskNotFoundException;
import org.examples.springrestapitest.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> getAllTasks() {
        List<Task> allTaskEntities = taskRepository.findAll();
        List<TaskDto> taskDtos = new ArrayList<>();
        allTaskEntities.forEach(task -> {
            taskDtos.add(mapToDto(task));
        });
        return taskDtos;
    }
    public TaskDto getTaskById(long id) {
        Optional<Task> taskById= taskRepository.findById(id);
        if(taskById.isPresent()) {
            return mapToDto(taskById.get());
        }else
        {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
    }

    public List<TaskDto> getTaskByName(String searchString) {
        List<Task> allTaskEntitiesByName= taskRepository.findByTaskNameContainingIgnoreCase(searchString);
        List<TaskDto> alltaskDtosByName = new ArrayList<>();
        allTaskEntitiesByName.forEach(task -> {
            alltaskDtosByName.add(mapToDto(task));
        });

        return alltaskDtosByName;
    }

    public TaskDto addTask(TaskDto newTask)
    {
              Task newTaskCreated=taskRepository.save(mapToEntity(newTask));
              return mapToDto(newTaskCreated);
    }
    public void deleteTask(TaskDto deleteTask)
    {
        taskRepository.delete(mapToEntity(deleteTask));
    }


    private static TaskDto mapToDto(Task task) {
        TaskDto build = TaskDto.builder()
                .taskId(task.getId())
                .taskName(task.getTaskName())
                .taskDescription(task.getDescription())
                .isCompleted(task.isCompleted())
                .build();
        return build;

    }

    private static Task mapToEntity(TaskDto taskdto) {

        return new Task(taskdto.getTaskId(),taskdto.getTaskName(),taskdto.getTaskDescription(),taskdto.getIsCompleted());
    }
}
