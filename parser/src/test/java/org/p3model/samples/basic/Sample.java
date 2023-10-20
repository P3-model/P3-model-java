package org.p3model.samples.basic;

import java.util.UUID;
import org.p3model.annotations.domain.staticModel.ddd.DddAggregate;
import org.p3model.annotations.domain.staticModel.ddd.DddValueObject;

@DddAggregate
public class Sample {

  private SomeValue someValue;

  public void doSomething(String someValue) {
      this.someValue = new SomeValue(someValue);
  }
  public String doSomethingElse() {
    return this.someValue.calculate();
  }

  @DddValueObject
  private static class SomeValue {

    private final String someValue1;

    public SomeValue(String someValue) {
      someValue1 = someValue;
    }

    String calculate() {
      return UUID.randomUUID() + someValue1;
    }
  }
}
