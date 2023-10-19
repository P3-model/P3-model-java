package org.p3model;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValue;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.List;

public class P3Parser {

  public String parse(String sourcePath) {

    String pkg = "org.p3model.samples.basic";
    String routeAnnotation = pkg + ".Route";
    try (ScanResult scanResult =
        new ClassGraph()
            .verbose()               // Log to stderr
            .enableAllInfo()         // Scan classes, methods, fields, annotations
            .acceptPackages(pkg)     // Scan com.xyz and subpackages (omit to scan all packages)
            .scan()) {               // Start the scan
      for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(routeAnnotation)) {
        AnnotationInfo routeAnnotationInfo = routeClassInfo.getAnnotationInfo(routeAnnotation);
        List<AnnotationParameterValue> routeParamVals = routeAnnotationInfo.getParameterValues();
        // @com.xyz.Route has one required parameter
        String route = (String) routeParamVals.get(0).getValue();
        System.out.println(routeClassInfo.getName() + " is annotated with route " + route);
      }
    }

    return "{\"test\":\"value\"}";
  }
}
