package org.p3model;

import static org.p3model.P3ClassgraphAnalyzer.ClassGraphFactory.create;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.PackageInfo;
import io.github.classgraph.PackageInfoList;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;
import org.p3model.P3Model.P3Relation;
import org.p3model.P3Model.P3RelationType;
import org.p3model.annotations.ModelElement;
import org.p3model.annotations.SystemDefinition;
import org.p3model.annotations.domain.staticModel.DomainModule;

public class P3ClassgraphAnalyzer implements P3ModelAnalyzer {

  private final String packageName;
  private final SystemNameExtractor systemNameExtractor = new SystemNameExtractor();
  private final ElementExtractor elementExtractor = new ElementExtractor();
  private final RelationExtractor relationExtractor = new RelationExtractor();

  // maybe for simpler test setup (in case of multiple implementations), package should be removed
  // from constructor and move to method or builder.
  public P3ClassgraphAnalyzer(String packageName) {
    this.packageName = packageName;
  }

  @Override
  public P3Model extract() {

    return scanner(scanResult -> {

      P3Model model = new P3Model(systemNameExtractor.extractSystemName(scanResult));
      model.addElements(elementExtractor.extractElements(scanResult));
      model.addRelations(relationExtractor.extractRelations(scanResult));

      return model;
    });
  }

  private static P3ElementType getP3ElementType(ClassInfo classInfo) {
    return P3ElementType.valueOf(
        classInfo.getAnnotationInfo().directOnly().get(0).getClassInfo().getSimpleName());
  }

  private P3Model scanner(Function<ScanResultWrapper, P3Model> extract) {
    try (ScanResult scanResult = create(packageName).scan()) {
      ScanResultWrapper wrapper = new ScanResultWrapper(scanResult);
      return extract.apply(wrapper);
    }
  }

   static class ScanResultWrapper {

    private final ScanResult scanResult;

    ScanResultWrapper(ScanResult scanResult) {
      this.scanResult = scanResult;
    }

    ClassInfoList getElementClasses() {
      return scanResult.getClassesWithAnnotation(ModelElement.class)
          .filter(classInfo -> !classInfo.isAnnotation());

    }

     PackageInfoList getPackageInfoAnnotatedWith(Class<?> annotationClass) {
      return scanResult.getPackageInfo().filter(packageInfo -> packageInfo.hasAnnotation(annotationClass.getName()));
    }
  }

  static class SystemNameExtractor {

    String extractSystemName(ScanResultWrapper scanResult) {
      // for now, we look only in package info, but in future there will be other places and hierarchy of extractors
      return scanResult.getPackageInfoAnnotatedWith(SystemDefinition.class)
          .stream()
          .map(packageInfo -> packageInfo.getAnnotationInfo(SystemDefinition.class).getParameterValues().getValue("name").toString())
          .findFirst()
          .orElseThrow(() -> new ExtractingException("no system name found"));
    }
  }

  static class ClassGraphFactory {

    private ClassGraphFactory() {
    }

    static ClassGraph create(String packageName) {
      return new ClassGraph()
          .enableAllInfo()
          .enableInterClassDependencies()
          .acceptPackages(packageName);
    }
  }

  static class ElementExtractor {

    List<P3Element> extractElements(ScanResultWrapper wrapper) {

      PackageInfoList domainModulePackages = wrapper.getPackageInfoAnnotatedWith(DomainModule.class);

      List<P3Element> foundElements = new ArrayList<>();

      for (final PackageInfo pkg : domainModulePackages) {

        foundElements.add(new P3Element("",P3ElementType.DomainModule, pkg.getAnnotationInfo(
            DomainModule.class.getName()).getParameterValues().getValue("name").toString()));
      }

      ClassInfoList elementClasses = wrapper.getElementClasses();
      for (ClassInfo elementClass : elementClasses) {
        foundElements.add(
              new P3Element("basic", P3ClassgraphAnalyzer.getP3ElementType(elementClass),
                  elementClass.getSimpleName()));
      }
      return foundElements;
    }
  }

  static class RelationExtractor {
    List<P3Relation> extractRelations(ScanResultWrapper wrapper) {

      ClassInfoList elementClasses = wrapper.getElementClasses();
      List<P3Relation> relations = new ArrayList<>();

      for (final ClassInfo ci : elementClasses) {
        for (final ClassInfo dep : ci.getClassDependencies()) {
          if (elementClasses.contains(dep)) {
            relations.add(new P3Relation(P3RelationType.DependsOn, "basic." + ci.getSimpleName(),
                "basic." + dep.getSimpleName()));
          }
        }
      }

      return relations;

    }
  }
}
