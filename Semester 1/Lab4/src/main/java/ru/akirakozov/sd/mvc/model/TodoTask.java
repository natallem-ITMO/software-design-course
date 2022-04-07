package ru.akirakozov.sd.mvc.model;

public class TodoTask {
    private int id;
    private String name;
    private int list_id;
    private boolean is_done = false;

    public TodoTask() {
    }

    public TodoTask(int id, String name, int list_id, boolean is_done) {
        this.id = id;
        this.name = name;
        this.list_id = list_id;
        this.is_done = is_done;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public boolean isDone() {
        return is_done;
    }

    public int getList_id() {
        return list_id;
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

}
