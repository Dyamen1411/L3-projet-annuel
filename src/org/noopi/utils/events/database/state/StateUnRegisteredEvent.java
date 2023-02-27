package org.noopi.utils.events.database.state;

import org.noopi.utils.machine.State;

public class StateUnRegisteredEvent {
  private final State s;
  
  public StateUnRegisteredEvent(State s) {
    this.s = s;
  }

  public State getState() {
    return s;
  }
}