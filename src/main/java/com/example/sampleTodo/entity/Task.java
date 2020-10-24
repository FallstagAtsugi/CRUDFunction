package com.example.sampleTodo.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Task {

    private int id;
    private int userId;
    private int typeId;
    private TaskType taskType;
    private String title;
    private String detail;
    private LocalDateTime deadline;
}
