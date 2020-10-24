package com.example.sampleTodo.service;


import com.example.sampleTodo.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> findAll();

    Optional<Task> getTask(int id); //IDに紐づけた一覧を返す

    void insert(Task task);

    void update(Task task);

    void deleteById(int id);
}
