package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.p3model.P3ClassgraphAnalyzer.ScanResultWrapper;
import org.p3model.P3ClassgraphAnalyzer.SystemNameExtractor;

class SystemExtractorTest {

  ScanResultWrapper wrapper = ScanWrapperFactory.create("org.p3model.samples.basic");

  @Test
  void should_extract_system_name_from_annotation() {

    SystemNameExtractor extractor1 = new SystemNameExtractor();

    String model = extractor1.extractSystemName("", wrapper);

    assertThat(model).isEqualTo("basic");
  }
  @Test
  void should_extract_system_name_from_provided_value() {

    SystemNameExtractor extractor1 = new SystemNameExtractor();

    String model = extractor1.extractSystemName("provided", wrapper);

    assertThat(model).isEqualTo("provided");
  }

}
