package org.p3model.annotations.domain.staticModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;

@Target({ElementType.MODULE, ElementType.METHOD, ElementType.PACKAGE})
public @interface ModelBoundary {
    String name() default "";
    ModuleStrategy moduleStrategy() default ModuleStrategy.EACH_SUBPACKAGE;

    /**
     * This parameter is used with {@link ModuleStrategy} EACH_SUBPACKAGE when
     * some of the packages should not be treated as modules.
     */
    String[] exclude() default {};
}
