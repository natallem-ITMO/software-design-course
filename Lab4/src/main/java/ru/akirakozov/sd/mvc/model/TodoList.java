package ru.akirakozov.sd.mvc.model;

import java.util.List;

public class TodoList {
    private int id;
    private String name;
    private List<TodoTask> tasks;

    public TodoList() {
    }

    public TodoList(int id, String name, int price) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TodoTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<TodoTask> tasks) {
        this.tasks = tasks;
    }
}
