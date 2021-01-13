package ru.otus.diplom.repositories.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.otus.diplom.enums.Departament;
import ru.otus.diplom.exceptions.EntetyException;
import ru.otus.diplom.executor.DBExecutor;
import ru.otus.diplom.executor.DBExecutorImpl;
import ru.otus.diplom.models.ClientFile;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.repositories.ClientTaskRepository;
import ru.otus.diplom.utils.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Repository
public class ClientTaskRepositoryImpl implements ClientTaskRepository {
    public static final String TASKS_LIST_QUERY = "select * from ( select t.* \n" +
            "        ,(LAG(t.p_time_start) OVER (PARTITION BY t.id ORDER BY t.p_time_start)) AS prev_start_task\n" +
            "   from tasks_view t\n" +
            "  where t.wplan_prs_id in (?, ?) and \n" +
            "        t.status in (0,1,3) and t.del_flag=0) tv\n" +
            "  where tv.p_time_start=tv.max_timestart or tv.max_timestart is null";
    public static final String TASK_BY_ID_QUERY = "select t.* \n" +
            "   from tasks_view t\n" +
            "  where t.id = ?";
    public static final String TASK_FILES_BY_ID_QUERY = "select tf.id, tf.tsk_id, tf.file_name, tf.file_blob \n" +
            "from task_files tf \n" +
            "where tf.tsk_id = ?";
    public static final String GET_FILE_BY_ID_QUERY = "select tf.id, tf.tsk_id, tf.file_name, tf.file_blob \n" +
            "from task_files tf \n" +
            "where tf.id = ?";
    public static final String UPDATE_TASK_QUERY = "{call TASK_EDIT.TASK_UPDATE(?,?,?,?)}";
    public static final String UPDATE_TASK_FILE_QUERY = "{call TASK_EDIT.TASK_FILE_EDIT(?,?,?,?)}";

    @Autowired
    @Qualifier("ClientConnectionPool")
    private DataSource dataSource;

    @SneakyThrows
    @Override
    public List<Task> getTasks() {
        log.debug("Get all task");
        try(Connection connection = dataSource.getConnection()) {
            DBExecutor<Task> executer = new DBExecutorImpl<>(connection);
            log.debug("Received query script for select record");
            Function<ResultSet, List<Task>> function = resultSet -> {
                List<Task> resultObject;
                try {
                    resultObject = JdbcUtil.getObjectsList(resultSet, Task.class);
                    if (resultObject == null)
                        throw new EntetyException(String.format("Record in DB with not found"));
                    return resultObject;
                } catch (SQLException e) {
                    log.error("Error: "+e.getMessage());
                }
                return null;
            };
            return executer.select(TASKS_LIST_QUERY, function, Departament.DEVELOPMENT.getValue(),
                    Departament.ADMINISTRATION.getValue());
        }
    }

    @SneakyThrows
    @Override
    public Task getTaskById(long id) {
        log.debug("Get task by ID");
        try(Connection connection = dataSource.getConnection()) {
            DBExecutor<Task> executer = new DBExecutorImpl<>(connection);
            log.debug("Received query script for select record");
            Function<ResultSet, Task> function = resultSet -> {
                Task resultObject;
                try {
                    resultObject = JdbcUtil.getObject(resultSet, Task.class);
                    if (resultObject == null)
                        throw new EntetyException(String.format("Record in DB with not found"));
                    return resultObject;
                } catch (SQLException e) {
                    log.error("Error: "+e.getMessage());
                }
                return null;
            };
            return executer.select(TASK_BY_ID_QUERY, function, id);
        }
    }

    @SneakyThrows
    @Override
    public List<ClientFile> getFileListByTaskID(long task_id) {
        log.debug("Get all task");
        try(Connection connection = dataSource.getConnection()) {
            DBExecutor<ClientFile> executer = new DBExecutorImpl<>(connection);
            log.debug("Received query script for select record");
            Function<ResultSet, List<ClientFile>> function = resultSet -> {
                List<ClientFile> resultObject;
                try {
                    resultObject = JdbcUtil.getObjectsList(resultSet, ClientFile.class);
                    if (resultObject == null)
                        throw new EntetyException(String.format("Record in DB with not found"));
                    return resultObject;
                } catch (SQLException e) {
                    log.error("Error: "+e.getMessage());
                }
                return null;
            };
            return executer.select(TASK_FILES_BY_ID_QUERY, function, task_id);
        }
    }

    @SneakyThrows
    @Override
    public ClientFile getFileByID(long id) {
        log.debug("Get all task");
        try(Connection connection = dataSource.getConnection()) {
            DBExecutor<ClientFile> executer = new DBExecutorImpl<>(connection);
            log.debug("Received query script for select record");
            Function<ResultSet, ClientFile> function = resultSet -> {
                ClientFile resultObject;
                try {
                    resultObject = JdbcUtil.getObject(resultSet, ClientFile.class);
                    if (resultObject == null)
                        throw new EntetyException(String.format("Record in DB with not found"));
                    return resultObject;
                } catch (SQLException e) {
                    log.error("Error: "+e.getMessage());
                }
                return null;
            };
            return executer.select(GET_FILE_BY_ID_QUERY, function, id);
        }
    }

    /**
     * Вызов процедуры "TASK_UPDATE" для обновления клиентской заявки
     * @param id
     * @param wplan_prs_id
     * @param priority
     * @param description
     * @return
     */
    @SneakyThrows
    @Override
    public boolean updateTask(long id, long wplan_prs_id, int priority, String description) {
        log.debug("Update task by ID - {}", id);
        try(Connection connection = dataSource.getConnection()) {
            DBExecutor<?> executer = new DBExecutorImpl<>(connection);
            List<Object> params = Arrays.asList(id, wplan_prs_id, priority, description);
            return executer.execute(UPDATE_TASK_QUERY, params);
        }
    }

    @SneakyThrows
    @Override
    public boolean updateClientFile(ClientFile clientFile) {
        log.debug("Update file by ID - {}", clientFile.getId());
        try(Connection connection = dataSource.getConnection()) {
            DBExecutor<?> executer = new DBExecutorImpl<>(connection);
            List<Object> params = Arrays.asList(clientFile.getId(), clientFile.getTsk_id(),
                    clientFile.getFile_name());
            return executer.execute(UPDATE_TASK_FILE_QUERY, params, clientFile.getFile_blob());
        }
    }
}
