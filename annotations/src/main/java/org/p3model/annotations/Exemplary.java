package org.p3model.annotations;

import java.lang.annotation.*;

/**
 *  Exemplary is annotation to point out the reference for future use.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.MODULE, ElementType.PACKAGE})
public @interface Exemplary {
}