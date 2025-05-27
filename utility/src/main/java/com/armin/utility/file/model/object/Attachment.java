package com.armin.utility.file.model.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD,
        ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Attachment {
    String container();

    String bucket();

    boolean autoBuild() default true;
}
