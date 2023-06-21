package org.p3model.annotations.domain.dynamicModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ProcessStep {
    String name() default "";

    String process() default "";

    String[] nextSteps() default {};

}
