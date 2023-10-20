package org.p3model.samples.basic;

import java.util.UUID;

public class DoSomethingElse {

  private final UUID id;

  public DoSomethingElse(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }
}
