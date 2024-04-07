package org.p3model.annotations.domain.staticModel.ddd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.p3model.annotations.ModelElement;
import org.p3model.annotations.domain.staticModel.ModuleStrategy;

@ModelElement
@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.MODULE})
public @interface DddBoundedContext {
    String name() default "";

    ModuleStrategy moduleStrategy() default ModuleStrategy.EACH_SUBPACKAGE;
    /**
     * This parameter is used with {@link ModuleStrategy} EACH_SUBPACKAGE when
     * some of the packages should not be treated as modules.
     */
    String[] exclude() default {};
}
