package org.p3model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class P3Model {

  private final String system;
  private final List<P3Element> elements;
  private final List<P3Relation> relations;

  public P3Model(String system, List<P3Element> elements) {
    this.system = system;
    this.elements = elements;
    this.relations = new ArrayList<>();
  }

  public void addElement(P3ElementType p3ElementType, String name, String path) {
    elements.add(new P3Element(path, p3ElementType, name));
  }

  public P3Model addElements(List<P3Element> elements) {
    this.elements.addAll(elements);
    return this;
  }

  public void addRelations(List<P3Relation> relations) {
    this.relations.addAll(relations);
  }

  public String getSystemName() {
    return system;
  }

  public List<P3Element> getElements() {
    return elements;
  }

  public List<P3Relation> getRelations() {
    return relations;
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
    DependsOn, IsImplementedBy
  }

  public static class P3Element {

    private final String id;
    private final P3ElementType type;
    private final String name;


    P3Element(String id, P3ElementType type, String name) {
      this(new HierarchyStructure.HierarchyPath(id), type,name);
    }

    P3Element(HierarchyStructure.HierarchyPath path, P3ElementType type, String name) {

      this.id = type.name() + "|" + path + name;
      this.type = type;
      this.name = name;
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

  }
}
