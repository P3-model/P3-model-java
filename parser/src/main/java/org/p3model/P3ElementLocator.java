package org.p3model;

import io.github.classgraph.ClassInfo;
import org.p3model.P3Model.P3Element;

public interface P3ElementLocator {
  P3Element find(ClassInfo info, P3Perspective domain);
}
