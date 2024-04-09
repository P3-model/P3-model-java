package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.p3model.P3Model.P3ElementType.DddEntity;
import static org.p3model.P3Model.P3ElementType.DomainModule;
import static org.p3model.P3Model.P3RelationType.DependsOn;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3Relation;

class P3ModelAnalyzerTest {

  @Test
  void should_extract_relations_between_types() {
    // Given
    P3ModelAnalyzer extractor = new P3ClassgraphAnalyzer("org.p3model.samples.basic");

    List<P3Relation> expectedRelations = Arrays.asList(
        DependsOn.ids("DddRepository|basic.SampleRepo", "DddAggregate|basic.Sample"),
        DependsOn.ids("DddAggregate|basic.Sample", "DddValueObject|basic.SomeValue"),
        DependsOn.ids("DddValueObject|basic.SomeValue", "DddAggregate|basic.Sample"),
        DependsOn.ids("DddApplicationService|basic.SampleService",
            "DddRepository|basic.SampleRepo"),
        DependsOn.ids("DddApplicationService|basic.SampleService", "DddAggregate|basic.Sample")
    );
    // When
    P3Model model = extractor.extract("basic");

    // Then
    assertThat(model.getRelations()).containsExactlyInAnyOrderElementsOf(expectedRelations);
  }

  @Test
  void should_generate_element_id_for_nested_modules() {
    // Given
    P3ModelAnalyzer extractor = new P3ClassgraphAnalyzer("org.p3model.samples.nestedModule");

    List<P3Element> expectedElements = Arrays.asList(
        DddEntity.from("nested", "Element0"),
        DddEntity.from("nested.level1A", "Element1A"),
        DddEntity.from("nested.level1B", "Element1B"),
        DddEntity.from("nested.level1A.level2A", "Element2A"),
        DddEntity.from("nested.level1A.level2B", "Element2B"),
        DddEntity.from("nested.level1B", "SubElement1"),
        DddEntity.from("nested.level1B", "SubElement2"),
        DomainModule.from("","nested")
    );

    // When
    P3Model model = extractor.extract("nested");

    // Then
    assertThat(model.getElements()).containsExactlyInAnyOrderElementsOf(expectedElements);
  }
}
