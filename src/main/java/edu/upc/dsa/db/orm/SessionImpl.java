package edu.upc.dsa.db.orm;

import edu.upc.dsa.db.orm.util.ObjectHelper;
import edu.upc.dsa.db.orm.util.QueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.reflections.Reflections.log;

public class SessionImpl implements Session {
    private final Connection conn;
    private boolean transactionActive = false;

    public SessionImpl(Connection conn) {
        this.conn = conn;
        // Iniciamos transacción automáticamente al abrir la sesión
        beginTransaction();
    }


    public void beginTransaction() {
        try {
            if (!transactionActive) {
                conn.setAutoCommit(false);
                transactionActive = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo iniciar transacción", e);
        }
    }

    public void commit() {
        if (!transactionActive) return;
        try {
            conn.commit();
            conn.setAutoCommit(true);
            transactionActive = false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al hacer commit", e);
        }
    }

    public void rollback() {
        if (!transactionActive) return;
        try {
            conn.rollback();
            conn.setAutoCommit(true);
            transactionActive = false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al hacer rollback", e);
        }
    }

    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar conexión", e);
        }
    }

    @Override
    public void save(Object entity) {
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        try (PreparedStatement pstm = conn.prepareStatement(insertQuery)) {
            int index = 1;
            for (String field : ObjectHelper.getNotNullFields(entity)) {
                Object value = ObjectHelper.getter(entity, field);
                pstm.setObject(index++, value);
            }
            System.out.println("Ejecutando consulta: " + pstm.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en save(): " + e.getMessage(), e);
        }
    }

    @Override
    public Object get(Class<?> theClass, List<String> filtros, List<Object> valores, List<String> deseados) {
        String sql = QueryHelper.createQuerySELECT(filtros, theClass, deseados);
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            for (int i = 0; i < valores.size(); i++) {
                pstm.setObject(i + 1, valores.get(i));
            }
            System.out.println("Ejecutando consulta: " + pstm.toString());
            ResultSet rs = pstm.executeQuery();
            return mapResultSetToEntity(rs, theClass);
        } catch (Exception e) {
            throw new RuntimeException("Error en get(): " + e.getMessage(), e);
        }
    }
//    @Override
//    public Object getLista(Class<?> theClass, List<String> filtros, List<Object> valores, List<String> deseados) {
//        String sql = QueryHelper.createQuerySELECT(filtros, theClass, deseados);
//        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
//            for (int i = 0; i < valores.size(); i++) {
//                pstm.setObject(i + 1, valores.get(i));
//            }
//            System.out.println("Ejecutando consulta: " + pstm.toString());
//            ResultSet rs = pstm.executeQuery();
//            return mapResultSetToEntityList(rs, theClass);
//        } catch (Exception e) {
//            throw new RuntimeException("Error en get(): " + e.getMessage(), e);
//        }
//    }

    @Override
    public Object getLista(Class<?> theClass, List<String> filtros, List<Object> valores, List<String> deseados) {
        String sql = QueryHelper.createQuerySELECT(filtros, theClass, deseados);
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Manejo seguro de valores nulos
            if(valores != null) {
                for (int i = 0; i < valores.size(); i++) {
                    pstm.setObject(i + 1, valores.get(i));
                }
            }
            System.out.println("Ejecutando consulta: " + pstm.toString());
            ResultSet rs = pstm.executeQuery();
            return mapResultSetToEntityList(rs, theClass);
        } catch (Exception e) {
            throw new RuntimeException("Error en getLista(): " + e.getMessage(), e);
        }
    }


    @Override
    public Object getCondicional(Class<?> theClass,
                                 List<String> filtros,
                                 List<String> deseados,
                                 List<String> orAttributes,
                                 List<Object> valores) {
        if (filtros == null || valores == null || filtros.size() != valores.size()) {
            throw new IllegalArgumentException("Los filtros y valores deben tener el mismo tamaño y no ser null");
        }
        String sql = QueryHelper.createQuerySELECTConditional(filtros, theClass, deseados, orAttributes);
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            for (int i = 0; i < valores.size(); i++) {
                pstm.setObject(i + 1, valores.get(i));
            }
            System.out.println("Ejecutando consulta: " + pstm.toString());
            ResultSet rs = pstm.executeQuery();
            return mapResultSetToEntity(rs, theClass);
        } catch (Exception e) {
            throw new RuntimeException("Error en getCondicional(): " + e.getMessage(), e);
        }
    }

    @Override
    public Object getWithJoin(Class<?> class1, Class<?> class2,
                              List<String> deseados,
                              List<String> filtros,
                              String valorOn, Object valores) {
//        String sql = QueryHelper.createQueryJoin(class1, class2, deseados, filtros, valorOn);
        String sql = ("SELECT o.*" +
                "  FROM Usuario_objeto uo" +
                "  JOIN Objeto o" +
                "    ON uo.objeto_nombre = o.nombre" +
                " WHERE uo.usuario_id = ?;");
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Asignar valores a los parámetros de la consulta
            pstm.setObject( 1, valores);

            System.out.println("Ejecutando consulta: " + pstm.toString());
            // Ejecutar la consulta
            ResultSet rs = pstm.executeQuery();
            return mapResultSetToEntityList(rs, class2);
        } catch (Exception e) {
            throw new RuntimeException("Error en getWithJoin(): " + e.getMessage(), e);
        }
    }


    @Override
    public void update(Class<?> theClass, List<String> cambios, List<String> filtros, List<Object> valores) {
        String sql = QueryHelper.createQueryUPDATE(theClass, cambios, filtros);
        System.out.println("SQL : "+sql);
	try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            for (int i = 0; i < valores.size(); i++) {
		    System.out.println(i +" : "+valores.get(i));
                pstm.setObject(i + 1, valores.get(i));
            }
            System.out.println("Ejecutando consulta: " + pstm.toString());
            int res = pstm.executeUpdate();
	    System.out.println("Res: "+res);
        } catch (Exception e) {
            throw new RuntimeException("Error en update(): " + e.getMessage(), e);
        }
    }

    /**
     * Mapea la primera fila del ResultSet a una instancia de la clase dada.
     */
    private Object mapResultSetToEntity(ResultSet rs, Class<?> theClass) throws Exception {
        if (!rs.next()) return null;
        Object entity = theClass.getDeclaredConstructor().newInstance();
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            String colName = meta.getColumnName(i);
            Object value = rs.getObject(i);
            ObjectHelper.setter(entity, colName, value);
        }
        return entity;
    }

    private List<Object> mapResultSetToEntityList(ResultSet rs, Class<?> theClass) throws Exception {
        List<Object> list = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        while (rs.next()) {
            Object entity = theClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= cols; i++) {
                String colName = meta.getColumnName(i);
                Object value = rs.getObject(i);
                ObjectHelper.setter(entity, colName, value);
            }
            list.add(entity);
        }

        return list;
    }

    @Override
    public void delete(Object theClass) {
        int x =0;
        List<String> atributos = ObjectHelper.getNotNullFields(theClass);
        List<Object> valores = new ArrayList<>();
        while (atributos.size() > x) {
            valores.add(ObjectHelper.getter(theClass, atributos.get(x)));
            x++;
        }
            String sql = QueryHelper.createQueryDELETE(theClass.getClass(), atributos);
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            for (int i = 0; i < valores.size(); i++) {
                pstm.setObject(i + 1, valores.get(i));
            }
            System.out.println("Ejecutando consulta: " + pstm.toString());
            pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error en delete(): " + e.getMessage(), e);
        }
    }
}
