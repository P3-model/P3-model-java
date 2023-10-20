package org.p3model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;

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

  public void addElement(P3ElementType p3ElementType, String name, String id) {
    elements.add(new P3Element(id, p3ElementType, name));
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

  public static class P3Trait {
    String name;
  }

  public static class P3Relation {
    String name;
  }

  public enum P3ElementType {
    DddRepository,
    DddAggregate,
    DddEntity,
    DddValueObject,
    DddApplicationService,
    ProcessStep
  }

  public static class P3Element {

    private final String id;
    private final P3ElementType type;
    private final String name;

    public P3Element(String id, P3ElementType type, String name) {
      this.id = id;
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
      return Objects.equal(id, p3Element.id) && type == p3Element.type
          && Objects.equal(name, p3Element.name);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(id, type, name);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("id", id)
          .add("type", type)
          .add("name", name)
          .toString();
    }
  }
}
