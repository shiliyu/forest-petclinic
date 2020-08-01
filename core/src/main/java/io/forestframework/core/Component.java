package io.forestframework.core;

import org.apiguardian.api.API;

import javax.inject.Scope;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@API(status = API.Status.EXPERIMENTAL, since = "0.3")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface Component {
}
