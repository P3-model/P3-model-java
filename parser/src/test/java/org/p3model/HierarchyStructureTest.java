package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.p3model.HierarchyStructure.HierarchyNode;

class HierarchyStructureTest {

  @Test
  void should_compute_path_for_namespace() {

    HierarchyStructure structure = new HierarchyStructure();
    structure.addRoot("module")
        .addChild("submodule");

    String path = structure.pathFor("org.p3model.module.submodule");

    assertThat(path).isEqualTo("module.submodule");
  }
  @Test
  void should_compute_path_from_namespace_meny_children() {

    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode root = structure.addRoot("module");
    root.addChild("submodule");
    root.addChild("sub2").addChild("sub2_1");
    root.addChild("sub3").addChild("sub3_1");

    String path = structure.pathFor("org.p3model.module.sub2.sub_3_1");

    assertThat(path).isEqualTo("module.sub2");
  }
}
