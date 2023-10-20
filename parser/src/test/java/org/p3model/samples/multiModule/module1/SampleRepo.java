package org.p3model.samples.multiModule.module1;

import java.util.UUID;
import org.p3model.annotations.domain.staticModel.ddd.DddRepository;

@DddRepository
public interface SampleRepo {

  SampleM1 load(UUID id);

  void save(SampleM1 sample);
}
