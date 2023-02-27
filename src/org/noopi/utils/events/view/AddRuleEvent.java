package org.noopi.utils.events.view;

import org.noopi.utils.machine.Transition;

public class AddRuleEvent {

  // ATTRIBUTS

  private Transition t;

  // CONSTRUCTEURS

  public AddRuleEvent(Transition t) {
    assert t != null;
    this.t = t;
  }

  // REQUETES

  public Transition geTransition() {
    return t;
  }
}
