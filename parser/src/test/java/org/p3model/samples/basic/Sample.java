package org.p3model.samples.basic;

import org.p3model.annotations.domain.staticModel.ddd.DddAggregate;
import org.p3model.annotations.domain.staticModel.ddd.DddValueObject;

@DddAggregate
public class Sample {

  private SomeValue someValue;

  public void doSomething(String someValue) {
      this.someValue = new SomeValue(someValue);
  }

  @DddValueObject
  private class SomeValue {

    private final String someValue1;

    public SomeValue(String someValue) {
      someValue1 = someValue;
    }
  }
}
