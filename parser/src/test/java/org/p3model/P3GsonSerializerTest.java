package org.p3model;

import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.Test;
import org.p3model.P3Model.P3ElementType;
@TestWithResources
class P3GsonSerializerTest {

  @GivenTextResource("serializer/single_element_model.json")
  String singleElementModel;

  @Test
  void should_serialize_single_element_model() {

    P3GsonSerializer serializer = new P3GsonSerializer();
    P3Model model = new P3Model("basic");
    model.addElement(P3ElementType.DddRepository, "SampleRepo", "basic.SampleRepo");

    String JSON = serializer.serialize(model);

    assertThat(JSON).isEqualToIgnoringWhitespace(singleElementModel);


  }
}
