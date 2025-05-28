package edu.upc.dsa.db.orm.util;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectHelper {
    public static String[] getFields(Object entity) { // Sirve para poder coger los atributos del objeto

        Class theClass = entity.getClass();

        Field[] fields = theClass.getDeclaredFields();

        String[] sFields = new String[fields.length];
        int i=0;

        for (Field f: fields) sFields[i++]=f.getName();

        return sFields;

    }

    public static List<String> getNotNullFields(Object entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser null");
        }

        List<String> result = new ArrayList<>();
        Class<?> theClass = entity.getClass();

        // Recorremos sólo la clase actual (o podrías subir la jerarquía si heredas campos)
        for (Field f : theClass.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                Object value = f.get(entity);
                if (value != null) {
                    result.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        "No se pudo leer el campo '" + f.getName() + "' de la entidad", e);
            }
        }
        return result;
    }


    public static void setter(Object object, String property, Object value) {
        //objeto es la classe, propety es el atributo y value pues el valor que le queremos meter
        Class theClass = object.getClass();
        Field[] fields = theClass.getDeclaredFields();
        for (Field f: fields) {
            if (f.getName().equals(property)) {
                f.setAccessible(true);//por si el atributo es private nos deja modificarlo esta linea
                try {
                    f.set(object, value);
                    return; // importante para cerrar el bucle
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//Aqui lo que acabamos de hacer es primero sacar los atributos del la classe del objeto, despues hemos hecho un
                //for para recorrer el vector de los atributos hasta encontrar el que se llama igual que propery
                //lo siguiente ha sifo meter el valor "value" a
            }
        }
    }

    public static Object getter(Object object, String property) {
        Class theClass = object.getClass();
        Field[] fields = theClass.getDeclaredFields();
        for (Field f: fields) {

            if (f.getName().equals(property)) {
                f.setAccessible(true);//por si el atributo es private nos deja modificarlo esta linea
                try {
                    return f.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
