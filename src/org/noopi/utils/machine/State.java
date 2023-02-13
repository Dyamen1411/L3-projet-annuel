package org.noopi.utils.machine;

public final class State {
  private final String name;

  public State(String name) {
    assert name != null;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof State) && ((State) o).name.equals(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
