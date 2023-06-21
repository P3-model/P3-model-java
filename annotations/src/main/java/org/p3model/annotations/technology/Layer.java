package org.p3model.annotations.technology;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.MODULE, ElementType.TYPE})
public @interface Layer {
    String name();

}
