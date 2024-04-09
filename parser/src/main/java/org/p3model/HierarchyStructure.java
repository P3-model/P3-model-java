package org.p3model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class HierarchyStructure {

  private static final String SYSTEM = "system";
  private final HierarchyNode treeRoot;

  public HierarchyStructure() {
    treeRoot = new HierarchyNode(SYSTEM, SYSTEM, null);
  }

  HierarchyPath pathFor(HierarchyPath namespace) {
    List<String> pathParts = new ArrayList<>();
    treeRoot.pathFor(namespace.getParts(), pathParts);
    return new HierarchyPath(pathParts);
  }

  public HierarchyNode getRoot() {
    return treeRoot;
  }

  @Override
  public String toString() {
    return treeRoot.toString(1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HierarchyStructure structure = (HierarchyStructure) o;
    return Objects.equals(treeRoot, structure.treeRoot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(treeRoot);
  }

  public void visit(HierarchyVisitor visitor) {
    treeRoot.visit(visitor);
  }

  static class HierarchyNode {

    private final String name;
    private final String sourceElementName;
    private final Set<HierarchyNode> children;
    private final HierarchyNode parent;

    HierarchyNode addChild(String name, String sourceElementName) {
      HierarchyNode node = new HierarchyNode(name, sourceElementName, this);
      children.add(node);
      return node;
    }

    HierarchyNode addChild(String name) {
      HierarchyNode node = new HierarchyNode(name, name, this);
      children.add(node);
      return node;
    }

    private HierarchyNode(String name, String sourceElementName, HierarchyNode parent) {
      this.name = name;
      this.sourceElementName = sourceElementName;
      this.parent = parent;
      this.children = new HashSet<>();
    }

    public void pathFor(List<String> namespace, List<String> pathBuilder) {

      if (name.equals(SYSTEM)) {
        for (HierarchyNode child : children) {
          child.pathFor(namespace, pathBuilder);
        }
      } else {

        List<String> remaining = new ArrayList<>();

        for (Iterator<String> iterator = namespace.iterator(); iterator.hasNext(); ) {
          String value = iterator.next();
          if (sourceElementName.equals(value)) {
            pathBuilder.add(name);
            iterator.forEachRemaining(remaining::add);
            break;
          }
        }

        for (HierarchyNode child : children) {
          child.pathFor(remaining, pathBuilder);
        }
      }
    }

    public String toString(int level) {
      StringBuilder builder = new StringBuilder()
          .append("-".repeat(Math.max(0, level)))
          .append(" ").append(name)
          .append(" (").append(sourceElementName).append(")");
      int childLevel = ++level;
      for (HierarchyNode child : children) {
        builder.append(System.lineSeparator());
        builder.append(child.toString(childLevel));
      }
      return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      HierarchyNode that = (HierarchyNode) o;
      return Objects.equals(name, that.name) && Objects.equals(sourceElementName,
          that.sourceElementName) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, sourceElementName, children);
    }

    public void visit(HierarchyVisitor visitor) {
      visitor.apply(this);
      children.forEach(visitor::apply);
    }

    public String path() {
      HierarchyNode next = parent;
      StringBuilder path = new StringBuilder();
      while (next != null && !Objects.equals(next.name, SYSTEM)) {
        if (path.length() == 0) {
          path.insert(0, next.name);
        } else {
          path.insert(0, next.name + ".");
        }
        next = next.parent;
      }
      return path.toString();
    }

    public String name() {
      return name;
    }

    public boolean isSystem() {
      return this.name.equals(SYSTEM);
    }
  }

  /**
   * This type is VO used for representing path in package tree or directory structure or domain
   * hierarchy.
   * <p>
   * !!! this is an experiment. If no additional methods will be added in future We should consider
   * removing it.
   */
  static class HierarchyPath {

    private final List<String> parts;

    HierarchyPath(String value) {
      // validate with regexp
      this.parts = Arrays.stream(value.split("\\."))
          .filter(s -> !s.isEmpty())
          .collect(Collectors.toList());
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

    public boolean isEmpty() {
      return parts.isEmpty();
    }
  }
}

