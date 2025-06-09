package edu.upc.dsa.db.orm.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("(");

        List<String> fields = ObjectHelper.getNotNullFields(entity);

        int i = 0;
        for (String field: fields) {
            if (i<fields.size()-1) sb.append(field).append(", ");
            else sb.append(field);
           i++;
        }
        sb.append(") VALUES (");

        i=0;
        for (String field: fields) {
            if (i<fields.size()-1) sb.append("?").append(", ");
            else sb.append("?");
            i++;
        }
        sb.append(")");

        return sb.toString();
    }

    public static String createQuerySELECT(List<String> filtros, Class<?> theClass, List<String> deseados) {
        StringBuilder sb = new StringBuilder();

        if (deseados == null || deseados.isEmpty()) {
            sb.append("SELECT * ");
        } else {
            sb.append("SELECT ")
                    .append(String.join(", ", deseados))
                    .append(" ");
        }

        sb.append("FROM ").append(theClass.getSimpleName());

        if (filtros != null && !filtros.isEmpty()) {
            sb.append(" WHERE ")
                    .append(filtros.stream()
                            .map(f -> f + " = ?")
                            .collect(Collectors.joining(" AND ")));
        }

        return sb.toString();
    }


    public static String createQuerySELECTConditional(
            List<String> filtros,
            Class<?> theClass,
            List<String> deseados,
            List<String> orAttributes) {

        StringBuilder sb = new StringBuilder();

        if (deseados == null || deseados.isEmpty()) {
            sb.append("SELECT * ");
        } else {
            sb.append("SELECT ")
                    .append(String.join(", ", deseados))
                    .append(" ");
        }

        sb.append("FROM ")
                .append(theClass.getSimpleName());

        if (filtros != null && !filtros.isEmpty()) {

            // separar filtros OR y filtros AND
            List<String> orFilters = filtros.stream()
                    .filter(f -> orAttributes != null && orAttributes.contains(f))
                    .collect(Collectors.toList());
            // si f no esta en orAtributes o orAtributtes es null, lo meto en andFilters
            List<String> andFilters = filtros.stream()
                    .filter(f -> orAttributes == null || !orAttributes.contains(f))
                    .collect(Collectors.toList());

            sb.append(" WHERE ");
            boolean firstClause = true;

            // OR group
            if (!orFilters.isEmpty()) {
                sb.append("(")
                        .append(orFilters.stream()
                                .map(f -> f + " = ?")
                                .collect(Collectors.joining(" OR ")))
                        .append(")");
                firstClause = false;
            }

            // AND group
            if (!andFilters.isEmpty()) {
                if (!firstClause) {
                    sb.append(" AND ");
                }
                sb.append(andFilters.stream()
                        .map(f -> f + " = ?")
                        .collect(Collectors.joining(" AND ")));
            }
        }

        return sb.toString();
    }
    public static String createQueryUPDATE(
            Class<?> theClass,
            List<String> campos,
            List<String> filtros) {

        if (campos == null || campos.isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos un campo a actualizar");
        }
        StringBuilder sb = new StringBuilder();
        // UPDATE nombreTabla
        sb.append("UPDATE ").append(theClass.getSimpleName()).append(" SET ");
        // SET col1 = ?, col2 = ?, ...
        sb.append(campos.stream()
                .map(c -> c + " = ?")
                .collect(Collectors.joining(", ")));
        // WHERE
        if (filtros != null && !filtros.isEmpty()) {
            sb.append(" WHERE ")
                    .append(filtros.stream()
                            .map(f -> f + " = ?")
                            .collect(Collectors.joining(" AND ")));
        }
        return sb.toString();
    }

    public static String createQueryJoin(
            Class<?> class1,
            Class<?> class2,
            List<String> deseados,
            List<String> filtros,   // [ joinFieldInClass1, whereFieldInClass1 ]
            String valorOn          // joinFieldInClass2
    ) {
        if (filtros == null || filtros.size() != 2) {
            throw new IllegalArgumentException("Se requieren 2 filtros: [joinField, whereField]");
        }

        String t1 = class1.getSimpleName().toLowerCase();
        String t2 = class2.getSimpleName().toLowerCase();

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        if (deseados == null || deseados.isEmpty()) {
            sb.append(t2).append(".*");
        } else {
            for (int i = 0; i < deseados.size(); i++) {
                sb.append(t2).append(".").append(deseados.get(i));
                if (i < deseados.size() - 1) sb.append(", ");
            }
        }

        sb.append(" FROM ").append(t1)
                .append(" JOIN ").append(t2)
                .append(" ON ").append(t1).append(".").append(filtros.get(0))
                .append(" = ").append(t2).append(".").append(valorOn);

        sb.append(" WHERE ").append(t1).append(".").append(filtros.get(1)).append(" = ?");

        return sb.toString();
    }

    public static String createQueryDELETE(Class<?> theClass, List<String> filtros) {
        if (filtros == null) {
            throw new IllegalArgumentException("Los filtros y valores deben tener el mismo tamaño y no ser null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(theClass.getSimpleName());

        if (!filtros.isEmpty()) {
            sb.append(" WHERE ")
                    .append(filtros.stream()
                            .map(f -> f + " = ?")
                            .collect(Collectors.joining(" AND ")));
        }

        return sb.toString();
    }


}
