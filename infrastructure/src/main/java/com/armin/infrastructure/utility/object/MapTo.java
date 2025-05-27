package com.armin.infrastructure.utility.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD,
        ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface MapTo {

    String target() default "";

    @Deprecated
    @Retention(RetentionPolicy.SOURCE)
    @Target({})
    @interface AnyAnnotation {}

}
