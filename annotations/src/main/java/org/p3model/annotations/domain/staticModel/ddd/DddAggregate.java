package org.p3model.annotations.domain.staticModel.ddd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that given type is a DDD Aggregate
 * If you want to learn more check <a href="https://www.domainlanguage.com/ddd/reference/">DDD Reference by Eric Evans</a>
 */
@Target(ElementType.TYPE)
public @interface DddAggregate {
    String name() default "";

}
