package org.examples.springrestapitest.repository;

import org.examples.springrestapitest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long>
{
    List<Task> findByTaskNameContainingIgnoreCase(String keyword);
}
