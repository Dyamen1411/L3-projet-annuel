package org.noopi.utils.events;

import org.noopi.utils.machine.Direction;

public class TapeMovedEvent implements ILayoutEvent {
  private final Direction d;

  public TapeMovedEvent(Direction d) {
    assert d != null;
    this.d = d;
  }

  public Direction getDirection() {
    return d;
  }
}
