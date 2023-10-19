package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.Test;

@TestWithResources
class P3ParserTest {

  @GivenTextResource("samples/basic.json")
  String expectedBasicModel;

  @Test
  void should_generate_model_for_basic() {

    String generatedP3Model = new P3Parser().parse("sourcePath");

    assertThat(generatedP3Model).isEqualToIgnoringWhitespace(expectedBasicModel);
  }
}
