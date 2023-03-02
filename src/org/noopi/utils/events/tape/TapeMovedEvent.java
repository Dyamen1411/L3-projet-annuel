package org.noopi.utils.events.tape;

import org.noopi.utils.machine.MachineAction;

public class TapeMovedEvent {
  private final MachineAction d;

  public TapeMovedEvent(MachineAction d) {
    assert d != null;
    this.d = d;
  }

  public MachineAction getDirection() {
    return d;
  }
}
