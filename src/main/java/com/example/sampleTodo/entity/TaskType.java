package com.example.sampleTodo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskType {
    private int id;
    private String type;
    private String comment;
}
