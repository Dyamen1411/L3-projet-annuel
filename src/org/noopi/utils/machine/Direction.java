package org.noopi.utils.machine;

public enum Direction {
  RIGHT, LEFT;

  public Direction opposite() {
    switch(this) {
      case LEFT: return RIGHT;
      case RIGHT: return LEFT;
    }
    return null;
  }
}
