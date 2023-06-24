package org.p3model.annotations.domain.staticModel.ddd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface DddDomainEvent {
    String name() default "";

}
