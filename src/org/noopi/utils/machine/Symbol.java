package org.noopi.utils.machine;

public final class Symbol {
  private final String name;

  public Symbol(String name) {
    assert name != null;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Symbol) && ((Symbol) o).name.equals(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
