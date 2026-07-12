package devops.platform.infrastructure.inbound.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@Tag(name = "Default", description = "Default description")
@RequestMapping
public @interface RestControllerDocumented {

    @AliasFor(annotation = Tag.class, attribute = "name")
    String name();

    @AliasFor(annotation = Tag.class, attribute = "description")
    String description() default "";

    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] path();
}