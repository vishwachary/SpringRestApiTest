package org.examples.springrestapitest.restcontroller;

import org.examples.springrestapitest.dto.TaskDto;
import org.examples.springrestapitest.entity.Task;
import org.examples.springrestapitest.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private AutoCloseable closeable;
    private List<TaskDto> mockTasks;
    private Task newTask;

   // @InjectMocks
  //  private TaskController taskController;

    @BeforeEach
    void setUp() {
        // This returns an AutoCloseable that we can close later
        closeable = MockitoAnnotations.openMocks(this);

        // Set up shared mock data
        newTask = new Task(3L, "Watch Water usage", "fix any plumbing issues", true);
        mockTasks = new ArrayList<>();
        mockTasks.add(new TaskDto(1L, "call electrician", false, "fix switch board"));
        mockTasks.add(new TaskDto(2L, "call bank", true, "check FD interest"));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(mockTasks);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].taskName").value("call electrician"))
                .andExpect(jsonPath("$[1].taskName").value("call bank"));
    }

    @Test
    void getAllTasksByName() {
    }

    @Test
    void getTaskById() {
    }
}