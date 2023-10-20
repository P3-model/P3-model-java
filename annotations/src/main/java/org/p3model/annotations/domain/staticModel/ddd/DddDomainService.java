package org.p3model.annotations.domain.staticModel.ddd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.p3model.annotations.ModelElement;

@ModelElement
@Target(ElementType.TYPE)
public @interface DddDomainService {
    String name() default "";

}
