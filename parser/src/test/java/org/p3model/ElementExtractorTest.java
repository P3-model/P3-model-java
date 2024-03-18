package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.p3model.P3ClassgraphAnalyzer.ElementExtractor;
import org.p3model.P3ClassgraphAnalyzer.ScanResultWrapper;
import org.p3model.P3Model.P3Element;
import org.p3model.P3Model.P3ElementType;

class ElementExtractorTest {

  ScanResultWrapper wrapper = ScanWrapperFactory.create("org.p3model.samples.basic");

  @Test
  void should_extract_elements_for_types() {

    List<P3Element> expectedElements = new ArrayList<>();
    expectedElements.add(new P3Element("basic", P3ElementType.DddRepository,"SampleRepo"));
    expectedElements.add(new P3Element("basic", P3ElementType.DddValueObject,"SomeValue"));
    expectedElements.add(new P3Element("basic", P3ElementType.DddApplicationService,"SampleService"));
    expectedElements.add(new P3Element("basic", P3ElementType.DddAggregate,"Sample"));

    ElementExtractor extractor = new ElementExtractor();

    List<P3Element> elements = extractor.extractElements(wrapper);

    assertThat(elements).containsAll(expectedElements);
  }
}
