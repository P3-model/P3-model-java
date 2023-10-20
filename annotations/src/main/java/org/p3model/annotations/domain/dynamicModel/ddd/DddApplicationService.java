package org.p3model.annotations.domain.dynamicModel.ddd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.p3model.annotations.ModelElement;

@ModelElement
@Target(ElementType.TYPE)
public @interface DddApplicationService {
    String name() default "";

}
