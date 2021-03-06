package io.forestframework.core.http.result;

import com.github.blindpirate.annotationmagic.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Extends(ResultProcessor.class)
@ResultProcessor(by = PlainTextResultProcessor.class)
public @interface PlainText {
}
