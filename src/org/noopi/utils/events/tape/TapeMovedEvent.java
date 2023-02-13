package org.noopi.utils.events.tape;

import org.noopi.utils.machine.Direction;

public class TapeMovedEvent {
  private final Direction d;

  public TapeMovedEvent(Direction d) {
    assert d != null;
    this.d = d;
  }

  public Direction getDirection() {
    return d;
  }
}
