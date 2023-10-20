package org.p3model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class P3GsonSerializer implements P3ModelSerializer {

  @Override
  public String serialize(P3Model model) {

    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .create();
    return gson.toJson(model);
  }

}
