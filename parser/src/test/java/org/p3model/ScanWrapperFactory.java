package org.p3model;

import io.github.classgraph.ScanResult;
import org.p3model.P3ClassgraphAnalyzer.ClassGraphFactory;
import org.p3model.P3ClassgraphAnalyzer.ScanResultWrapper;

public class ScanWrapperFactory {

  static ScanResultWrapper create(String packageName) {
    ScanResult scanResult = ClassGraphFactory.create(packageName).scan();
    return new ScanResultWrapper(scanResult);
  }
}
