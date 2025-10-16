package org.examples.springrestapitest.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long taskId;
    private String taskName;
    private Boolean isCompleted;
    private String taskDescription;
}
