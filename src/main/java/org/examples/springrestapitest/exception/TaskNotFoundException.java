package org.examples.springrestapitest.exception;

import org.springframework.http.ProblemDetail;

public class TaskNotFoundException extends RuntimeException{

    // Constructors should be public so Spring (and other code) can instantiate them
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(ProblemDetail problemDetail) {
        super(problemDetail != null ? problemDetail.getDetail() : "Task not found");
    }
}
