package org.noopi.utils;

public class SymbolDatabase extends AbstractDatabase<String, Symbol> {
  @Override
  protected Symbol createEntry(String name) {
    return new Symbol(name);
  }
}
