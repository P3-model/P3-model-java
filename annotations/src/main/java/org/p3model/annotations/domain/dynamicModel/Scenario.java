package org.p3model.annotations.domain.dynamicModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.p3model.annotations.ModelElement;

@ModelElement
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Scenario {
    String name() default "";

}
