package com.adtsw.jcommons.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtils {
    
    public static Type[] getParameterizedClassArguements(Class<?> currentClass) {
        
        ParameterizedType parameterizedType = null;
        
        while (currentClass != null && !(currentClass.getGenericSuperclass() instanceof ParameterizedType)) {
            currentClass = currentClass.getSuperclass();
        }
        
        if (currentClass == null) {
            throw new RuntimeException(
                "No root generic superclass found for AbstractProfileDAO implementation"
            );
        } else {
            parameterizedType = ((ParameterizedType) currentClass.getGenericSuperclass());
        }

        Type[] typeArgs = parameterizedType.getActualTypeArguments();
        return typeArgs;
    }
}
