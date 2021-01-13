package ru.otus.diplom.executor;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

public interface DBExecutor<T> {
    void formatDB(String query);
    long insert(String query, List<Object> params);
    boolean update(String query, List<Object> params, long id);
    boolean execute(String query, List<Object> params);
    boolean execute(String query, List<Object> params, InputStream blob);
    boolean delete(String query, long id);
    <T> T select(String query, Function<ResultSet, T> function, long id);
    List<T> select(String query, Function<ResultSet, List<T>> function, Long id1);
    List<T> select(String query, Function<ResultSet, List<T>> function, Long dev_prs_id, Long sa_prs_id);
}
