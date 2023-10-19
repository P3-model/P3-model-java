package org.p3model;

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

  public static class P3Trait {
    String name;
  }

  public static class P3Relation {
    String name;
  }

  public enum P3ElementType {
    DddRepository
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
  }
}
