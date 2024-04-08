package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;
import org.p3model.P3Model.P3Relation;
import org.p3model.P3Model.P3RelationType;

class P3ModelAnalyzerTest {


  @Test
  //@Disabled("nested module parsing is being implemented")
  void should_extract_relations_between_types() {
    P3ModelAnalyzer extractor = new P3ClassgraphAnalyzer("org.p3model.samples.basic");

    P3Model model = extractor.extract("basic");

    List<P3Relation> expectedRelations = new ArrayList<>();
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "DddRepository|basic.SampleRepo", "DddAggregate|basic.Sample"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "DddAggregate|basic.Sample", "DddValueObject|basic.SomeValue"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "DddValueObject|basic.SomeValue", "DddAggregate|basic.Sample"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "DddApplicationService|basic.SampleService", "DddRepository|basic.SampleRepo"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "DddApplicationService|basic.SampleService", "DddAggregate|basic.Sample"));

    assertThat(model.getRelations()).containsExactlyInAnyOrderElementsOf(expectedRelations);
  }

  @Test
  //@Disabled("nested module parsing is being implemented")
  void should_generate_element_id_for_nested_modules() {
    P3ModelAnalyzer extractor = new P3ClassgraphAnalyzer("org.p3model.samples.nestedModule");

    P3Model model = extractor.extract("nested");

    List<P3Element> expectedElements = new ArrayList<>();
    expectedElements.add(new P3Element("nested", P3ElementType.DddEntity,"Element0"));
    expectedElements.add(new P3Element("nested.level1A", P3ElementType.DddEntity,"Element1A"));
    expectedElements.add(new P3Element("nested.level1B", P3ElementType.DddEntity,"Element1B"));
    expectedElements.add(new P3Element("nested.level1A.level2A", P3ElementType.DddEntity,"Element2A"));
    expectedElements.add(new P3Element("nested.level1A.level2B", P3ElementType.DddEntity,"Element2B"));
    expectedElements.add(new P3Element("nested.level1B", P3ElementType.DddEntity,"SubElement1"));
    expectedElements.add(new P3Element("nested.level1B", P3ElementType.DddEntity,"SubElement2"));


    assertThat(model.getElements()).containsAll(expectedElements);

  }
}
