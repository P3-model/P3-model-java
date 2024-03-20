package org.p3model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class HierarchyStructure {

  private HierarchyNode treeRoot;

  String pathFor(String namespace) {
    List<String> pathBuilder = new ArrayList<>();
    String[] split = namespace.split("\\.");
    treeRoot.pathFor(Arrays.asList(split), pathBuilder);
    return String.join(".", pathBuilder);
  }

  HierarchyNode addRoot(String name) {
    this.treeRoot = new HierarchyNode(name);
    return treeRoot;
  }

  static class HierarchyNode {

    private final String name;
    private final List<HierarchyNode> children;

    HierarchyNode addChild(String name) {
      HierarchyNode node = new HierarchyNode(name);
      children.add(node);
      return node;
    }
    private HierarchyNode(String name) {
      this.name = name;
      this.children = new ArrayList<>();
    }

    public void pathFor(List<String> namespace, List<String> pathBuilder) {

      List<String> remaining = new ArrayList<>();

      for (Iterator<String> i = namespace.iterator(); i.hasNext();) {
        String value = i.next();
        if (name.equals(value)) {
          pathBuilder.add(value);
          i.forEachRemaining(remaining::add);
          break;
        }
      }

      for (HierarchyNode child : children) {
        child.pathFor(remaining, pathBuilder);
      }
    }
  }
}

