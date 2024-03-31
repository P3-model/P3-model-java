package org.p3model;

import org.p3model.HierarchyStructure.HierarchyNode;

public class DomainHierarchyFactory {

  static HierarchyStructure twoLevels(String moduleName, String submoduleName) {
    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode root = structure.getRoot();
    HierarchyNode child = root.addChild(moduleName);
    child.addChild(submoduleName);
    return structure;
  }
  static HierarchyStructure oneLevel(String moduleName) {
    HierarchyStructure structure = new HierarchyStructure();
    structure.getRoot().addChild(moduleName);
    return structure;
  }

  static HierarchyStructure nested() {
    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode nested = structure.getRoot().addChild("nested", "nestedModule");
    HierarchyNode level1A = nested.addChild("level1A");
    level1A.addChild("level2A");
    level1A.addChild("level2B");
    nested.addChild("level1B");
    return structure;
  }

  static HierarchyStructure empty() {
    return new HierarchyStructure();
  }
}
