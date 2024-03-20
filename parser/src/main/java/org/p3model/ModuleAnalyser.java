package org.p3model;

import io.github.classgraph.PackageInfo;
import org.p3model.P3ClassgraphAnalyzer.ModelBuilder;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;
import org.p3model.annotations.domain.staticModel.DomainModule;

public class ModuleAnalyser {

  // change to module graph builder
  void analyse(PackageInfo packageInfo, ModelBuilder builder) {
    if(packageInfo.hasAnnotation(DomainModule.class)) {
      builder.add(new P3Element(packageInfo.getName(), P3ElementType.DomainModule, getModuleName(packageInfo)));
    }
  }

  private static String getModuleName(PackageInfo packageInfo) {
    Object name = packageInfo.getAnnotationInfo(DomainModule.class).getParameterValues().getValue("name");

    if (name != null) {
      return name.toString();
    } else {
      return extractLastElement(packageInfo.getName());
    }
  }

  private static String extractLastElement(String name) {
    String[] tokens = name.split("\\.");
    return tokens[tokens.length -1];
  }

}
