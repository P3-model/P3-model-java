package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.p3model.P3ClassgraphAnalyzer.DomainHierarchyExtractor;
import org.p3model.P3ClassgraphAnalyzer.ScanResultWrapper;

class DomainHierarchyExtractorTest {

  DomainHierarchyExtractor extractor = new DomainHierarchyExtractor();

  @Test
  void should_extract_basic() {
    // Given
    HierarchyStructure expectedStructure = DomainHierarchyFactory.oneLevel("basic");
    ScanResultWrapper wrapper = ScanWrapperFactory.create("org.p3model.samples.basic");
    // When
    HierarchyStructure structure = extractor.extract(wrapper);
    // Then
    assertThat(structure).hasToString(expectedStructure.toString());
  }

  @Test
  void should_extract_layers() {
    // Given
    HierarchyStructure expectedStructure = DomainHierarchyFactory.oneLevel("layers");
    DomainHierarchyExtractor extractor = new DomainHierarchyExtractor();
    ScanResultWrapper wrapper = ScanWrapperFactory.create("org.p3model.samples.layers");
    // When
    HierarchyStructure structure = extractor.extract(wrapper);
    // Then
    assertThat(structure).hasToString(expectedStructure.toString());
  }

  @Test
  void should_extract_nested() {
    // Given
    HierarchyStructure expectedStructure = DomainHierarchyFactory.nested();
    DomainHierarchyExtractor extractor = new DomainHierarchyExtractor();
    ScanResultWrapper wrapper = ScanWrapperFactory.create("org.p3model.samples.nestedModule");
    // When
    HierarchyStructure structure = extractor.extract(wrapper);
    // Then
    assertThat(structure).hasToString(expectedStructure.toString());
  }
}
