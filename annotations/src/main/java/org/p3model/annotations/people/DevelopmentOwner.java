package org.p3model.annotations.people;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.MODULE, ElementType.TYPE})
public @interface DevelopmentOwner {
    String name();

}
