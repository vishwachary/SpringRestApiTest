package org.examples.springrestapitest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@Setter
@Entity
@Table(name = "TASK")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TASK_NAME", nullable = false, unique = true)
    private String taskName;

    @Column(name = "DESCRIPTION ", nullable = false)
    private String description ;

    @Lob
    @Column(name = "IS_COMPLETED",nullable = false)
    private boolean isCompleted;
}
