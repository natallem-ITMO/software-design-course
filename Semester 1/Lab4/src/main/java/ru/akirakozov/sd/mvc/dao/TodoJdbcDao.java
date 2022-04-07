package ru.akirakozov.sd.mvc.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.akirakozov.sd.mvc.model.TodoList;
import ru.akirakozov.sd.mvc.model.TodoTask;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodoJdbcDao extends JdbcDaoSupport implements TodoDao {

    public TodoJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public int changeStateTodoTask(int taskId) {
        String sql = "update todotask set is_done = not is_done where id = ?;";
        return getJdbcTemplate().update(sql, Integer.toString(taskId));
    }

    @Override
    public int addTodoList(TodoList todoList) {
        String sql = "insert into todolist (id, name) values ((select max(id) + 1 from todolist),?)";
        return getJdbcTemplate().update(sql, todoList.getName());
    }

    @Override
    public int addTodoTask(TodoTask todoTask) {
        String sql = "insert into todotask (id, name, list_id, is_done) values ((select max(id) + 1 from todotask),?, ?, False)";
        return getJdbcTemplate().update(sql, todoTask.getName(), todoTask.getList_id());
    }

    @Override
    public int deleteTodoTask(int todoTaskId) {
        String sql = "delete from todotask where id = ?";
        return getJdbcTemplate().update(sql, todoTaskId);
    }

    @Override
    public int deleteTodoList(int todoListId) {
        String sql_del_list = "delete from todolist where id = ?";
        String sql_del_tasks = "delete from todotask where list_id = ?";
        getJdbcTemplate().update(sql_del_tasks, todoListId);
        return getJdbcTemplate().update(sql_del_list, todoListId);
    }

    @Override
    public List<TodoList> getTodoLists() {
        String sql_list = "select * from todolist";
        String sql_task = "select * from todotask";
        List<TodoList> lists = getTodoListsByRequest(sql_list);
        for (TodoList list : lists) {
            list.setTasks(getTodoTasksByRequest("select * from todotask where list_id=" + list.getId()));
        }
        return lists;
    }

    private List<TodoTask> getTodoTasksByRequest(String sql) {
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(TodoTask.class));
    }

    private List<TodoList> getTodoListsByRequest(String sql) {
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(TodoList.class));
    }

}
