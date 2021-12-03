package ru.akirakozov.sd.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akirakozov.sd.mvc.dao.TodoDao;
import ru.akirakozov.sd.mvc.model.TodoList;
import ru.akirakozov.sd.mvc.model.TodoTask;

import java.util.List;

/**
 * @author akirakozov
 */
@Controller
public class TodoListController {
    private final TodoDao todoDao;

    public TodoListController(TodoDao todoListDao) {
        this.todoDao = todoListDao;
    }

    @RequestMapping(value = "/add-todoList", method = RequestMethod.POST)
    public String addQuestion(@ModelAttribute("todoList") TodoList todoList) {
        todoDao.addTodoList(todoList);
        return "redirect:/get-todoLists";
    }

    @RequestMapping(value = "/add-todoTask", method = RequestMethod.POST)
    public String addQuestion(@ModelAttribute("todoTask") TodoTask todoTask) {
        todoDao.addTodoTask(todoTask);
        return "redirect:/get-todoLists";
    }

    @RequestMapping(value = "/get-todoLists", method = RequestMethod.GET)
    public String getTodoLists(ModelMap map) {
        prepareModelMap(map, todoDao.getTodoLists());
        return "index";
    }

    @RequestMapping(value = "/delete-todoList", method = RequestMethod.GET)
    public String deleteTodoList(@RequestParam String todoListId, ModelMap map) {
        todoDao.deleteTodoList(Integer.parseInt(todoListId));
        prepareModelMap(map, todoDao.getTodoLists());
        return "index";
    }

    @RequestMapping(value = "/delete-todoTask", method = RequestMethod.GET)
    public String deleteTodoTask(@RequestParam String todoTaskId, ModelMap map) {
        todoDao.deleteTodoTask(Integer.parseInt(todoTaskId));
        prepareModelMap(map, todoDao.getTodoLists());
        return "index";
    }

    @RequestMapping(value = "/change-state-todoTask", method = RequestMethod.GET)
    public String changeStateTodoTask(@RequestParam String todoTaskId, ModelMap map) {
        todoDao.changeStateTodoTask(Integer.parseInt(todoTaskId));
        prepareModelMap(map, todoDao.getTodoLists());
        return "index";
    }

    private void prepareModelMap(ModelMap map, List<TodoList> todoLists) {
        map.addAttribute("todoLists", todoLists);
        map.addAttribute("todoList", new TodoList());
        map.addAttribute("todoTask", new TodoTask());
    }
}
