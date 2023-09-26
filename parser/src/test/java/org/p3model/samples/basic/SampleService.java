package org.p3model.samples.basic;

import org.p3model.annotations.domain.dynamicModel.ProcessStep;
import org.p3model.annotations.domain.dynamicModel.ddd.DddApplicationService;

@DddApplicationService
public class SampleService {

  SampleRepo repo;

  @ProcessStep
  void handle(DoSomething command) {

    Sample sample  = repo.load(command.getId());
    sample.doSomething(command.getSomeValue());
    repo.save(sample);

  }

}
