package org.p3model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.MODULE, ElementType.PACKAGE})
public @interface SystemDefinition {
  String name();
}
