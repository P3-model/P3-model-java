package org.p3model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class HierarchyStructure {

  private HierarchyNode treeRoot;

  HierarchyPath pathFor(HierarchyPath namespace) {
    List<String> pathParts = new ArrayList<>();
    treeRoot.pathFor(namespace.getParts(), pathParts);
    return new HierarchyPath(pathParts);
  }

  HierarchyNode addRoot(String name, String sourceElementName) {
    this.treeRoot = new HierarchyNode(name, sourceElementName);
    return treeRoot;
  }
  HierarchyNode addRoot(String name) {
    this.treeRoot = new HierarchyNode(name, name);
    return treeRoot;
  }

  public HierarchyNode getRoot() {
    return treeRoot;
  }

  static class HierarchyNode {

    private final String name;
    private final String sourceElementName;
    private final List<HierarchyNode> children;

    HierarchyNode addChild(String name, String sourceElementName) {
      HierarchyNode node = new HierarchyNode(name, sourceElementName);
      children.add(node);
      return node;
    }
    HierarchyNode addChild(String name) {
      HierarchyNode node = new HierarchyNode(name, name);
      children.add(node);
      return node;
    }



    private HierarchyNode(String name, String sourceElementName) {
      this.name = name;
      this.sourceElementName = sourceElementName;
      this.children = new ArrayList<>();
    }

    public void pathFor(List<String> namespace, List<String> pathBuilder) {

      List<String> remaining = new ArrayList<>();

      for (Iterator<String> i = namespace.iterator(); i.hasNext();) {
        String value = i.next();
        if (sourceElementName.equals(value)) {
          pathBuilder.add(name);
          i.forEachRemaining(remaining::add);
          break;
        }
      }

      for (HierarchyNode child : children) {
        child.pathFor(remaining, pathBuilder);
      }
    }
  }

  /**
   * This type is VO used for representing path in package tree or directory
   * structure or domain hierarchy.
   * <p>
   * !!! this is an experiment. If no additional methods will be added in future
   * We should consider removing it.
   */
  static class HierarchyPath {

    private final List<String> parts;

    HierarchyPath(String value) {
      // validate with regexp
      this.parts = Arrays.asList(value.split("\\."));
    }

    HierarchyPath(List<String> pathParts) {
      this.parts = pathParts;
    }

    static HierarchyPath from(String name) {
      return new HierarchyPath(name);
    }

    List<String> getParts() {
      return Collections.unmodifiableList(parts);
    }

    @Override
    public String toString() {
      return String.join(".", parts);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      HierarchyPath that = (HierarchyPath) o;

      return parts.equals(that.parts);
    }

    @Override
    public int hashCode() {
      return parts.hashCode();
    }
  }
}

