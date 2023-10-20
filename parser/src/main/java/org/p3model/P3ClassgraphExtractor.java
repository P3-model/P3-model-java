package org.p3model;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;
import org.p3model.P3Model.P3Relation;
import org.p3model.P3Model.P3RelationType;
import org.p3model.P3Model.P3Trait;
import org.p3model.annotations.ModelElement;
import org.p3model.annotations.SystemDefinition;

public class P3ClassgraphExtractor implements P3ModelExtractor {

  private final String packageName;

  // maybe for simpler test setup (in case of multiple implementations), package should be removed
  // from constructor and move to method or builder.
  public P3ClassgraphExtractor(String packageName) {
    this.packageName = packageName;
  }

  @Override
  public P3Model extract() {

    return scanner(scanResult -> {

      P3Model model = new P3Model(extractSystemName(scanResult));
      model.addElements(extractElements(scanResult));
      model.addRelations(extractRelations(scanResult));
      model.addTraits(extractTraits(scanResult));

      return model;
    });
  }

  private List<P3Trait> extractTraits(ScanResult scanResult) {
    return new ArrayList<>();
  }

  private List<P3Relation> extractRelations(ScanResult scanResult) {

    ScanResultWrapper wrapper = new ScanResultWrapper(scanResult);

    ClassInfoList elementClasses = wrapper.getElementClasses();
    List<P3Relation> relations = new ArrayList<>();

    for (final ClassInfo ci : elementClasses) {
      for (final ClassInfo dep : ci.getClassDependencies()) {
        if (elementClasses.contains(dep)) {
          relations.add(new P3Relation(P3RelationType.DependsOn, "basic."+ ci.getSimpleName(), "basic." + dep.getSimpleName()));
        }
      }
    }

    return relations;

  }

  private List<P3Element> extractElements(ScanResult scanResult) {

    // 1. find all modules
    // 2. create dependency graph
    // 3. assign element to module
    // 4. find path to element


    ScanResultWrapper wrapper = new ScanResultWrapper(scanResult);
    return wrapper.getElementClasses()
        .stream().map(classInfo -> new P3Element("basic",
            getP3ElementType(classInfo),
            classInfo.getSimpleName())).collect(Collectors.toList());


  }

  private static P3ElementType getP3ElementType(ClassInfo classInfo) {
    return P3ElementType.valueOf(
        classInfo.getAnnotationInfo().directOnly().get(0).getClassInfo().getSimpleName());
  }

  private String extractSystemName(ScanResult scanResult) {
    // for now, we look only in package info, but in future there will be other places and hierarchy of extractors
    return scanResult.getPackageInfo()
        .filter(packageInfo -> packageInfo.hasAnnotation(SystemDefinition.class)).stream().map(
            packageInfo -> packageInfo.getAnnotationInfo(SystemDefinition.class)
                .getParameterValues().getValue("name").toString()).findFirst()
        .orElseThrow(() -> new ExtractingException("no system name found"));
  }

  private P3Model scanner(Function<ScanResult, P3Model> extract) {
    try (ScanResult scanResult =
        new ClassGraph()
            .enableAllInfo()
            .enableInterClassDependencies()
            .acceptPackages(packageName)
            .scan()) {

      return extract.apply(scanResult);
    }
  }

  private class ScanResultWrapper {

    private final ScanResult scanResult;

    ScanResultWrapper(ScanResult scanResult) {
      this.scanResult = scanResult;
    }

    ClassInfoList getElementClasses() {
      return scanResult.getClassesWithAnnotation(ModelElement.class)
          .filter(classInfo -> !classInfo.isAnnotation());

    }

    ScanResult getScanResult() {
      return scanResult;
    }
  }
}
