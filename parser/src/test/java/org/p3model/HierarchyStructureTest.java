package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.p3model.HierarchyStructure.*;

import org.junit.jupiter.api.Test;
import org.p3model.HierarchyStructure.HierarchyNode;

class HierarchyStructureTest {

  @Test
  void should_compute_path_for_namespace() {

    HierarchyStructure structure = new HierarchyStructure();
    structure.addRoot("module")
        .addChild("submodule");

    HierarchyPath path = structure.pathFor(HierarchyPath.from("org.p3model.module.submodule"));

    assertThat(path).isEqualTo(HierarchyPath.from("module.submodule"));
  }
  @Test
  void should_compute_path_from_namespace_meny_children() {

    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode root = structure.addRoot("module");
    root.addChild("submodule");
    root.addChild("sub2").addChild("sub2_1");
    root.addChild("sub3").addChild("sub3_1");

    HierarchyPath path = structure.pathFor(new HierarchyPath("org.p3model.module.sub2.sub_3_1"));

    assertThat(path).isEqualTo(new HierarchyPath("module.sub2"));
  }
  @Test
  void should_compute_path_from_namespace_with_gaps() {

    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode root = structure.addRoot("domain_module");
    root.addChild("submodule");

    HierarchyPath path = structure.pathFor(new HierarchyPath("org.p3model.domain_module.gap.submodule"));

    assertThat(path).isEqualTo(new HierarchyPath("domain_module.submodule"));
  }
  @Test
  void should_compute_path_from_namespace_with_names_different_than_module_names() {

    HierarchyStructure structure = new HierarchyStructure();
    HierarchyNode root = structure.addRoot("domain_name", "tech_name");
    root.addChild("submodule", "submodule_tech");

    HierarchyPath path = structure.pathFor(new HierarchyPath("org.p3model.tech_name.submodule_tech"));

    assertThat(path).isEqualTo(new HierarchyPath("domain_name.submodule"));
  }
}
