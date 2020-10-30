package com.example.sampleTodo.repository;

import com.example.sampleTodo.entity.Task;
import com.example.sampleTodo.entity.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CRUD機能
 */

@Repository
public class TaskDaoImpl implements TaskDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Task> findAll() {    //Listを返すもの　複製を返す

        String sql = "SELECT task.id, user_id, type_id, title, detail, deadline, "
                + "type, comment FROM task "
                + "INNER JOIN task_type ON task.type_id = task_type.id";

        //タスク一覧をMapのListで取得
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);

        //return用の空のListを用意
        List<Task> list = new ArrayList<>();

        //2つのテーブルのデータをTaskにまとめる
        for (Map<String, Object> result : resultList) { //resultがMapの１行ずつ

            Task task = new Task();
            task.setId((int) result.get("id")); //返ってくるのはObjectなので必ずキャスト
            task.setUserId((int) result.get("user_id"));
            task.setTypeId((int) result.get("type_id"));
            task.setTitle((String) result.get("title"));
            task.setDetail((String) result.get("detail"));
            task.setDeadline(((Timestamp) result.get("deadline")).toLocalDateTime()); //Deadlineは戻り値がTimestamp型になるので型変換が必要

            TaskType type = new TaskType();
            type.setId((int) result.get("type_id"));
            type.setType((String) result.get("type"));
            type.setComment((String) result.get("comment"));

            //TaskにTaskTypeをセット
            task.setTaskType(type);

            list.add(task);
        }
        return list;
    }

    @Override
    public Optional<Task> findById(int id) {
        String sql = "SELECT task.id, user_id, type_id, title, detail, deadline, "
                + "type, comment FROM task "
                + "INNER JOIN task_type ON task.type_id = task_type.id "
                + "WHERE task.id = ?";

        //タスクを1件取得
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, id);

        Task task = new Task();
        task.setId((int) result.get("id"));
        task.setUserId((int) result.get("user_id"));
        task.setTypeId((int) result.get("type_id"));
        task.setTitle((String) result.get("title"));
        task.setDetail((String) result.get("detail"));
        task.setDeadline(((Timestamp) result.get("deadline")).toLocalDateTime());

        TaskType type = new TaskType();
        type.setId((int) result.get("type_id"));
        type.setType((String) result.get("type"));
        type.setComment((String) result.get("comment"));
        task.setTaskType(type);

        //taskをOptionalでラップする
        Optional<Task> taskOpt = Optional.ofNullable(task);

        return taskOpt;
    }

    @Override
    public void insert(Task task) {
        jdbcTemplate.update("INSERT INTO task(user_id, type_id, title, detail, deadline) VALUES(?, ?, ?, ?,?)",
                task.getUserId(), task.getTypeId(), task.getTitle(), task.getDetail(), task.getDeadline());
    }

    @Override
    public int update(Task task) {
        return jdbcTemplate.update("UPDATE task SET type_id = ?, title = ?, detail = ?,deadline = ? WHERE id = ?",
                task.getTypeId(), task.getTitle(), task.getDetail(), task.getDeadline(), task.getId());
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM task WHERE id = ?", id);
    }

}


