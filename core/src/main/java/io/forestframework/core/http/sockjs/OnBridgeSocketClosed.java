package io.forestframework.core.http.sockjs;

import com.github.blindpirate.annotationmagic.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Extends(Bridge.class)
@Bridge(eventTypes = {BridgeEventType.SOCKET_CLOSED})
public @interface OnBridgeSocketClosed {
    String value() default "";
}
