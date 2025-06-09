package edu.upc.dsa.db.orm;

import java.util.List;

public interface Session {

    void beginTransaction();

    void commit();

    void rollback();

    void save(Object entity);

    Object get(
            Class<?> theClass,
            List<String> filtros,
            List<Object> valores,
            List<String> deseados
    );

    public Object getLista(Class<?> theClass,
                           List<String> filtros,
                           List<Object> valores,
                           List<String> deseados);

    Object getCondicional(
            Class<?> theClass,
            List<String> filtros,
            List<String> deseados,
            List<String> orAttributes,
            List<Object> valores
    );

    public Object getWithJoin(Class<?> class1, Class<?> class2,
                              List<String> deseados,
                              List<String> filtros,
                              String valorOn, Object valores);

    void update(
            Class<?> theClass,
            List<String> cambios,
            List<String> filtros,
            List<Object> valores
    );

    void delete(
            Object theClass
    );

    void close();
}