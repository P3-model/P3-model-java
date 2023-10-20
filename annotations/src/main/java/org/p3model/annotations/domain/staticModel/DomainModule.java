package org.p3model.annotations.domain.staticModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.MODULE, ElementType.PACKAGE, ElementType.TYPE})
public @interface DomainModule {
    String name() default "";

}
