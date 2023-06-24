package org.p3model.annotations.technology;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.MODULE})
public @interface DeployableUnit {
    String name();

}
