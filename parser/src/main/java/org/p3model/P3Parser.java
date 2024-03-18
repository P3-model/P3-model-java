package org.p3model;

import org.p3model.annotations.domain.dynamicModel.ProcessStep;

public class P3Parser {

  public static P3Parser forPackage(String packageName) {
    return new P3Parser(new P3ClassgraphAnalyzer(packageName), new P3GsonSerializer());
  }
  private final P3ModelAnalyzer analyzer;
  private final P3ModelSerializer serializer;

  private P3Parser(P3ModelAnalyzer analyzer, P3ModelSerializer serializer) {
    this.analyzer = analyzer;
    this.serializer = serializer;
  }

  @ProcessStep
  public String parse(String systemName) {
    P3Model model = analyzer.extract(systemName);
    return serializer.serialize(model);
  }
}
