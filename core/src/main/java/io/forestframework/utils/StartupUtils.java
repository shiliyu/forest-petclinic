package io.forestframework.utils;

import com.github.blindpirate.annotationmagic.AnnotationMagic;
import io.forestframework.core.Component;

/**
 * For internal use only.
 */
public class StartupUtils {
    public static boolean isComponentClass(Class<?> klass) {
        return AnnotationMagic.isAnnotationPresent(klass, Component.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T instantiateWithDefaultConstructor(Class<?> klass) {
        try {
            return (T) klass.getConstructor().newInstance();
        } catch (Throwable e) {
            throw new RuntimeException("Can't instantiate " + klass + ", an class instantiated at startup time must have an accessible default constructor.");
        }
    }
}
