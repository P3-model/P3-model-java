package org.p3model;

import io.github.classgraph.ClassInfo;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.p3model.HierarchyStructure.HierarchyPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P3Model {

  private final String system;
  private final List<P3Element> elements;
  private final List<P3Relation> relations;

  public P3Model(String system, List<P3Element> elements, List<P3Relation> relations) {
    this.system = system;
    this.elements = elements;
    this.relations = relations;
  }

  public List<P3Element> getElements() {
    return elements;
  }

  public List<P3Relation> getRelations() {
    return relations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    P3Model p3Model = (P3Model) o;
    return Objects.equals(system, p3Model.system) && Objects.equals(elements,
        p3Model.elements) && Objects.equals(relations, p3Model.relations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(system, elements, relations);
  }

  public static class P3Relation {

    private final P3RelationType type;
    private final String source;
    private final String destination;

    public P3Relation(P3RelationType type, String source, String destination) {
      this.type = type;
      this.source = source;
      this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      P3Relation that = (P3Relation) o;
      return type == that.type && Objects.equals(source, that.source)
          && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, source, destination);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", P3Relation.class.getSimpleName() + "[", "]")
          .add("type=" + type)
          .add("source='" + source + "'")
          .add("destination='" + destination + "'")
          .toString();
    }
  }

  public enum P3ElementType {
    DddRepository, DddAggregate, DddEntity, DddValueObject, DddApplicationService, ProcessStep, DomainModule

  }

  public enum P3RelationType {
    DependsOn, IsImplementedBy, Contains
  }

  public static class P3Element {
    static final Logger logger = LoggerFactory.getLogger(P3Element.class);
    private final String id;
    private final P3ElementType type;
    private final String name;
    // This should be some neutral thing, not library specific
    private ClassInfo classInfo;


    P3Element(String path, P3ElementType type, String name) {
      this(new HierarchyStructure.HierarchyPath(path), type,name, null);
    }

    P3Element(HierarchyPath path, P3ElementType type, String name, ClassInfo info) {
      logger.atInfo().log(path.toString());
      logger.atInfo().log(String.valueOf(path.isEmpty()));
      this.id = type.name() + "|" + (path.isEmpty() ? name : path + "."+ name);
      this.type = type;
      this.name = name;
      this.classInfo = info;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      P3Element p3Element = (P3Element) o;
      return Objects.equals(id, p3Element.id) && type == p3Element.type
          && Objects.equals(name, p3Element.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, type, name);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", P3Element.class.getSimpleName() + "[", "]")
          .add("id='" + id + "'")
          .add("type=" + type)
          .add("name='" + name + "'")
          .toString();
    }

    public ClassInfo getInfo() {
      return classInfo;
    }

    public String id() {
      return id;
    }

    public boolean hasInfo(ClassInfo info) {
      return this.classInfo.equals(info);
    }
  }
}
