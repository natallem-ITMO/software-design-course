package ru.akirakozov.sd.mvc.dao;

import ru.akirakozov.sd.mvc.model.TodoList;
import ru.akirakozov.sd.mvc.model.TodoTask;

import java.util.List;
import java.util.Optional;

/**
 * @author akirakozov
 */
public interface TodoDao {
    int addTodoList(TodoList todoList);

    int deleteTodoList(int todoListId);

    int addTodoTask(TodoTask todoTask) ;

    int deleteTodoTask(int todoTaskId) ;

    List<TodoList> getTodoLists();

    int changeStateTodoTask(int taskId);
}
