package org.p3model;

public class HierarchyId {

  private final String value;

  public HierarchyId(String value) {
    this.value = value;
  }

  public static HierarchyId from(String name) {
    return new HierarchyId(name);
  }
}
