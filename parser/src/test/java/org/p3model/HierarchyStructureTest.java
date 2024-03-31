package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.p3model.DomainHierarchyFactory.twoLevels;
import static org.p3model.HierarchyStructure.HierarchyPath;

import org.junit.jupiter.api.Test;
import org.p3model.HierarchyStructure.HierarchyNode;

class HierarchyStructureTest {

  static final String MODULE_NAME = "module_name";
  static final String SUBMODULE_NAME = "submodule";

  @Test
  void should_compute_path_for_namespace() {
    // Given
    HierarchyStructure domainStructure = twoLevels(MODULE_NAME, SUBMODULE_NAME);

    // When
    HierarchyPath path = domainStructure.pathFor(HierarchyPath.from("org.p3model." + MODULE_NAME + "." + SUBMODULE_NAME));

    // Then
    assertThat(path).isEqualTo(HierarchyPath.from(MODULE_NAME + "." + SUBMODULE_NAME));
  }

  @Test
  void should_compute_path_from_namespace_meny_children() {
    // Given
    HierarchyStructure domainStructure = DomainHierarchyFactory.empty();
    HierarchyNode child = domainStructure.getRoot().addChild(MODULE_NAME);
    child.addChild("sub2").addChild("sub2_1");
    child.addChild("sub3").addChild("sub3_1");

    // When
    HierarchyPath path = domainStructure.pathFor(
        new HierarchyPath("org.p3model." + MODULE_NAME + ".sub2.sub_3_1"));

    // Then
    assertThat(path).isEqualTo(new HierarchyPath(MODULE_NAME + ".sub2"));
  }

  @Test
  void should_compute_path_from_namespace_with_gaps() {

    // Given
    HierarchyStructure domainStructure = twoLevels(MODULE_NAME, SUBMODULE_NAME);

    // When
    HierarchyPath path = domainStructure.pathFor(
        new HierarchyPath("org.p3model."+MODULE_NAME+".gap." + SUBMODULE_NAME));

    // Then
    assertThat(path).isEqualTo(new HierarchyPath(MODULE_NAME + "." + SUBMODULE_NAME));
  }

  @Test
  void should_compute_path_from_namespace_with_names_different_than_module_names() {
    // Given
    HierarchyStructure domainStructure = DomainHierarchyFactory.empty();
    HierarchyNode child = domainStructure.getRoot().addChild("domain_name", "tech_name");
    child.addChild("submodule", "submodule_tech");

    // When
    HierarchyPath path = domainStructure.pathFor(
        new HierarchyPath("org.p3model.tech_name.submodule_tech"));

    // Then
    assertThat(path).isEqualTo(new HierarchyPath("domain_name.submodule"));
  }
}
