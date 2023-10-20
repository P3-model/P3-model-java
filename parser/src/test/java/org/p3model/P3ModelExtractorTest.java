package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;
import org.p3model.P3Model.P3Relation;
import org.p3model.P3Model.P3RelationType;

class P3ModelExtractorTest {

  @Test
  void should_extract_system_name() {

    P3ModelExtractor extractor = new P3ClassgraphExtractor("org.p3model.samples.basic");

    P3Model model = extractor.extract();

    assertThat(model.getSystemName()).isEqualTo("basic");
  }
  @Test
  void should_extract_elements_for_types() {

    P3ModelExtractor extractor = new P3ClassgraphExtractor("org.p3model.samples.basic");

    P3Model model = extractor.extract();

    List<P3Element> expectedElements = new ArrayList<>();
    expectedElements.add(new P3Element("basic", P3ElementType.DddRepository,"SampleRepo"));
    expectedElements.add(new P3Element("basic", P3ElementType.DddValueObject,"SomeValue"));
    expectedElements.add(new P3Element("basic", P3ElementType.DddApplicationService,"SampleService"));
    expectedElements.add(new P3Element("basic", P3ElementType.DddAggregate,"Sample"));


    assertThat(model.getElements()).containsAll(expectedElements);
  }

  @Test
  void should_extract_relations_between_types() {
    P3ModelExtractor extractor = new P3ClassgraphExtractor("org.p3model.samples.basic");

    P3Model model = extractor.extract();

    List<P3Relation> expectedRelations = new ArrayList<>();
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "basic.SampleRepo", "basic.Sample"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "basic.Sample", "basic.SomeValue"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "basic.SampleService", "basic.SampleRepo"));
    expectedRelations.add(new P3Relation(P3RelationType.DependsOn, "basic.SampleService", "basic.Sample"));

    assertThat(model.getRelations()).containsAll(expectedRelations);
  }

  @Test
  void should_generate_element_id_for_nested_modules() {
    P3ModelExtractor extractor = new P3ClassgraphExtractor("org.p3model.samples.nestedModule");

    P3Model model = extractor.extract();

    List<P3Element> expectedElements = new ArrayList<>();
    expectedElements.add(new P3Element("nested", P3ElementType.DddEntity,"Element0"));
    expectedElements.add(new P3Element("nested.level1A", P3ElementType.DddEntity,"ElementA1"));
    expectedElements.add(new P3Element("nested.level1B", P3ElementType.DddEntity,"Element1B"));
    expectedElements.add(new P3Element("nested.level1A.level2A", P3ElementType.DddEntity,"Element2A"));
    expectedElements.add(new P3Element("nested.level1A.level2B", P3ElementType.DddEntity,"Element2B"));
    expectedElements.add(new P3Element("nested.level1B", P3ElementType.DddEntity,"SubElement1"));
    expectedElements.add(new P3Element("nested.level1B", P3ElementType.DddEntity,"SubElement2"));


    assertThat(model.getElements()).containsAll(expectedElements);

  }
}
