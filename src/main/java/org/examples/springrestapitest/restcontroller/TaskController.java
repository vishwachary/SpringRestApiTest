package org.examples.springrestapitest.restcontroller;

import org.examples.springrestapitest.dto.TaskDto;
import org.examples.springrestapitest.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService  taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    ResponseEntity<List<TaskDto>> getAllTasks()
    {
        List<TaskDto> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/tasks")
    ResponseEntity<List<TaskDto>> getAllTasksByName(@RequestParam String name)
    {
        List<TaskDto> allTasks = taskService.getTaskByName(name);
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") long taskId) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        return ResponseEntity.ok(taskDto);
    }
}
