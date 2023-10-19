package org.p3model.samples.multimodule.module2;

import java.util.UUID;
import org.p3model.annotations.domain.staticModel.ddd.DddRepository;

@DddRepository
public interface SampleRepo {

  SampleM2 load(UUID id);

  void save(SampleM2 sample);
}
