package org.p3model;

interface P3ModelAnalyzer {
  P3Model extract(String systemName);

  class ExtractingException extends RuntimeException {

    public ExtractingException(String message) {
      super(message);
    }
  }
}
