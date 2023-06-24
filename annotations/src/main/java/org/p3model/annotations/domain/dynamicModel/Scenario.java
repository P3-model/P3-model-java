package org.p3model.annotations.domain.dynamicModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Scenario {
    String name() default "";

}
