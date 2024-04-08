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
import org.p3model.HierarchyStructure.HierarchyPath;
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
    private final List<P3Relation> relations = new ArrayList<>();
    private final List<RelationResolver> resolvers = new ArrayList<>();
    private final HierarchyStructure hierarchyStructure;

    ModelBuilder(String systemName, HierarchyStructure hierarchyStructure) {
      this.systemName = systemName;
      this.hierarchyStructure = hierarchyStructure;
    }

    P3Model build() {

      for (RelationResolver resolver : resolvers) {
        relations.addAll(resolver.resolve(
            (info, domain) -> elements.stream().filter(element -> element.hasInfo(info)).findFirst().orElse(null)));
      }
      return new P3Model(systemName, elements, relations);
    }

    public void addElement(ClassInfo info, P3ElementType type, String name) {
      HierarchyPath path = hierarchyStructure.pathFor(new HierarchyPath(info.getPackageName()));
      P3Element p3Element = new P3Element(path, type, name, info);
      elements.add(p3Element);
      resolvers.add(locator -> {
        List<P3Relation> locatedRelations = new ArrayList<>();
        ClassInfoList classDependencies = p3Element.getInfo().getClassDependencies();
        for (ClassInfo classDependency : classDependencies) {
          P3Element targetElement = locator.find(classDependency, P3Perspective.Domain);
          if (targetElement != null) {
            locatedRelations.add(new P3Relation(P3RelationType.DependsOn, p3Element.id(), targetElement.id()));
          }
        }
        return locatedRelations;
      });
    }
  }

  static class ScanResultWrapper {

    private final ScanResult scanResult;

    ScanResultWrapper(ScanResult scanResult) {
      this.scanResult = scanResult;
    }

    ClassInfoList getElementClasses() {
      return scanResult.getAllClasses()
          .filter(classInfo -> classInfo.getAnnotations()
              .stream().anyMatch(annotation -> annotation.hasAnnotation(ModelElement.class)));

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
          //.disableJarScanning()
          .acceptPackages(packageName)
          .acceptJars("p3-model-annotations*");
    }
  }

  static class ElementExtractor {

    Logger logger = LoggerFactory.getLogger(ElementExtractor.class);

    void extractElements(ScanResultWrapper wrapper, ModelBuilder builder) {

      ClassInfoList elementClasses = wrapper.getElementClasses();
      for (ClassInfo elementClass : elementClasses) {
        logger.atInfo().log(elementClass.getName());
        builder.addElement(elementClass, getP3ElementType(elementClass),
            elementClass.getSimpleName());
      }
    }

    private P3ElementType getP3ElementType(ClassInfo classInfo) {
      return P3ElementType.valueOf(
          classInfo.getAnnotationInfo().directOnly().get(0).getClassInfo().getSimpleName());
    }
  }

  static class DomainHierarchyExtractor {

    Logger logger = LoggerFactory.getLogger(DomainHierarchyExtractor.class);

    HierarchyStructure extract(ScanResultWrapper scanResult) {
      PackageInfoList infoList = scanResult.scanResult.getPackageInfo();
      HierarchyStructure domainStructure = new HierarchyStructure();

      if (!infoList.isEmpty()) {
        if (logger.isTraceEnabled()) {
          infoList.forEach(packageInfo -> logger.atTrace().log(packageInfo.toString()));
        }
        PackageInfo packageInfo = infoList.filter(
            pi -> !pi.getClassInfoRecursive().get(0).isExternalClass()).get(0);
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

    private String extractLastElement(String name) {
      String[] tokens = name.split("\\.");
      return tokens[tokens.length - 1];
    }

    private PackageInfo getRootPackage(PackageInfo packageInfo) {
      if (packageInfo == null) {
        throw new IllegalArgumentException();
      }
      PackageInfo current = packageInfo;
      PackageInfo previous = packageInfo;

      while (current != null) {
        previous = current;
        current = current.getParent();
      }
      return previous;
    }
  }
}
