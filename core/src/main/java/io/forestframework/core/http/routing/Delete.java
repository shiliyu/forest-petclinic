package io.forestframework.core.http.routing;

import com.github.blindpirate.annotationmagic.Extends;
import io.forestframework.core.http.HttpMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Extends(Route.class)
@Route(methods = {HttpMethod.DELETE})
public @interface Delete {
    String value() default "";

    String regex() default "";
}
