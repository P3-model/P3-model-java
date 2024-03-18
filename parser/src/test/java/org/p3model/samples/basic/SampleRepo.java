package org.p3model.samples.basic;

import java.util.UUID;
import org.p3model.annotations.domain.staticModel.ddd.DddRepository;

@DddRepository
public interface SampleRepo {

  Sample load(UUID id);

  void save(Sample sample);
}
