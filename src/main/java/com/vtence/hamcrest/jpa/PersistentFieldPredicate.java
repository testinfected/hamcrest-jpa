package com.vtence.hamcrest.jpa;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class PersistentFieldPredicate implements Predicate<Field> {

    public boolean test(Field input) {
        return isPersistent(input);
    }

    private static boolean isPersistent(Field each) {
        return !Reflection.isStatic(each) && !Reflection.isTransient(each);
    }

    public static Field[] persistentFieldsOf(Object entity) {
        return Reflection.fieldsOf(entity, new PersistentFieldPredicate());
    }
}
