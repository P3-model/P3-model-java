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
import org.p3model.HierarchyStructure.HierarchyNode;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;
import org.p3model.P3Model.P3Relation;
import org.p3model.P3Model.P3RelationType;
import org.p3model.annotations.ModelElement;
import org.p3model.annotations.SystemDefinition;
import org.p3model.annotations.domain.staticModel.DomainModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P3ClassgraphAnalyzer implements P3ModelAnalyzer {

  private final String packageName;
  private final SystemNameExtractor systemNameExtractor = new SystemNameExtractor();
  private final DomainHierarchyExtractor domainHierarchyExtractor = new DomainHierarchyExtractor();
  private final ElementExtractor elementExtractor = new ElementExtractor();

  // maybe for simpler test setup (in case of multiple implementations), package should be removed
  // from constructor and move to method or builder.
  public P3ClassgraphAnalyzer(String packageName) {
    this.packageName = packageName;
  }

  @Override
  public P3Model extract(String systemName) {

    return scanner(scanResult -> {

      ModelBuilder builder = new ModelBuilder(
          systemNameExtractor.extract(systemName, scanResult),
          domainHierarchyExtractor.extract(scanResult));

      elementExtractor.extractElements(scanResult, builder);
      return builder.build();
    });
  }


  private P3Model scanner(Function<ScanResultWrapper, P3Model> extract) {
    try (ScanResult scanResult = create(packageName).scan()) {
      ScanResultWrapper wrapper = new ScanResultWrapper(scanResult);
      return extract.apply(wrapper);
    }
  }

  static class ModelBuilder {

    private final String systemName;
    private final List<P3Element> elements = new ArrayList<>();
    private final HierarchyStructure hierarchyStructure;

    ModelBuilder(String systemName, HierarchyStructure hierarchyStructure) {
      this.systemName = systemName;
      this.hierarchyStructure = hierarchyStructure;
    }

    P3Model build() {
      return new P3Model(systemName, elements);
    }

    public void add(P3Element element) {
      elements.add(element);
    }

    public void addBB(String name, P3ElementType p3ElementType,
        HierarchyStructure.HierarchyPath namespace) {
      elements.add(new P3Element(hierarchyStructure.pathFor(namespace), p3ElementType, name));
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
      return scanResult.getPackageInfo()
          .filter(packageInfo -> packageInfo.hasAnnotation(annotationClass.getName()));
    }
  }

  static class SystemNameExtractor {

    String extract(String externalName, ScanResultWrapper scanResult) {

      if (!externalName.isBlank()) {
        return externalName;
      } else {
        return scanResult.getPackageInfoAnnotatedWith(SystemDefinition.class)
            .stream()
            .map(packageInfo -> packageInfo.getAnnotationInfo(SystemDefinition.class)
                .getParameterValues().getValue("name").toString())
            .findFirst()
            .orElseThrow(() -> new ExtractingException("no system name found"));
      }
    }
  }

  static class ClassGraphFactory {

    private ClassGraphFactory() {
    }

    static ClassGraph create(String packageName) {
      return new ClassGraph()
          .enableAllInfo()
          .enableInterClassDependencies()
          .disableJarScanning()
          .acceptPackages(packageName);
    }
  }

  static class ElementExtractor {

    void extractElements(ScanResultWrapper wrapper, ModelBuilder builder) {

      PackageInfoList domainModulePackages = wrapper.getPackageInfoAnnotatedWith(
          DomainModule.class);

      for (final PackageInfo pkg : domainModulePackages) {

        builder.add(new P3Element("", P3ElementType.DomainModule, pkg.getAnnotationInfo(
            DomainModule.class.getName()).getParameterValues().getValue("name").toString()));
      }

      ClassInfoList elementClasses = wrapper.getElementClasses();
      for (ClassInfo elementClass : elementClasses) {
        elementClass.getPackageName();
        builder.add(
            new P3Element("basic", getP3ElementType(elementClass), elementClass.getSimpleName()));
      }
    }

    private P3ElementType getP3ElementType(ClassInfo classInfo) {
      return P3ElementType.valueOf(
          classInfo.getAnnotationInfo().directOnly().get(0).getClassInfo().getSimpleName());
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

  static class DomainHierarchyExtractor {
    Logger logger = LoggerFactory.getLogger(DomainHierarchyExtractor.class);

    HierarchyStructure extract(ScanResultWrapper scanResult) {
      PackageInfoList infoList = scanResult.scanResult.getPackageInfo();
      HierarchyStructure domainStructure = new HierarchyStructure();

      if (!infoList.isEmpty()) {
        if(logger.isTraceEnabled()) {
          infoList.forEach(packageInfo -> logger.atTrace().log(packageInfo.toString()));
        }
        PackageInfo packageInfo = infoList.get(0);
        logger.atTrace().log(packageInfo.toString());

        HierarchyNode node = domainStructure.getRoot();
        PackageInfo rootPackage = getRootPackage(packageInfo);

        extract(rootPackage, node);
      }
      return domainStructure;
    }

    private void extract(PackageInfo packageInfo, HierarchyNode node) {
      if (packageInfo.hasAnnotation(DomainModule.class)) {
        node = node.addChild(
            packageInfo.getAnnotationInfo(DomainModule.class).getParameterValues()
                .getValue("name").toString(), extractLastElement(packageInfo.getName()));
      }
      PackageInfoList children = packageInfo.getChildren();
      for (PackageInfo child : children) {
        extract(child, node);
      }
    }

    private  String extractLastElement(String name) {
      String[] tokens = name.split("\\.");
      return tokens[tokens.length -1];
    }

    private PackageInfo getRootPackage(PackageInfo packageInfo) {
      if (packageInfo == null) {
        throw new IllegalArgumentException();
      }
      PackageInfo current = packageInfo;
      PackageInfo previous = packageInfo;

      while (current != null) {
        System.out.println("get: " + current);
        previous = current;
        current = current.getParent();
      }
      return previous;
    }
  }
}
