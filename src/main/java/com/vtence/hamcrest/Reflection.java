package com.vtence.hamcrest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Reflection {

    private Reflection() {}

    public static Object readField(Object argument, Field field) {
        boolean accessible = bypassSecurity(field);
        try {
            return field.get(argument);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Can't access field " + field.getName() + " of class " + argument.getClass().getName(), e);
        } finally {
            restoreSecurity(field, accessible);
        }
    }

    public static boolean isTransient(Field each) {
        return Modifier.isTransient(each.getModifiers());
    }

    public static boolean isStatic(Field each) {
        return Modifier.isStatic(each.getModifiers());
    }

    public static Field[] fieldsOf(Object entity, Predicate<Field> predicate) {
        return fieldsOf(entity.getClass(), predicate);
    }

    public static Field[] fieldsOf(Class<?> entity, Predicate<Field> predicate) {
        Field[] allFields = entity.getDeclaredFields();
        List<Field> retained = new ArrayList<>();
        for (Field each : allFields) {
            if (predicate.test(each)) retained.add(each);
        }
        return retained.toArray(new Field[retained.size()]);
    }

    private static void restoreSecurity(Field field, boolean accessible) {
        field.setAccessible(accessible);
    }

    private static boolean bypassSecurity(Field field) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        return accessible;
    }
}
