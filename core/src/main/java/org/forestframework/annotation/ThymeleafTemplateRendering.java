package org.forestframework.annotation;

import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.forestframework.ThymeleafRenderingProcessor;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@TemplateRendering(engine = ThymeleafTemplateEngine.class)
@ReturnValueProcessedBy(ThymeleafRenderingProcessor.class)
public @interface ThymeleafTemplateRendering {
}