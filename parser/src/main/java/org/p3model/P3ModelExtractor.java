package org.p3model;

interface P3ModelExtractor {
  P3Model extract();

  class ExtractingException extends RuntimeException {

    public ExtractingException(String message) {
      super(message);
    }
  }
}
