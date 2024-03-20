package org.p3model;

import io.github.classgraph.ClassInfo;
import org.p3model.P3ClassgraphAnalyzer.ModelBuilder;
import org.p3model.P3Model.P3ElementType;

public class ServiceAnalyser {


  void analyse(ClassInfo classInfo, ModelBuilder builder) {
    builder.addBB("name", P3ElementType.DddApplicationService,"namespace");
  }

}
