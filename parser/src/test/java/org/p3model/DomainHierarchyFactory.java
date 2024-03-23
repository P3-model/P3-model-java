package org.p3model;

import org.p3model.HierarchyStructure.HierarchyNode;

public class DomainHierarchyFactory {
  static final String MODULE_NAME = "module_name";
  static final String SUBMODULE_NAME = "submodule";

  static HierarchyStructure basicStructure() {
    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode root = structure.addRoot(MODULE_NAME);
    root.addChild(SUBMODULE_NAME);
    return structure;
  }

}
