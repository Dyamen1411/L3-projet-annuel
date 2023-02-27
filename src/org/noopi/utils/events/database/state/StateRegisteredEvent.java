package org.noopi.utils.events.database.state;

import org.noopi.utils.machine.State;

public class StateRegisteredEvent {
  private final State s;
  
  public StateRegisteredEvent(State s) {
    this.s = s;
  }

  public State getState() {
    return s;
  }
}