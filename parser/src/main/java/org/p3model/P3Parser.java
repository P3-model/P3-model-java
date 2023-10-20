package org.p3model;

public class P3Parser {

  public static P3Parser forPackage(String packageName) {
    return new P3Parser(new P3ClassgraphExtractor(packageName), new P3GsonSerializer());
  }
  private final P3ModelExtractor extractor;
  private final P3ModelSerializer serializer;

  private P3Parser(P3ModelExtractor extractor, P3ModelSerializer serializer) {
    this.extractor = extractor;
    this.serializer = serializer;
  }

  public String parse() {
    P3Model model = extractor.extract();
    return serializer.serialize(model);
  }
}
