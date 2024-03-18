package org.p3model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class P3Model {

  private final String system;
  private final List<P3Element> elements;
  private final List<P3Relation> relations;
  private final List<P3Trait> traits;

  public P3Model(String system) {
    this.system = system;
    elements = new ArrayList<>();
    relations = new ArrayList<>();
    traits = new ArrayList<>();
  }

  public void addElement(P3ElementType p3ElementType, String name, String path) {
    elements.add(new P3Element(path, p3ElementType, name));
  }

  public void addElements(List<P3Element> elements) {
    this.elements.addAll(elements);
  }

  public void addRelations(List<P3Relation> relations) {
    this.relations.addAll(relations);
  }

  public void addTraits(List<P3Trait> traits) {
    this.traits.addAll(traits);
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

  public static class P3Trait {

    String name;
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

    public P3Element(String path, P3ElementType type, String name) {
      if (!path.isBlank()) path = path + ".";
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
