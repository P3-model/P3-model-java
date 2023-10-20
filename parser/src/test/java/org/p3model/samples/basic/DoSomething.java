package org.p3model.samples.basic;

import java.util.UUID;


public class DoSomething {

  private final UUID id;
  private final String someValue;

  public DoSomething(UUID id, String someValue) {
    this.id = id;
    this.someValue = someValue;
  }

  public UUID getId() {
    return id;
  }

  public String getSomeValue() {
    return someValue;
  }
}
