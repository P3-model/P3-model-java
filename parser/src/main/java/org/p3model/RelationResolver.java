package org.p3model;

import java.util.List;
import org.p3model.P3Model.P3Relation;

@FunctionalInterface
public interface RelationResolver {
  List<P3Relation> resolve(P3ElementLocator locator);
}
