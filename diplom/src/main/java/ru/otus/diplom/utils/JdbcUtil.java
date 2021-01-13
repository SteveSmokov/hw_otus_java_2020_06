package ru.otus.diplom.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class JdbcUtil {

    public static <T> List<T> getObjectsList(ResultSet resultSet, Class<T> clazz) throws SQLException {
        List<T> result = new ArrayList<>();
        List<Field> listField =  ReflectionUtil.getFields(clazz);
        while (resultSet.next()){
            T row = ReflectionUtil.instantiate(clazz);
            for (Field field : listField){
                Object fieldObject = null;
                if (field.getType().equals(boolean.class)) {
                    fieldObject = resultSet.getBoolean(field.getName());
                }
                else if (field.getType().equals(byte.class)) {
                    fieldObject = resultSet.getByte(field.getName());
                }
                else if (field.getType().equals(short.class)) {
                    fieldObject = resultSet.getShort(field.getName());
                }
                else if (field.getType().equals(int.class) ||
                        field.getType().equals(Integer.class)) {
                    fieldObject = resultSet.getInt(field.getName());
                }
                else if (field.getType().equals(long.class) ||
                        field.getType().equals(Long.class)) {
                    fieldObject = resultSet.getLong(field.getName());
                }
                else if (field.getType().equals(float.class)) {
                    fieldObject = resultSet.getFloat(field.getName());
                }
                else if (field.getType().equals(double.class)) {
                    fieldObject = resultSet.getDouble(field.getName());
                }
                else if (field.getType().equals(String.class)) {
                    fieldObject = resultSet.getString(field.getName());
                }
                else if (field.getType().equals(Date.class)) {
                    fieldObject = resultSet.getDate(field.getName());
                }
                else if (field.getType().equals(Blob.class)) {
                    fieldObject = resultSet.getBlob(field.getName());
                }
                else if (field.getType().equals(Clob.class)) {
                    fieldObject = resultSet.getClob(field.getName());
                }
                else if (field.getType().equals(byte[].class)) {
                    fieldObject = resultSet.getBytes(field.getName());
                }
                else if (field.getType().equals(Array.class)) {
                    fieldObject = resultSet.getArray(field.getName());
                }
                else if (field.getType().equals(InputStream.class)) {
                    fieldObject = resultSet.getBlob(field.getName()).getBinaryStream();
                }
                else {
                    fieldObject = resultSet.getObject(field.getName());
                }
                ReflectionUtil.setFieldValue(row, field, fieldObject);
            }
            result.add(row);
        }
        return result;
    }

    public static <T> T getObject(ResultSet resultSet, Class<T> clazz) throws SQLException {
        T result = null;
        List<Field> listField =  ReflectionUtil.getFields(clazz);
        while (resultSet.next()){
            result = ReflectionUtil.instantiate(clazz);
            for (Field field : listField){
                Object fieldObject = null;
                if (field.getType().equals(boolean.class)) {
                    fieldObject = resultSet.getBoolean(field.getName());
                }
                else if (field.getType().equals(byte.class)) {
                    fieldObject = resultSet.getByte(field.getName());
                }
                else if (field.getType().equals(short.class)) {
                    fieldObject = resultSet.getShort(field.getName());
                }
                else if ((field.getType().equals(int.class)) &&
                        (field.getType().equals(Integer.class))) {
                    fieldObject = resultSet.getInt(field.getName());
                }
                else if ((field.getType().equals(long.class)) &&
                        (field.getType().equals(Long.class))) {
                    fieldObject = resultSet.getLong(field.getName());
                }
                else if (field.getType().equals(float.class)) {
                    fieldObject = resultSet.getFloat(field.getName());
                }
                else if (field.getType().equals(double.class)) {
                    fieldObject = resultSet.getDouble(field.getName());
                }
                else if (field.getType().equals(String.class)) {
                    fieldObject = resultSet.getString(field.getName());
                }
                else if (field.getType().equals(Date.class)) {
                    fieldObject = resultSet.getDate(field.getName());
                }
                else if (field.getType().equals(Blob.class)) {
                    fieldObject = resultSet.getBlob(field.getName());
                }
                else if (field.getType().equals(Clob.class)) {
                    fieldObject = resultSet.getClob(field.getName());
                }
                else if (field.getType().equals(byte[].class)) {
                    fieldObject = resultSet.getBytes(field.getName());
                }
                else if (field.getType().equals(Array.class)) {
                    fieldObject = resultSet.getArray(field.getName());
                }
                else if (field.getType().equals(InputStream.class)) {
                    fieldObject = resultSet.getBlob(field.getName()).getBinaryStream();
                }
                else {
                    fieldObject = resultSet.getObject(field.getName());
                }
                ReflectionUtil.setFieldValue(result, field, fieldObject);
            }
        }
        return result;
    }
}
