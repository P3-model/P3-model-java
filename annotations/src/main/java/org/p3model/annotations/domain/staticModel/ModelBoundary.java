package org.p3model.annotations.domain.staticModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.MODULE, ElementType.METHOD, ElementType.TYPE})
public @interface ModelBoundary {
    String name() default "";

}
