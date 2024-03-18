package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@TestWithResources
class P3ParserTest {

  @GivenTextResource("samples/basic.json")
  String expectedBasicModel;

  @Test
  @Disabled("full parsing is being implemented")
  void should_generate_model_for_basic() {

    P3Parser p3Parser = P3Parser.forPackage("org.p3model.samples.basic");
    String JSON = p3Parser.parse("basic");

    assertThat(JSON).isEqualToIgnoringWhitespace(expectedBasicModel);
  }
}
