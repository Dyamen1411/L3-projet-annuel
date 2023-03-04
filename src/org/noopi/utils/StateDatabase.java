package org.noopi.utils;

public class StateDatabase extends AbstractDatabase<String, State> {
  @Override
  protected State createEntry(String name) {
    return new State(name);
  }
}