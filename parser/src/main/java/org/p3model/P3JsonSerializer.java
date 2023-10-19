package org.p3model;

import com.google.gson.Gson;

public class P3JsonSerializer {

  String serialize(P3Model model) {
    return new Gson().toJson(model);
  }

}
