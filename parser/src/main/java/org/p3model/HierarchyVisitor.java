package org.p3model;

import org.p3model.HierarchyStructure.HierarchyNode;

public interface HierarchyVisitor {

  void apply(HierarchyNode hierarchyNode);
}
