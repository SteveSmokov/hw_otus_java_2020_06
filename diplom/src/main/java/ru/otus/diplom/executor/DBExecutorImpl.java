package ru.otus.diplom.executor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class DBExecutorImpl<T> implements DBExecutor<T> {
    private Connection connection;

    public DBExecutorImpl(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    @Override
    public void formatDB(String query) {
        try(final Statement statement = connection.createStatement()){
            statement.execute(query);
        }
    }

    @SneakyThrows
    @Override
    public long insert(String query, List<Object> params) {
        try (final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            int i = 1;
            for (Object p : params){
                statement.setObject(i++,p);
            }
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()){
                if(resultSet.next()){
                    return resultSet.getInt(1);
                } else return 0;
            }
        }
    }

    @SneakyThrows
    @Override
    public boolean update(String query, List<Object> params, long id) {
        try (final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            int i = 1;
            for (Object p : params){
                statement.setObject(i++,p);
            }
            statement.setObject(i++, id);
            statement.executeUpdate();
            return (statement.getUpdateCount() > 0);
        }
    }

    @SneakyThrows
    @Override
    public boolean execute(String query, List<Object> params) {
        try (final CallableStatement statement = connection.prepareCall(query)){
            int i = 1;
            for (Object p : params){
                statement.setObject(i++,p);
            }
            statement.executeUpdate();
            return true;
        }
    }

    @SneakyThrows
    @Override
    public boolean execute(String query, List<Object> params, InputStream blob) {
        try (final CallableStatement statement = connection.prepareCall(query)){
            int i = 1;
            for (Object p : params){
                statement.setObject(i++,p);
            }
            statement.setBlob(i++, blob);
            statement.executeUpdate();
            return true;
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(String query, long id) {
        try (final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setObject(1, id);
            statement.executeUpdate();
            return (statement.getUpdateCount() > 0);
        }
    }

    @SneakyThrows
    @Override
    public <T> T select(String query, Function<ResultSet, T> function, long id) {
        try (final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setObject(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                return function.apply(resultSet);
            }
        }
    }

    @SneakyThrows
    @Override
    public List<T> select(String query, Function<ResultSet, List<T>> function, Long id1) {
        try (final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setObject(1, id1);
            try(ResultSet resultSet = statement.executeQuery()){
                return function.apply(resultSet);
            }
        }
    }

    @SneakyThrows
    @Override
    public List<T> select(String query, Function<ResultSet, List<T>> function, Long dev_prs_id, Long sa_prs_id) {
        try (final PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setObject(1, dev_prs_id);
            statement.setObject(2, sa_prs_id);
            try(ResultSet resultSet = statement.executeQuery()){
                return function.apply(resultSet);
            }
        }
    }
}
